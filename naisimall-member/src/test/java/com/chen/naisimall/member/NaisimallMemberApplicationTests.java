package com.chen.naisimall.member;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class NaisimallMemberApplicationTests {

    @Test
    void contextLoads() {
        String s = DigestUtils.md5Hex("123456");
        //e10adc3949ba59abbe56e057f20f883e
        System.out.println(s);

        String s1 = Md5Crypt.md5Crypt("123456".getBytes());
        //加盐(默认)
        //$1$DHPiqbaI$2KsLUBYxSRqcwRdabETGy0
        System.out.println(s1);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123456");
        //$2a$10$oVn1cLxaAE5oykh1g0.cq.0N8T.ch1eieshjM0FqtahkvIk9Dk4/W
        //$2a$10$UaxeA/O6Y9z0IRoWLkYFpuucMljTB2mHHHYPkFkJXFyepkhlRgphO
        System.out.println(encode);

        boolean matches = passwordEncoder.matches("123456", "$2a$10$UaxeA/O6Y9z0IRoWLkYFpuucMljTB2mHHHYPkFkJXFyepkhlRgphO");
        //true
        System.out.println(matches);


    }

}
