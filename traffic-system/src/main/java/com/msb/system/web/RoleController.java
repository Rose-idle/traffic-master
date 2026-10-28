package com.msb.system.web;

import com.msb.api.code.SystemCode;
import com.msb.api.commons.ResponseResult;
import com.msb.api.commons.ResponseResultFactory;
import com.msb.system.entity.RoleEntity;
import com.msb.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @RequestMapping("/addRole")
    public ResponseResult addRole(RoleEntity roleEntity){

        boolean result = roleService.addRole(roleEntity);

        return returnResponseResult(result, SystemCode.TRAFFIC_SYSTEM_SUCCESS);
    }

    @RequestMapping("/queryRoles")
    public ResponseResult queryAllRole(){
        List<RoleEntity> roleEntities = roleService.queryRoles();
        return ResponseResultFactory.buildResponseResult(SystemCode.TRAFFIC_SYSTEM_SUCCESS,roleEntities);
    }

    //返回结果视图
    private ResponseResult returnResponseResult(boolean bl , String code) {
        ResponseResult responseResult;
        if (bl) {
            responseResult = ResponseResultFactory.buildResponseResult();
        } else {
            responseResult = ResponseResultFactory.buildResponseResult(code);
        }
        return responseResult;
    }
}
