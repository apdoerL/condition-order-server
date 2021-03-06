package org.apdoer.condition.core.event.handler.impl;


import org.apdoer.condition.common.data.QuotData;
import org.apdoer.condition.common.enums.TriggerType;
import org.apdoer.condition.common.event.SourceEvent;
import org.apdoer.condition.common.model.dto.IndexPriceDto;
import org.apdoer.condition.core.event.handler.QuotHandler;
import org.springframework.stereotype.Component;


/**
 * 指数价格类型-价格实时缓存
 * @author apdoer
 */
@Component("indexPriceCacheHandler")
public class IndexPriceCacheHandler implements QuotHandler {
	
	@Override
	public void handle(SourceEvent event) {
		if (event.getData() instanceof IndexPriceDto) {
			this.cachePrice((IndexPriceDto) event.getData());
		}
	}
	
	private void cachePrice(IndexPriceDto priceDto) {
		QuotData.set(TriggerType.INDEX.getCode(), priceDto.getContractId(), priceDto.getIndexPrice());
	}
}
