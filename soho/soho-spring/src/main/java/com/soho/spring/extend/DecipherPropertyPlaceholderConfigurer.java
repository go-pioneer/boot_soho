package com.soho.spring.extend;

import com.soho.spring.utils.AESUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Spring加载配置文件,解密属性字段
 *
 * @author shadow
 */
public class DecipherPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private String[] dekeys;

    public DecipherPropertyPlaceholderConfigurer(String[] dekeys) {
        this.dekeys = dekeys;
    }

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (dekeys != null && dekeys.length > 0) {
            for (String dekey : dekeys) {
                if (propertyName.equals(dekey)) {
                    propertyValue = AESUtils.decode(propertyValue);
                }
            }
        }
        return super.convertProperty(propertyName, propertyValue);
    }

}