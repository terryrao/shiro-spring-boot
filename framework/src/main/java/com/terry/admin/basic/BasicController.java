package com.terry.admin.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class BasicController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String pageNotFound() {
        return "/error";
    }
}
