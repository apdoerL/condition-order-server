package org.apdoer.condition.quot.source.deserializer.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.util.JacksonUtil;
import org.apdoer.condition.common.model.dto.IndexPriceDto;
import org.apdoer.condition.quot.payload.QuotPriceMessageSourcePayload;
import org.apdoer.condition.quot.source.deserializer.ZmqDeserializer;

/**
 * @author Li
 * @version 1.0
 * @date 2020/5/12 11:19
 */
@Slf4j
public class QuotZmqDeserializer implements ZmqDeserializer {

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
        log.debug("QUOT origin data[{}]", source);
        IndexPriceDto indexPriceDto = JacksonUtil.jsonToObj(source, IndexPriceDto.class);
//        if (System.currentTimeMillis() - NumberUtils.microToMil(indexPriceDto.getTime()) > WARN_REALTIME_INTER) {
//            log.error("index price later, data={}", source);
//        }
        QuotPriceMessageSourcePayload payload = new QuotPriceMessageSourcePayload();
        payload.setMessageType(indexPriceDto.getMessageType());
        payload.setData(indexPriceDto);
        return payload;
    }
}
