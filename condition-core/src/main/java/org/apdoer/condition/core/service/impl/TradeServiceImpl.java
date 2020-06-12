package org.apdoer.condition.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.util.ResultVoBuildUtils;
import org.apdoer.condition.common.data.ConditionOrderData;
import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.common.db.service.ContractConditionOrderService;
import org.apdoer.condition.common.enums.OrderStatusEnum;
import org.apdoer.condition.common.utils.OrderNumberUtil;
import org.apdoer.condition.core.handle.impl.RedisCacheHandler;
import org.apdoer.condition.core.model.vo.OrderConditionReqVo;
import org.apdoer.condition.core.service.TradeService;
import org.apdoer.condition.core.utils.ModeBuildlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class TradeServiceImpl implements TradeService {
	
	@Autowired
	private OrderNumberUtil orderNumberUtil;
	
	@Autowired
	private RedisCacheHandler redisCacheHandler;
	
	@Autowired
	private ContractConditionOrderService contractConditionOrderService;

	@Override
	public ResultVo placeConditionOrder(Integer userId, BigDecimal curtPrice, OrderConditionReqVo requestVo) {
		ContractConditionOrderPo orderPo = ModeBuildlUtils.buildContractConditionOrderPo(userId, curtPrice, this.orderNumberUtil.getOrderUuid(), requestVo);
		int result = this.contractConditionOrderService.insertOrder(orderPo);
		if (1 != result) {
        	throw new RuntimeException("insert order error");
        }
		ConditionOrderData.add(orderPo);
		this.redisCacheHandler.hPutConditionOrder(userId, orderPo.getConditionOrderId(), orderPo);
        log.info("condition order succ, userId={}, conditionOrderId={}", orderPo.getUserId(), orderPo.getConditionOrderId());
        return ResultVoBuildUtils.buildSuccessResultVo(orderPo.getConditionOrderId());
	}

	@Override
	public ResultVo cancelConditionOrder(ContractConditionOrderPo orderPo) {
		int result = this.contractConditionOrderService.updateStatus(orderPo.getConditionOrderId(), orderPo.getUserId(), OrderStatusEnum.CANCEL.getCode());
		if (result != 0) {
			ConditionOrderData.remove(orderPo);
			this.redisCacheHandler.hDeleteConditionOrder(orderPo.getUserId(), orderPo.getConditionOrderId());
			
			log.info("Condition order cancel succ, userId={}, conditionOrderId={}", orderPo.getUserId(), orderPo.getConditionOrderId());
			return ResultVoBuildUtils.buildSuccessResultVo();
		} else {
			return ResultVoBuildUtils.buildFaildResultVo();
		}
	}


}
