package com.terry.admin.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 菜单类型
 */
public enum MenuType {

    MENU("菜单", "MENU"),

    BUTTON("按钮", "BUTTON");


    private String chineseName;

    private String value;

    MenuType(String chineseName, String value) {
        this.chineseName = chineseName;
        this.value = value;
    }

    /**
     * 获取中文名称.
     */
    public String getChineseName() {
        return chineseName;
    }

    public String getValue() {
        return value;
    }

    /**
     * 解析字符串.
     */
    public static MenuType parse(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        try {
            return MenuType.valueOf(value);
        } catch (Throwable t) {
            return null;
        }
    }

    public static String getCnNameByValue(String value) {
        for (MenuType as : MenuType.values())
            if (as.getValue().equals(value))
                return as.getChineseName();
        return "";
    }
}
