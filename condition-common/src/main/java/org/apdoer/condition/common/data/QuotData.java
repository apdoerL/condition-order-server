package org.apdoer.condition.common.data;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.condition.common.enums.TriggerType;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 最新指数价,成交价格,标记价格缓存区域
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/6/12 14:15
 */
@Slf4j
public class QuotData {

    //逐笔成交 key为contractId,value为最新价
    private static ConcurrentHashMap<Integer, BigDecimal> TICK_PRICE_DATA = new ConcurrentHashMap<>();
    //标记价格
    private static ConcurrentHashMap<Integer, BigDecimal> MARK_PRICE_DATA = new ConcurrentHashMap<>();
    //行情指数
    private static ConcurrentHashMap<Integer, BigDecimal> INDEX_PRICE_DATA = new ConcurrentHashMap<>();


    public static BigDecimal get(Integer type, Integer contractId) {
        if (TriggerType.INDEX.getCode() == type) {
            return INDEX_PRICE_DATA.get(contractId);
        } else if (TriggerType.MARK.getCode() == type) {
            return MARK_PRICE_DATA.get(contractId);
        } else {
            return TICK_PRICE_DATA.get(contractId);
        }
    }

    public static void set(Integer type, Integer contractId, BigDecimal price) {
        log.debug("realtime price set, type={}, contractId={}, price={}", type, contractId, price);
        if (TriggerType.INDEX.getCode() == type) {
            INDEX_PRICE_DATA.put(contractId, price);
        } else if (TriggerType.MARK.getCode() == type) {
            MARK_PRICE_DATA.put(contractId, price);
        } else {
            TICK_PRICE_DATA.put(contractId, price);
        }
    }

}
