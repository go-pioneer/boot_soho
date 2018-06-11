package com.soho.aliyun.ggk.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.soho.spring.model.OSSConfig;
import com.soho.spring.utils.SpringUtils;

/**
 * Created by shadow on 2018/5/24.
 */
public class GGKUtils {

    public static IAcsClient iAcsClient = null;

    static {
        try {
            OSSConfig ossConfig = SpringUtils.getBean(OSSConfig.class);
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ossConfig.getAppId(), ossConfig.getAppKey());
            iAcsClient = new DefaultAcsClient(profile);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "afs", "afs.aliyuncs.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
