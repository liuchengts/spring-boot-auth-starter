package com.boot.auth.starter.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

public final class AESUtil {
    private final static org.slf4j.Logger log = LoggerFactory.getLogger(AESUtil.class);
    //加密方式
    private static final String ALGORITHM = "AES/CBC/PKCS7Padding";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String PROVIDER = "BC";
    private static final String IV = "1234567890123456";
    private static IvParameterSpec IV_PARAMETER_SPEC;

    static {
        if (Security.getProvider(PROVIDER) != null) {
            Security.removeProvider(PROVIDER);
        }
        Security.addProvider(new BouncyCastleProvider());
    }

    private static IvParameterSpec getIV() {
        if (null == IV_PARAMETER_SPEC) {
            try {
                IV_PARAMETER_SPEC = new IvParameterSpec(IV.getBytes(CHARSET_NAME));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return IV_PARAMETER_SPEC;
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
                log.error("加密数据异常,内容或私钥为空");
                return null;
            }
            // 创建密码器
            Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
            byte[] byteContent = content.getBytes(CHARSET_NAME);
            // 初始化
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(aesKey), getIV());
            //执行加密
            byte[] encryptResult = cipher.doFinal(byteContent);
            //用16进制加密
            return bytesToHex(encryptResult);
//            return Base64.getUrlEncoder().encodeToString(encryptResult);
        } catch (Exception e) {
            log.error("AES加密数据异常:", e);
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
                log.error("解密数据异常,内容或私钥为空");
                return null;
            }
            //先将16进制字符串转为byte数组
//            byte[] contentByte = Base64.getUrlDecoder().decode(content);
            byte[] contentByte = hexToByteArray(content);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(aesKey), getIV());
            //执行解密
            byte[] result = cipher.doFinal(contentByte);
            return new String(result, CHARSET_NAME);
        } catch (Exception e) {
            log.error("AES解密数据异常:", e);
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
        String algorithm = "AES";
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            Security.addProvider(new BouncyCastleProvider());
            random.setSeed(aesKey.getBytes());
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(random);
            return new SecretKeySpec(keyGenerator.generateKey().getEncoded(), algorithm);
        } catch (NoSuchAlgorithmException e) {
            log.error("获取加密秘钥异常:", e);
        }
        return null;
    }

    /**
     * 字节数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = (byte) Integer.parseInt(inHex.substring(i, i + 2), 16);
            j++;
        }
        return result;
    }

//    public static void main(String[] args) {
//        String key = "11111";
//        String str = "111";
//        String estr = encrypt(str, key);
//        System.out.println(estr);
//        String b = decrypt(estr, key);
//        System.out.println(b);
//    }
}