package com.chen.naisimall.thirdparty;

import com.aliyun.oss.OSSClient;
import com.chen.naisimall.thirdparty.component.SmsComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class NaisimallThirdPartyApplicationTests {

    @Autowired
    OSSClient ossClient;

    @Autowired
    SmsComponent smsComponent;

    @Test
    public void testSendCode() throws Exception {
        smsComponent.sendSmsCode("13292122993","234568");
    }

    @Test
    public void testUpload() throws FileNotFoundException {
        // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        InputStream inputStream = new FileInputStream("C:\\Users\\woita\\Desktop\\6.jpg");
        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        ossClient.putObject("naisimall", "haha.jpg", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("上传成功");
    }

    @Test
    void contextLoads() {


    }

}
