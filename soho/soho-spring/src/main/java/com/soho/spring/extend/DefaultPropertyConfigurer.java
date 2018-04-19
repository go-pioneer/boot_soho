package com.soho.spring.extend;

import java.util.Properties;

/**
 * Spring加载配置文件
 *
 * @author shadow
 */
public class DefaultPropertyConfigurer extends AbstractPropertyConfigurer {

    public DefaultPropertyConfigurer(String filePath, String[] decodeKeys) {
        super(filePath, decodeKeys);
    }

    @Override
    public Properties appendProperties(Properties properties) {
        return properties;
    }

}