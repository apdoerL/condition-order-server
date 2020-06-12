package org.apdoer.condition.core.thread.factory;


import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;

public interface RunnableFactory {
	
	Runnable newConditionOrderPlaceInstance(ContractConditionOrderPo orderPo);
	
	Runnable newStopProfitLossCancelRunnable(ContractConditionOrderPo orderPo);
	
	Runnable newConditionOrderChannelSendRunnable(ContractConditionOrderPo orderPo);
}
