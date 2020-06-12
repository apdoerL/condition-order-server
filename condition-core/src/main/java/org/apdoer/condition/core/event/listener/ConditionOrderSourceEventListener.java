package org.apdoer.condition.core.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.common.event.SourceEvent;
import org.apdoer.condition.common.event.listener.SourceEventListener;
import org.apdoer.condition.core.thread.factory.RunnableFactory;
import org.apdoer.condition.core.thread.factory.impl.RunnableFactoryImpl;
import org.apdoer.condition.core.thread.pool.ConditionOrderPlaceThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * 条件单下单数据监听器
 *
 * @author apdoer
 */
@Slf4j
@Service("conditionOrderSourceEventListener")
@Scope("prototype")
public class ConditionOrderSourceEventListener implements SourceEventListener {

    @Autowired
    private ConditionOrderPlaceThreadPool conditionOrderPlaceThreadPool;

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
            this.conditionOrderPlace((ContractConditionOrderPo) event.getData());
        }
    }

    private void conditionOrderPlace(ContractConditionOrderPo orderPo) {
        try {
            while (this.conditionOrderPlaceThreadPool.tryDo(1, 1L, TimeUnit.SECONDS)) {
                //数据反压中
            }
            this.conditionOrderPlaceThreadPool.execute(this.factory.newConditionOrderPlaceInstance(orderPo));
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
