package org.apdoer.condition.common.data;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.thread.NameableThreadFactory;
import org.apdoer.condition.common.data.base.BaseConditionOrderData;
import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.common.db.service.ContractConditionOrderService;
import org.apdoer.condition.common.enums.SlaveDataSortTypeEnum;
import org.apdoer.condition.common.enums.TriggerType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 条件单数据区
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/6/12 15:12
 */
@Slf4j
public class ConditionOrderData {
    // 从持仓数据存储结构，以{cid}-1作为list下标， List<Map<{_price},Map<{uuid},data>>>
    static BaseConditionOrderData indexOrderDESC = new BaseConditionOrderData(SlaveDataSortTypeEnum.DESC);
    static BaseConditionOrderData indexOrderASC = new BaseConditionOrderData(SlaveDataSortTypeEnum.ASC);
    static BaseConditionOrderData tickOrderDESC = new BaseConditionOrderData(SlaveDataSortTypeEnum.DESC);
    static BaseConditionOrderData tickOrderASC = new BaseConditionOrderData(SlaveDataSortTypeEnum.ASC);
    static BaseConditionOrderData markOrderDESC = new BaseConditionOrderData(SlaveDataSortTypeEnum.DESC);
    static BaseConditionOrderData markOrderASC = new BaseConditionOrderData(SlaveDataSortTypeEnum.ASC);

    public static void init(int fixCount, List<Integer> contractIds, ContractConditionOrderService contractConditionOrderService) {
        log.info("==== condition order init start");
        memorySpaceInit(contractIds);
        dataInit(fixCount, contractConditionOrderService);
        // 初始化数据
        log.info("==== condition order init end");
    }

    private static void dataInit(int fixCount, ContractConditionOrderService contractConditionOrderService) {
        log.info("==== condition order data init start");
        CountDownLatch countDownLatch = new CountDownLatch(fixCount);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(fixCount,
                fixCount, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), new NameableThreadFactory("data-init-thread"));
        for (int i = 0; i < fixCount; i++) {
            int finalI = i;
            threadPoolExecutor.execute(() -> {
                log.info("==== condition order userMod={} init start", finalI);
                int loadSize = 0;
                try {
                    List<ContractConditionOrderPo> list = contractConditionOrderService.getUnTriggerOrder(finalI);
                    if (null != list && list.size() != 0) {
                        loadSize = list.size();
                        for (ContractConditionOrderPo orderPo : list) {
                            ConditionOrderData.add(orderPo);
                        }
                    }
                } catch (Exception e) {
                    log.error("condition order init error userMod={}", finalI, e);
                } finally {
                    countDownLatch.countDown();
                    log.info("==== condition order userMod={} init end, loadSize={}", finalI, loadSize);
                }
            });
        }
    }

    public static void add(ContractConditionOrderPo orderPo) {
        BaseConditionOrderData orderData = conditionDataRoute(orderPo);
        orderData.add(orderPo);
    }

    public static void remove(ContractConditionOrderPo orderPo) {
        BaseConditionOrderData orderData = conditionDataRoute(orderPo);
        orderData.remove(orderPo.getContractId(), orderPo.getConditionOrderId(), orderPo.getTriggerPrice());
    }


    public static List<ContractConditionOrderPo> getTriggerList(Integer contractId, Integer triggerType, BigDecimal triggerPrice) {
        List<ContractConditionOrderPo> allConditionList = new ArrayList<>();
        List<Map<Long, ContractConditionOrderPo>> conditionList = new ArrayList<>(2);

        if (TriggerType.INDEX.getCode() == triggerType) {
            conditionList.addAll(indexOrderDESC.getRemoveList(contractId, triggerPrice));
            conditionList.addAll(indexOrderASC.getRemoveList(contractId, triggerPrice));
        } else if (TriggerType.MARK.getCode() == triggerType) {
            conditionList.addAll(markOrderDESC.getRemoveList(contractId, triggerPrice));
            conditionList.addAll(markOrderASC.getRemoveList(contractId, triggerPrice));
        } else {
            conditionList.addAll(tickOrderASC.getRemoveList(contractId, triggerPrice));
            conditionList.addAll(tickOrderDESC.getRemoveList(contractId, triggerPrice));
        }
        for (Map<Long, ContractConditionOrderPo> conditionOrderPoMap : conditionList) {
            for (Map.Entry<Long, ContractConditionOrderPo> entry : conditionOrderPoMap.entrySet()) {
                allConditionList.add(entry.getValue());
            }
        }
        return allConditionList;
    }


    private static BaseConditionOrderData conditionDataRoute(ContractConditionOrderPo orderPo) {
        if (TriggerType.INDEX.getCode() == orderPo.getTriggerType()) {
            //触发价格比当前价格高-则正序
            return orderPo.getTriggerPrice().compareTo(orderPo.getCurtPrice()) >= 0 ? indexOrderASC : indexOrderDESC;
        } else if (TriggerType.MARK.getCode() == orderPo.getTriggerType()) {
            //触发价格比当前价格高-则正序
            return orderPo.getTriggerPrice().compareTo(orderPo.getCurtPrice()) >= 0 ? markOrderASC : markOrderDESC;
        } else {
            //触发价格比当前价格高-则正序
            return orderPo.getTriggerPrice().compareTo(orderPo.getCurtPrice()) >= 0 ? tickOrderASC : tickOrderDESC;
        }
    }

    private static void memorySpaceInit(List<Integer> contractIds) {
        log.info("==== condition order memory space init start");
        // 开辟从持仓空间
        indexOrderDESC.init(contractIds);
        indexOrderASC.init(contractIds);
        tickOrderDESC.init(contractIds);
        tickOrderASC.init(contractIds);
        markOrderDESC.init(contractIds);
        markOrderASC.init(contractIds);
        log.info("==== condition order memory space  init end");
    }


    public static void flushMemorySpace(List<Integer> contractIds) {
        log.info("==== condition order flush start");
        memorySpaceInit(contractIds);
        // 初始化数据
        log.info("==== condition order flush end");
    }

}
