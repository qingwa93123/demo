package com.qf.util;

import com.qf.vo.ResultVO;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Random;

import static com.qf.constant.SsmConstant.*;

@Component
public class SendSMSUtil {

    @Value("${yunpian.apikey}")
    private String apikey;

    public ResultVO sendSMS(String phone, HttpSession session){
        //初始化clnt,使用单例方式
        YunpianClient clnt = new YunpianClient(apikey).init();
        //生成验证码
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        //发送短信API
        Map<String, String> param = clnt.newParam(2);
        param.put(YunpianClient.MOBILE, phone);
        param.put(YunpianClient.TEXT, "【云片网】您的验证码是"+verifyCode);
        session.setAttribute(VERTIFY_CODE,verifyCode);
        Result<SmsSingleSend> r = clnt.sms().single_send(param);
        //获取返回结果，返回码:r.getCode(),返回码描述:r.getMsg(),API结果:r.getData(),其他说明:r.getDetail(),调用异常:r.getThrowable()
        Integer code = r.getCode();
        String msg = r.getMsg();
        ResultVO resultVO = new ResultVO(code, msg, null);
        //账户:clnt.user().* 签名:clnt.sign().* 模版:clnt.tpl().* 短信:clnt.sms().* 语音:clnt.voice().* 流量:clnt.flow().* 隐私通话:clnt.call().*

        //释放clnt
        clnt.close();
        return resultVO;
    }

}
