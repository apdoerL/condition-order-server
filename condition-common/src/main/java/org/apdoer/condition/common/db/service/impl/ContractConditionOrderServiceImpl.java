package org.apdoer.condition.common.db.service.impl;

import org.apdoer.condition.common.constants.CommonConstants;
import org.apdoer.condition.common.db.mapper.ContractConditionOrderMapper;
import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.common.db.service.ContractConditionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ContractConditionOrderServiceImpl implements ContractConditionOrderService {
	
	@Autowired
	private ContractConditionOrderMapper contractConditionOrderMapper;
	
	@Override
	public int insertOrder(ContractConditionOrderPo orderPo) {
		String tablename = CommonConstants.CONDITION_ORDER_TABLE_PRE + orderPo.getUserId() % 10;
		orderPo.setTableName(tablename);
		return this.contractConditionOrderMapper.insertSelective(orderPo);
	}
	
	@Override
	public List<Integer> queryContractIds() {
		return this.contractConditionOrderMapper.queryContractIds();
	}
	
	@Override
	public List<ContractConditionOrderPo> queryUnTriggerOrderByUuid(Integer userId, String uuid) {
		String tablename = CommonConstants.CONDITION_ORDER_TABLE_PRE + userId % 10;
		return this.contractConditionOrderMapper.queryUnTriggerOrderByUuid(tablename, uuid);
	}
	
	@Override
	public ContractConditionOrderPo queryOrder(Long conditionOrderId, Integer userId, Integer status) {
		String tablename = CommonConstants.CONDITION_ORDER_TABLE_PRE + userId % 10;
		return this.contractConditionOrderMapper.queryOrder(tablename, conditionOrderId, userId, status);
	}

	@Override
	public List<ContractConditionOrderPo> getUnTriggerOrder(int userMod) {
		String tablename = CommonConstants.CONDITION_ORDER_TABLE_PRE + userMod;
		return this.contractConditionOrderMapper.getUnTriggerOrder(tablename);
	}

	@Override
	public int updateStatus(Long conditionOrderId, Integer userId, Integer status) {
		String tablename = CommonConstants.CONDITION_ORDER_TABLE_PRE + userId % 10;
		return this.contractConditionOrderMapper.updateStatus(tablename, conditionOrderId, userId, status);
	}

	@Override
	public List<ContractConditionOrderPo> queryHistory(Integer userId, Integer contractId,
			Integer side, Integer status) {
		String tablename = CommonConstants.CONDITION_ORDER_TABLE_PRE + userId % 10;
		return this.contractConditionOrderMapper.queryHistory(tablename, userId, contractId, side, status);
	}

	@Override
	public int updateRecordStatusSucc(String orderId, Long conditionOrderId, Integer userId, BigDecimal realTriggerPrice) {
		String tablename = CommonConstants.CONDITION_ORDER_TABLE_PRE + userId % 10;
		return this.contractConditionOrderMapper.updateRecordStatusSucc(tablename, orderId, conditionOrderId, userId, realTriggerPrice);
	}

	@Override
	public int updateRecordStatusFail(Long conditionOrderId, Integer code, Integer userId, BigDecimal realTriggerPrice) {
		String tablename = CommonConstants.CONDITION_ORDER_TABLE_PRE + userId % 10;
		return this.contractConditionOrderMapper.updateRecordStatusFail(tablename, conditionOrderId, code, userId, realTriggerPrice);
	}

}
