package com.github.framework.core.encryption;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util {

    /**
     * base64算法加密
     * @param data
     * @return
     */
    public static String base64Encrypt(byte[] data){
        String result = new BASE64Encoder().encode(data);
        return result;
    }

    /**
     * base64算法解密
     * @param data
     * @return
     * @throws Exception
     */
    public static String base64Decrypt(String data) throws Exception{
        byte[] resultBytes = new BASE64Decoder().decodeBuffer(data);
        return new String(resultBytes);
    }

    //待加密明文
    public static final String DATA = "hi, welcome to my git area!";

    public static void main(String[] args) throws Exception {
        String base64Result = Base64Util.base64Encrypt(DATA.getBytes());
        System.out.println("DATA ========>>>base64加密===========>>>>>>> " + base64Result);

        String base64Plain = Base64Util.base64Decrypt(base64Result);
        System.out.println("DATA ========>>>base64解密===========>>>>>>> " + base64Plain);
        System.out.println("DATA ========>>>base64解密===========>>>>>>> " + base64Plain.hashCode());

    }

}
