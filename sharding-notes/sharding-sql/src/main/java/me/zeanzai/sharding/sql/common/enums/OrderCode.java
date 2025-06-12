package me.zeanzai.sharding.sql.common.enums;

/**
 * @author shawnwang
 */
public enum OrderCode {
    ADD_ORDER_SUCCESS("100","订单添加成功"),
    ADD_ORDER_ERROR("200", "订单添加失败"),
    UPDATE_ORDER_SUCCESS("300","订单修改成功"),
    UPDATE_ORDER_ERROR("400", "订单修改失败"),
    DELETE_ORDER_SUCCESS("500","订单修改成功"),
    DELETE_ORDER_ERROR("600", "订单修改失败"),
    QUERY_ORDER_ERROR("700", "查询订单异常"),
    ;
    public final String code;
    public final String desc;

    OrderCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
