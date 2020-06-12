package org.apdoer.condition.core.handle.impl;

import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.core.model.vo.OrderConditionHisQueryRespVo;
import org.apdoer.condition.core.utils.ModeBuildlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


@Service
public class RedisCacheHandler {
	
    /*条件redis前缀，规则 o_{userId}*/
    private static final String ORDER_REDIS_PREFIX = "cdo_";

    @Autowired
    private RedisTemplate<Object, Object> redisTemplateOrder;


    /**
     * 条件单入-redis
     * @param userId
     * @param orderId
     * @param order
     */
    public void hPutConditionOrder(Integer userId, Long orderId, ContractConditionOrderPo order) {
        String key = ORDER_REDIS_PREFIX + userId;
        redisTemplateOrder.opsForHash().put(key, orderId.toString(), order);
    }
    
    /**
     * 条件单-删除redis
     * @param userId
     * @param uuid
     */
    public void hDeleteConditionOrder(Integer userId, Long uuid) {
        String key = ORDER_REDIS_PREFIX + userId;
        redisTemplateOrder.opsForHash().delete(key, uuid.toString());
    }
    
    /**
     * 获取条件单列表
     * @param userId
     * @param contractId
     * @return
     */
    public List<OrderConditionHisQueryRespVo> hEntriesConditionOrder(Integer userId, Integer contractId) {
        String key = ORDER_REDIS_PREFIX + userId;
        Map<Object, Object> resultMap = redisTemplateOrder.opsForHash().entries(key);

        List<OrderConditionHisQueryRespVo> result = new ArrayList<>(resultMap.size());
        for (Map.Entry<Object, Object> entry : resultMap.entrySet()) {
        	ContractConditionOrderPo order = (ContractConditionOrderPo) entry.getValue();
        	if (null == contractId || contractId.intValue() == order.getContractId().intValue()) {
        		result.add(ModeBuildlUtils.buildOrderConditionHisQueryRespVo(order));
        	}
        }
        result.sort(Comparator.comparing(OrderConditionHisQueryRespVo::getCreateTime).reversed());
        return result;
    }
    
    public int activeOrderSize(Integer userId, Integer contractId) {
    	int times = 0;
    	List<OrderConditionHisQueryRespVo> list = this.hEntriesConditionOrder(userId, contractId);
    	if (null == list || list.size() == 0) {
    		return times;
    	}
		for (OrderConditionHisQueryRespVo v : list) {
			if (v.getContractId().intValue() == contractId.intValue()) {
				times ++;
			}
		}
		return times;
    }
}
