package com.msb.system.util;

import com.msb.system.entity.RoleEntity;
import com.msb.system.entity.UserEntity;
import com.msb.system.info.RoleInfo;
import com.msb.system.info.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {
    /**
     * 转换list
     * @param userInfos
     * @return
     */
    public static List<UserEntity> userInfosConvertUserEntitys(List<UserInfo> userInfos){

        List<UserEntity> list = new ArrayList<>();

        for(UserInfo u : userInfos){
            list.add(userInfoConvertUserEntity(u));
        }

        return list;
    }

    /**
     * 将 UserInfo转换成 UserEntity
     * @param userInfo
     * @return UserEntity
     */
    public static UserEntity userInfoConvertUserEntity(UserInfo userInfo){

        UserEntity userEntity = new UserEntity();
        userEntity.setT1(userInfo.getT1());
        userEntity.setDesc(userInfo.getUdesc());
        userEntity.setUaccount(userInfo.getUaccount());
        userEntity.setUid(userInfo.getUid());
        userEntity.setUmail(userInfo.getUmail());
        userEntity.setUphone(userInfo.getUphone());
        userEntity.setUname(userInfo.getUname());
        userEntity.setUtime(userInfo.getUtime());

        if(null != userInfo.getRoles()){
            userEntity.setRoles(roleInfosConvertRoleEntitys(userInfo.getRoles()));
        }

        return userEntity;
    }

    /**
     * 将UserEntity 转换成  UserInfo
     * @param userEntity
     * @return UserInfo
     */
    public static UserInfo userEntityConvertUserInfo(UserEntity userEntity){

        UserInfo userInfo = new UserInfo();

        userInfo.setT1(userEntity.getT1());
        userInfo.setUaccount(userEntity.getUaccount());

        //注意一下
        userInfo.setUdesc(userEntity.getDesc());

        userInfo.setUid(userEntity.getUid());
        userInfo.setUmail(userEntity.getUmail());
        userInfo.setUname(userEntity.getUname());
        userInfo.setUpass(userEntity.getUpass());
        userInfo.setUphone(userEntity.getUphone());

        return userInfo;
    }

    /**
     * 集合转换
     * @param roleInfos
     * @return
     */
    public static List<RoleEntity> roleInfosConvertRoleEntitys(List<RoleInfo> roleInfos){

        List<RoleEntity> list = new ArrayList<>();
        for(RoleInfo r : roleInfos){
            list.add(roleInfoConvertRoleEntity(r));
        }

        return list;
    }

    //实体类的转换
    public static RoleEntity roleInfoConvertRoleEntity(RoleInfo roleInfo){
        RoleEntity roleEntity = new RoleEntity();

        roleEntity.setRid(roleInfo.getRid());
        roleEntity.setRedesc(roleInfo.getRdesc());
        roleEntity.setRname(roleInfo.getRname());
        roleEntity.setRtype(roleInfo.getRtype());

        //解决鸡生蛋生鸡的问题;规避角色里面有用户,用户里面有角色;
        if(null != roleInfo.getUsers() && roleInfo.getUsers().size() > 0
                && roleInfo.getUsers().get(0).getRoles() == null){
            roleEntity.setUsers(userInfosConvertUserEntitys(roleInfo.getUsers()));
        }

        return roleEntity;
    }

    //转换
    public static RoleInfo roleEntityConvertRoleInfo(RoleEntity roleEntity){
        RoleInfo roleInfo = new RoleInfo();

        roleInfo.setRid(roleEntity.getRid());
        roleInfo.setRname(roleEntity.getRname());
        roleInfo.setRtype(roleEntity.getRtype());
        roleInfo.setRdesc(roleEntity.getRedesc());

        return roleInfo;
    }

}
