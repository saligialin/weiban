package com.wbteam.weiban.controller;

import com.wbteam.weiban.annotation.ApiJsonObject;
import com.wbteam.weiban.annotation.ApiJsonProperty;
import com.wbteam.weiban.entity.Carer;
import com.wbteam.weiban.entity.ResponseData;
import com.wbteam.weiban.entity.enums.ResponseStates;
import com.wbteam.weiban.entity.enums.User;
import com.wbteam.weiban.service.CarerService;
import com.wbteam.weiban.service.MsgValidateService;
import com.wbteam.weiban.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "护工接口")
@RestController
@RequestMapping("/carer")
public class CarerController {

    @Autowired
    private CarerService carerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MsgValidateService msgValidateService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     *
     * @param tel
     * @return
     */
    @ApiOperation(("获取验证码"))
    @GetMapping("/getMessage")
    public ResponseData getMessage(@RequestParam("tel") String tel) {
        boolean send = msgValidateService.messsageSend(tel);
        if (send) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("注册")
    @PostMapping("/register")
    public ResponseData doRegister(@ApiJsonObject(name = "carerRegister",value = {
            @ApiJsonProperty(key = "tel", example = "用户手机号"),
            @ApiJsonProperty(key = "password", example = "密码"),
            @ApiJsonProperty(key = "code", example = "验证码")
    }) @RequestBody Map<String, String> parameter) {
        String tel = parameter.get("tel");
        String password = parameter.get("password");
        String code = parameter.get("code");
//        经费不足，用不起短信验证码
//        if (!msgValidateService.messageCheck(tel,code)) return new ResponseData(ResponseStates.MESSAGE_CODE_INCORRECT.getValue(), ResponseStates.MESSAGE_CODE_INCORRECT.getMessage());
        if (!code.equals("123456")) return new ResponseData(ResponseStates.MESSAGE_CODE_INCORRECT.getValue(), ResponseStates.MESSAGE_CODE_INCORRECT.getMessage());
        Carer dbCarer = carerService.selectByTel(tel);
        if (dbCarer!=null) return new ResponseData(ResponseStates.MESSAGE_CODE_INCORRECT.getValue(), ResponseStates.MESSAGE_CODE_INCORRECT.getMessage());
        Carer carer = new Carer();
        carer.setTel(tel);
        carer.setPassword(passwordEncoder.encode(password));
        int insertCarer = carerService.insertCarer(carer);
        if (insertCarer>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("密码登录")
    @PostMapping("/loginByPassword")
    public ResponseData doLoginByPassword(@ApiJsonObject(name = "carerPasswordLogin", value = {
            @ApiJsonProperty(key = "tel", example = "用户手机号"),
            @ApiJsonProperty(key = "password",example = "密码")
    })@RequestBody Map<String,String> parameter) {
        String tel = parameter.get("tel");
        String password = parameter.get("password");
        Carer dbCarer = carerService.selectByTel(tel);
        if (dbCarer==null) return new ResponseData(ResponseStates.UNKNOWN_ACCOUNT.getValue(), ResponseStates.UNKNOWN_ACCOUNT.getMessage());
        if (passwordEncoder.matches(password,dbCarer.getPassword())) {
            Map<String, Object> data = new HashMap<>();
            String token = jwtUtil.getToken(dbCarer.getId(), User.CARER);
            redisTemplate.opsForValue().set("carer"+dbCarer.getId(),token,1, TimeUnit.DAYS);
            data.put("carer",dbCarer);
            data.put("token",token);
            return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
        } else {
            return new ResponseData(ResponseStates.PASSWORD_INCORRECT.getValue(), ResponseStates.PASSWORD_INCORRECT.getMessage());
        }
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("验证码登录")
    @PostMapping("/loginBycode")
    public ResponseData doLoginByCode(@ApiJsonObject(name = "carerCodeLogin", value = {
            @ApiJsonProperty(key = "tel", example = "用户手机号"),
            @ApiJsonProperty(key = "code",example = "验证码")
    })@RequestBody Map<String,String> parameter) {
        String tel = parameter.get("tel");
        String code = parameter.get("code");
        Carer dbCarer = carerService.selectByTel(tel);
        if (dbCarer==null) return new ResponseData(ResponseStates.UNKNOWN_ACCOUNT.getValue(), ResponseStates.UNKNOWN_ACCOUNT.getMessage());
        if (/*经费不足，用不起验证码*//*msgValidateService.messageCheck(tel,code)*/ code.equals("123456")){
            Map<String, Object> data = new HashMap<>();
            String token = jwtUtil.getToken(dbCarer.getId(), User.CARER);
            redisTemplate.opsForValue().set("carer"+dbCarer.getId(),token,1, TimeUnit.DAYS);
            data.put("carer",dbCarer);
            data.put("token",token);
            return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
        } else {
            return new ResponseData(ResponseStates.PASSWORD_INCORRECT.getValue(), ResponseStates.PASSWORD_INCORRECT.getMessage());
        }
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("更新Token")
    @PostMapping("/refershToken")
    public ResponseData refershToken(@ApiJsonObject(name = "carerRefershToken", value = @ApiJsonProperty(key = "token", example = "token"))@RequestBody Map<String, String> parameter) {
        String token = parameter.get("token");
        Carer carer = (Carer) jwtUtil.getUser(token);
        if (carer==null) return new ResponseData(ResponseStates.TOKEN_IS_ERROR.getValue(), ResponseStates.TOKEN_IS_ERROR.getMessage());
        String redisToken = redisTemplate.opsForValue().get("carer" + carer.getId());
        if  (!redisToken.equals(token)) return new ResponseData(ResponseStates.TOKEN_IS_ERROR.getValue(), ResponseStates.TOKEN_IS_ERROR.getMessage());
        String refreshToken = jwtUtil.refreshToken(carer.getId(), User.CARER);
        redisTemplate.opsForValue().set("carer"+carer.getId(),refreshToken,14,TimeUnit.DAYS);
        Map<String,Object> data = new HashMap<>();
        data.put("token",refreshToken);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("修改密码")
    @PostMapping("/changePassword")
    public ResponseData changePassword(@ApiJsonObject(name = "carerChangePassword", value = {
            @ApiJsonProperty(key = "id", example = "用户ID"),
            @ApiJsonProperty(key = "tel", example = "用户手机号"),
            @ApiJsonProperty(key = "password", example = "新密码"),
            @ApiJsonProperty(key = "code", example = "验证码")
    })@RequestBody Map<String, String> parameter) {
        String id = parameter.get("id");
        String tel = parameter.get("tel");
        String password = parameter.get("password");
        String code = parameter.get("code");
        if (/*经费不足，用不起验证码*//*msgValidateService.messageCheck(tel,code)*/ code.equals("123456")){
            Carer carer = new Carer();
            carer.setId(id);
            carer.setPassword(passwordEncoder.encode(password));
            boolean b = carerService.changePassword(carer);
            if (b) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
            else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        } else {
            return new ResponseData(ResponseStates.MESSAGE_CODE_INCORRECT.getValue(), ResponseStates.MESSAGE_CODE_INCORRECT.getMessage());
        }
    }


    /**
     *
     * @param carer
     * @return
     */
    @ApiOperation("修改用户信息")
    @PutMapping("/update")
    public ResponseData updateChild(@RequestBody Carer carer) {
        if (carer.getId()==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        carer.setPassword(null);
        int update = carerService.updateById(carer);
        if (update>0) {
            Map<String, Object> data = new HashMap<>();
            data.put("carer",carer);
            return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
        } else {
            return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        }
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("删除用户")
    @DeleteMapping("/delete")
    public ResponseData deleteChild(@ApiJsonObject(name = "carerDelete", value = @ApiJsonProperty(key = "id", example = "用户ID")) @RequestBody Map<String,String> parameter) {
        String id = parameter.get("id");
        if (id==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int delete = carerService.deleteById(id);
        if (delete>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }
}
