package me.zeanzai.shardingmultids.common.enums;

/**
 * @author shawnwang
 */
public enum DeleteFlag {
    NOT_DELETE(0, "未删除"),
    DELETED(1, "已删除"),
    ;
    public final Integer code;
    public final String desc;

    DeleteFlag(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

