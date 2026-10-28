package com.msb.system.web;

import com.msb.api.code.SystemCode;
import com.msb.api.commons.ResponseResult;
import com.msb.api.commons.ResponseResultFactory;
import com.msb.api.commons.SystemUtils;
import com.msb.system.entity.UserEntity;
import com.msb.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    //我在哪个类里面打印的日志
    final Logger logger = LoggerFactory.getLogger(UserController.class);

    final long UPD_USER_ZERO = 0;//代表id是0
    final int NAME_LENGTH = 3; //用户名的长度
    final int ACCOUNT_LENGTH = 6;//账号的长度
    final int PASS_LENGTH = 6;//密码的长度

    @Autowired
    UserService userService;

    /**
     * 添加用户的请求
     * @param userEntity
     * @return 是否成功
     */
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public ResponseResult addUser(UserEntity userEntity){

        logger.info("system user addUser start");

        //参数为空
        if(SystemUtils.isNull(userEntity)){
            logger.error("system user addUser UserEntity is null");
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_PARAM_NULL);
            logger.info("system user addUser return msg : " + responseResult);
            return responseResult;
        }

        //用户名为空
        if(SystemUtils.isNullOrEmpty(userEntity.getUname())){
            logger.error("system user addUser uname is null");
            logger.info("param : " + userEntity);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_NAME_NULL,SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_NAME_NULL_MSG);
            logger.info("system user addUser return msg : " + responseResult);
            return responseResult;
        }

        //账号为空
        if(SystemUtils.isNullOrEmpty(userEntity.getUaccount())){
            logger.error("system user addUser uaccount is null");
            logger.info("param : " + userEntity);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_ACCOUNT_NULL);
            logger.info("system user addUser return msg : " + responseResult);
            return responseResult;
        }

        //密码为空
        if(SystemUtils.isNullOrEmpty(userEntity.getUpass())){
            logger.error("system user addUser upass is null");
            logger.info("param : " + userEntity);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_PASS_NULL);
            logger.info("system user addUser return msg : " + responseResult);
            return responseResult;
        }

        //手机为空
        if(SystemUtils.isNullOrEmpty(userEntity.getUphone())){
            logger.error("system user addUser uphone is null");
            logger.info("param : " + userEntity);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_PHONE_NULL);
            logger.info("system user addUser return msg : " + responseResult);
            return responseResult;
        }

        //用户名小于3个长度
        if(userEntity.getUname().trim().length() < NAME_LENGTH){
            logger.error("system user addUser uname length < 3");
            logger.info("param : " + userEntity);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_NAME_SIZE);
            logger.info("system user addUser return msg : " + responseResult);
            return responseResult;
        }

        //账号的长度
        if(userEntity.getUaccount().trim().length() < ACCOUNT_LENGTH){
            logger.error("system user addUser uaccount length < 3");
            logger.info("param : " + userEntity);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_ACCOUNT_SIZE);
            logger.info("system user addUser return msg : " + responseResult);
            return responseResult;
        }

        //密码的长度
        if(userEntity.getUpass().trim().length() < PASS_LENGTH){
            logger.error("system user addUser upass length < 3");
            logger.info("param : " + userEntity);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_PASS_SIZE);
            logger.info("system user addUser return msg : " + responseResult);
            return responseResult;
        }

        //千万不要在这里记录密码;违反安全规范的
        //密码的加密操作
        logger.info("system user addUser pass digest");
        userEntity.setUpass(DigestUtils.md5DigestAsHex(userEntity.getUpass().getBytes()));

        //保存用户信息
        logger.info("system user addUser userService addUser start");
        boolean result = userService.addUser(userEntity);
        logger.info("system user addUser userService addUser end : " + result);

        logger.info("system user addUser end");
        //根据保存结果返回给前台信息
        return returnResponseResult(result,SystemCode.SYSTEM_USER_ERROR_ADD_FAIL);
    }

    /**
     * 删除用户的请求
     * @param uid = "1" 删除一个;   uid="1,2,3" 删除多个用逗号分隔
     * @return 是否成功
     */
    @RequestMapping(value = "/delUser",method = RequestMethod.POST)
    public ResponseResult delUser(String uid){

        logger.info("system user delUser start");

        //传过来的参数是空的
        if(SystemUtils.isNullOrEmpty(uid)){
            logger.error("system user delUser uid is null");
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_DEL_FAIL_UID_NULL);
            logger.info("system user delUser return msg : " + responseResult);
            return responseResult;
        }

        //简单的逻辑判断;  1: 可以在这里判断; 2: 交给业务层判断
        logger.info("system user delUser UserService start");
        boolean bl = userService.delUser(uid);
        logger.info("system user delUser UserService end "+bl);

        ResponseResult responseResult;
        if(bl){
            logger.info("system user delUser success ");
            responseResult = new ResponseResult(SystemCode.TRAFFIC_SYSTEM_SUCCESS);
        }else{
            logger.error("system user delUser fail");
            responseResult = new ResponseResult(SystemCode.TRAFFIC_SYSTEM_ERROR);
        }

        logger.info("system user delUser end ");
        return responseResult;
    }

    /**
     * 更新用户信息
     * @param userEntity
     * @return
     */
    @RequestMapping(value = "/updUser",method = RequestMethod.POST)
    public ResponseResult updUser(UserEntity userEntity){

        logger.info("system user updUser start");

        //参数为空
        if(SystemUtils.isNull(userEntity)){
            logger.error("system user updUser UserEntity is null");
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_UPD_FAIL_PARAM_NULL);
            logger.info("system user updUser return msg : " + responseResult);
            return responseResult;
        }

        //没有传id
        if(SystemUtils.isNull(userEntity.getUid()) || userEntity.getUid() == UPD_USER_ZERO){
            logger.error("system user updUser uid is null");
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_UPD_FAIL_UID_NULL);
            logger.info("system user updUser uid msg : " + responseResult);
            return responseResult;
        }

        logger.info("system user updUser service start :" + userEntity);
        boolean bl = userService.updUser(userEntity);
        logger.info("system user updUser service end :" + bl);

        logger.info("system user updUser end");
        return returnResponseResult(bl,SystemCode.SYSTEM_USER_ERROR_UPD_FAIL);

    }

    /**
     * 查询所有的用户信息
     * @return
     */
    @RequestMapping(value = "/allUser")
    public ResponseResult<List<UserEntity>> findAllUser(){

        //虽然我接下来不回去打印日志了,你们要去根据上面的规范去打印日志
        List<UserEntity> allUser = userService.findAllUser();
        ResponseResult<List<UserEntity>> responseResult = ResponseResultFactory.buildResponseResult(SystemCode.TRAFFIC_SYSTEM_SUCCESS,allUser);
        return responseResult;
    }

    /**
     * 根据条件查询数据
     * @param userEntity
     * @return 查询列表
     */
    @RequestMapping("/findUsers")
    public ResponseResult<List<UserEntity>> findUsersByWhere(UserEntity userEntity){

        List<UserEntity> users = userService.findAllUserByWhere(userEntity);
        ResponseResult<List<UserEntity>> responseResult = ResponseResultFactory.buildResponseResult(SystemCode.TRAFFIC_SYSTEM_SUCCESS,users);
        return responseResult;
    }

    /**
     * 根据条件查询数据
     * @param userEntity
     * @return 查询列表
     */
    @RequestMapping("/findUsersByTime")
    public ResponseResult<List<UserEntity>> findUsersByTime(UserEntity userEntity){

        List<UserEntity> users = userService.findUsersByTime(userEntity.getStartTime(),userEntity.getEndTime());
        ResponseResult<List<UserEntity>> responseResult = ResponseResultFactory.buildResponseResult(SystemCode.TRAFFIC_SYSTEM_SUCCESS,users);
        return responseResult;
    }

    /**
     * 通用的分页查询
     * 排序,分页,条件查询
     * @param userEntity
     * @return
     */
    @RequestMapping("/queryUsers")
    public ResponseResult queryUsers(UserEntity userEntity){

        //分页查询+条件查询
        Map<String, Object> map = userService.queryUsers(userEntity);
        ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.TRAFFIC_SYSTEM_SUCCESS,map);

        return responseResult;
    }

    /**
     * 查询用户的写法之一
     * @return
     */
    private List<UserEntity> findAllUser2(){

        //虽然我接下来不回去打印日志了,你们要去根据上面的规范去打印日志
        List<UserEntity> allUser = userService.findAllUser();
        return allUser;
    }

    //返回结果视图
    private ResponseResult returnResponseResult(boolean bl , String code) {
        ResponseResult responseResult;
        if (bl) {
            responseResult = ResponseResultFactory.buildResponseResult();
        } else {
            responseResult = ResponseResultFactory.buildResponseResult(code);
            logger.error("system user  returnResponseResult fail : " + code);
        }

        logger.info("system user  returnResponseResult , return :" + responseResult);
        return responseResult;
    }

}
