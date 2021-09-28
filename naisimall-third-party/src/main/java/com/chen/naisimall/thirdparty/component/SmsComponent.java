package com.chen.naisimall.thirdparty.component;

import com.aliyun.tea.*;
import com.aliyun.dysmsapi20170525.*;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.*;
import com.aliyun.teaopenapi.models.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
@Component
@Data
public class SmsComponent {

    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private String templateCode;

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }
    public void sendSmsCode(String phone,String code) throws Exception {
        com.aliyun.dysmsapi20170525.Client client = SmsComponent.createClient(accessKeyId, accessKeySecret);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(signName)
                .setTemplateCode(templateCode)
                .setTemplateParam("{\"code\":\""+code+"\"}");
        // 复制代码运行请自行打印 API 的返回值
        client.sendSms(sendSmsRequest);
    }

    public static void main(String[] args_) throws Exception {
        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.dysmsapi20170525.Client client = SmsComponent.createClient("LTAI4FmBJKHvgxTwrU61E883", "Ilrn65B4hA6AGLA9IbKu3HpYA7jSBu");
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers("13292122993")
                .setSignName("欧巴学院在线教育网站")
                .setTemplateCode("SMS_185246556")
                .setTemplateParam("{\"code\":\"123432\"}");
        // 复制代码运行请自行打印 API 的返回值
        client.sendSms(sendSmsRequest);
    }
}
