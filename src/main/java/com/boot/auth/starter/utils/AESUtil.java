package com.boot.auth.starter.utils;

import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Map;

public final class AESUtil {
    private final static org.slf4j.Logger log = LoggerFactory.getLogger(AESUtil.class);
    //加密方式
    private static final String ALGORITHM = "AES";
    private static final String CHARSET_NAME = "UTF-8";

    static {
        Security.setProperty("crypto.policy", "unlimited");
        fixKeyLength();
    }

    /**
     * 解决aes加密的秘钥长度限制
     */
    private static void fixKeyLength() {
        String errorString = "Failed manually overriding key-length permissions.";
        int newMaxKeyLength;
        try {
            if ((newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES")) < 256) {
                Class c = Class.forName("javax.crypto.CryptoAllPermissionCollection");
                Constructor con = c.getDeclaredConstructor();
                con.setAccessible(true);
                Object allPermissionCollection = con.newInstance();
                Field f = c.getDeclaredField("all_allowed");
                f.setAccessible(true);
                f.setBoolean(allPermissionCollection, true);

                c = Class.forName("javax.crypto.CryptoPermissions");
                con = c.getDeclaredConstructor();
                con.setAccessible(true);
                Object allPermissions = con.newInstance();
                f = c.getDeclaredField("perms");
                f.setAccessible(true);
                ((Map) f.get(allPermissions)).put("*", allPermissionCollection);
                c = Class.forName("javax.crypto.JceSecurityManager");
                f = c.getDeclaredField("defaultPolicy");
                f.setAccessible(true);
                Field mf = Field.class.getDeclaredField("modifiers");
                mf.setAccessible(true);
                mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                f.set(null, allPermissions);

                newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES");
            }
        } catch (Exception e) {
            throw new RuntimeException(errorString, e);
        }
        if (newMaxKeyLength < 256)
            throw new RuntimeException(errorString); // hack failed
    }

    /**
     * AES加密
     *
     * @param content 要加密的内容
     * @param aesKey  秘钥
     * @return 加密的结果
     */
    public static String encrypt(String content, String aesKey) {
        try {
            if (StringUtils.isEmpty(content) || StringUtils.isEmpty(aesKey)) {
                log.error("加密数据异常,内容或私钥为空[content:{};aesKey:{}]", content, aesKey);
                return null;
            }
            // 创建密码器
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] byteContent = content.getBytes(CHARSET_NAME);
            // 初始化
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(aesKey));
            //执行加密
            byte[] encryptResult = cipher.doFinal(byteContent);
            //用16进制加密
            return HexConver.conver16HexStr(encryptResult);
        } catch (Exception e) {
            log.error("AES加密数据异常：[content:{};aesKey:{}]", content, aesKey, e);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content 要解密的内容
     * @param aesKey  秘钥
     * @return 解密后的内容
     */
    public static String decrypt(String content, String aesKey) {
        try {
            if (StringUtils.isEmpty(content) || StringUtils.isEmpty(aesKey)) {
                log.error("解密数据异常,内容或私钥为空[content:{};aesKey:{}]", content, aesKey);
                return null;
            }
            //先将16进制字符串转为byte数组
            byte[] contentByte = HexConver.conver16HexToByte(content);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(aesKey));
            //执行解密
            byte[] result = cipher.doFinal(contentByte);
            return new String(result, CHARSET_NAME);
        } catch (Exception e) {
            log.error("AES解密数据异常：[content:{}; aesKey:{}]", content, aesKey);
        }
        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @param aesKey 秘钥
     * @return 生成的秘钥
     */
    private static SecretKeySpec getSecretKey(final String aesKey) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(aesKey.getBytes());
            keyGenerator.init(256, random);
            SecretKey secretKey = keyGenerator.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch (NoSuchAlgorithmException ex) {
            log.error("获取加密秘钥异常：[aesKey:{}]", aesKey);
        }
        return null;
    }
}