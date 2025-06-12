package me.zeanzai.seataatorder.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (Storage)实体类
 *
 * @author makejava
 * @since 2023-03-19 12:29:17
 */
public class Storage implements Serializable {
    private static final long serialVersionUID = -34396930198790993L;

    private Long id;

    private String name;
    /**
     * 数量
     */
    private Long num;

    private Date createTime;
    /**
     * 单价，单位分
     */
    private Long price;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

}

