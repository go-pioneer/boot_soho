package com.soho.spring.extend;

import com.soho.spring.utils.AESUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Properties;

/**
 * Spring加载配置文件
 *
 * @author shadow
 */
public abstract class AbstractPropertyConfigurer extends PropertyPlaceholderConfigurer {

    protected final static Logger log = LoggerFactory.getLogger(AbstractPropertyConfigurer.class);

    private String[] decodeKeys;

    public AbstractPropertyConfigurer(String[] filePath, String[] decodeKeys) {
        this.decodeKeys = decodeKeys;
        if (filePath == null || filePath.length <= 0) {
            log.error("没有找到项目启动配置文件");
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
        // 遍历保存数据,丢弃敏感数据
        for (Map.Entry entry : properties.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            if ((key.endsWith("username") || key.endsWith("password"))) {
                continue;
            }
            ConfigUtils.properties.put(key, value);
        }
        String encrypt = properties.getProperty("default.encrypt");
        if (StringUtils.isEmpty(encrypt) || !Boolean.valueOf(encrypt.trim())) {
            return properties;
        }
        String projectKey = properties.getProperty("default.projectKey");
        String projectCode = properties.getProperty("default.projectCode");
        if (StringUtils.isEmpty(projectCode) || StringUtils.isEmpty(projectKey)) {
            log.error("没有找到项目编码,密钥配置");
            System.exit(-1);
        }
        String aes_key = AESUtils.decrypt(projectKey.trim());
        for (String key : decodeKeys) {
            String data = properties.getProperty(key);
            if (!StringUtils.isEmpty(data)) {
                properties.put(key, AESUtils.decrypt(data.trim(), aes_key));
            }
        }
        for (Map.Entry entry : properties.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            if ((key.endsWith("datasource.username") || key.endsWith("datasource.password")) && !StringUtils.isEmpty(value)) {
                properties.put(key, AESUtils.decrypt(entry.getValue().toString().trim(), aes_key));
            }
        }
        return properties;
    }

    public abstract Properties appendProperties(Properties properties);

}
