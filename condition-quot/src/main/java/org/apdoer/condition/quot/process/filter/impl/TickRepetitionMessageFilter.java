package org.apdoer.condition.quot.process.filter.impl;

import org.apdoer.condition.common.model.dto.LatestPriceDto;
import org.apdoer.condition.quot.payload.QuotPriceMessageSourcePayload;
import org.apdoer.condition.quot.process.filter.Filter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * 逐笔成交数据过滤
 * @author apdoer
 */
public class TickRepetitionMessageFilter implements Filter<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload> {

	private Map<Integer, BigDecimal> tickPriceMap = new HashMap<>();

    @Override
    public QuotPriceMessageSourcePayload filter(QuotPriceMessageSourcePayload resource) {
    	if (null != resource && !this.isFilter((LatestPriceDto) resource.getData())) {
    		return resource;
    	}
    	return null;
    }
    

    private boolean isFilter(LatestPriceDto priceDto) {
		if (this.tickPriceMap.containsKey(priceDto.getContractId())) {
			BigDecimal cachePrice = this.tickPriceMap.get(priceDto.getContractId());
			//去除兩次一样的指数
			if (priceDto.getTradePrice().compareTo(cachePrice) != 0) {
				this.tickPriceMap.put(priceDto.getContractId(), priceDto.getTradePrice());
				return false;
			} else {
				return true;
			}
		} else {
			this.tickPriceMap.put(priceDto.getContractId(), priceDto.getTradePrice());
			return false;
		}
    }
}