package com.terryrao.shiro.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "tr.shiro",
        ignoreUnknownFields = false/*有属性不能匹配到声明的域的时候抛出异常 */)
@Setter
@Getter
@ToString
public class ShiroProperties {

    private String rememberSeed;

    private String loginUrl;

    private String unauthorizedPath;

    private ShiroCache cache;
}
