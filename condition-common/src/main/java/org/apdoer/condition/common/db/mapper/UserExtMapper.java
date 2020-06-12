package org.apdoer.condition.common.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.apdoer.common.service.common.BaseMapper;
import org.apdoer.condition.common.db.model.po.UserExtPo;


public interface UserExtMapper extends BaseMapper<UserExtPo> {

	UserExtPo selectByUserId(@Param("userId") Integer userId);
}
