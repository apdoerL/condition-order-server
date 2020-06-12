package org.apdoer.condition.core.event.handler.impl;

import org.apdoer.condition.common.data.QuotData;
import org.apdoer.condition.common.enums.TriggerType;
import org.apdoer.condition.common.event.SourceEvent;
import org.apdoer.condition.common.model.dto.LatestPriceDto;
import org.apdoer.condition.core.event.handler.QuotHandler;
import org.springframework.stereotype.Component;


/**
 * 逐比价格类型-价格实时缓存
 *
 * @author apdoer
 */
@Component("tickPriceCacheHandler")
public class TickPriceCacheHandler implements QuotHandler {

    @Override
    public void handle(SourceEvent event) {
        if (event.getData() instanceof LatestPriceDto) {
            this.cachePrice((LatestPriceDto) event.getData());
        }
    }

    private void cachePrice(LatestPriceDto priceDto) {
        QuotData.set(TriggerType.TICK.getCode(), priceDto.getContractId(), priceDto.getTradePrice());
    }
}
