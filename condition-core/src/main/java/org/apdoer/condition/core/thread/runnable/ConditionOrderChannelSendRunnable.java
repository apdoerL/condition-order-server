package org.apdoer.condition.core.thread.runnable;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import org.apdoer.channel.client.client.ChannelClient;
import org.apdoer.channel.client.dto.MsgerRequestDto;
import org.apdoer.common.service.code.ExceptionCode;
import org.apdoer.common.service.model.po.UserPo;
import org.apdoer.common.service.service.UserService;
import org.apdoer.common.service.util.JacksonUtil;
import org.apdoer.condition.common.db.model.po.ContractConditionOrderPo;
import org.apdoer.condition.common.enums.ConditionOrderTypeEnum;
import org.apdoer.condition.core.constants.BusinessConstants;
import org.apdoer.condition.core.utils.BrokerChannelUtils;
import org.apdoer.condition.core.utils.ModeBuildlUtils;
import org.apdoer.condition.core.utils.TradeDateUtil;

import java.util.HashMap;
import java.util.Map;

public class ConditionOrderChannelSendRunnable implements Runnable {

	private ContractConditionOrderPo orderPo;
	private ChannelClient channelClient;
	private UserService userService;

	public ConditionOrderChannelSendRunnable(ContractConditionOrderPo orderPo, ChannelClient channelClient,
			UserService userService) {
		this.orderPo = orderPo;
		this.channelClient = channelClient;
		this.userService = userService;
	}

	@Override
	public void run() {
		if (BrokerChannelUtils.isSon(this.orderPo.getUserId())) {
	       	String brokerTopic = BrokerChannelUtils.getBrokerChannelName(this.orderPo.getUserId());
         	//券商則發送到topic
         	if (StringUtils.isNotBlank(brokerTopic)) {
         		BrokerChannelUtils.sendSms(brokerTopic, JacksonUtil.toJson(ModeBuildlUtils.buildConditionOrderMqDto(this.orderPo)));
         	} else {
         		this.channelSend(this.orderPo);
         	}
		} else {
			this.channelSend(this.orderPo);
		}
	}

	/**
	 * 短信發送
	 * 
	 * @param orderPo
	 */
	private void channelSend(ContractConditionOrderPo orderPo) {
		MsgerRequestDto requestDto = this.buildMsgerRequestDto(orderPo);
		if (null != requestDto) {
			this.channelClient.send(requestDto);
		} else {
			return;
		}
	}

	private MsgerRequestDto buildMsgerRequestDto(ContractConditionOrderPo orderPo) {
		// 触发成功
		if (ExceptionCode.SUCCESS.getCode() == orderPo.getCode()) {
			if (ConditionOrderTypeEnum.COMMON.getCode() == orderPo.getConditionOrderType()) {
				return this.buildCommonConditionOrderSuccMsgerRequestDto(orderPo);
			} else if (ConditionOrderTypeEnum.LOSS.getCode() == orderPo.getConditionOrderType()) {
				return this.buildLossConditionOrderSuccMsgerRequestDto(orderPo);
			} else {
				return this.buildProfitConditionOrderSuccMsgerRequestDto(orderPo);
			}
		} else { // 触发失败
			if (ConditionOrderTypeEnum.COMMON.getCode() == orderPo.getConditionOrderType()) {
				return this.buildCommonConditionOrderFailMsgerRequestDto(orderPo);
			} else if (ConditionOrderTypeEnum.LOSS.getCode() == orderPo.getConditionOrderType()) {
				return this.buildLossConditionOrderFailMsgerRequestDto(orderPo);
			} else {
				return this.buildProfitConditionOrderFailMsgerRequestDto(orderPo);
			}
		}
	}

