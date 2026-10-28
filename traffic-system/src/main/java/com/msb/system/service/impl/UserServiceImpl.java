package com.msb.system.service.impl;

import com.msb.api.commons.SystemUtils;
import com.msb.system.entity.UserEntity;
import com.msb.system.info.RoleInfo;
import com.msb.system.info.UserInfo;
import com.msb.system.repostitory.RoleRepository;
import com.msb.system.repostitory.UserRepository;
import com.msb.system.service.UserService;
import com.msb.system.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    final int ZERO = 0;
    final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository repository;

    /**
     * 添加用户
     * @param userEntity
     * @return true:保存成功
     */
    public boolean addUser(UserEntity userEntity){
        UserInfo user = null;
        logger.info("system user service addUser start : " + userEntity);

        logger.info("system user service addUser userRepository save start ");
        try{
            UserInfo userInfo = ConvertUtil.userEntityConvertUserInfo(userEntity);

            //设定角色信息
            if(!SystemUtils.isNullOrEmpty(userEntity.getRolesStr())){
                String[] roleids = userEntity.getRolesStr().split(",");

                List<RoleInfo> roleInfos = new ArrayList<>();
                for(String ids : roleids){ //遍历角色的id

                    if(null != repository.findById(Long.parseLong(ids)).get()){
                        roleInfos.add(repository.findById(Long.parseLong(ids)).get());
                    }
                }

                //设置关系;  添加这个用户,它有哪些角色;把数据保存到第三方表
                userInfo.setRoles(roleInfos);
            }

            user = userRepository.save(userInfo);
            logger.info("system user service addUser userRepository save end " );
            logger.info("user:" + user);
        }catch (Exception exception){
            logger.error("system user service addUser fail" + exception);
            return false;
        }

        if(!SystemUtils.isNull(user) && user.getUid() != ZERO){
            logger.info("system user service addUser success");
            return true;
        }
        logger.error("system user service addUser fail");
        return false;
    }

    /**
     * 删除用户
     * @param uid 需要删除的id
     * @return true:删除成功
     */
    @Override
    public boolean delUser(String uid) {

        //删除一条删除多条的业务逻辑
        String[] ids = uid.split(",");

        if(SystemUtils.isNull(ids) || ids.length == 0){
            return false;
        }

        //删除一条
        if(ids.length == 1){
            //先查询后更新;
            try{
                Long id = Long.parseLong(ids[0]);
                UserInfo userInfo = userRepository.findById(id).get();
                userInfo.setUstatus(1);
                userRepository.save(userInfo);
                return true;
            }catch (Exception exception){
                return false;
            }

        }else{ //删除多条

            try{
                Set<Long> sets = new HashSet<>();
                for(String id : ids){
                    sets.add(Long.parseLong(id));
                }
                userRepository.updates(sets);
                return true;
            }catch (Exception exception){
                return false;
            }
        }
    }

    /**
     * 修改用户
     * @param userEntity 需要修改的信息
     * @return true:修改成功
     */
    @Override
    public boolean updUser(UserEntity userEntity) {

        //1: 去数据库里面查询有没有这个记录
        UserInfo userInfo = userRepository.findById(userEntity.getUid()).get();
        //2:根据你前端来的信息去进行修改

        //代表这个信息不是空;需要修改
        if(!SystemUtils.isNullOrEmpty(userEntity.getUname())){
            userInfo.setUname(userEntity.getUname());
        }

        //手机修改
        if(!SystemUtils.isNullOrEmpty(userEntity.getUphone())){
            userInfo.setUphone(userEntity.getUphone());
        }

        if(!SystemUtils.isNullOrEmpty(userEntity.getUmail())){
            userInfo.setUmail(userEntity.getUmail());
        }

        UserInfo save = userRepository.save(userInfo);

        return true;
    }

    /**
     * 查询所有的用户
     * @return
     */
    @Override
    public List<UserEntity> findAllUser() {

        List<UserInfo> allinfo = userRepository.findAll();

        return ConvertUtil.userInfosConvertUserEntitys(allinfo);
    }

    /**
     * 条件查询
     * @param userEntity
     * @return
     */
    public List<UserEntity> findAllUserByWhere(UserEntity userEntity){

        //将前端来的实体类转换成数据库用的实体类
        UserInfo userInfo = ConvertUtil.userEntityConvertUserInfo(userEntity);

        //匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("uphone",m->m.contains())
                .withIgnorePaths("uid")
                .withIgnorePaths("ustatus");

        //根据实体类去构建查询条件
        Example example = Example.of(userInfo,matcher);

        //根据咱们的条件去查询数据
        List<UserInfo> all = userRepository.findAll(example);

        return ConvertUtil.userInfosConvertUserEntitys(all);
    }

    /**
     * 根据时间查询
     * @param t1 起始时间
     * @param t2 结束时间
     * @return
     */
    public List<UserEntity> findUsersByTime(String t1,String t2){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<UserInfo> users = null;
        try {
            users = userRepository.findAllByUtimeBetween(sdf.parse(t1),sdf.parse(t2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ConvertUtil.userInfosConvertUserEntitys(users);
    }

    /**
     * 分页查询;
     * 条件查询;
     * @param userEntity
     * @return
     */
    public Map<String,Object> queryUsers(UserEntity userEntity){

        //分页的对象 排序
        //默认的分页规则;
        Pageable of = PageRequest.of(userEntity.getCurrentPage(), userEntity.getPageSize());

        //如果需要排序,按照指定字段排序;
        if(!SystemUtils.isNullOrEmpty(userEntity.getSort())){
            String[] sorts = null;
            sorts = userEntity.getSort().split(",");

            if("ASC".equals(userEntity.getSortType())){
                of = PageRequest.of(userEntity.getCurrentPage(), userEntity.getPageSize(), Sort.Direction.ASC,sorts);
            }else{
                of = PageRequest.of(userEntity.getCurrentPage(), userEntity.getPageSize(), Sort.Direction.DESC,sorts);
            }
        }

        //条件查询
        Specification<UserInfo> spec = new Specification<UserInfo>() {
            @Override
            public Predicate toPredicate(Root<UserInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {

                //自定义条件的对象
                Predicate predicate = cb.conjunction();

                //根据邮箱查询
                if(!SystemUtils.isNullOrEmpty(userEntity.getUmail())){
                    predicate.getExpressions().add(cb.equal(root.get("umail"),userEntity.getUmail()));
                }

                //根据手机是否模糊匹配查询
                if(!SystemUtils.isNullOrEmpty(userEntity.getUphone())){
                    predicate.getExpressions().add(cb.like(root.get("uphone").as(String.class),"%"+userEntity.getUphone()+"%"));
                }

                //起始时间
                if(!SystemUtils.isNullOrEmpty(userEntity.getStartTime())){
                    predicate.getExpressions().add(
                            cb.greaterThanOrEqualTo(root.get("utime").as(String.class),userEntity.getStartTime())
                    );
                }

                //终止时间
                if(!SystemUtils.isNullOrEmpty(userEntity.getEndTime())){
                    predicate.getExpressions().add(
                            cb.lessThanOrEqualTo(root.get("utime").as(String.class),userEntity.getEndTime())
                    );
                }

                return predicate;
            }
        };

        //根据指定的分页和条件查询规则查询当前页内容
        Page<UserInfo> page = userRepository.findAll(spec,of);

        //定义我们的数据库查询出来的数据
        List<UserInfo> content = page.getContent();
        //转成我们前端使用的对象;
        List<UserEntity> users = ConvertUtil.userInfosConvertUserEntitys(content);

        //通过页内容,构建数据
        Map<String,Object> map = new HashMap<>();
        map.put("users",users); //查询到的列表信息
        map.put("totalPage",page.getTotalPages());//总页数
        map.put("currentPage",userEntity.getCurrentPage());
        map.put("pageSize",userEntity.getPageSize());

        return map;
    }



}
