package com.msb.system.repostitory;

import com.msb.system.info.RoleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 负责数据查询的接口
 */
public interface RoleRepository extends JpaRepository<RoleInfo,Long>, JpaSpecificationExecutor<RoleInfo> {

}
