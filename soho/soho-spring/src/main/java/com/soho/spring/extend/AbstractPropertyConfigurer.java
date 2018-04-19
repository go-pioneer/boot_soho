package com.soho.spring.extend;

import com.soho.spring.utils.AESUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * Spring加载配置文件
 *
 * @author shadow
 */
public abstract class AbstractPropertyConfigurer extends PropertyPlaceholderConfigurer {

    private String[] decodeKeys;

    public AbstractPropertyConfigurer(String filePath, String[] decodeKeys) {
        this.decodeKeys = decodeKeys;
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource(filePath);
        setLocation(resource);
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties properties) {
        properties = appendProperties(properties);
        properties = decodeProperties(properties);
        super.processProperties(beanFactoryToProcess, properties);
    }

    public Properties decodeProperties(Properties properties) {
        String encrypt_key = properties.getProperty("default.encrypty_key");
        encrypt_key = StringUtils.isEmpty(encrypt_key) ? "" : AESUtils.decrypt(encrypt_key);
        if (!StringUtils.isEmpty(decodeKeys) && decodeKeys.length > 0) {
            for (String decodeKey : decodeKeys) {
                String data = properties.getProperty(decodeKey);
                if (!StringUtils.isEmpty(data)) {
                    properties.put(decodeKey, AESUtils.decrypt(data, encrypt_key));
                }
            }
        }
        return properties;
    }

    public abstract Properties appendProperties(Properties properties);

}
