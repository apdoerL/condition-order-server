package org.apdoer.condition.quot.process.cleaner.impl;


import org.apdoer.condition.quot.enums.QuotMessageTypeEnum;
import org.apdoer.condition.quot.payload.QuotPriceMessageSourcePayload;
import org.apdoer.condition.quot.process.cleaner.Cleaner;


/**
 * 逐笔成交数据清洗
 * @author apdoer
 */
public class TickMessageCleaner implements Cleaner<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload> {

    @Override
    public QuotPriceMessageSourcePayload clean(QuotPriceMessageSourcePayload data) {
        if (null != data && QuotMessageTypeEnum.TRADE_PRICE.getCode() == data.getMessageType()) {
            return data;
        } else {
            return null;
        }
    }
}