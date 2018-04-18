package com.soho.spring.security.imp;

import com.soho.spring.model.ConfigData;
import com.soho.spring.security.EncryptService;
import com.soho.spring.utils.AESUtils;
import com.soho.spring.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shadow
 */
@Component
public class EncryptServiceImp implements EncryptService {

    @Autowired(required = false)
    private ConfigData config;

    @Override
    public String md5(String object) {
        return MD5Utils.encrypt(object, config.getEncrypty_key());
    }

    @Override
    public String aes_e(String object) {
        return AESUtils.encrypt(object, config.getEncrypty_key());
    }

    @Override
    public String aes_d(String object) {
        return AESUtils.decrypt(object, config.getEncrypty_key());
    }

}
