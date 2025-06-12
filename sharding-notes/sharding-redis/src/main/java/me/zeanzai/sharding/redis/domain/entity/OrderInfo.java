package me.zeanzai.sharding.redis.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author shawnwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("order_info")
public class OrderInfo extends BaseEntity {


    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     *运费
     */
    private BigDecimal orderFreight;

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
     * 支付状态,1待支付,2支付成功
     */
    private Long payStatus;

    /**
     *
     *商户ID
     */
    private Long merchantId;

    /**
     *
     * 用户ID
     */
    private Long userId;

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
     *
     * 支付优惠金额
     */
    private BigDecimal payDiscountAmount;

    /**
     *
     * 收货地址ID
     */
    private Long addressId;

    /**
     *
     * 订单备注留言
     */
    private String remark;
    /**
     *配送方式，1自提。2配送
     */
    private Long deliveryType;

    /**
     *配送状态，0待收货，1已收货配送中，2已收货，已送达',
     */
    private Long deliveryStatus;

    /**
     *配送预计送达时间
     */
    private Date deliveryExpectTime;

    /**
     *配送送达时间
     */
    private Date deliveryCompleteTime;
    /**
     *配送运费
     */
    private BigDecimal deliveryAmount;

    /**
     *买家状态，0待收货，1已收货，2换货，3退货
     */
    private Long buyerStatus;
    /**
     *优惠券id
     */
    private Long couponId;
}
