package com.wbteam.weiban.controller;

import com.wbteam.weiban.annotation.ApiJsonObject;
import com.wbteam.weiban.annotation.ApiJsonProperty;
import com.wbteam.weiban.entity.Elder;
import com.wbteam.weiban.entity.Health;
import com.wbteam.weiban.entity.ResponseData;
import com.wbteam.weiban.entity.enums.ResponseStates;
import com.wbteam.weiban.entity.enums.User;
import com.wbteam.weiban.service.ElderService;
import com.wbteam.weiban.service.HealthService;
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

@Api(tags = "老人接口")
@RestController
@RequestMapping("/elder")
public class ElderController {

    @Autowired
    private ElderService elderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MsgValidateService msgValidateService;

    @Autowired
    private HealthService healthService;

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
    public ResponseData doRegister(@ApiJsonObject(name = "elderRegister",value = {
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
        Elder dbElder = elderService.selectByTel(tel);
        if (dbElder!=null) return new ResponseData(ResponseStates.EXISTED_ACCOUNT.getValue(), ResponseStates.EXISTED_ACCOUNT.getMessage());
        Elder elder = new Elder();
        elder.setTel(tel);
        elder.setPassword(passwordEncoder.encode(password));
        int insertElder = elderService.insertElder(elder);
        if (insertElder>0) {
            Elder selectElder = elderService.selectByTel(tel);
            Health health = new Health();
            health.setElderId(selectElder.getId());
            int insertHealth = healthService.insertHealth(health);
            if(insertHealth>0) {
                return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
            } else {
                elderService.deleteById(selectElder.getId());
                return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
            }
        } else {
            return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        }
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("密码登录")
    @PostMapping("/loginByPassword")
    public ResponseData doLoginByPassword(@ApiJsonObject(name = "ElderPasswordLogin", value = {
            @ApiJsonProperty(key = "tel", example = "用户手机号"),
            @ApiJsonProperty(key = "password",example = "密码")
    })@RequestBody Map<String,String> parameter) {
        String tel = parameter.get("tel");
        String password = parameter.get("password");
        Elder dbElder = elderService.selectByTel(tel);
        if (dbElder==null) return new ResponseData(ResponseStates.UNKNOWN_ACCOUNT.getValue(), ResponseStates.UNKNOWN_ACCOUNT.getMessage());
        if(passwordEncoder.matches(password, dbElder.getPassword())) {
            Map<String,Object> data = new HashMap<>();
            String token = jwtUtil.getToken(dbElder.getId(), User.ELDER);
            redisTemplate.opsForValue().set("elder"+dbElder.getId(),token,1, TimeUnit.DAYS);
            data.put("elder", dbElder);
            data.put("token",token);
            data.put("role",1);
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
    public ResponseData doLoginByCode(@ApiJsonObject(name = "ElderCodeLogin", value = {
            @ApiJsonProperty(key = "tel", example = "用户手机号"),
            @ApiJsonProperty(key = "code",example = "验证码")
    })@RequestBody Map<String,String> parameter) {
        String tel = parameter.get("tel");
        String code = parameter.get("code");
        Elder dbElder = elderService.selectByTel(tel);
        if (dbElder==null) return new ResponseData(ResponseStates.UNKNOWN_ACCOUNT.getValue(), ResponseStates.UNKNOWN_ACCOUNT.getMessage());
        if (/*经费不足，用不起验证码*//*msgValidateService.messageCheck(tel,code)*/ code.equals("123456")){
            Map<String, Object> data = new HashMap<>();
            String token = jwtUtil.getToken(dbElder.getId(), User.ELDER);
            redisTemplate.opsForValue().set("elder"+dbElder.getId(),token,1, TimeUnit.DAYS);
            data.put("elder", dbElder);
            data.put("token",token);
            data.put("role",1);
            return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
        } else {
            return new ResponseData(ResponseStates.MESSAGE_CODE_INCORRECT.getValue(), ResponseStates.MESSAGE_CODE_INCORRECT.getMessage());
        }
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("更新Token")
    @PostMapping("/refershToken")
    public ResponseData refershToken(@ApiJsonObject(name = "elderRefershToken", value = @ApiJsonProperty(key = "token", example = "token"))@RequestBody Map<String, String> parameter) {
        String token = parameter.get("token");
        Elder elder = (Elder) jwtUtil.getUser(token);
        if (elder==null) return new ResponseData(ResponseStates.TOKEN_IS_ERROR.getValue(), ResponseStates.TOKEN_IS_ERROR.getMessage());
        String redisToken = redisTemplate.opsForValue().get("elder" + elder.getId());
        if (!redisToken.equals(token)) return new ResponseData(ResponseStates.TOKEN_IS_ERROR.getValue(), ResponseStates.TOKEN_IS_ERROR.getMessage());
        String refreshToken = jwtUtil.refreshToken(elder.getId(), User.ELDER);
        redisTemplate.opsForValue().set("elder"+elder.getId(), refreshToken, 14, TimeUnit.DAYS);
        Map<String, Object> data = new HashMap<>();
        data.put("token", refreshToken);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("修改密码")
    @PostMapping("/changePassword")
    public ResponseData changePassword(@ApiJsonObject(name = "elderChangePassword", value = {
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
            Elder elder = new Elder();
            elder.setId(id);
            elder.setPassword(passwordEncoder.encode(password));
            boolean b = elderService.changePassword(elder);
            if (b) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
            else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        } else {
            return new ResponseData(ResponseStates.MESSAGE_CODE_INCORRECT.getValue(), ResponseStates.MESSAGE_CODE_INCORRECT.getMessage());
        }
    }

    /**
     *
     * @param elder
     * @return
     */
    @ApiOperation("修改用户信息")
    @PutMapping("/update")
    public ResponseData updateElder(@RequestBody Elder elder) {
        if (elder.getId()==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        elder.setPassword(null);
        int update = elderService.updateById(elder);
        if(update>0) {
            Map<String,Object> data =new HashMap<>();
            data.put("elder",elder);
            return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(),data);
        } else{
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
    public ResponseData deleteElder(@ApiJsonObject(name = "elderDelete", value = @ApiJsonProperty(key = "id", example = "用户ID")) @RequestBody Map<String,String> parameter){
        String id = parameter.get("id");
        if (id==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int delete = elderService.deleteById(id);
        if (delete>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param tel
     * @return
     */
    @ApiOperation("通过手机号获取详细信息")
    @GetMapping("/getByTel")
    public ResponseData getByTel(@RequestParam("tel") String tel) {
        if (tel==null) return new ResponseData(ResponseStates.ERROR.getValue(),  ResponseStates.ERROR.getMessage());
        Elder elder = elderService.selectByTel(tel);
        if (elder==null) return new ResponseData(ResponseStates.UNKNOWN_ACCOUNT.getValue(), ResponseStates.UNKNOWN_ACCOUNT.getMessage());
        Map<String, Object> data = new HashMap<>();
        data.put("elder", elder);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

}
