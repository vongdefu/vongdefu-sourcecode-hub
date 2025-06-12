package me.zeanzai.sharding.datasource.domain.query;

import lombok.Data;

/**
 * @author shawnwang
 */
@Data
public class BaseQuery{

    /**
     * 页数
     */
    private Integer pageNo = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

}

