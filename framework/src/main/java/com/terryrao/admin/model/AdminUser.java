package com.terryrao.admin.model;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.terryrao.admin.enums.UserStatus;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Data
public class AdminUser implements Serializable {

    private Long id; // 用户id
    private String adminNo; // admin编号
    private String name; // 用户名
    private String password; // 密码
    private String realName; // 真实姓名
    private String phone; // 手机号码
    private String email; // 邮箱
    private UserStatus status; // 状态
    private Date createTime; // 创建时间
    private Date updateTime; // 修改时间
    private String roleId;


    public Set<String> getRoleIdSet(){
        return Sets.newHashSet(Optional.ofNullable(this.getRoleId())
                .map(ids -> Lists.newArrayList(ids.split(",")))
                .orElse(Lists.newArrayList()));
    }
}
