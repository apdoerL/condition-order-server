package org.apdoer.condition.quot.source.deserializer.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.util.JacksonUtil;
import org.apdoer.condition.common.model.dto.LatestPriceDto;
import org.apdoer.condition.quot.payload.QuotPriceMessageSourcePayload;
import org.apdoer.condition.quot.source.deserializer.ZmqDeserializer;

@Slf4j
public class TickPriceDeserializer implements ZmqDeserializer {
    private static final long WARN_REALTIME_INTER = 60000L;

    @Override
    public QuotPriceMessageSourcePayload deserialize(String source) {
        if (null != source) {
            return this.buildMessageSourcePayload(source);
        } else {
            return null;
        }
    }

    private QuotPriceMessageSourcePayload buildMessageSourcePayload(String source) {
        //log.info("latestPrice origin data[{}]", source);
        LatestPriceDto latestPriceDto = JacksonUtil.jsonToObj(source, LatestPriceDto.class);
//        if (System.currentTimeMillis() - NumberUtils.microToMil(latestPriceDto.getTimestamp()) > WARN_REALTIME_INTER) {
//            log.error("index price later, data={}", source);
//        }
        QuotPriceMessageSourcePayload payload = new QuotPriceMessageSourcePayload();
        payload.setMessageType(latestPriceDto.getMessageType());
        payload.setData(latestPriceDto);
        return payload;
    }

}
