package com.terryrao.admin.vo;

import com.google.common.collect.Lists;
import com.terryrao.admin.model.AdminPermission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MenuTree implements Serializable {
    private static final long serialVersionUID = 6384598893693849820L;

    private AdminPermission model;//model代表当前模块

    private List<MenuTree> children = new ArrayList<>();//children代表当前模块下面的子模块，

    private int flag = 0;//菜单级别

    private int depth = 0;//深度


    /**
     * 递归方法根据传入的模块集合形成层级菜单
     */
    public MenuTree createTree(List<AdminPermission> ms) {
        //
        MenuTree root = new MenuTree();
        ArrayList<AdminPermission> parents = Lists.newArrayList();// 用来存储parentId为空的父节点；
        ArrayList<AdminPermission> children = Lists.newArrayList();// 用来存储不是系统的模块
        ms.forEach(permission -> {
            String parentId = permission.getParentId();
            if (StringUtils.isBlank(parentId)) {
                parents.add(permission);
            } else {
                children.add(permission);
            }
        });

        parents.forEach(parent -> {
            MenuTree node = new MenuTree();
            node.setFlag(0);
            node.setModel(parent);
            root.getChildren().add(node);
            appendChild(node, children);
        });

        root.setDepth(this.depth);
        return root;
    }

    /**
     * node 节点 children 为所以系统下的子节点
     */
    public void appendChild(MenuTree node, List<AdminPermission> children) {
        if (node == null) {
            return;
        }

        String systemId = node.getModel().getPermissionNo();
        int flag = node.getFlag();
        if (!CollectionUtils.isEmpty(children)) {
            children.forEach(child -> {
                String parentId = child.getParentId();
                if (parentId.equals(systemId)) {
                    MenuTree childTree = new MenuTree();
                    childTree.setModel(child);
                    childTree.setFlag(flag + 1);
                    int temp = flag + 1;
                    if (temp > this.getDepth())
                        this.setDepth(temp);
                    node.getChildren().add(childTree);
                    appendChild(childTree, children);
                }
            });
        }
    }
}
