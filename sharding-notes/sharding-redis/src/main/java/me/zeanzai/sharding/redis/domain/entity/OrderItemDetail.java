package me.zeanzai.sharding.redis.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author shawnwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("order_item_detail")
public class OrderItemDetail extends BaseEntity {


    /**
     *订单号
     */
    private String orderNo;

    /**
     *商品ID
     */
    private Long productId;


    /**
     *商品分类ID
     */
    private Long categoryId;

    /**
     *商品购买数量
     */
    private BigDecimal goodsNum;

    /**
     *商品单价
     */
    private BigDecimal goodsPrice;

    /**
     *商品总价
     */
    private BigDecimal goodsAmount;

    /**
     *商品优惠金额
     */
    private BigDecimal discountAmount;

    /**
     *参与活动ID
     */
    private Long discountId;

}