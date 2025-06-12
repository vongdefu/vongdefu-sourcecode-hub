package me.zeanzai.shardingmultids.bean.request.order;

import lombok.Data;
import me.zeanzai.shardingmultids.domain.entity.OrderInfo;
import me.zeanzai.shardingmultids.domain.entity.OrderItemDetail;

import java.io.Serializable;
import java.util.List;

/**
 * @author shawnwang
 */
@Data
public class UserOrderInfoRequestDTO implements Serializable {
    /**
     * 订单信息
     */
    private OrderInfo orderInfo;
    /**
     * 订单详情
     */
    private List<OrderItemDetail> orderItemDetailList;

}

