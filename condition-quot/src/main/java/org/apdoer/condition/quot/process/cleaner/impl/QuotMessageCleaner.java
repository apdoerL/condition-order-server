package org.apdoer.condition.quot.process.cleaner.impl;


import org.apdoer.condition.quot.enums.QuotMessageTypeEnum;
import org.apdoer.condition.quot.payload.QuotPriceMessageSourcePayload;
import org.apdoer.condition.quot.process.cleaner.Cleaner;

/**
 * 行情消息清洗器
 * @author apdoer
 */
public class QuotMessageCleaner implements Cleaner<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload> {

    @Override
    public QuotPriceMessageSourcePayload clean(QuotPriceMessageSourcePayload data) {
        if (null != data && QuotMessageTypeEnum.QUOT_SNAPSHOT.getCode() == data.getMessageType()) {
            return data;
        } else {
            return null;
        }
    }
}