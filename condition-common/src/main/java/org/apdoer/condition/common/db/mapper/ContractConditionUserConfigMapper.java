package org.apdoer.condition.common.db.mapper;
import org.apache.ibatis.annotations.Param;
import org.apdoer.common.service.common.BaseMapper;
import org.apdoer.condition.common.db.model.po.ContractConditionUserConfigPo;

public interface ContractConditionUserConfigMapper extends BaseMapper<ContractConditionUserConfigPo> {
	
	ContractConditionUserConfigPo queryUserConfig(@Param("userId") Integer userId, @Param("contractId") Integer contractId);

}
