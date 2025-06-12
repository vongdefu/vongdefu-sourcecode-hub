package me.zeanzai.shardingmultids.domain.vo;

import lombok.Data;
import me.zeanzai.shardingmultids.domain.dto.OrderItemDetailDto;
import me.zeanzai.shardingmultids.domain.entity.OrderInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author shawnwang
 */
@Data
public class OrderItemDetailVO implements Serializable {
    /**
     * 订单表
     */
    private OrderInfo orderInfo;
    /**
     * 订单详情表
     */
    private List<OrderItemDetailDto> orderItemDetails;
}

