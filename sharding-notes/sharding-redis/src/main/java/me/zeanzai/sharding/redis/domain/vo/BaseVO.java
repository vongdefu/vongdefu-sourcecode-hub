package me.zeanzai.sharding.redis.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shawnwang
 */
@Data
public class BaseVO implements Serializable {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建用户
     */
    private Long createUserId;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新用户
     */
    private Long updateUserId;
    /**
     * 是否已删除
     */
    private Integer deleteFlag;

}

