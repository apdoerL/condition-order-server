package org.apdoer.condition.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.condition.common.db.mapper.BrokerUserMapper;
import org.apdoer.condition.common.db.mapper.UserExtMapper;
import org.apdoer.condition.common.db.model.po.UserExtPo;
import org.apdoer.condition.core.producer.RocketProducer;
import org.apdoer.condition.core.service.MsBrokerService;
import org.apdoer.condition.core.utils.RedisKeyBuildUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MsBrokerServiceImpl implements MsBrokerService {

	private static final long DEFAULT_PUSH_TIME_OUT = 600;

	@Value("${rocket-mq.order-property.tags}")
	public String tags;

	@Value("${rocket-mq.notice.send-all:false}")
	public boolean sendTopicAllFlag;

	@Value("${rocket-mq.notice.topic:topic_test}")
	public String noticeTopic;

	@Autowired
	private RocketProducer rocketProducer;
	@Autowired
	private BrokerUserMapper brokerUserMapper;
	@Autowired
	private UserExtMapper userExtMapper;
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Cacheable(value = "cdo", key = "'cdo_ext_'+#userId.toString()")
	@Override
	public UserExtPo selectUserExtByUserId(Integer userId) {
		try {
			return this.userExtMapper.selectByUserId(userId);
		} catch (Exception e) {
			log.error("Query user ext info error", e);
			throw new RuntimeException("Query user ext info error");
		}
	}

	@Cacheable(value = "PUSH", key = "'Push_subject_'+#userId.toString()")
	@Override
	public String queryBrokerChannelSubject(Integer userId) {
		Integer groupId = this.brokerUserMapper.queryGroup(userId);
		if (null == groupId) {
			return null;
		}
		return this.brokerUserMapper.queryChannelNameSelectByGroupId(groupId);
	}

	@Override
	public Integer queryUserGroup(Integer userId) {
		String key = RedisKeyBuildUtils.buildPushUserGroupRedisKey(userId);
		Object data = this.redisTemplate.opsForValue().get(key);
		if (null != data) {
			return (Integer) data;
		} else {
			Integer groupId = this.brokerUserMapper.queryGroup(userId);
			if (null == groupId) {
				groupId = 0; // 没有组，则默认为：0
			}
			this.redisTemplate.opsForValue().set(key, groupId, DEFAULT_PUSH_TIME_OUT, TimeUnit.SECONDS);
			return groupId;
		}
	}

	@Override
	public void sendSms(String topic, String body) {
		this.rocketProducer.send(topic, this.tags, body);
	}

	@Override
	public boolean isSendTopicAll() {
		return this.sendTopicAllFlag;
	}

	@Override
	public String getConfigNoticTopic() {
		return this.noticeTopic;
	}

}
