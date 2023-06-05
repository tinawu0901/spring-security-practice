package com.tinawu.springSecuritybase.utils;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CodecUtil {
    private static Logger logger = LoggerFactory.getLogger(CodecUtil.class);

    public static String getSHA256Str(String str) {
        MessageDigest messageDigest;
        String encdeStr = "";//密碼員馬
        try {//拿到SHA-256轉換器
            messageDigest = MessageDigest.getInstance("SHA-256");
         // 轉換並返回結果，也是位元組陣列
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
          //位元組轉換為字串
            encdeStr = Hex.encodeHexString(hash);//c6be412623e868da8e50690af59b6d2798b9a94eca3668d11256e82f8c146aa1
        } catch (NoSuchAlgorithmException e) {
            logger.error("getSHA256Str Occur NoSuchAlgorithmExceptionException", e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            logger.error("getSHA256Str Occur UnsupportedEncodingException", e.getMessage(), e);
        }
        return encdeStr;
    }
}
