package org.apdoer.condition.monitor.listener.manager;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.util.SpringBeanUtils;
import org.apdoer.condition.monitor.handler.QuotHandler;
import org.apdoer.condition.monitor.listener.impl.QuotPriceSourceEventListener;
import org.apdoer.condition.common.event.listener.SourceEventListener;
import org.apdoer.condition.common.event.listener.SourceEventListenerManager;
import org.apdoer.condition.common.utils.HandlerNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/6/12 16:11
 */
@Component
@Slf4j
public class QuotSourceEventListenerManager implements SourceEventListenerManager {

    private static final String DEFAULT_POSTFIX = "ConditionOrderMonitorHandler";

    private Map<String, SourceEventListener> listenerMap = new ConcurrentHashMap<>();

    @Autowired
    private SpringBeanUtils springBeanUtils;

    @Override
    public SourceEventListener getListener(String listenerName) {
        return listenerMap.get(listenerName);
    }

    @Override
    public SourceEventListener buildListener(String listenerName, String handlerName) {
        if (exists(listenerName)) {
            return this.getListener(listenerName);
        } else {
            SourceEventListener listener = build(listenerName, handlerName);
            listenerMap.put(listenerName, listener);
            log.info("build quot listener success name:{}", listenerName);
            return listener;
        }
    }

    private SourceEventListener build(String listenerName, String handlerName) {
        QuotPriceSourceEventListener listener = springBeanUtils.getBean("quotPriceSourceEventListener", QuotPriceSourceEventListener.class);
        String realHandlerName = HandlerNameUtils.buildHandleName(handlerName.toLowerCase(), DEFAULT_POSTFIX);
        QuotHandler handler = this.springBeanUtils.getBean(realHandlerName, QuotHandler.class);
        listener.setQuotHandler(handler);
        return listener;
    }

    @Override
    public boolean exists(String listenerName) {
        return listenerMap.containsKey(listenerName);
    }
}
