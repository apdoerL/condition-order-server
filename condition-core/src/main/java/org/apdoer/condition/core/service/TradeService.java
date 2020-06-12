package org.apdoer.condition.core.service;

import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.core.model.vo.OrderConditionReqVo;

import java.math.BigDecimal;

public interface TradeService {

	/**
	 * 條件單下單
	 * 
	 * @param userId
	 * @param requestVo
	 * @return
	 */
	ResultVo placeConditionOrder(Integer userId, BigDecimal curtPrice, OrderConditionReqVo requestVo);

	/**
	 * 
	 * @param orderPo
	 * @return
	 */
	ResultVo cancelConditionOrder(ContractConditionOrderPo orderPo);

}
