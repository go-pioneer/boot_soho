package com.soho.spring.extend;

import com.soho.spring.utils.AESUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * Spring加载配置文件
 *
 * @author shadow
 */
public abstract class AbstractPropertyConfigurer extends PropertyPlaceholderConfigurer {

    private String[] decodeKeys;

    public AbstractPropertyConfigurer(String[] decodeKeys) {
        this.decodeKeys = decodeKeys;
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties properties) {
        properties = appendProperties(properties);
        properties = decodeProperties(properties);
        super.processProperties(beanFactoryToProcess, properties);
    }

    public Properties decodeProperties(Properties properties) {
        if (!StringUtils.isEmpty(decodeKeys) && decodeKeys.length > 0) {
            for (String decodeKey : decodeKeys) {
                String data = properties.getProperty(decodeKey);
                if (!StringUtils.isEmpty(data)) {
                    properties.put(decodeKey, AESUtils.decode(data));
                }
            }
        }
        return properties;
    }

    public abstract Properties appendProperties(Properties properties);

}
