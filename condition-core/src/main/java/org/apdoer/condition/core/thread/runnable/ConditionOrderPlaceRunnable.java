package org.apdoer.condition.core.thread.runnable;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.common.db.service.ContractConditionOrderService;
import org.apdoer.condition.common.enums.OrderStatusEnum;
import org.apdoer.condition.common.event.SourceEvent;
import org.apdoer.condition.common.event.eventbus.impl.GuavaEventBus;
import org.apdoer.condition.common.event.eventbus.impl.GuavaEventBusManager;
import org.apdoer.condition.core.code.ExceptionCode;
import org.apdoer.condition.core.constants.TradeCoreConstants;
import org.apdoer.condition.core.handle.impl.RedisCacheHandler;

import java.util.concurrent.TimeUnit;


/**
 * 條件單 下单
 *
 * @author apdoer
 */
@Slf4j
public class ConditionOrderPlaceRunnable implements Runnable {

//	private FutureTraderService futuretraderService;

    private ContractConditionOrderService contractConditionOrderService;

    private ContractConditionOrderPo orderPo;

    private RedisCacheHandler redisCacheHandler;

    public ConditionOrderPlaceRunnable(
            ContractConditionOrderService contractConditionOrderService, ContractConditionOrderPo orderPo,
            RedisCacheHandler redisCacheHandler) {
        this.contractConditionOrderService = contractConditionOrderService;
        this.redisCacheHandler = redisCacheHandler;
        this.orderPo = orderPo;
    }

    @Override
    public void run() {
        //撮合下单
//		FuturePlaceOrderVo orderVo = this.buildFuturePlaceOrderVo(this.orderPo);
//		ResultVo resultVo = this.futuretraderService.placeOrder(this.orderPo.getUserId(), orderVo);
        ResultVo resultVo = new ResultVo();
        this.orderPo.setCode(resultVo.getCode());
        this.redisCacheHandler.hDeleteConditionOrder(this.orderPo.getUserId(), this.orderPo.getConditionOrderId());
        if (ExceptionCode.SUCCESS.getCodeNo() == resultVo.getCode()) {
            String orderId = resultVo.getData().toString();
            this.orderPo.setOrderId(orderId);
            this.orderPo.setStatus(OrderStatusEnum.SUCC.getCode());
            log.info("Condition order place succ, userId={}, conditionOrderId={}, orderId={}", this.orderPo.getUserId(),
                    this.orderPo.getConditionOrderId(), orderId);
            int record = this.contractConditionOrderService.updateRecordStatusSucc(orderId,
                    this.orderPo.getConditionOrderId(), this.orderPo.getUserId(), this.orderPo.getRealTriggerPrice());
            if (record == 1) {
                log.info("Condition order update status succ, userId={}, conditionOrderId={}",
                        this.orderPo.getConditionOrderId(), this.orderPo.getUserId());
            } else {
                log.info("Condition order update status faild, userId={}, conditionOrderId={}",
                        this.orderPo.getConditionOrderId(), this.orderPo.getUserId());
            }
        } else {
            this.orderPo.setStatus(OrderStatusEnum.FAIL.getCode());
            log.info("Condition order place error, userId={}, conditionOrderId={}, errCode={}",
                    this.orderPo.getUserId(), this.orderPo.getConditionOrderId(), resultVo.getCode());
            int record = this.contractConditionOrderService.updateRecordStatusFail(this.orderPo.getConditionOrderId(),
                    resultVo.getCode(), this.orderPo.getUserId(), this.orderPo.getRealTriggerPrice());
            if (record == 1) {
                log.info("Condition order update status succ, userId={}, conditionOrderId={}",
                        this.orderPo.getConditionOrderId(), this.orderPo.getUserId());
            } else {
                log.info("Condition order update status faild, userId={}, conditionOrderId={}",
                        this.orderPo.getConditionOrderId(), this.orderPo.getUserId());
            }
        }
        try {
            GuavaEventBus eventBus = (GuavaEventBus) GuavaEventBusManager.getInstance()
                    .getEventBus(TradeCoreConstants.CONDITION_ORDER_CHANNEL);
            if (null != eventBus) {
                while (eventBus.tryDo(1, 1L, TimeUnit.SECONDS)) {
                    // 反压
                }
                eventBus.publish(new SourceEvent(this.orderPo));
            }
        } catch (Exception e) {
            log.error("condition order trigger eventBus publish error", e);
        }

    }

//	private FuturePlaceOrderVo buildFuturePlaceOrderVo(ContractConditionOrderPo orderPo) {
//		FuturePlaceOrderVo placeOrderVo = new FuturePlaceOrderVo();
//		placeOrderVo.setContractId(orderPo.getContractId());
//		if (null != orderPo.getMarginRate()) {
//			placeOrderVo.setMarginRate(orderPo.getMarginRate().toPlainString());
//		}
//		placeOrderVo.setMarginType(orderPo.getMarginType());
//		if (null != orderPo.getMinimalQuantity()) {
//			placeOrderVo.setMinimalQuantity(orderPo.getMinimalQuantity().toPlainString());
//		}
//		placeOrderVo.setOrderSubType(OrderSubType.DEFALUT.getCode());
//		placeOrderVo.setOrderType(orderPo.getOrderType());
//		placeOrderVo.setPositionEffect(orderPo.getPositionEffect());
//		if (null != orderPo.getOrderPrice()) {
//			placeOrderVo.setPrice(orderPo.getOrderPrice().toPlainString());
//		}
//		if (null != orderPo.getQuantity()) {
//			placeOrderVo.setQuantity(orderPo.getQuantity().toPlainString());
//		}
//		placeOrderVo.setSide(orderPo.getSide());
//		// placeOrderVo.setStopCondition(stopCondition);
//		// placeOrderVo.setStopPrice(stopPrice);
//		return placeOrderVo;
//	}
}
