package com.soho.spring.extend;

import com.soho.spring.utils.AESUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Spring加载配置文件
 *
 * @author shadow
 */
public abstract class AbstractPropertyConfigurer extends PropertyPlaceholderConfigurer {

    private String[] decodeKeys;

    public AbstractPropertyConfigurer(String[] filePath, String[] decodeKeys) {
        this.decodeKeys = decodeKeys;
        if (filePath == null || filePath.length <= 0) {
            System.err.println("没有找到项目启动配置文件");
            System.exit(-1);
        }
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = new Resource[filePath.length];
        for (int i = 0; i < filePath.length; i++) {
            Resource resource = resolver.getResource(filePath[i]);
            resources[i] = resource;
        }
        setLocations(resources);
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties properties) {
        properties = appendProperties(properties);
        properties = decodeProperties(properties);
        super.processProperties(beanFactoryToProcess, properties);
    }

    public Properties decodeProperties(Properties properties) {
        String encrypt = properties.getProperty("default.encrypt");
        if (StringUtils.isEmpty(encrypt) || !Boolean.valueOf(encrypt.trim())) {
            return properties;
        }
        String projectKey = properties.getProperty("default.projectKey");
        if (projectKey == null || StringUtils.isEmpty(projectKey.trim())) {
            System.err.println("没有找到项目密钥配置");
            System.exit(-1);
        }
        projectKey = AESUtils.decrypt(projectKey.trim());
        for (String key : decodeKeys) {
            String data = properties.getProperty(key);
            if (!StringUtils.isEmpty(data)) {
                properties.put(key, AESUtils.decrypt(data.trim(), projectKey));
            }
        }
        for (Map.Entry entry : properties.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            if ((key.endsWith("datasource.username") || key.endsWith("datasource.password")) && !StringUtils.isEmpty(value)) {
                properties.put(key, AESUtils.decrypt(entry.getValue().toString().trim(), projectKey));
            }
        }
        return properties;
    }

    public abstract Properties appendProperties(Properties properties);

}
