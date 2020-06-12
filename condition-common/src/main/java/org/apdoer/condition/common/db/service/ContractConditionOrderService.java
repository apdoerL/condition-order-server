package org.apdoer.condition.common.db.service;

import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;

import java.math.BigDecimal;
import java.util.List;

public interface ContractConditionOrderService {
	
	int insertOrder(ContractConditionOrderPo orderPo);
	
	List<Integer> queryContractIds();
	
	List<ContractConditionOrderPo> queryUnTriggerOrderByUuid(Integer userId, String uuid);
	
	ContractConditionOrderPo queryOrder(Long conditionOrderId, Integer userId, Integer status);
	
	List<ContractConditionOrderPo> getUnTriggerOrder(int userMod);
	
	int updateStatus(Long conditionOrderId, Integer userId, Integer status);
	
	
	List<ContractConditionOrderPo> queryHistory(Integer userId, Integer contractId, Integer side, Integer status);
	
	int updateRecordStatusSucc(String orderId, Long conditionOrderId, Integer userId, BigDecimal realTriggerPrice);
	
	int updateRecordStatusFail(Long conditionOrderId, Integer code, Integer userId, BigDecimal realTriggerPrice);

}
