package org.apdoer.condition.core.utils;


import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.core.model.dto.ConditionOrderMqDto;
import org.apdoer.condition.core.model.vo.OrderConditionHisQueryRespVo;
import org.apdoer.condition.core.model.vo.OrderConditionReqVo;

import java.math.BigDecimal;
import java.util.Date;

public class ModeBuildlUtils {
	
	public static ConditionOrderMqDto buildConditionOrderMqDto(ContractConditionOrderPo orderPo) {
		ConditionOrderMqDto orderDto = new ConditionOrderMqDto();
		orderDto.setUserId(orderPo.getUserId());
		orderDto.setConditionOrderId(orderPo.getConditionOrderId().toString());
		orderDto.setContractId(orderPo.getContractId());
		orderDto.setQuantity(orderPo.getQuantity());
		orderDto.setSide(orderPo.getSide());
		orderDto.setOrderType(orderPo.getOrderType());
		orderDto.setOrderPrice(orderPo.getOrderPrice());
		orderDto.setPositionEffect(orderPo.getPositionEffect());
		orderDto.setMarginType(orderPo.getMarginType());
		orderDto.setMarginRate(orderPo.getMarginRate());
		orderDto.setTriggerType(orderPo.getTriggerType());
		orderDto.setTriggerPrice(orderPo.getTriggerPrice());
		orderDto.setRealTriggerPrice(orderPo.getRealTriggerPrice());
		orderDto.setConditionOrderType(orderPo.getConditionOrderType());
		orderDto.setUuid(orderPo.getUuid());
		orderDto.setCode(orderPo.getCode());
		orderDto.setOrderId(orderPo.getOrderId());
		orderDto.setStatus(orderPo.getStatus());
		orderDto.setCurtPrice(orderPo.getCurtPrice());
		orderDto.setUpdateTime(System.currentTimeMillis());
		orderDto.setCreateTime(orderPo.getCreateTime().getTime());
		return orderDto;
	}
	
	public static ContractConditionOrderPo buildContractConditionOrderPo(Integer userId, BigDecimal curtPrice, Long conditionOrderId, 
			OrderConditionReqVo requestVo) {
		ContractConditionOrderPo orderPo = new ContractConditionOrderPo();
		orderPo.setUserId(userId);
		orderPo.setConditionOrderId(conditionOrderId);
		orderPo.setContractId(requestVo.getContractId());
		orderPo.setQuantity(requestVo.getQuantity());
		orderPo.setSide(requestVo.getSide());
		orderPo.setOrderType(requestVo.getOrderType());
		orderPo.setOrderPrice(requestVo.getOrderPrice());
		orderPo.setPositionEffect(requestVo.getPositionEffect());
		orderPo.setMarginType(requestVo.getMarginType());
		if (null != requestVo.getMarginRate()) {
			orderPo.setMarginRate(requestVo.getMarginRate());
		} else {
			orderPo.setMarginRate(BigDecimal.ZERO);
		}
		orderPo.setMinimalQuantity(requestVo.getMinimalQuantity());
		orderPo.setTriggerPrice(requestVo.getTriggerPrice());
		orderPo.setTriggerType(requestVo.getTriggerType());
		orderPo.setConditionOrderType(requestVo.getConditionOrderType());
		orderPo.setCurtPrice(curtPrice);
		orderPo.setUuid(requestVo.getUuid());
		orderPo.setCreateTime(new Date());
		orderPo.setUpdateTime(new Date());
		return orderPo;
	}
	
	public static OrderConditionHisQueryRespVo buildOrderConditionHisQueryRespVo(ContractConditionOrderPo orderPo) {
		OrderConditionHisQueryRespVo respVo = new OrderConditionHisQueryRespVo();
		respVo.setConditionOrderId(orderPo.getConditionOrderId().toString());
		respVo.setUserId(orderPo.getUserId());
		respVo.setContractId(orderPo.getContractId());
		respVo.setQuantity(orderPo.getQuantity());
		respVo.setSide(orderPo.getSide());
		respVo.setOrderType(orderPo.getOrderType());
		respVo.setOrderPrice(orderPo.getOrderPrice());
		respVo.setPositionEffect(orderPo.getPositionEffect());
		respVo.setMarginType(orderPo.getMarginType());
		respVo.setMarginRate(orderPo.getMarginRate());
		respVo.setMinimalQuantity(orderPo.getMinimalQuantity());
		respVo.setCurtPrice(orderPo.getCurtPrice());
		respVo.setTriggerType(orderPo.getTriggerType());
		respVo.setTriggerPrice(orderPo.getTriggerPrice());
		respVo.setRealTriggerPrice(orderPo.getRealTriggerPrice());
		respVo.setConditionOrderType(orderPo.getConditionOrderType());
		respVo.setStopProfitPrice(orderPo.getStopProfitPrice());
		respVo.setStopLossPrice(orderPo.getStopLossPrice());
		respVo.setCode(orderPo.getCode());
		respVo.setOrderId(orderPo.getOrderId());
		respVo.setStatus(orderPo.getStatus());
		respVo.setUpdateTime(orderPo.getUpdateTime());
		respVo.setCreateTime(orderPo.getCreateTime());
		respVo.setUuid(orderPo.getUuid());
		return respVo;
	}

}
