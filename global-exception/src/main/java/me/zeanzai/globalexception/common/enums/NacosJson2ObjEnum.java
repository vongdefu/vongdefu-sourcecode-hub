package me.zeanzai.globalexception.common.enums;

import java.util.Map;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/22
 */
public enum NacosJson2ObjEnum {

    MESSAGE_ZH_CN("message_zh_cn", "业务异常码表（简中）", Map.class),
    MESSAGE_EN_US("message_en_us", "业务异常码表（英文）", Map.class)
    ;

    private String dataId;
    private String desc;
    private Class clz;

    NacosJson2ObjEnum(String dataId, String desc, Class clz) {
        this.dataId = dataId;
        this.desc=desc;
        this.clz =clz;
    }

    public String getDataId(){
        return dataId;
    }

    public String getDesc(){
        return desc;
    }

    public Class getClz(){
        return clz;
    }
}
