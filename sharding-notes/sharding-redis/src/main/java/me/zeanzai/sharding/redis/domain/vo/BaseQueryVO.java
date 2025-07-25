package me.zeanzai.sharding.redis.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shawnwang
 */
@Data
public class BaseQueryVO implements Serializable {

    /**
     * 逻辑删除标识
     */
    private Integer deleteFlag;

}
