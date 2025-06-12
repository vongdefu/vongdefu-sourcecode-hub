package me.zeanzai.sharding.datasource.domain.entity;

import lombok.Data;
import me.zeanzai.sharding.datasource.common.enums.DeleteFlag;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shawnwang
 */
@Data
public class BaseEntity implements Serializable {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建用户
     */
    private Long createUser;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新用户
     */
    private Long updateUser;
    /**
     * 是否已删除
     */
    private Integer deleteFlag;

    /**
     * 初始化参数
     */
    public void initEntity() {
        this.createTime = new Date();
        this.updateTime = new Date();
        this.deleteFlag = DeleteFlag.NOT_DELETE.getCode();
    }

    /**
     * 初始化 - 指定操作人id
     * @param operatorId
     */
    public void initEntity(Long operatorId) {
        this.createTime = new Date();
        this.createUser = operatorId;
        this.updateTime = new Date();
        this.updateUser = operatorId;
        this.deleteFlag = DeleteFlag.NOT_DELETE.getCode();
    }

    /**
     * 修改初始化
     */
    public void updateEntity(){
        this.updateTime = new Date();
    }

    /**
     * 修改初始化 - 指定操作人
     * @param operatorId
     */
    public void updateEntity(Long operatorId){
        this.updateTime = new Date();
        this.updateUser = operatorId;
    }

    /**
     * 删除初始化
     */
    public void deleteEntity(){
        this.deleteFlag = DeleteFlag.DELETED.getCode();
    }

    /**
     * 删除初始化 - 指定操作人
     * @param operatorId
     */
    public void deleteEntity(Long operatorId){
        this.deleteFlag = DeleteFlag.DELETED.getCode();
        this.updateUser = operatorId;
    }

}
