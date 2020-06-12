package org.apdoer.condition.monitor.listener.impl;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.apdoer.condition.monitor.handler.Handler;
import org.apdoer.condition.monitor.handler.QuotHandler;
import org.apdoer.condition.common.event.SourceEvent;
import org.apdoer.condition.common.event.listener.SourceEventListener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/6/12 16:06
 */
@Component("quotPriceSourceEventListener")
@Scope("prototype")
public class QuotPriceSourceEventListener implements SourceEventListener {

    private Handler quotHandler;

    private Set<String> subscribedChannels = new HashSet<>();

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void listen(SourceEvent event) {
        if (null != event && null != event.getData()) {
            this.quotHandler.handle(event);
        }
    }

    @Override
    public Set<String> getSubscribedChannels() {
        return this.subscribedChannels;
    }

    @Override
    public boolean isSubscribed(String channelName) {
        return subscribedChannels.contains(channelName);
    }

    @Override
    public void subscribeChannel(String channelName) {
        if (!isSubscribed(channelName)) {
            subscribedChannels.add(channelName);
        }
    }

    public void setQuotHandler(QuotHandler quotHandler) {
        this.quotHandler = quotHandler;
    }
}
