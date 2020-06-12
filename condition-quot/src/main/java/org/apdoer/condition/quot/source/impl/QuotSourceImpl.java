package org.apdoer.condition.quot.source.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.condition.quot.payload.QuotPriceMessageSourcePayload;
import org.apdoer.condition.quot.properties.ZmqSourceProperties;
import org.apdoer.condition.quot.source.AbstractCacheZmqSource;
import org.apdoer.condition.quot.source.deserializer.Deserializer;

/**
 * 行情快照数据源
 * @author apdoer
 * @version 1.0
 * @date 2020/6/7 10:52
 */
@Slf4j
public class QuotSourceImpl extends AbstractCacheZmqSource<QuotPriceMessageSourcePayload> {

    private Deserializer<String, QuotPriceMessageSourcePayload> deserializer;

    public QuotSourceImpl(ZmqSourceProperties.ZmqSource quotConfig, Deserializer<String, QuotPriceMessageSourcePayload> deserializer) {
        super(quotConfig);
        this.deserializer = deserializer;
    }
    @Override
    protected void queueRelease() {
        if (this.queue.size() == this.queueSize) {
            for (int i = 0; i < this.releaseSize; i++) {
                this.queue.poll();
            }
        }
    }

    @Override
    public QuotPriceMessageSourcePayload read() {
        return deserializer.deserialize(this.queue.poll());
    }
}
