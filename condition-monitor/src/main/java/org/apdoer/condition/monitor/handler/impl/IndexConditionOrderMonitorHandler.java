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
 * 指数价格类型-条件单数据处理
 * @author apdoer
 */
@Slf4j
@Component("indexConditionOrderMonitorHandler")
public class IndexConditionOrderMonitorHandler implements QuotHandler {
	
	@Autowired
	private QuotConfigCenterService quotConfigCenterService;

	@Override
	public void handle(SourceEvent event) {
		if (event.getData() instanceof IndexPriceDto) {
			this.handle((IndexPriceDto) event.getData());
		}
	}
	
	private void handle(IndexPriceDto priceDto) {
		List<ContractConditionOrderPo> orders = ConditionOrderData.getTriggerList(TriggerType.INDEX.getCode(), priceDto.getContractId(), priceDto.getIndexPrice());
		if (orders.size() != 0) {
			log.info("condition order monitor[index] contractId={} price={} size={}", priceDto.getContractId(), priceDto.getIndexPrice(), orders.size());
			try {
				ContractChannelMappingPo mappingPo = this.quotConfigCenterService.queryContractChannelMaping(priceDto.getContractId());
				if (null != mappingPo) {
					for (ContractConditionOrderPo order : orders) {
						GuavaEventBus eventbus = (GuavaEventBus) GuavaEventBusManager.getInstance().getEventBus(mappingPo.getConditionOrderChannel());
						if (null != eventbus) {
							while(eventbus.tryDo(1, 1L, TimeUnit.SECONDS)) {
								//反压
							}
							order.setRealTriggerPrice(priceDto.getIndexPrice());
							eventbus.publish(new SourceEvent(order));
						}
					}
				}
			} catch (Exception e) {
				log.error("condition order monitor[index] thread error", e);
			}
		} else {
			log.info("condition order monitor[index] contractId={} price={} size=0", priceDto.getContractId(), priceDto.getIndexPrice());
		}
	}

}
