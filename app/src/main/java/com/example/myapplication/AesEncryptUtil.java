package com.example.myapplication;

        import android.util.Base64;

        import javax.crypto.Cipher;
        import javax.crypto.spec.IvParameterSpec;
        import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密解密工具类（CBC模式 + PKCS7Padding填充）
 */
public class AesEncryptUtil {
    // 加密算法/模式/填充方式
    private static final String AES_MODE = "AES/CBC/PKCS7Padding";
    // 加密算法
    private static final String AES_ALGORITHM = "AES";
    // 编码格式
    private static final String CHARSET = "UTF-8";

    /**
     * AES加密
     * @param plainText 待加密明文
     * @param key 密钥（16位字符，AES-128）
     * @param iv 初始向量（16位字符）
     * @return Base64编码的密文
     * @throws Exception 加密异常
     */
    public static String encrypt(String plainText, String key, String iv) throws Exception {
        // 校验参数
        if (plainText == null || key == null || iv == null) {
            throw new IllegalArgumentException("明文、key、iv不能为空");
        }
        if (key.length() != 16 || iv.length() != 16) {
            throw new IllegalArgumentException("AES-128的key和iv必须是16位字符");
        }

        // 初始化密钥和IV
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET), AES_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(CHARSET));

        // 初始化加密器
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        // 加密并转Base64
        byte[] encryptBytes = cipher.doFinal(plainText.getBytes(CHARSET));
        return Base64.encodeToString(encryptBytes, Base64.NO_WRAP);
    }

    /**
     * AES解密
     * @param cipherText Base64编码的密文
     * @param key 密钥（16位字符）
     * @param iv 初始向量（16位字符）
     * @return 解密后的明文
     * @throws Exception 解密异常
     */
    public static String decrypt(String cipherText, String key, String iv) throws Exception {
        // 校验参数
        if (cipherText == null || key == null || iv == null) {
            throw new IllegalArgumentException("密文、key、iv不能为空");
        }
        if (key.length() != 16 || iv.length() != 16) {
            throw new IllegalArgumentException("AES-128的key和iv必须是16位字符");
        }

        // 初始化密钥和IV
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET), AES_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(CHARSET));

        // 初始化解密器
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        // 解密：先Base64解码，再解密
        byte[] cipherBytes = Base64.decode(cipherText, Base64.NO_WRAP);
        byte[] plainBytes = cipher.doFinal(cipherBytes);
        return new String(plainBytes, CHARSET);
    }
}
