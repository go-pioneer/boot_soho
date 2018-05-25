package com.soho.aliyun.ggk.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.soho.spring.model.OSSData;
import com.soho.spring.utils.SpringUtils;

/**
 * Created by shadow on 2018/5/24.
 */
public class GGKUtils {

    public static IAcsClient iAcsClient = null;

    static {
        try {
            OSSData ossData = SpringUtils.getBean(OSSData.class);
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ossData.getAppId(), ossData.getAppKey());
            iAcsClient = new DefaultAcsClient(profile);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "afs", "afs.aliyuncs.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
