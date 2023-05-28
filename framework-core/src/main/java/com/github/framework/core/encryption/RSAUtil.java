package com.github.framework.core.encryption;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RSAUtil {

//    public static final String PUBLIC_KEY = "RSAPublicKey";
//    public static final String PRIVATE_KEY = "RSAPrivateKey";

    private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtil.class);

    private static final Map<String,Object> KEY_MAP = new HashMap<String,Object>();

    /**
     * 生成RSA的公钥和私钥
     */
    public static Map<String, Object> initKey(String pubKey,String priKey){
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            //初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(1024,new SecureRandom());
            //生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();//得到私钥
            PublicKey publicKey = keyPair.getPublic();//得到公钥
            //得到公钥字符串
            String publicKeyString=new String(Base64.encodeBase64(publicKey.getEncoded()));
            //得到私钥字符串
            String privateKeyString=new String(Base64.encodeBase64(privateKey.getEncoded()));
            KEY_MAP.put(pubKey, publicKeyString);
            KEY_MAP.put(priKey, privateKeyString);
        }catch (Exception e){
            LOGGER.error("初始化共钥/私钥异常:",e);
        }
        return KEY_MAP;
    }

    /**
     * 获得公钥
     */
    public static RSAPublicKey getpublicKey(String pubKey){
        RSAPublicKey publicKey = (RSAPublicKey) KEY_MAP.get(pubKey);
        return publicKey;
    }

    /**
     * 获得私钥
     */
    public static RSAPrivateKey getPrivateKey(String priKey){
        RSAPrivateKey privateKey = (RSAPrivateKey) KEY_MAP.get(priKey);
        return privateKey;
    }

    /**
     * 公钥加密
     */
    public static byte[] encrypt(byte[] data, RSAPublicKey publicKey){
        byte[] cipherBytes = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
             cipherBytes = cipher.doFinal(data);
            return cipherBytes;
        }catch (Exception e){
            LOGGER.error("共钥加密异常:",e);
        }
        return cipherBytes;
    }

    /**
     * 私钥解密
     */
    public static byte[] decrypt(byte[] data, RSAPrivateKey privateKey){
        byte[] plainBytes = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            plainBytes = cipher.doFinal(data);
            return plainBytes;
        }catch (Exception e){
            LOGGER.error("私钥解密异常:",e);
        }
        return plainBytes;
    }

    //待加密原文
    public static final String DATA = "测试rsa非对称加密/解密";

    public static void main(String[] args) {
        String pubKey = UUID.randomUUID().toString().substring(0,10);
        String priKey = UUID.randomUUID().toString().substring(0,16);;
        Map<String, Object> keyMap = RSAUtil.initKey(pubKey,priKey);

        RSAPublicKey rsaPublicKey = RSAUtil.getpublicKey(pubKey);
        RSAPrivateKey rsaPrivateKey = RSAUtil.getPrivateKey(priKey);
        System.out.println("RSA 共钥: " + rsaPublicKey.getPublicExponent());
        System.out.println("RSA 私钥: " + rsaPrivateKey.getPrivateExponent());

        byte[] rsaResult = RSAUtil.encrypt(DATA.getBytes(), rsaPublicKey);
        System.out.println(DATA + "====>>>> RSA 加密>>>>====" + BytesToHex.fromBytesToHex(rsaResult));

        byte[] plainResult = RSAUtil.decrypt(rsaResult, rsaPrivateKey);
        System.out.println(DATA + "====>>>> RSA 解密>>>>====" + BytesToHex.fromBytesToHex(plainResult));

        System.out.println(DATA + "====>>>> RSA 解密222>>>>====" + new String(plainResult));
    }

}
