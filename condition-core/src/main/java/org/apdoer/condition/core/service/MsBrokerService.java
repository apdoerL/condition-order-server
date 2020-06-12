package org.apdoer.condition.core.service;


import org.apdoer.condition.common.db.model.po.UserExtPo;

public interface MsBrokerService {

	/**
	 * 是否全部發送到topic
	 * 
	 * @return
	 */
	boolean isSendTopicAll();

	/**
	 * 获取配置的topic
	 * 
	 * @return
	 */
	String getConfigNoticTopic();

	/**
	 * 查询用户额外信息
	 * 
	 * @param userId
	 * @return
	 */
	UserExtPo selectUserExtByUserId(Integer userId);

	/**
	 * 查询子账号对于券商的消息主题
	 * 
	 * @param userId
	 * @return
	 */
	String queryBrokerChannelSubject(Integer userId);

	/**
	 * 查询用户组
	 * 
	 * @param userId
	 * @return
	 */
	Integer queryUserGroup(Integer userId);

	/**
	 * 向券商發送SMS消息（mq）
	 * 
	 * @param topic
	 * @param tags
	 * @param body
	 */
	void sendSms(String topic, String body);

}
