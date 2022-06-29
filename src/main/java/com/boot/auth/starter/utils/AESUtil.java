package com.boot.auth.starter.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.util.Map;

public final class AESUtil {
    private final static org.slf4j.Logger log = LoggerFactory.getLogger(AESUtil.class);
    //加密方式
    private static final String ALGORITHM = "AES/CBC/PKCS7Padding";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String PROVIDER = "BC";

    static {
        if (Security.getProvider(PROVIDER) != null) {
            Security.removeProvider(PROVIDER);
        }
        Security.addProvider(new BouncyCastleProvider());
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
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(aesKey), new IvParameterSpec(new byte[16]));
            //执行加密
            byte[] encryptResult = cipher.doFinal(byteContent);
            //用16进制加密
            return Base64.getEncoder().encodeToString(encryptResult);
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
            byte[] contentByte = Base64.getDecoder().decode(content);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(aesKey), new IvParameterSpec(new byte[16]));
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

    public static void main(String[] args) {
//        https://blog.csdn.net/lijun169/article/details/82736103
        String key = "5136796459362114404125733499221448628095239751552846523902990090962942534711629841712950696905779806829274259986483908672792734832841063147657867601278627311474100165615103855530193273455059262869349253056194325470283668038035277913173792476812675321979645750336495052247466307863580839005507172381362475843403948840409679155632872355458180714760020125993942474575408148517376426288596699726831507775676374708476380063728284050626413901646030365565589724221161815007619898031669458614807443014766631288867994994547897784619661872271984961847442186766561593096259846120275460470262031071412870342688967302960753339314966637943486897434364961489077348586221110507726548995076867946594695808694672363807347965465494783266151580001876070825158315745198480509464248829588389087409325279003056394502659181281479882347988178006424504905184752639743092152080732317799173228609517606936237362620305908057124236035672015215954249915929";
        String a = encrypt("你好", key);
        System.out.println(a);
        String b = decrypt(a, key);
        System.out.println(b);
    }
}