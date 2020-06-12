package org.apdoer.condition.core.event.manager;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.util.SpringBeanUtils;
import org.apdoer.condition.common.event.listener.SourceEventListener;
import org.apdoer.condition.common.event.listener.SourceEventListenerManager;
import org.apdoer.condition.common.utils.HandlerNameUtils;
import org.apdoer.condition.core.event.handler.QuotHandler;
import org.apdoer.condition.core.event.listener.ConditionOrderChannelSourceEventListener;
import org.apdoer.condition.core.event.listener.ConditionOrderSourceEventListener;
import org.apdoer.condition.core.event.listener.ConditionOrderTriggerSourceEventListener;
import org.apdoer.condition.core.event.listener.RealtimePriceSourceEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/6/12 17:56
 */
@Slf4j
@Component
public class TradeSourceEventListenerManager implements SourceEventListenerManager {


    private static final String DEFAULT_POSTFIX = "PriceCacheHandler";

    private Map<String, SourceEventListener> listenerMap = new ConcurrentHashMap<>();

    @Autowired
    private SpringBeanUtils springBeanUtils;

    @Override
    public SourceEventListener getListener(String listenerName) {
        return this.listenerMap.get(listenerName);
    }

    @Override
    public SourceEventListener buildListener(String listenerName, String handlerName) {
        return null;
    }

    @Override
    public boolean exists(String listenerName) {
        return listenerMap.containsKey(listenerName);
    }


    public SourceEventListener buildRealtimePriceListener(String listenerName, String handleType) {
        if (this.exists(listenerName)) {
            return this.getListener(listenerName);
        } else {
            SourceEventListener listener = build(listenerName, handleType);
            this.listenerMap.put(listenerName, listener);
            log.info("build realtime cache listener success name:{}", listenerName);
            return listener;
        }
    }

    public SourceEventListener buildConditionSourceListener(String listenerName) {
        if (this.exists(listenerName)) {
            return this.getListener(listenerName);
        } else {
            SourceEventListener listener =this.springBeanUtils.getBean("conditionOrderSourceEventListener",
                    ConditionOrderSourceEventListener.class);
            this.listenerMap.put(listenerName, listener);
            log.info("build condition order listener success name:{}", listenerName);
            return listener;
        }
    }

    private SourceEventListener build(String listenerName, String typeName) {
        RealtimePriceSourceEventListener listener = this.springBeanUtils.getBean("realtimePriceSourceEventListener",
                RealtimePriceSourceEventListener.class);
        String handlerName = HandlerNameUtils.buildHandleName(typeName.toLowerCase(), DEFAULT_POSTFIX);
        QuotHandler handler = this.springBeanUtils.getBean(handlerName, QuotHandler.class);
        listener.setQuotHandle(handler);
        return listener;
    }


    public SourceEventListener buildConditionChannelSourceListener(String listenerName) {
        if (this.exists(listenerName)) {
            return this.getListener(listenerName);
        } else {
            SourceEventListener listener = this.springBeanUtils.getBean("conditionOrderChannelSourceEventListener",
                    ConditionOrderChannelSourceEventListener.class);
            this.listenerMap.put(listenerName, listener);
            log.info("build condition order channel listener success name:{}", listenerName);
            return listener;
        }
    }

    public SourceEventListener buildConditionTriggerSourceListener(String listenerName) {
        if (this.exists(listenerName)) {
            return this.getListener(listenerName);
        } else {
            SourceEventListener listener =this.springBeanUtils.getBean("conditionOrderTriggerSourceEventListener",
                    ConditionOrderTriggerSourceEventListener.class);
            this.listenerMap.put(listenerName, listener);
            log.info("build condition order trigger listener success name:{}", listenerName);
            return listener;
        }
    }

}
