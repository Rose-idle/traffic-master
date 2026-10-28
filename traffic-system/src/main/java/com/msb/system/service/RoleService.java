package com.msb.system.service;

import com.msb.system.entity.RoleEntity;

import java.util.List;

public interface RoleService {

    /**
     * 添加角色
     * @param roleEntity
     * @return
     */
    public boolean addRole(RoleEntity roleEntity);

    /**
     * 查询所有角色
     * @return 角色列表
     */
    public List<RoleEntity> queryRoles();
}
