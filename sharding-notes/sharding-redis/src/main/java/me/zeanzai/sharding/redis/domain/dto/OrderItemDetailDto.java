package me.zeanzai.sharding.redis.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author shawnwang
 */
@Data
public class OrderItemDetailDto implements Serializable {
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

