package com.terryrao.shiro.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * freemarker 相关配置
 */

public class FreeMarkerConfig {

    @Autowired
    private FreeMarkerConfigurer configuration;

    /**
     * 配置shiro 相关标签
     */
    @PostConstruct
    public void getShiroTagFreeMarkerConfigurer() throws IOException, TemplateException {
        configuration.afterPropertiesSet();
        configuration.getConfiguration().setSharedVariable("shiro", new ShiroTags());
        // warning : 请求里给变量取名的时候不需要使用 shiro 否则会覆盖此声明，导致模板渲染错误

        /*Map<String,Object> variables = new HashMap<>();
        variables.put("xml_escape",new XmlEscape());
        configuration.setFreemarkerVariables(variables)*/
        ;
    }

/*

    @Bean("multipartResolver")
    public CommonsMultipartResolver  getCommonsMultipartResolver() {
        return new CommonsMultipartResolver();
    }
*/


}
