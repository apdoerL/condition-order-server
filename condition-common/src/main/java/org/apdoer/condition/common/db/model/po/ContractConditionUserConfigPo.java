package org.apdoer.condition.common.db.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "web_contract_condition_user_config")
public class ContractConditionUserConfigPo implements Serializable {
	
	@Id
	@Column(name = "user_id")
    private Integer userId;
	
	@Id
	@Column(name = "contract_id")
    private Integer contractId;
	
	
	@Column(name = "active_times")
    private Integer activeTimes;


}
