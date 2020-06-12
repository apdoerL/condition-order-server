package org.apdoer.condition.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.condition.common.config.EventBusThreadPoolProperties;
import org.apdoer.condition.common.db.model.po.ContractChannelMappingPo;
import org.apdoer.condition.common.db.service.QuotConfigCenterService;
import org.apdoer.condition.common.enums.TriggerType;
import org.apdoer.condition.common.event.eventbus.EventBus;
import org.apdoer.condition.common.event.eventbus.impl.GuavaEventBus;
import org.apdoer.condition.common.event.eventbus.impl.GuavaEventBusManager;
import org.apdoer.condition.common.event.listener.SourceEventListener;
import org.apdoer.condition.common.event.listener.SourceEventListenerManager;
import org.apdoer.condition.core.constants.TradeCoreConstants;
import org.apdoer.condition.core.event.listener.RealtimePriceSourceEventListener;
import org.apdoer.condition.core.event.manager.TradeSourceEventListenerManager;
import org.apdoer.condition.core.service.MsBrokerService;
import org.apdoer.condition.core.service.TradeCoreInitService;
import org.apdoer.condition.core.thread.pool.ConditionOrderPlaceChannelSendThreadPool;
import org.apdoer.condition.core.thread.pool.ConditionOrderPlaceThreadPool;
import org.apdoer.condition.core.thread.pool.StopProfitLossCancelThreadPool;
import org.apdoer.condition.core.utils.BrokerChannelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/6/12 17:24
 */
@Component
@Slf4j
public class TradeCoreInitServiceImpl implements TradeCoreInitService {
    @Autowired
    private MsBrokerService msBrokerService;
    @Autowired
    private QuotConfigCenterService quotConfigCenterService;
    @Autowired
    private TradeSourceEventListenerManager sourceEventListenerManager;
    @Autowired
    private ConditionOrderPlaceThreadPool conditionOrderPlaceThreadPool;
    @Autowired
    private StopProfitLossCancelThreadPool stopProfitLossCancelThreadPool;
    @Autowired
    private ConditionOrderPlaceChannelSendThreadPool conditionOrderPlaceChannelSendThreadPool;
    @Autowired
    private EventBusThreadPoolProperties eventBusThreadPoolProperties;


    @Override
    public void init() {
        BrokerChannelUtils.msBrokerService = this.msBrokerService;
        //止盈线程池初始化
        this.stopProfitLossCancelThreadPool.init();
        //条件单下单线程池初始化
        this.conditionOrderPlaceThreadPool.init();
        //条件单发送渠道消息线程池初始化
        this.conditionOrderPlaceChannelSendThreadPool.init();
        //条件单事件初始化
        this.conditionOrderEventInit();
        this.flush();
    }

    @Override
    public void flush() {
        List<ContractChannelMappingPo> channelMappingPos = quotConfigCenterService.queryAllMapping();
        if (CollectionUtils.isEmpty(channelMappingPos)) {
            log.error("channel not config");
            return;
        }
        //最新指数价缓存监听器
        this.indexRealtimeCacheListenerInit(channelMappingPos);
        //最新标记价缓存监听器
        this.clearRealtimeCacheListenerInit(channelMappingPos);
        //最新逐笔成交价格缓存监听器
        this.tickRealtimeCacheListenerInit(channelMappingPos);
        //条件单监听器初始化
        this.conditionOrderListenerInit(channelMappingPos);
    }

    private void conditionOrderListenerInit(List<ContractChannelMappingPo> channelMappingPos) {
        log.info("conditionOrderListenerInit start===========");
        for (ContractChannelMappingPo channelMappingPo : channelMappingPos) {
            SourceEventListener listener = sourceEventListenerManager.buildConditionSourceListener(channelMappingPo.getConditionOrderListener());
            if (!listener.isSubscribed(channelMappingPo.getConditionOrderChannel())) {
                EventBus eventBus = GuavaEventBusManager.getInstance().getEventBus(channelMappingPo.getConditionOrderChannel());
                if (eventBus != null) {
                    eventBus.subscribe(listener);
                    listener.subscribeChannel(channelMappingPo.getConditionOrderChannel());
                    log.info("conditionOrderListener:{},subscribe eventbus:{},success", channelMappingPo.getConditionOrderListener(), channelMappingPo.getConditionOrderChannel());
                } else {
                    log.error("conditionOrder eventbus:{},not init-====", channelMappingPo.getConditionOrderChannel());
                }
            }
        }
        log.info("conditionOrderListenerInit end===========");
    }

