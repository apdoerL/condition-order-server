package org.apdoer.condition.core.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.common.event.SourceEvent;
import org.apdoer.condition.common.event.listener.SourceEventListener;
import org.apdoer.condition.core.thread.factory.RunnableFactory;
import org.apdoer.condition.core.thread.factory.impl.RunnableFactoryImpl;
import org.apdoer.condition.core.thread.pool.ConditionOrderPlaceChannelSendThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 渠道消息发送监听器
 * @author apdoer
 */
@Slf4j
@Service("conditionOrderChannelSourceEventListener")
@Scope("prototype")
public class ConditionOrderChannelSourceEventListener implements SourceEventListener {
	
	@Autowired
	private ConditionOrderPlaceChannelSendThreadPool conditionOrderPlaceChannelSendThreadPool;
	
	@Autowired
	private RunnableFactory factory;

	// 已监听的内部数据通道名称
	private Set<String> subscribeChannels = new HashSet<>();

	@Override
	@Subscribe
	@AllowConcurrentEvents
	public void listen(SourceEvent event) {
		if (null != event && null != event.getData() 
				&& event.getData() instanceof ContractConditionOrderPo) {
			this.conditionOrderTriggerAfterHadle((ContractConditionOrderPo)event.getData());
		}
	}
	
	private void conditionOrderTriggerAfterHadle(ContractConditionOrderPo orderPo) {
		try {
			while (this.conditionOrderPlaceChannelSendThreadPool.tryDo(1, 1L, TimeUnit.SECONDS)) {
				//数据反压中
			}
			this.conditionOrderPlaceChannelSendThreadPool.execute(this.factory.newConditionOrderChannelSendRunnable(orderPo));
		} catch (Exception e) {
			log.error("condition order place error", e);
		}
	}

	@Override
	public Set<String> getSubscribedChannels() {
		return this.subscribeChannels;
	}

	@Override
	public boolean isSubscribed(String channelName) {
		return this.subscribeChannels.contains(channelName);
	}

	@Override
	public void subscribeChannel(String channelName) {
		if (!this.isSubscribed(channelName)) {
			this.subscribeChannels.add(channelName);
		}
	}
}
