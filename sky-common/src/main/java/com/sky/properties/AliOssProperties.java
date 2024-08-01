package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 通过 @ConfigurationProperties(prefix = "sky.alioss") 注解可以将配置文件中的属性映射到 Java 类的字段上，这是因为 Spring Boot 提供了一种自动绑定配置文件属性到 Java Bean 的机制。
 */
@Component
@ConfigurationProperties(prefix = "sky.alioss")
@Data
public class AliOssProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

}
