package com.terryrao;

import com.terryrao.shiro.config.FreeMarkerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(FreeMarkerConfig.class)
public class BaseConfig {
}
