package com.terryrao.shiro;


import com.google.common.base.Objects;
import com.terry.admin.model.AdminUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ShiroUser implements Serializable {
    private static final long serialVersionUID = -987494180888127371L;
    private String username;
    private String adminNo;
    private String realName;
    private String roleId;
    private String password;

    public ShiroUser(AdminUser adminUser) {
        username = adminUser.getName();
        adminNo = adminUser.getAdminNo();
        realName = adminUser.getRealName();
        roleId = adminUser.getRoleId();
        password = adminUser.getPassword();
    }

    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
        return username;
    }

    /**
     * 重载hashCode,只计算loginName;
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    /**
     * 重载equals,只计算loginName;
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShiroUser other = (ShiroUser) obj;
        if (username == null) {
            if (other.getUsername() != null)
                return false;
        } else if (!username.equals(other.getUsername()))
            return false;
        return true;
    }

}
