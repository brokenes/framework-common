package com.github.framework.core.encryption;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

public class RsaUtil {

    public static final String PUBLIC_KEY = "RSAPublicKey";
    public static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 生成RSA的公钥和私钥
     */
    public static Map<String, Object> initKey() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);  //512-65536 & 64的倍数
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        return keyMap;
    }

    /**
     * 获得公钥
     */
    public static RSAPublicKey getpublicKey(Map<String, Object> keyMap){
        RSAPublicKey publicKey = (RSAPublicKey) keyMap.get(PUBLIC_KEY);
        return publicKey;
    }

    /**
     * 获得私钥
     */
    public static RSAPrivateKey getPrivateKey(Map<String, Object> keyMap){
        RSAPrivateKey privateKey = (RSAPrivateKey) keyMap.get(PRIVATE_KEY);
        return privateKey;
    }

    /**
     * 公钥加密
     */
    public static byte[] encrypt(byte[] data, RSAPublicKey publicKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherBytes = cipher.doFinal(data);
        return cipherBytes;
    }

    /**
     * 私钥解密
     */
    public static byte[] decrypt(byte[] data, RSAPrivateKey privateKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] plainBytes = cipher.doFinal(data);
        return plainBytes;
    }

    //待加密原文
    public static final String DATA = "测试rsa非对称加密/解密";

    public static void main(String[] args) throws Exception {
        Map<String, Object> keyMap = RsaUtil.initKey();

        RSAPublicKey rsaPublicKey = RsaUtil.getpublicKey(keyMap);
        RSAPrivateKey rsaPrivateKey = RsaUtil.getPrivateKey(keyMap);
        System.out.println("RSA 共钥: " + rsaPublicKey.getPublicExponent());
        System.out.println("RSA 私钥: " + rsaPrivateKey.getPrivateExponent());

        byte[] rsaResult = RsaUtil.encrypt(DATA.getBytes(), rsaPublicKey);
        System.out.println(DATA + "====>>>> RSA 加密>>>>====" + BytesToHex.fromBytesToHex(rsaResult));

        byte[] plainResult = RsaUtil.decrypt(rsaResult, rsaPrivateKey);
        System.out.println(DATA + "====>>>> RSA 解密>>>>====" + BytesToHex.fromBytesToHex(plainResult));

        System.out.println(DATA + "====>>>> RSA 解密222>>>>====" + new String(plainResult));
    }

}
