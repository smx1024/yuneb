package com.sx.config;

import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "aliyun.oss.file")
@Component
@Setter
public class ConstantOssPropertiesUtils implements InitializingBean {
    public static String ENDPOINT;

    public static String ACCESSKEYID;
    public static String ACCESSKEYSECRET;
    public static String BUCKETNAME;

    String endpoint;
    String accessKeyId;
    String accessKeySecret;
    String bucketName;

    @Override
    public void afterPropertiesSet() throws Exception {
        ENDPOINT = endpoint;
        ACCESSKEYID = accessKeyId;
        ACCESSKEYSECRET = accessKeySecret;
        BUCKETNAME = bucketName;
    }
}
