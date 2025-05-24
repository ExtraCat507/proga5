package org.xtracat.security;

import org.xtracat.usershit.PasswordRecord;
import org.xtracat.usershit.UserRecord;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class HashManager {

    final static String PEPPER = "7``3+ib65-mVLC(#";

    public static PasswordRecord hash(String passwd) {
        String salt = getRandomString();
        return hashWithSalt(passwd,salt);
    }

    public static PasswordRecord hashWithSalt(String password, String salt){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-384");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] resultHash;
        try {
            resultHash =  md.digest(
                    (password + PEPPER + salt).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return new PasswordRecord(salt,resultHash);
    }

    private static String getRandomString() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+-_=!@#$%^&*)(`~";
        Random random = new Random();
        int length = random.nextInt(11) + 5; // от 5 до 15 включительно
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }


}
