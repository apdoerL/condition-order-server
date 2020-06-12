package org.apdoer.condition.common.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.apdoer.condition.common.db.model.po.MsBrokerUserPo;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

public interface BrokerUserMapper extends BaseMapper<MsBrokerUserPo> {

	/**
	 * 查询用户券商信息
	 * 
	 * @param groupId
	 * @return
	 */
	String queryChannelNameSelectByGroupId(@Param("groupId") Integer groupId);

	/**
	 * 查询用户组所属券商
	 * 
	 * @param userId
	 * @return
	 */
	Integer queryGroup(@Param("userId") Integer userId);

	/**
	 * 查询券商（非运营中心），旗下所有组（包括旗下运营中心组）
	 * 
	 * @param brokerId
	 * @return
	 */
	List<Integer> queryAllBrokerGroupListByBrokerId(@Param("brokerId") Integer brokerId);

	/**
	 * 查询用户可管理的组列表（包括旗下经济人组，不包括旗下运营中心组）
	 * 
	 * @param userId
	 * @return
	 */
	List<Integer> queryBrokerManagerGroupByBrokerId(@Param("userId") Integer userId);

	/**
	 * 查詢某個券商權限值
	 * 
	 * @param brokerId
	 * @param paramId
	 * @return
	 */
	Integer queryBrokerParamValueByBrokerIdAndParamId(@Param("brokerId") Integer brokerId,
                                                      @Param("paramId") Long paramId);

}