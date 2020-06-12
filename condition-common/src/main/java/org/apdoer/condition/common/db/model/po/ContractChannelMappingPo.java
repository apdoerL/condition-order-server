
package org.apdoer.condition.common.db.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author apdoer
 */
@Table(name = "web_contract_condition_channel_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractChannelMappingPo implements Serializable {
    @Id
    @Column(
            name = "contract_id"
    )
    private Integer contractId;
    @Column(
            name = "quot_channel"
    )
    private String quotChannel;
    @Column(
            name = "index_quot_listener"
    )
    private String indexQuotListener;
    @Column(
            name = "clear_quot_listener"
    )
    private String clearQuotListener;
    @Column(
            name = "index_cache_listener"
    )
    private String indexCacheListener;
    @Column(
            name = "clear_cache_listener"
    )
    private String clearCacheListener;
    @Column(
            name = "tick_channel"
    )
    private String tickChannel;
    @Column(
            name = "tick_listener"
    )
    private String tickListener;
    
    @Column(
            name = "tick_cache_listener"
    )
    private String tickCacheListener;

    @Column(
            name = "condition_order_channel"
    )
    private String conditionOrderChannel;
    @Column(
            name = "condition_order_listener"
    )
    private String conditionOrderListener;
    @Column(
            name = "description"
    )
    private String description;
    @Column(
            name = "create_time"
    )
    private Date createTime;


}


