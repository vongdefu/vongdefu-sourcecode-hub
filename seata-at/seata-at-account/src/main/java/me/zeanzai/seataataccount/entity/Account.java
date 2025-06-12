package me.zeanzai.seataataccount.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Account)实体类
 *
 * @author makejava
 * @since 2023-03-19 14:25:36
 */
public class Account implements Serializable {
    private static final long serialVersionUID = -93213755322017090L;

    private Long id;
    /**
     * 用 户userId
     */
    private String userId;
    /**
     * 余额，单位分
     */
    private Long money;
    /**
     * 创建时间
     */
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}

