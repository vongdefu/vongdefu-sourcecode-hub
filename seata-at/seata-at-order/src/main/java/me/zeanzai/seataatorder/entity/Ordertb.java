package me.zeanzai.seataatorder.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Ordertb)实体类
 *
 * @author makejava
 * @since 2023-03-19 14:53:29
 */
public class Ordertb implements Serializable {
    private static final long serialVersionUID = -46099949608075372L;

    private Long id;
    /**
     * 商品Id
     */
    private Long productId;
    /**
     * 数量
     */
    private Long num;
    /**
     * 用户唯一Id
     */
    private String userId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 订单状态 1 未付款 2 已付款 3 已完成
     */
    private Integer status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}

