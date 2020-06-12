package org.apdoer.condition.core.check;


import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.condition.core.model.vo.OrderConditionCancelReqVo;
import org.apdoer.condition.core.model.vo.OrderConditionCancelsReqVo;
import org.apdoer.condition.core.model.vo.OrderConditionReqVo;

public interface OrderCheck {
	
	ResultVo checkPlaceOrderParamIsLegal(Integer userId, OrderConditionReqVo requestVo);
	
	ResultVo checkOrderCancelParamIsLegal(Integer userId, OrderConditionCancelReqVo requestVo);
	
	ResultVo checkOrderCancelsParamIsLegal(OrderConditionCancelsReqVo requestVo);
	
	ResultVo checkOrderHisQueryParamIsLegal(Integer contractId, Integer side, Integer status);

}
