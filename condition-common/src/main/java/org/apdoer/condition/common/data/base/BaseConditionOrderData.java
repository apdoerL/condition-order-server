package org.apdoer.condition.common.data.base;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.common.enums.SlaveDataSortTypeEnum;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 条件单数据区
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/6/12 14:28
 */
@Slf4j
public class BaseConditionOrderData {

    //ConcurrentSkipListMap 的排序方式
    private SlaveDataSortTypeEnum sortType;

    private Lock lock = new ReentrantLock(true);

    //合约对应数据区域下标 contractId=>数据区下标映射
    private Map<Integer, Integer> contractIndexMap = new HashMap<>();

    //这里为什么不用线程安全的CopyOnWriteArrayList? 线程安全自己实现
    private List<ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, ContractConditionOrderPo>>> orderDataList = new ArrayList<>();

    public BaseConditionOrderData(SlaveDataSortTypeEnum sortType) {
        this.sortType = sortType;
    }

    public void init(List<Integer> contractIdList) {
        for (Integer contractId : contractIdList) {
            this.initMemorySpace(contractId);
        }
    }

    private void initMemorySpace(Integer contractId) {
        if (!this.contractIndexMap.containsKey(contractId)) {
            ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, ContractConditionOrderPo>> memorySpace = null;
            if (SlaveDataSortTypeEnum.DESC == this.sortType) {
                memorySpace = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
            } else {
                memorySpace = new ConcurrentSkipListMap<>();
            }
            this.orderDataList.add(memorySpace);
            Integer currentIndex = this.orderDataList.size() - 1;
            this.contractIndexMap.put(contractId, currentIndex);

            log.info("newMemorySpace succ, contractId={}", contractId);
        }
    }

    public void add(ContractConditionOrderPo orderPo) {
        Integer index = this.getContractDataIndex(orderPo.getContractId());
        if (index == null) {
            log.warn("contractId={} orderData not exist", orderPo.getContractId());
            return;
        }
        //获取该合约下所有条件单
        ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, ContractConditionOrderPo>> contractOrderMap = this.orderDataList.get(index);
        //获取该强平价下所有持仓
        ConcurrentHashMap<Long, ContractConditionOrderPo> priceOrderMap = contractOrderMap.get(orderPo.getTriggerPrice().stripTrailingZeros());
        if (null != priceOrderMap) {
            priceOrderMap.put(orderPo.getConditionOrderId(), orderPo);
        } else {
            try {
                lock.lock();
                if (null == (priceOrderMap = contractOrderMap.get(orderPo.getTriggerPrice().stripTrailingZeros()))) {
                    ConcurrentHashMap<Long, ContractConditionOrderPo> newPriceOrderMap = new ConcurrentHashMap<>();
                    newPriceOrderMap.put(orderPo.getConditionOrderId(), orderPo);
                    contractOrderMap.put(orderPo.getTriggerPrice(), newPriceOrderMap);
                } else {
                    priceOrderMap.put(orderPo.getConditionOrderId(), orderPo);
                }
            } finally {
                lock.unlock();
            }
        }
    }


    public ContractConditionOrderPo remove(Integer contractId, Long conditionOrderId, BigDecimal triggerPrice) {
        Integer index = this.getContractDataIndex(contractId);
        if (index == null) {
            log.warn("contractId={} orderData not exist", contractId);
            return null;
        }
        //获取该合约下所有条件单
        ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, ContractConditionOrderPo>> contractOrderMap = this.orderDataList.get(index);
        //获取该强平价下所有持仓
        ConcurrentHashMap<Long, ContractConditionOrderPo> priceOrderMap = contractOrderMap.get(triggerPrice);
        if (CollectionUtils.isEmpty(priceOrderMap)) {
            return null;
        }
        return priceOrderMap.get(conditionOrderId);
    }


    public List<Map<Long, ContractConditionOrderPo>> getRemoveList(Integer contractId, BigDecimal price) {
        if (SlaveDataSortTypeEnum.ASC.getCode() == this.sortType.getCode()) {
            return this.getASCSortTriggerList(contractId, price);
        } else {
            return this.getDESCSortTriggerList(contractId, price);
        }
    }

    private List<Map<Long, ContractConditionOrderPo>> getDESCSortTriggerList(Integer contractId, BigDecimal price) {
        List<Map<Long, ContractConditionOrderPo>> result = new ArrayList<>();

        Integer index = this.getContractDataIndex(contractId);
        if (null == index) {
            return result;
        }
        ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, ContractConditionOrderPo>> contractOrderMap = this.orderDataList.get(index);

        for (Map.Entry<BigDecimal, ConcurrentHashMap<Long, ContractConditionOrderPo>> entry : contractOrderMap.entrySet()) {
            //看空,正序,强平价小于等于当前价(即触发强平),如果大于则不继续遍历
            if (entry.getKey().compareTo(price) > 0) {
                break;
            }
            result.add(entry.getValue());
        }
        return result;
    }

    private List<Map<Long, ContractConditionOrderPo>> getASCSortTriggerList(Integer contractId, BigDecimal price) {
        List<Map<Long, ContractConditionOrderPo>> result = new ArrayList<>();

        Integer index = this.getContractDataIndex(contractId);
        if (null == index) {
            return result;
        }
        ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, ContractConditionOrderPo>> contractOrderMap = this.orderDataList.get(index);

        for (Map.Entry<BigDecimal, ConcurrentHashMap<Long, ContractConditionOrderPo>> entry : contractOrderMap.entrySet()) {
            //看涨,倒序,强平价大于等于当前价(即触发强平),如果小于则不继续遍历
            if (entry.getKey().compareTo(price) < 0) {
                break;
            }
            result.add(entry.getValue());
        }
        return result;
    }

    private Integer getContractDataIndex(Integer contractId) {
        return contractIndexMap.get(contractId);
    }


}