	/**
	 * 普通条件单-触发成功
	 * 
	 * @param orderPo
	 * @return
	 */
	private MsgerRequestDto buildCommonConditionOrderSuccMsgerRequestDto(ContractConditionOrderPo orderPo) {
		UserPo userPo = this.userService.getUserById(orderPo.getUserId());
		if (null != userPo) {
			String template = null;
			String contact = null;
			if (StringUtils.isNotBlank(userPo.getPhone())) {
				template = BusinessConstants.ConditionOrderSuccessPhone;
				contact = userPo.getPhone();
			} else if (StringUtils.isNotBlank(userPo.getEmail())) {
				template = BusinessConstants.ConditionOrderSuccessEmail;
				contact = userPo.getEmail();
			} else {
				return null;
			}
			Map<String, String> params = new HashMap<String, String>(3);
			params.put(BusinessConstants.contract, this.getSymbolByCurrencyId(orderPo.getContractId()));
			params.put(BusinessConstants.time, TradeDateUtil.formatDay(System.currentTimeMillis()));
			params.put(BusinessConstants.price, orderPo.getRealTriggerPrice().toPlainString());
			MsgerRequestDto requestDto = this.buildMsgerRequestDto(template, params);
			requestDto.setContact(contact);
			requestDto.setIp("127.0.0.1");
			return requestDto;
		} else {
			return null;
		}
	}

	/**
	 * 普通条件单-触发失败
	 * 
	 * @param orderPo
	 * @return
	 */
	private MsgerRequestDto buildCommonConditionOrderFailMsgerRequestDto(ContractConditionOrderPo orderPo) {
		UserPo userPo = this.userService.getUserById(orderPo.getUserId());
		if (null != userPo) {
			String template = null;
			String contact = null;
			if (StringUtils.isNotBlank(userPo.getPhone())) {
				template = BusinessConstants.ConditionOrderFailedPhone;
				contact = userPo.getPhone();
			} else if (StringUtils.isNotBlank(userPo.getEmail())) {
				template = BusinessConstants.ConditionOrderFailedEmail;
				contact = userPo.getEmail();
			} else {
				return null;
			}
			Map<String, String> params = new HashMap<String, String>(3);
			params.put(BusinessConstants.contract, this.getSymbolByCurrencyId(orderPo.getContractId()));
			params.put(BusinessConstants.time, TradeDateUtil.formatDay(System.currentTimeMillis()));
			params.put(BusinessConstants.price, orderPo.getRealTriggerPrice().toPlainString());
			MsgerRequestDto requestDto = this.buildMsgerRequestDto(template, params);
			requestDto.setContact(contact);
			requestDto.setIp("127.0.0.1");
			return requestDto;
		} else {
			return null;
		}
	}

	/**
	 * 止损条件单-触发成功
	 * 
	 * @param orderPo
	 * @return
	 */
	private MsgerRequestDto buildLossConditionOrderSuccMsgerRequestDto(ContractConditionOrderPo orderPo) {
		UserPo userPo = this.userService.getUserById(orderPo.getUserId());
		if (null != userPo) {
			String template = null;
			String contact = null;
			if (StringUtils.isNotBlank(userPo.getPhone())) {
				template = BusinessConstants.StopLossOrderSuccessPhone;
				contact = userPo.getPhone();
			} else if (StringUtils.isNotBlank(userPo.getEmail())) {
				template = BusinessConstants.StopLossOrderSuccessEmail;
				contact = userPo.getEmail();
			} else {
				return null;
			}
			Map<String, String> params = new HashMap<String, String>(3);
			params.put(BusinessConstants.contract, this.getSymbolByCurrencyId(orderPo.getContractId()));
			params.put(BusinessConstants.time, TradeDateUtil.formatDay(System.currentTimeMillis()));
			params.put(BusinessConstants.price, orderPo.getRealTriggerPrice().toPlainString());
			MsgerRequestDto requestDto = this.buildMsgerRequestDto(template, params);
			requestDto.setContact(contact);
			requestDto.setIp("127.0.0.1");
			return requestDto;
		} else {
			return null;
		}
	}