    private void tickRealtimeCacheListenerInit(List<ContractChannelMappingPo> channelMappingPos) {
        log.info("tickPriceListenerInit start===========");
        for (ContractChannelMappingPo mappingPo : channelMappingPos) {
            SourceEventListener listener = sourceEventListenerManager.buildRealtimePriceListener(mappingPo.getTickCacheListener(), TriggerType.TICK.name());
            if (!listener.isSubscribed(mappingPo.getTickChannel())) {
                GuavaEventBus eventBus = (GuavaEventBus) GuavaEventBusManager.getInstance().getEventBus(mappingPo.getTickChannel());
                if (null != eventBus) {
                    eventBus.subscribe(listener);
                    listener.subscribeChannel(mappingPo.getTickChannel());
                    log.info("tickPriceListener:{},subscribe eventbus:{},success", mappingPo.getTickCacheListener(), mappingPo.getTickChannel());
                } else {
                    log.error(" tick eventbus:{},not init-====", mappingPo.getTickChannel());
                }
            }
        }
        log.info("tickPriceListenerInit success===========");
    }

    private void clearRealtimeCacheListenerInit(List<ContractChannelMappingPo> channelMappingPos) {
        log.info("clearRealtimeCacheListener init start===========");
        for (ContractChannelMappingPo channelMappingPo : channelMappingPos) {
            SourceEventListener listener = sourceEventListenerManager.buildRealtimePriceListener(channelMappingPo.getClearCacheListener(), TriggerType.MARK.name());
            if (!listener.isSubscribed(channelMappingPo.getQuotChannel())) {
                EventBus eventBus = GuavaEventBusManager.getInstance().buildGuavaEventBus(channelMappingPo.getQuotChannel());
                if (eventBus != null) {
                    listener.subscribeChannel(channelMappingPo.getQuotChannel());
                    eventBus.subscribe(listener);
                    log.info("clearlistener:{},subscribe eventbus:{},success", channelMappingPo.getClearCacheListener(), channelMappingPo.getQuotChannel());
                }
                log.error(" eventbus:{},not init-====", channelMappingPo.getQuotChannel());
            }
        }
        log.info("clearRealtimeCacheListener init success===========");

    }

    private void indexRealtimeCacheListenerInit(List<ContractChannelMappingPo> channelMappingPos) {
        log.info("====indexRealtimeCacheListenerInit start====== ");
        for (ContractChannelMappingPo channelMappingPo : channelMappingPos) {
            SourceEventListener listener = sourceEventListenerManager.buildRealtimePriceListener(channelMappingPo.getIndexCacheListener(), TriggerType.INDEX.name());
            if (listener.isSubscribed(channelMappingPo.getQuotChannel())) {
                EventBus eventBus = GuavaEventBusManager.getInstance().getEventBus(channelMappingPo.getQuotChannel());
                if (eventBus != null) {
                    eventBus.subscribe(listener);
                    listener.subscribeChannel(channelMappingPo.getQuotChannel());
                    log.info("index price listener:{} subscribe eventbus:{} success ", channelMappingPo.getIndexCacheListener(), channelMappingPo.getQuotChannel());
                } else {
                    log.error("event bus {} not init", channelMappingPo.getQuotChannel());
                }
            }
        }
        log.info("====indexRealtimeCacheListenerInit success====== ");
    }


    private void conditionOrderEventInit() {
        String channelName = TradeCoreConstants.CONDITION_ORDER_CHANNEL;

        EventBusThreadPoolProperties.ThreadPoolConfig config = eventBusThreadPoolProperties.getConditionOrderEventBusConfig();
        GuavaEventBus eventBus = (GuavaEventBus) GuavaEventBusManager.getInstance().buildGuavaEventBus(channelName,
                config.getCorePoolSize(),
                config.getMaxPoolSize(),
                config.getBackPressureSize(),
                config.getInitCapacity(),
                config.getKeepAlive());


        //止盈/止损/撤单监听器
        String listenerName = TradeCoreConstants.CONDITION_ORDER_TRIGGER_LISTENER;
        SourceEventListener listener = this.sourceEventListenerManager.buildConditionTriggerSourceListener(listenerName);
        // 沒有綁定改通道
        if (!listener.isSubscribed(channelName)) {
            eventBus.subscribe(listener);
            listener.subscribeChannel(channelName);
            log.info("conditionOrderEventInit={} sub evenbus={} success", listenerName, channelName);
        } else {
            log.error("conditionOrderEventInit, eventBus={} not find", channelName);
        }

        //消息发送监听器
        String channelListenerName = TradeCoreConstants.CONDITION_ORDER_CHANNEL_LISTENER;
        SourceEventListener channellistener = this.sourceEventListenerManager.buildConditionChannelSourceListener(channelListenerName);
        // 沒有綁定改通道
        if (!channellistener.isSubscribed(channelName)) {
            eventBus.subscribe(channellistener);
            channellistener.subscribeChannel(channelName);
            log.info("conditionOrderEventInit={} sub evenbus={} success", channelListenerName, channelName);
        } else {
            log.error("conditionOrderEventInit, eventBus={} not find", channelName);
        }
    }


}
