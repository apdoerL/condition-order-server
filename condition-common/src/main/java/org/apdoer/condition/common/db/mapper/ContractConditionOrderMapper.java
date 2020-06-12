package org.apdoer.condition.common.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.apdoer.common.service.common.BaseMapper;
import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;

import java.math.BigDecimal;
import java.util.List;

public interface ContractConditionOrderMapper extends BaseMapper<ContractConditionOrderPo> {

    List<Integer> queryContractIds();

    ContractConditionOrderPo queryOrder(@Param("tableName") String tableName, @Param("conditionOrderId") Long conditionOrderId, @Param("userId") Integer userId, @Param("status") Integer status);

    List<ContractConditionOrderPo> queryUnTriggerOrderByUuid(@Param("tableName") String tableName, @Param("uuid") String uuid);

    List<ContractConditionOrderPo> getUnTriggerOrder(@Param("tableName") String tableName);

    int updateStatus(@Param("tableName") String tableName, @Param("conditionOrderId") Long conditionOrderId,
                     @Param("userId") Integer userId, @Param("status") Integer status);


    List<ContractConditionOrderPo> queryHistory(@Param("tableName") String tableName, @Param("userId") Integer userId,
                                                @Param("contractId") Integer contractId, @Param("side") Integer side, @Param("status") Integer status);

    int updateRecordStatusSucc(@Param("tableName") String tableName, @Param("orderId") String orderId,
                               @Param("conditionOrderId") Long conditionOrderId, @Param("userId") Integer userId, @Param("realTriggerPrice") BigDecimal realTriggerPrice);

    int updateRecordStatusFail(@Param("tableName") String tableName, @Param("conditionOrderId") Long conditionOrderId,
                               @Param("code") Integer code, @Param("userId") Integer userId, @Param("realTriggerPrice") BigDecimal realTriggerPrice);
}