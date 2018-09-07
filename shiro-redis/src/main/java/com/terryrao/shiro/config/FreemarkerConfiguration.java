package com.terryrao.shiro.config;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Map;

/**
 * @Description：
 * @author： linjianding on 2018/5/14.
 * @data 2018/5/14.
 */
@Configuration
public class FreemarkerConfiguration extends FreeMarkerAutoConfiguration.FreeMarkerWebConfiguration   {
    @Value("${static.admin.url}")
    private String StaticUrl;

    @Override
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = super.freeMarkerConfigurer();

        Map<String, Object> freemarkerVariables = Maps.newHashMap();
        freemarkerVariables.put("staticUrl", StaticUrl);


        configurer.setFreemarkerVariables(freemarkerVariables);
        return configurer;
    }
}
