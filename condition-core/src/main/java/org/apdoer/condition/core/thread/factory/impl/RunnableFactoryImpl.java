package org.apdoer.condition.core.thread.factory.impl;


import org.apdoer.channel.client.client.ChannelClient;
import org.apdoer.common.service.service.UserService;
import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.common.db.service.ContractConditionOrderService;
import org.apdoer.condition.core.handle.impl.RedisCacheHandler;
import org.apdoer.condition.core.service.TradeService;
import org.apdoer.condition.core.thread.factory.RunnableFactory;
import org.apdoer.condition.core.thread.runnable.ConditionOrderChannelSendRunnable;
import org.apdoer.condition.core.thread.runnable.ConditionOrderPlaceRunnable;
import org.apdoer.condition.core.thread.runnable.StopProfitLossCancelRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RunnableFactoryImpl implements RunnableFactory {
	
	@Autowired
	private ContractConditionOrderService contractConditionOrderService;
	@Autowired
	private TradeService traderService;
	@Autowired
	private ChannelClient channelClient;
	@Autowired
	private UserService userService;
	@Autowired
	private RedisCacheHandler redisCacheHandler;

	@Override
	public Runnable newConditionOrderPlaceInstance(ContractConditionOrderPo orderPo) {
		return new ConditionOrderPlaceRunnable( contractConditionOrderService, orderPo, this.redisCacheHandler);
	}

	@Override
	public Runnable newStopProfitLossCancelRunnable(ContractConditionOrderPo orderPo) {
		return new StopProfitLossCancelRunnable(orderPo, this.contractConditionOrderService, this.traderService);
	}

	@Override
	public ConditionOrderChannelSendRunnable newConditionOrderChannelSendRunnable(ContractConditionOrderPo orderPo) {
		return new ConditionOrderChannelSendRunnable(orderPo, this.channelClient, this.userService);
	}
	
	
}
