package me.zeanzai.sharding.datasource.bean.request.order;

import lombok.Data;
import me.zeanzai.sharding.datasource.domain.entity.OrderInfo;
import me.zeanzai.sharding.datasource.domain.entity.OrderItemDetail;

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

