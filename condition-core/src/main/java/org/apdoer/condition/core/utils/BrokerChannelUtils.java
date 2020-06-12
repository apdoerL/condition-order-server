package org.apdoer.condition.core.utils;


import org.apdoer.common.service.util.JacksonUtil;
import org.apdoer.condition.common.db.model.po.UserExtPo;
import org.apdoer.condition.common.enums.UserTypeEnum;
import org.apdoer.condition.core.service.MsBrokerService;

import java.util.Map;


public class BrokerChannelUtils {

	public static MsBrokerService msBrokerService;

	public static boolean isSon(Integer accountId) {
		// 如果配置了全部发送到topic
		if (msBrokerService.isSendTopicAll()) {
			return true;
		}
		UserExtPo po = msBrokerService.selectUserExtByUserId(accountId);
		if (null != po) {
			if (UserTypeEnum.SON.getCode().intValue() == po.getUserType().intValue()
					|| UserTypeEnum.BROKER_OPERATION.getCode().intValue() == po.getUserType().intValue()
					|| UserTypeEnum.OPERATION_SON.getCode().intValue() == po.getUserType().intValue()) {
				return true;
			} else {
				return false;
			}

		}
		return false;
	}

	public static String getBrokerChannelName(Integer accountId) {
		if (msBrokerService.isSendTopicAll()) {
			return msBrokerService.getConfigNoticTopic();
		}
		return msBrokerService.queryBrokerChannelSubject(accountId);
	}

	public static void sendSms(String topic, Map<String, Object> data) {
		msBrokerService.sendSms(topic, JacksonUtil.toJson(data));
	}
	
	public static void sendSms(String topic, String data) {
		msBrokerService.sendSms(topic, data);
	}
}
