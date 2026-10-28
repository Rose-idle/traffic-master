package com.msb.system.repostitory;

import com.msb.system.info.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 负责数据查询的接口
 */
public interface UserRepository extends JpaRepository<UserInfo,Long>, JpaSpecificationExecutor<UserInfo> {

    //批量更新
    @Modifying
    @Transactional
    @Query("update UserInfo set ustatus = 1 where uid in (?1)")
    public int updates(Collection<Long> ids);


    /**
     * 根据条件查询用户信息
     */
    @Query("select ui from UserInfo ui where ui.uphone = ?1")
    public List<UserInfo> findUsers1(String phone);

    /**
     * 就是使用sql语句来进行查询
     * @param email
     * @return
     */
    @Query(nativeQuery=true,value="select * from t_user where umail = ?1")
    public List<UserInfo> findUsers2(String email);

    public List<UserInfo> findAllByUtimeBetween(Date t1 , Date t2);

}
