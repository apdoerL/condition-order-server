package org.apdoer.condition.core.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.apdoer.condition.common.event.SourceEvent;
import org.apdoer.condition.common.event.listener.SourceEventListener;
import org.apdoer.condition.core.event.handler.QuotHandler;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


/**
 * 最新价数据监听器 ,index mark tick
 * @author apdoer
 */
@Service("realtimePriceSourceEventListener")
@Scope("prototype")
public class RealtimePriceSourceEventListener implements SourceEventListener {

	private QuotHandler quotHandle;

	// 已监听的内部数据通道名称
	private Set<String> subscribeChannels = new HashSet<>();

	@Override
	@Subscribe
	@AllowConcurrentEvents
	public void listen(SourceEvent event) {
		if (null != event && null != event.getData()) {
			this.quotHandle.handle(event);
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

	public void setQuotHandle(QuotHandler quotHandle) {
		this.quotHandle = quotHandle;
	}
}
