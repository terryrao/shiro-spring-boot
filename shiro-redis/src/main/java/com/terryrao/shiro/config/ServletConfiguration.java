package com.terryrao.shiro.config;

import com.tx.common.servlet.JcaptchaServlet;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注册servlet
 */
@Configuration
public class ServletConfiguration extends SpringBootServletInitializer {
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ServletConfiguration.class);
    }

    @Bean
    public JcaptchaServlet jcaptchaServlet() {
        return new JcaptchaServlet();
    }

    @Bean
    public ServletRegistrationBean jcaptchaServletRegistration() {
        return new ServletRegistrationBean(jcaptchaServlet(), "/txjcaptcha.svl");

    }

}
