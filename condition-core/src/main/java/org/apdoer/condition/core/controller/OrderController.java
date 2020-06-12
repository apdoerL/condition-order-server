package org.apdoer.condition.core.controller;

import org.apdoer.common.service.annotation.RequiresIdempotency;
import org.apdoer.common.service.annotation.RequiresPermissions;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.condition.core.handle.OrderHandler;
import org.apdoer.condition.core.model.vo.OrderConditionCancelReqVo;
import org.apdoer.condition.core.model.vo.OrderConditionCancelsReqVo;
import org.apdoer.condition.core.model.vo.OrderConditionReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/future/condition/order")
public class OrderController {
	
	@Autowired
	private OrderHandler orderHandler;
	
    /**
     * 条件单-开单
     */
    @RequiresIdempotency
    @RequiresPermissions(value = {"order:place"})
    @PostMapping("/placeOrder")
    public ResultVo placeOrder(@RequestBody OrderConditionReqVo requestVo) {
    	return this.orderHandler.placeOrderHandle(requestVo);
    }
    
    /**
     * 条件单-撤单
     */
    @RequiresIdempotency
    @RequiresPermissions(value = {"order:place"})
    @PostMapping("/cancel")
    public ResultVo orderCancel(@RequestBody OrderConditionCancelReqVo requestVo) {
    	return this.orderHandler.orderCancelHandle(requestVo);
    }
    
    @RequiresIdempotency
    @RequiresPermissions(value = {"order:place"})
    @PostMapping("/cancels")
    public ResultVo orderCancels(@RequestBody OrderConditionCancelsReqVo requestVo) {
    	return this.orderHandler.orderCancelsHandle(requestVo);
    }
    
    /**
     * 条件单-查询
     */
    @GetMapping("/activeQuery")
    public ResultVo orderConditionQuery(@RequestParam(name="contractId", required=false) Integer contractId) {
		return this.orderHandler.orderConditionQueryHandle(contractId);
    }
    
    /**
     * 条件单-查询
     */
    @GetMapping("/historyQuery")
    public ResultVo orderConditionHisQuery(@RequestParam(name="contractId", required=false) Integer contractId,
    		@RequestParam(name="side", required=false) Integer side,
    		@RequestParam(name="status", required=false) Integer status,
    		@RequestParam(name="pageNum", defaultValue="1") Integer pageNum,
    		@RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		return this.orderHandler.orderConditionHisQueryHandle(contractId, side, status, pageNum, pageSize);
    }

}