	/**
	 * 止损条件单-触发失败
	 * 
	 * @param orderPo
	 * @return
	 */
	private MsgerRequestDto buildLossConditionOrderFailMsgerRequestDto(ContractConditionOrderPo orderPo) {
		UserPo userPo = this.userService.getUserById(orderPo.getUserId());
		if (null != userPo) {
			String template = null;
			String contact = null;
			if (StringUtils.isNotBlank(userPo.getPhone())) {
				template = BusinessConstants.StopLossOrderFailedPhone;
				contact = userPo.getPhone();
			} else if (StringUtils.isNotBlank(userPo.getEmail())) {
				template = BusinessConstants.StopLossOrderFailedEmail;
				contact = userPo.getEmail();
			} else {
				return null;
			}
			Map<String, String> params = new HashMap<String, String>(3);
			params.put(BusinessConstants.contract, this.getSymbolByCurrencyId(orderPo.getContractId()));
			params.put(BusinessConstants.time, TradeDateUtil.formatDay(System.currentTimeMillis()));
			params.put(BusinessConstants.price, orderPo.getRealTriggerPrice().toPlainString());
			MsgerRequestDto requestDto = this.buildMsgerRequestDto(template, params);
			requestDto.setContact(contact);
			requestDto.setIp("127.0.0.1");
			return requestDto;
		} else {
			return null;
		}
	}

	/**
	 * 止损条件单-触发成功
	 * 
	 * @param orderPo
	 * @return
	 */
	private MsgerRequestDto buildProfitConditionOrderSuccMsgerRequestDto(ContractConditionOrderPo orderPo) {
		UserPo userPo = this.userService.getUserById(orderPo.getUserId());
		if (null != userPo) {
			String template = null;
			String contact = null;
			if (StringUtils.isNotBlank(userPo.getPhone())) {
				template = BusinessConstants.StopProfitOrderSuccessPhone;
				contact = userPo.getPhone();
			} else if (StringUtils.isNotBlank(userPo.getEmail())) {
				template = BusinessConstants.StopProfitOrderSuccessEmail;
				contact = userPo.getEmail();
			} else {
				return null;
			}
			Map<String, String> params = new HashMap<String, String>(3);
			params.put(BusinessConstants.contract, this.getSymbolByCurrencyId(orderPo.getContractId()));
			params.put(BusinessConstants.time, TradeDateUtil.formatDay(System.currentTimeMillis()));
			params.put(BusinessConstants.price, orderPo.getRealTriggerPrice().toPlainString());
			MsgerRequestDto requestDto = this.buildMsgerRequestDto(template, params);
			requestDto.setContact(contact);
			requestDto.setIp("127.0.0.1");
			return requestDto;
		} else {
			return null;
		}
	}

	/**
	 * 止损条件单-触发失败
	 * 
	 * @param orderPo
	 * @return
	 */
	private MsgerRequestDto buildProfitConditionOrderFailMsgerRequestDto(ContractConditionOrderPo orderPo) {
		UserPo userPo = this.userService.getUserById(orderPo.getUserId());
		if (null != userPo) {
			String template = null;
			String contact = null;
			if (StringUtils.isNotBlank(userPo.getPhone())) {
				template = BusinessConstants.StopProfitOrderFailedPhone;
				contact = userPo.getPhone();
			} else if (StringUtils.isNotBlank(userPo.getEmail())) {
				template = BusinessConstants.StopProfitOrderFailedEmail;
				contact = userPo.getEmail();
			} else {
				return null;
			}
			Map<String, String> params = new HashMap<String, String>(3);
			params.put(BusinessConstants.contract, this.getSymbolByCurrencyId(orderPo.getContractId()));
			params.put(BusinessConstants.time, TradeDateUtil.formatDay(System.currentTimeMillis()));
			params.put(BusinessConstants.price, orderPo.getRealTriggerPrice().toPlainString());
			MsgerRequestDto requestDto = this.buildMsgerRequestDto(template, params);
			requestDto.setContact(contact);
			requestDto.setIp("127.0.0.1");
			return requestDto;
		} else {
			return null;
		}
	}

	private String getSymbolByCurrencyId(Integer contractId) {
		return null;
	}

	private MsgerRequestDto buildMsgerRequestDto(String template, Map<String, String> params) {
		MsgerRequestDto dto = new MsgerRequestDto();
		dto.setTemplate(template);
		dto.setParams(params);
		return dto;
	}

}
