package com.terryrao.admin.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Data
public class AdminRole implements Serializable {
    private Long id; // 角色ID
    private String roleNo; // 角色编号
    private String roleName; //
    private String roleDescn; // 角色描述
    private Date createTime; // 创建时间
    private Date updateTime; // 修改时间
}
