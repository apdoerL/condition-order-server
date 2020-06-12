package org.apdoer.condition.common.db.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apdoer.common.service.common.BaseMapper;
import org.apdoer.condition.common.db.model.po.ContractChannelMappingPo;

import java.util.List;

/**
 * 可以通过此注解开启二级缓存
 *
 * @author apdoer
 */
@CacheNamespace(blocking = true)
public interface ContractChannelMappingMapper extends BaseMapper<ContractChannelMappingPo> {

    /**
     * 查询所有条件单相关通道配置
     *
     * @return
     */
    List<ContractChannelMappingPo> queryAllMapping();

    /**
     * 按合约查询通道配置
     *
     * @param var1
     * @return
     */
    ContractChannelMappingPo queryMappingByContractId(@Param("contractId") Integer var1);
}
