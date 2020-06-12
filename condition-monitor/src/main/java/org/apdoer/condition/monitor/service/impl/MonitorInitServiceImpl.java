package org.apdoer.condition.monitor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.condition.monitor.listener.manager.QuotSourceEventListenerManager;
import org.apdoer.condition.monitor.service.MonitorInitService;
import org.apdoer.condition.common.config.EventBusThreadPoolProperties;
import org.apdoer.condition.common.db.model.po.ContractChannelMappingPo;
import org.apdoer.condition.common.db.service.QuotConfigCenterService;
import org.apdoer.condition.common.enums.TriggerType;
import org.apdoer.condition.common.event.eventbus.EventBus;
import org.apdoer.condition.common.event.eventbus.impl.GuavaEventBus;
import org.apdoer.condition.common.event.eventbus.impl.GuavaEventBusManager;
import org.apdoer.condition.common.event.listener.SourceEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/6/12 16:35
 */
@Component
@Slf4j
public class MonitorInitServiceImpl implements MonitorInitService {

    @Autowired
    private QuotConfigCenterService quotConfigCenterService;

    @Autowired
    private QuotSourceEventListenerManager quotSourceEventListenerManager;

    @Autowired
    private EventBusThreadPoolProperties eventBusThreadPoolProperties;

    @Override
    public void init() {
        this.flush();
    }

    @Override
    public void flush() {
        List<ContractChannelMappingPo> channelMappingPos = quotConfigCenterService.queryAllMapping();
        if (CollectionUtils.isEmpty(channelMappingPos)) {
            log.error("channel not config");
            return;
        }
        //行情价格监控 初始化
        this.indexPriceListenerInit(channelMappingPos);
        this.clearPriceListenerInit(channelMappingPos);
        this.tickPriceListenerInit(channelMappingPos);

        //条件单通道监控初始化
        this.conditionOrderChannelInit(channelMappingPos);
    }

    private void conditionOrderChannelInit(List<ContractChannelMappingPo> channelMappingPos) {
        log.info("condition order channel init start");
        EventBusThreadPoolProperties.ThreadPoolConfig config = eventBusThreadPoolProperties.getConditionOrderEventBusConfig();
        for (ContractChannelMappingPo mappingPo : channelMappingPos) {
            EventBus eventBus = GuavaEventBusManager.getInstance().buildGuavaEventBus(mappingPo.getConditionOrderChannel(),
                    config.getCorePoolSize(),
                    config.getMaxPoolSize(),
                    config.getBackPressureSize(),
                    config.getInitCapacity(),
                    config.getKeepAlive());
        }
        log.info("condition order channel init end");
    }

    private void tickPriceListenerInit(List<ContractChannelMappingPo> channelMappingPos) {
        log.info("tickPriceListenerInit start===========");
        for (ContractChannelMappingPo mappingPo : channelMappingPos) {
            SourceEventListener listener = quotSourceEventListenerManager.buildListener(mappingPo.getTickListener(), TriggerType.TICK.name());
            if (!listener.isSubscribed(mappingPo.getTickChannel())){
                GuavaEventBus eventBus = (GuavaEventBus) GuavaEventBusManager.getInstance().getEventBus(mappingPo.getTickChannel());
                if (null!=eventBus){
                    eventBus.subscribe(listener);
                    listener.subscribeChannel(mappingPo.getTickChannel());
                    log.info("latestPriceListener:{},subscribe eventbus:{},success",mappingPo.getTickListener(),mappingPo.getTickChannel());
                }else {
                    log.error(" latest price eventbus:{},not init-====",mappingPo.getTickChannel());
                }
            }
        }
        log.info("tickPriceListenerInit success===========");
    }

    private void clearPriceListenerInit(List<ContractChannelMappingPo> channelMappingPos) {
        log.info("clearPriceListener init start===========");
        for (ContractChannelMappingPo mappingPo : channelMappingPos) {
            SourceEventListener listener = this.quotSourceEventListenerManager.buildListener(mappingPo.getClearCacheListener(), TriggerType.MARK.name());
            if (!listener.isSubscribed(mappingPo.getQuotChannel())){
                EventBus eventBus = GuavaEventBusManager.getInstance().getEventBus(mappingPo.getQuotChannel());
                if (null!=eventBus){
                    eventBus.subscribe(listener);
                    listener.subscribeChannel(mappingPo.getQuotChannel());
                    log.info("clearlistener:{},subscribe eventbus:{},success",mappingPo.getClearQuotListener(), mappingPo.getQuotChannel());
                }else {
                    log.error(" eventbus:{},not init-====",mappingPo.getQuotChannel());
                }
            }
        }
        log.info("clearPriceListener init success===========");
    }

    private void indexPriceListenerInit(List<ContractChannelMappingPo> channelMappingPos) {
        log.info("====condition order index Listener init start====== ");
        for (ContractChannelMappingPo channelMappingPo : channelMappingPos) {
            SourceEventListener listener = this.quotSourceEventListenerManager.buildListener(channelMappingPo.getIndexQuotListener(), TriggerType.INDEX.name());
            if (!listener.isSubscribed(channelMappingPo.getQuotChannel())) {
                EventBus eventBus = GuavaEventBusManager.getInstance().getEventBus(channelMappingPo.getQuotChannel());
                if (null != eventBus) {
                    eventBus.subscribe(listener);
                    listener.subscribeChannel(channelMappingPo.getQuotChannel());
                    log.info("index price listener:{} subscribe eventbus:{} success ", channelMappingPo.getIndexQuotListener(), channelMappingPo.getQuotChannel());
                } else {
                    log.error("event bus {} not init", channelMappingPo.getQuotChannel());
                }
            }
        }
        log.info("====condition order index Listener init success====== ");
    }
}
