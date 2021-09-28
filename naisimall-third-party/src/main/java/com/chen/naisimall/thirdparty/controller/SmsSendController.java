package com.chen.naisimall.thirdparty.controller;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.chen.common.utils.R;
import com.chen.naisimall.thirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsSendController {

    @Autowired
    SmsComponent smsComponent;

    @GetMapping("/sendcode")
    public R sendCode(@RequestParam("phone")String phone,@RequestParam("code")String code) throws Exception {
        smsComponent.sendSmsCode(phone,code);
        return R.ok();
    }
}
