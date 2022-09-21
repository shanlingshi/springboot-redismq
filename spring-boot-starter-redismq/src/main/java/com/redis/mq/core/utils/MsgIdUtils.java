package com.redis.mq.core.utils;

import com.redis.mq.RedisMessage;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MsgIdUtils {

    public static String msgId(RedisMessage message) {
        String msgUniqueKey = message.getBody() + "|" + System.currentTimeMillis();
        return md5(msgUniqueKey);
    }

    public static String md5(String str) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());

            byte[] byteDigest = md.digest();
            StringBuilder stringBuilder = new StringBuilder();

            int i;
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];

                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    stringBuilder.append("0");
                }

                stringBuilder.append(Integer.toHexString(i));
            }

            return stringBuilder.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Generate msgId exception", e);
        }

    }

}

