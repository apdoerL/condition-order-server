package org.apdoer.condition.core.utils;

public class RedisKeyBuildUtils {

	private static final String PUSH_USER_GROUP_REDIS_KEY = "PUG_";// 用户组

	public static String buildPushUserGroupRedisKey(Integer userId) {
		return PUSH_USER_GROUP_REDIS_KEY + userId;
	}
}
