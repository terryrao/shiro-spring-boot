package com.terryrao.admin.vo;


import com.terryrao.admin.enums.MenuType;
import com.terryrao.admin.enums.UserStatus;
import com.terryrao.admin.model.AdminPermission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@ToString
public class MenuVo extends AdminPermission {
    private String parentName;
    private List<MenuVo> children = new ArrayList<>();
    private UserStatus roleCanUse;

    public String getMenuTypeCn() {
        return MenuType.getCnNameByValue(this.getType());
    }

}
