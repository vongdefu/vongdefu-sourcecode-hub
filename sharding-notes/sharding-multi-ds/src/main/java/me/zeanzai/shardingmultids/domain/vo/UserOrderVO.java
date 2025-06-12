package me.zeanzai.shardingmultids.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author shawnwang
 */
@Data
public class UserOrderVO implements Serializable {
    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单状态,10待付款，20待接单，30已接单，40配送中，50已完成，55部分退款，60全部退款，70取消订单'
     */
    private Integer orderStatus;

    /**
     *
     *'交易时间'
     */
    private Date transTime;

    /**
     *
     *商户Id
     */
    private Long merchantId;

    /**
     *
     * 支付完成时间
     */
    private Date rechargeTime;

    /**
     *
     * 实际支付金额
     */
    private BigDecimal payAmount;

    /**
     *配送状态，0待收货，1已收货配送中，2已收货，已送达',
     */
    private Integer deliveryStatus;

    /**
     *配送预计送达时间
     */
    private Date deliveryExpectTime;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;
}
