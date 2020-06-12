package org.apdoer.condition.common.db.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户扩展表
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "web_user_ext")
public class UserExtPo implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6583700479564175544L;

	//uid
    @Id
    private Integer userId;

    private Integer userType;

    private Integer nodeType;

    private Integer partnerType;

    private String countryCode;

    private String areaCode;

    private Long krwDailyLimit;

    private Date createTime;

    private Date updateTime;
}
