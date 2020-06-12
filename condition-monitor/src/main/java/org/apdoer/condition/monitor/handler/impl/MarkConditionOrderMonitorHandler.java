package org.apdoer.condition.monitor.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.condition.monitor.handler.QuotHandler;
import org.apdoer.condition.common.data.ConditionOrderData;
import org.apdoer.condition.common.db.model.po.ContractChannelMappingPo;
import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.common.db.service.QuotConfigCenterService;
import org.apdoer.condition.common.enums.TriggerType;
import org.apdoer.condition.common.event.SourceEvent;
import org.apdoer.condition.common.event.eventbus.impl.GuavaEventBus;
import org.apdoer.condition.common.event.eventbus.impl.GuavaEventBusManager;
import org.apdoer.condition.common.model.dto.IndexPriceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 标记价格,条件单数据处理
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/6/12 15:55
 */
@Slf4j
@Component("markConditionOrderMonitorHandler")
public class MarkConditionOrderMonitorHandler implements QuotHandler {


    @Autowired
    private QuotConfigCenterService quotConfigCenterService;

    @Override
    public void handle(SourceEvent sourceEvent) {
        if (sourceEvent.getData() instanceof IndexPriceDto) {
            this.handle((IndexPriceDto) sourceEvent.getData());
        }
    }

    private void handle(IndexPriceDto priceDto) {
        List<ContractConditionOrderPo> orders = ConditionOrderData.getTriggerList(TriggerType.MARK.getCode(), priceDto.getContractId(), priceDto.getClearPrice());

        if (orders.size() != 0) {
            log.info("condition order monitor[mark] contractId={} price={} size={}", priceDto.getContractId(), priceDto.getClearPrice(), orders.size());
            try {
                ContractChannelMappingPo mappingPo = this.quotConfigCenterService.queryContractChannelMaping(priceDto.getContractId());
                if (mappingPo != null) {
                    for (ContractConditionOrderPo order : orders) {
                        GuavaEventBus eventBus = ((GuavaEventBus) GuavaEventBusManager.getInstance().getEventBus(mappingPo.getConditionOrderChannel()));
                        if (null != eventBus) {
                            while (eventBus.tryDo(1, 1L, TimeUnit.SECONDS)) {
                            }
                            order.setRealTriggerPrice(priceDto.getClearPrice());
                            eventBus.publish(new SourceEvent(order));
                        }
                    }
                }
            } catch (Exception e) {
                log.error("condition order monitor[mark] thread error", e);
            }
        } else {
            log.info("condition order monitor[mark] contractId={} price={} size=0", priceDto.getContractId(), priceDto.getClearPrice());
        }

    }
}
