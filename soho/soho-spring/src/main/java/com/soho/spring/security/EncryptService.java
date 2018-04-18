package com.soho.spring.security;

/**
 * @author shadow
 */
public interface EncryptService {

    // MD5加密
    public String md5(String object);

    // AES加密
    public String aes_e(String object);

    // AES解密
    public String aes_d(String object);

}
