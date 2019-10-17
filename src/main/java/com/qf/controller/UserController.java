package com.qf.controller;

import com.qf.constant.SsmConstant;
import com.qf.entity.User;
import com.qf.enums.ExceptionEnum;
import com.qf.exception.SsmException;
import com.qf.service.UserService;
import com.qf.util.ResultVOUtil;
import com.qf.util.SendSMSUtil;
import com.qf.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SendSMSUtil sendSMSUtil;

    /**
     * 跳转至登陆页面
     * @return
     */
    @GetMapping("/register-ui")
    public String registerUI(){
        return "user/register";
    }


    /**
     * 异步校验用户名
      * @param user
     * @return
     */
    @PostMapping("/check-username")
    @ResponseBody
    public ResultVO checkUsername(@RequestBody User user){
        userService.checkUsername(user.getUsername());
        return ResultVOUtil.success();
    }


    /**
     * 发送手机验证码
     * @param phone
     * @param session
     * @return
     */
    @PostMapping("/send-sms")
    @ResponseBody
    public ResultVO sendSMS(String phone, HttpSession session){
        if (phone == null && phone.length()!=11){
            log.info("【发送短信验证码】 手机号格式不正确！！phone = {}" , phone);
            throw new SsmException(11,"发送失败！");
        }
        ResultVO resultVO = sendSMSUtil.sendSMS(phone, session);
        return resultVO;
    }

    /**
     * 注册
     * @param user
     * @param bindingResult
     * @param registerCode
     * @param session
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public ResultVO register(@Valid User user, BindingResult bindingResult,String registerCode,HttpSession session){
        String vertifyCode = (String) session.getAttribute(SsmConstant.VERTIFY_CODE);
        if (vertifyCode == null || registerCode == null || !vertifyCode.equals(registerCode)){
            log.info(" 【执行注册】 验证码输入错误！ realvertifyCode = {} , registerCode = {} " , vertifyCode,registerCode);
            throw new SsmException(ExceptionEnum.VERTIFY_CODE_ERROR);
        }
        if (bindingResult.hasErrors()){
            String message = bindingResult.getFieldError().getDefaultMessage();
            log.info("【执行注册】 参数不合法 , msg = {},user = {}",message,user);
            throw new SsmException(ExceptionEnum.PARAM_ERROR.getCode(),message);
        }
        userService.register(user);
        return ResultVOUtil.success();
    }


    /**
     * 跳转至登陆页面
     * @return
     */
    @GetMapping("/login-ui")
    public String loginUI(){
        return "user/login";
    }

    /**
     * 用户登陆
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public ResultVO login(String username,String password,HttpSession session){
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            log.info(" 【执行登陆】 用户名或密码为空！ username = {} , password = {} " , username, password);
            throw new SsmException(ExceptionEnum.USRE_LOGIN_ERROR);
        }
        User user = userService.login(username, password);
        session.setAttribute(SsmConstant.USER_INFO, user);
        return ResultVOUtil.success();
    }
}
