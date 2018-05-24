package com.soho.aliyun.ggk.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.soho.spring.model.GGKData;
import com.soho.spring.shiro.utils.SessionUtils;
import com.soho.spring.utils.SpringUtils;
import org.springframework.util.StringUtils;

/**
 * Created by shadow on 2018/5/24.
 */
public class GGKUtils {

    public static IAcsClient iAcsClient = null;

    static {
        try {
            GGKData ggkData = SpringUtils.getBean(GGKData.class);
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ggkData.getAppId(), ggkData.getAppKey());
            iAcsClient = new DefaultAcsClient(profile);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "afs", "afs.aliyuncs.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String GGK_VALID = "GGK_VALID";

    public static boolean validate() {
        Object object = SessionUtils.getAttribute(GGK_VALID);
        if (!StringUtils.isEmpty(object)) {
            return true;
        }
        return false;
    }

    public static void release() {
        SessionUtils.removeAttribute(GGK_VALID);
    }

    public static void success() {
        SessionUtils.setAttribute(GGK_VALID, 1);
    }

}
