package com.techstar.om.dasi.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AESUtils {
    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();

    private static final String Key = "6e6757620d3fcbfc";
    private static final String AlgorithmStr = "AES/ECB/PKCS5Padding";
    private static final String Algorithm = "AES";

    public static String encrypt(String content) throws Exception {
        return encrypt(content, Key);
    }

    public static String decrypt(String content) throws Exception {
        return decrypt(content, Key);
    }

    public static String encrypt(String content, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AlgorithmStr);
        cipher.init(Cipher.ENCRYPT_MODE, keyOf(key));
        return encoder.encodeToString(cipher.doFinal(byteOf(content)));
    }

    public static String decrypt(String content, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AlgorithmStr);
        cipher.init(Cipher.DECRYPT_MODE, keyOf(key));
        return new String(cipher.doFinal(decoder.decode(content)));
    }

    private static byte[] byteOf(String content) {
        return content.getBytes(StandardCharsets.UTF_8);
    }

    private static SecretKeySpec keyOf(String password) {
        return new SecretKeySpec(byteOf(password), Algorithm);
    }

    public static void main(String[] args) throws Exception {
        String content = encrypt("123@456#789");
        System.out.println(content);
        System.out.println(decrypt(content));
    }

}
