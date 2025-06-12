package me.zeanzai.nacosasconfiger.config;


import java.util.List;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/21
 */
public enum ENacosJsonDefinition {
    MYGIRLS("mygirls", List.class,"我的女朋友");

    private String dataId;
    private Class cls; // 需要转换成的对象
    private String desc;

    ENacosJsonDefinition(String dataId, Class cls, String desc) {
        this.dataId = dataId;
        this.cls = cls;
        this.desc = desc;
    }

    public String getDataId() {
        return dataId;
    }

    public Class getCls() {
        return cls;
    }

    public String getDesc() {
        return desc;
    }



}
