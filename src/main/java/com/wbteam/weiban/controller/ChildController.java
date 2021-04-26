package com.wbteam.weiban.controller;

import com.wbteam.weiban.annotation.ApiJsonObject;
import com.wbteam.weiban.annotation.ApiJsonProperty;
import com.wbteam.weiban.entity.Child;
import com.wbteam.weiban.entity.ResponseData;
import com.wbteam.weiban.entity.enums.ResponseStates;
import com.wbteam.weiban.entity.enums.User;
import com.wbteam.weiban.service.ChildService;
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

@Api(tags = "家属接口")
@RestController
@RequestMapping("/child")
public class ChildController {

    @Autowired
    private ChildService childService;

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
    public ResponseData doRegister(@ApiJsonObject(name = "childRegister",value = {
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
        Child dbChild = childService.selectByTel(tel);
        if (dbChild!=null) return new ResponseData(ResponseStates.EXISTED_ACCOUNT.getValue(), ResponseStates.EXISTED_ACCOUNT.getMessage());
        Child child = new Child();
        child.setTel(tel);
        child.setPassword(passwordEncoder.encode(password));
        int insertChild = childService.insertChild(child);
        if (insertChild>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("密码登录")
    @PostMapping("/loginByPassword")
    public ResponseData doLoginByPassword(@ApiJsonObject(name = "childPasswordLogin", value = {
            @ApiJsonProperty(key = "tel", example = "用户手机号"),
            @ApiJsonProperty(key = "password",example = "密码")
    })@RequestBody Map<String,String> parameter) {
        String tel = parameter.get("tel");
        String password = parameter.get("password");
        Child dbChild = childService.selectByTel(tel);
        if (dbChild==null) return new ResponseData(ResponseStates.UNKNOWN_ACCOUNT.getValue(), ResponseStates.UNKNOWN_ACCOUNT.getMessage());
        if (passwordEncoder.matches(password,dbChild.getPassword())){
            Map<String,Object> data = new HashMap<>();
            String token = jwtUtil.getToken(dbChild.getId(), User.CHILD);
            redisTemplate.opsForValue().set("child"+dbChild.getId(),token,1, TimeUnit.DAYS);
            data.put("child",dbChild);
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
    public ResponseData doLoginByCode(@ApiJsonObject(name = "childCodeLogin", value = {
            @ApiJsonProperty(key = "tel", example = "用户手机号"),
            @ApiJsonProperty(key = "code",example = "验证码")
    })@RequestBody Map<String,String> parameter) {
        String tel = parameter.get("tel");
        String code = parameter.get("code");
        Child dbChild = childService.selectByTel(tel);
        if (dbChild==null) return new ResponseData(ResponseStates.UNKNOWN_ACCOUNT.getValue(), ResponseStates.UNKNOWN_ACCOUNT.getMessage());
        if (/*经费不足，用不起验证码*//*msgValidateService.messageCheck(tel,code)*/ code.equals("123456")){
            Map<String, Object> data = new HashMap<>();
            String token = jwtUtil.getToken(dbChild.getId(), User.CHILD);
            redisTemplate.opsForValue().set("child"+dbChild.getId(),token,1, TimeUnit.DAYS);
            data.put("child",dbChild);
            data.put("token",token);
            return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(),data);
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
    public ResponseData refershToken(@ApiJsonObject(name = "childRefershToken", value = @ApiJsonProperty(key = "token", example = "token"))@RequestBody Map<String, String> parameter) {
        String token = parameter.get("token");
        Child child = (Child) jwtUtil.getUser(token);
        if (child==null) return new ResponseData(ResponseStates.TOKEN_IS_ERROR.getValue(), ResponseStates.TOKEN_IS_ERROR.getMessage());
        String redisToken = redisTemplate.opsForValue().get("child" + child.getId());
        if (!redisToken.equals(token)) return new ResponseData(ResponseStates.TOKEN_IS_ERROR.getValue(), ResponseStates.TOKEN_IS_ERROR.getMessage());
        String refreshToken = jwtUtil.refreshToken(child.getId(), User.CHILD);
        redisTemplate.opsForValue().set("child"+child.getId(),refreshToken,14, TimeUnit.DAYS);
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
    public ResponseData changePassword(@ApiJsonObject(name = "childChangePassword", value = {
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
            Child child = new Child();
            child.setId(id);
            child.setPassword(passwordEncoder.encode(password));
            boolean b = childService.changePassword(child);
            if (b) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
            else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        } else {
            return new ResponseData(ResponseStates.MESSAGE_CODE_INCORRECT.getValue(), ResponseStates.MESSAGE_CODE_INCORRECT.getMessage());
        }
    }

    /**
     *
     * @param child
     * @return
     */
    @ApiOperation("修改用户信息")
    @PutMapping("/update")
    public ResponseData updateChild(@RequestBody Child child) {
        if (child.getId()==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        child.setPassword(null);
        int update = childService.updateById(child);
        if (update>0) {
            Map<String, Object> data = new HashMap<>();
            data.put("child",child);
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
    public ResponseData deleteChild(@ApiJsonObject(name = "childDelete", value = @ApiJsonProperty(key = "id", example = "用户ID")) @RequestBody Map<String,String> parameter) {
        String id = parameter.get("id");
        if (id==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int delete = childService.deleteById(id);
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
        Child child = childService.selectByTel(tel);
        if (child==null) return new ResponseData(ResponseStates.UNKNOWN_ACCOUNT.getValue(), ResponseStates.UNKNOWN_ACCOUNT.getMessage());
        Map<String, Object> data = new HashMap<>();
        data.put("child", child);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }


}
