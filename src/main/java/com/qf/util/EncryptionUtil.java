package com.qf.util;


import com.qf.constant.SsmConstant;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtil {

    public static String encryption(String password,String salt){
        return new Md5Hash(password,salt, SsmConstant.NUMBER_OF_ENCRYPTION).toString();
    }
}
