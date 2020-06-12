package org.apdoer.condition.quot.process.filter.impl;


import org.apdoer.condition.quot.payload.QuotPriceMessageSourcePayload;
import org.apdoer.condition.quot.process.filter.Filter;

/**
 * 行情消息过滤器
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/12 14:55
 */
public class QuotRepetitionMessageFilter implements Filter<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload> {

    @Override
    public QuotPriceMessageSourcePayload filter(QuotPriceMessageSourcePayload resource) {
    	return resource;
    }
}