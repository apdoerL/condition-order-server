package org.apdoer.condition.core.handle.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.service.SecurityUtils;
import org.apdoer.common.service.util.ResultVoBuildUtils;
import org.apdoer.condition.common.data.QuotData;
import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.common.db.service.ContractConditionOrderService;
import org.apdoer.condition.core.check.OrderCheck;
import org.apdoer.condition.core.code.ExceptionCode;
import org.apdoer.condition.core.handle.OrderHandler;
import org.apdoer.condition.core.model.vo.OrderConditionCancelReqVo;
import org.apdoer.condition.core.model.vo.OrderConditionCancelsReqVo;
import org.apdoer.condition.core.model.vo.OrderConditionHisQueryRespVo;
import org.apdoer.condition.core.model.vo.OrderConditionReqVo;
import org.apdoer.condition.core.service.TradeService;
import org.apdoer.condition.core.utils.ModeBuildlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OrderHandlerImpl implements OrderHandler {
	
	@Autowired
	private SecurityUtils securityUtils;

	@Autowired
	private OrderCheck orderCheck;
	
	@Autowired
	private TradeService tradeService;
	
	@Autowired
	private RedisCacheHandler redisCacheHandler;
	
	@Autowired
	private ContractConditionOrderService contractConditionOrderService;

	@Override
	public ResultVo placeOrderHandle(OrderConditionReqVo requestVo) {
		Integer accountId = this.getAccountId();
		if (null == accountId) {
			return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_NOT_LOGGED_IN.getCodeNo(), ExceptionCode.USER_NOT_LOGGED_IN.getCodeName());
		}
		ResultVo checkResult = this.orderCheck.checkPlaceOrderParamIsLegal(accountId, requestVo);
		if (ExceptionCode.SUCCESS.getCodeNo() != checkResult.getCode()) {
			return checkResult;
		}
		BigDecimal curtPrice = QuotData.get(requestVo.getTriggerType(), requestVo.getContractId());
		if (null == curtPrice) {
			return ResultVoBuildUtils.buildFaildResultVo("server not ready");
		}
		try {
			return this.tradeService.placeConditionOrder(accountId, curtPrice, requestVo);
		} catch (Exception e) {
			log.info("", e);
			return ResultVoBuildUtils.buildFaildResultVo("system error");
		}
	}

	@Override
	public ResultVo orderCancelHandle(OrderConditionCancelReqVo requestVo) {
		Integer accountId = this.getAccountId();
		if (null == accountId) {
			return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_NOT_LOGGED_IN.getCodeNo(), ExceptionCode.USER_NOT_LOGGED_IN.getCodeName());
		}
		ResultVo checkResult = this.orderCheck.checkOrderCancelParamIsLegal(accountId, requestVo);
		if (ExceptionCode.SUCCESS.getCodeNo()  != checkResult.getCode()) {
			return checkResult;
		}
		ContractConditionOrderPo orderPo = (ContractConditionOrderPo) checkResult.getData();
		return this.tradeService.cancelConditionOrder(orderPo);
	}
	
	@Override
	public ResultVo orderCancelsHandle(OrderConditionCancelsReqVo requestVo) {
		Integer accountId = this.getAccountId();
		if (null == accountId) {
			return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_NOT_LOGGED_IN.getCodeNo(), ExceptionCode.USER_NOT_LOGGED_IN.getCodeName());
		}
		ResultVo checkResult = this.orderCheck.checkOrderCancelsParamIsLegal(requestVo);
		if (ExceptionCode.SUCCESS.getCodeNo()  != checkResult.getCode()) {
			return checkResult;
		}
		for (Long conditionOrderId : requestVo.getConditionOrderIdList()) {
			OrderConditionCancelReqVo carequestVo = new OrderConditionCancelReqVo();
			carequestVo.setConditionOrderId(conditionOrderId);
			checkResult = this.orderCheck.checkOrderCancelParamIsLegal(accountId, carequestVo);
			if (ExceptionCode.SUCCESS.getCodeNo() == checkResult.getCode()) {
				ContractConditionOrderPo orderPo = (ContractConditionOrderPo) checkResult.getData();
				this.tradeService.cancelConditionOrder(orderPo);
			}
		}
		return ResultVoBuildUtils.buildSuccessResultVo();
	}


	@Override
	public ResultVo orderConditionQueryHandle(Integer contractId) {
		Integer accountId = this.getAccountId();
		if (null == accountId) {
			return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_NOT_LOGGED_IN.getCodeNo(), ExceptionCode.USER_NOT_LOGGED_IN.getCodeName());
		}
		return ResultVoBuildUtils.buildSuccessResultVo(this.redisCacheHandler.hEntriesConditionOrder(accountId, contractId));
	}

	@Override
	public ResultVo orderConditionHisQueryHandle(Integer contractId, Integer side, Integer status, Integer pageNum, Integer pageSize) {
		Integer accountId = this.getAccountId();
		if (null == accountId) {
			return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_NOT_LOGGED_IN.getCodeNo(), ExceptionCode.USER_NOT_LOGGED_IN.getCodeName());
		}
		ResultVo checkResult = this.orderCheck.checkOrderHisQueryParamIsLegal(contractId, side, status);
		if (ExceptionCode.SUCCESS.getCodeNo() != checkResult.getCode()) {
			return checkResult;
		}
		try {
			pageSize = pageSize < 100 ? pageSize : 100;
            PageHelper.startPage(pageNum, pageSize);
            List<ContractConditionOrderPo> orderList = this.contractConditionOrderService.queryHistory(accountId, contractId, side, status);
            PageInfo<ContractConditionOrderPo> poPageInfo = new PageInfo<>(orderList);

            List<OrderConditionHisQueryRespVo> respVoList = new ArrayList<>(orderList.size());
            for (ContractConditionOrderPo orderPo : orderList) {
                respVoList.add(ModeBuildlUtils.buildOrderConditionHisQueryRespVo(orderPo));
            }

            PageInfo<OrderConditionHisQueryRespVo> voPageInfo = new PageInfo<>(respVoList);
            voPageInfo.setPageNum(poPageInfo.getPageNum());
            voPageInfo.setPageSize(poPageInfo.getPageSize());
            voPageInfo.setTotal(poPageInfo.getTotal());
            voPageInfo.setPages(poPageInfo.getPages());
            voPageInfo.setSize(poPageInfo.getSize());
            return ResultVoBuildUtils.buildSuccessResultVo(voPageInfo);
        } catch (Exception e) {
            log.error("historyOrderList error.", e);
            return ResultVoBuildUtils.buildFaildResultVo("system error");
        }
	}
	
	private Integer getAccountId() {
		return this.securityUtils.getCurrentUserId();
	}
}
