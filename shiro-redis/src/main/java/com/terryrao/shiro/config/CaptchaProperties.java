package com.terryrao.shiro.config;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "tr.captcha",
        ignoreUnknownFields = false/*有属性不能匹配到声明的域的时候抛出异常 */)
@Setter
@Getter
@ToString
public class CaptchaProperties {

    private String randomWord; //随机字符

    private Integer minAcceptedWordLength; //产生的随机字符中的最少字符数

    private Integer maxAcceptedWordLength; //产生的随机字符中的最多字符数
    //生成颜色的rgb值
    private Integer r; //red
    private Integer b; //blue
    private Integer g; //green
}
