package org.apdoer.condition.core.check.impl;


import org.apache.commons.lang.StringUtils;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.util.ResultVoBuildUtils;
import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.common.db.service.ContractConditionOrderService;
import org.apdoer.condition.common.db.service.QuotConfigCenterService;
import org.apdoer.condition.common.enums.OrderStatusEnum;
import org.apdoer.condition.core.check.OrderCheck;
import org.apdoer.condition.core.code.ExceptionCode;
import org.apdoer.condition.core.model.vo.OrderConditionCancelReqVo;
import org.apdoer.condition.core.model.vo.OrderConditionCancelsReqVo;
import org.apdoer.condition.core.model.vo.OrderConditionReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class OrderChecker implements OrderCheck {

	@Autowired
	private ContractConditionOrderService contractConditionOrderService;
	@Autowired
	private QuotConfigCenterService quotConfigCenterService;

	
	@Override
	public ResultVo checkPlaceOrderParamIsLegal(Integer userId, OrderConditionReqVo requestVo) {

		//當前委托次數受限制

		// 判断数量是否合法

		// 买卖方向检查

		// 检查orderType是否合法

		// 限价时，检查price是否合法

		// 开仓，平仓标志检查

		// 检查marginType是否合法

		// 检查marginRate是否合法

		// 判断数量是否合法

		//校验触发类型是否合法

		//触发价格是否合法

		//校验
		return ResultVoBuildUtils.buildSuccessResultVo();
	}

	@Override
	public ResultVo checkOrderCancelParamIsLegal(Integer userId, OrderConditionCancelReqVo requestVo) {
		if (null == requestVo || null == requestVo.getConditionOrderId()) {
			return ResultVoBuildUtils.buildResultVo(ExceptionCode.REQUEST_PARAM_INVALID.getCodeNo(), ExceptionCode.REQUEST_PARAM_INVALID.getCodeName());
		}
		ContractConditionOrderPo orderPo = this.contractConditionOrderService.queryOrder(requestVo.getConditionOrderId(), userId, OrderStatusEnum.WAIT.getCode());
		if (null != orderPo) {
			return ResultVoBuildUtils.buildSuccessResultVo(orderPo);	
		} else {
			return ResultVoBuildUtils.buildFaildResultVo("orderId invalid");
		}
	}
	

	@Override
	public ResultVo checkOrderCancelsParamIsLegal(OrderConditionCancelsReqVo requestVo) {
		if (null == requestVo || null == requestVo.getConditionOrderIdList()) {
			return ResultVoBuildUtils.buildResultVo(ExceptionCode.REQUEST_PARAM_INVALID.getCodeNo(), ExceptionCode.REQUEST_PARAM_INVALID.getCodeName());
		}
		return ResultVoBuildUtils.buildSuccessResultVo();
	}
	
	@Override
	public ResultVo checkOrderHisQueryParamIsLegal(Integer contractId, Integer side, Integer status) {

		return ResultVoBuildUtils.buildSuccessResultVo();
	}
	
	private boolean hasTimesLimit(Integer userId, Integer contractId) {
		return true;
	}
	
	/**
	 * 检查orderType是否合法
	 * 
	 * @param orderTypeCode
	 * @return
	 */
	private boolean checkOrderTypeIsLegal(Integer orderTypeCode) {

		return true;
	}
	
	/**
	 * 检查side是否合法
	 * 
	 * @param side
	 * @return
	 */
	private boolean checkSideIsLegal(Integer side) {

		return true;
	}
	
	/**
	 * 检查maginType是否合法
	 * 
	 * @param marginType
	 * @return
	 */
	private boolean checkMarginTypeIsLegal(Integer marginType) {

		return true;
	}
	
	/**
	 * 校验保证金率是否合法
	 * @param marginType
	 * @param marginRate
	 * @return
	 */
	private boolean checkMarginRateIsLegal(Integer marginType, BigDecimal marginRate) {
		return true;
	} 
	
	/**
	 * 检查marginRate是否合法
	 * 
	 * @param marginRate
	 * @return
	 */
	private boolean checkMarginRateIsLegal(BigDecimal marginRate, boolean zeroFlag) {
		if (null == marginRate) {
			return false;
		} else {
			if (!zeroFlag) {
				//不允許：0
				if (marginRate.compareTo(BigDecimal.ZERO) <= 0) {
					return false;
				}
			} else {
				//允許：0
				if (marginRate.compareTo(BigDecimal.ZERO) < 0) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * 开仓，平仓标志检查
	 * 
	 * @param posititionEffect
	 * @return
	 */
	private boolean checkPositionEffectIsLegal(Integer posititionEffect) {
		return true;
	}
	
	/**
	 * 报价是否合法
	 * 
	 * @param price
	 * @param priceTick
	 * @return
	 */
	private boolean checkPriceIsLegal(BigDecimal price, BigDecimal priceTick) {
		if (null == price) {
			return false;
		} else {
			// 是否大于0
			if (price.compareTo(BigDecimal.ZERO) <= 0) {
				return false;
			}
			if (null == priceTick || priceTick.compareTo(new BigDecimal(0)) == 0) {
				return true;
			}
			// price是否是priceTick的整数倍
			BigDecimal priceRemainder = price.divideAndRemainder(priceTick)[1];
			if (priceRemainder.compareTo(BigDecimal.ZERO) != 0) {
				return false;
			}
			return true;
		}
	}
	
	/**
	 * 检查数量是否合法
	 * 
	 * @return
	 */
	private boolean checkQuantityIsLegal(BigDecimal quantity, BigDecimal lotSize) {
		if (null == quantity) {
			return false;
		} else {
			// 是否大于0
			if (quantity.compareTo(new BigDecimal(0)) <= 0) {
				return false;
			}
			if (null == lotSize || lotSize.compareTo(new BigDecimal(0)) == 0) {
				return true;
			}
			// 数量是否是lotSize的整数倍
			BigDecimal numRemainder = quantity.divideAndRemainder(lotSize)[1];
			if (numRemainder.compareTo(BigDecimal.ZERO) != 0) {
				return false;
			}
			return true;
		}
	}
	
	/**
	 * 检查是否为数字
	 * 
	 * @param arg
	 * @return
	 */
	private boolean isFigure(String arg) {
		if (StringUtils.isBlank(arg)) {
			return false;
		}
		// 数量是否为数字
		Pattern patternNum = Pattern.compile("(\\-?)[0-9]*(\\.?)[0-9]*");
		Matcher matcherNum = patternNum.matcher(arg);
		if (!matcherNum.matches()) {
			return false;
		} else {
			return true;
		}
	}

}
