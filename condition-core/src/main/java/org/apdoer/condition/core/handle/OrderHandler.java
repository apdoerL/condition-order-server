package org.apdoer.condition.core.handle;


import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.condition.core.model.vo.OrderConditionCancelReqVo;
import org.apdoer.condition.core.model.vo.OrderConditionCancelsReqVo;
import org.apdoer.condition.core.model.vo.OrderConditionReqVo;

public interface OrderHandler {
	
	ResultVo placeOrderHandle(OrderConditionReqVo requestVo);
	
	ResultVo orderCancelHandle(OrderConditionCancelReqVo requestVo);
	
	ResultVo orderCancelsHandle(OrderConditionCancelsReqVo requestVo);
	
	ResultVo orderConditionQueryHandle(Integer contractId);
	
	ResultVo orderConditionHisQueryHandle(Integer contractId, Integer side, Integer status, Integer pageNum, Integer pageSize);

}
