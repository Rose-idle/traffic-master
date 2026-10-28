package com.msb.system.service.impl;

import com.msb.system.entity.RoleEntity;
import com.msb.system.info.RoleInfo;
import com.msb.system.repostitory.RoleRepository;
import com.msb.system.service.RoleService;
import com.msb.system.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public boolean addRole(RoleEntity roleEntity) {
        RoleInfo save = roleRepository.save(ConvertUtil.roleEntityConvertRoleInfo(roleEntity));
        return null != save && save.getRid() != 0;
    }

    /**
     * 查询所有角色
     * @return
     */
    @Override
    public List<RoleEntity> queryRoles() {

        List<RoleInfo> roles = roleRepository.findAll();
        return ConvertUtil.roleInfosConvertRoleEntitys(roles);
    }


}
