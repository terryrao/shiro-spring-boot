package com.terryrao.admin.enums;


import org.apache.commons.lang3.StringUtils;

public enum UserStatus {
    JY("禁用"),
    QY("启用"),
    SD("锁定"),
    JS("解锁");


    private String chineseName;

    UserStatus(String chineseName) {
        this.chineseName = chineseName;
    }

    /**
     * 获取中文名称.
     */
    public String getChineseName() {
        return chineseName;
    }

    /**
     * 解析字符串.
     */
    public static UserStatus parse(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        try {
            return UserStatus.valueOf(value);
        } catch (Throwable t) {
            return null;
        }
    }
}
