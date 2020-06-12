package org.apdoer.condition.core.thread.runnable;


import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.common.db.service.ContractConditionOrderService;
import org.apdoer.condition.common.enums.ConditionOrderTypeEnum;
import org.apdoer.condition.core.code.ExceptionCode;
import org.apdoer.condition.core.service.TradeService;

import java.util.List;

/**
 * 止盈止损撤单
 *
 * @author apdoer
 */
public class StopProfitLossCancelRunnable implements Runnable {

    private TradeService tradeService;
    private ContractConditionOrderService conditionOrderService;
    private ContractConditionOrderPo orderPo;

    public StopProfitLossCancelRunnable(ContractConditionOrderPo orderPo,
            ContractConditionOrderService conditionOrderService,
            TradeService tradeService) {
        this.orderPo = orderPo;
        this.conditionOrderService = conditionOrderService;
        this.tradeService = tradeService;
    }

    @Override
    public void run() {
        if (this.hasExecute(this.orderPo)) {
            List<ContractConditionOrderPo> cancels = this.conditionOrderService.queryUnTriggerOrderByUuid(this.orderPo.getUserId(), this.orderPo.getUuid());
            if (null != cancels && cancels.size() != 0) {
                for (ContractConditionOrderPo tempOrder : cancels) {
                    this.tradeService.cancelConditionOrder(tempOrder);
                }
            }
        }
    }

    private boolean hasExecute(ContractConditionOrderPo orderPo) {
        if (null != orderPo.getUuid() && null != this.orderPo.getConditionOrderType()
                && ConditionOrderTypeEnum.COMMON.getCode() != this.orderPo.getConditionOrderType()
                && orderPo.getCode() == ExceptionCode.SUCCESS.getCodeNo()) {
            return true;
        } else {
            return false;
        }
    }


}
