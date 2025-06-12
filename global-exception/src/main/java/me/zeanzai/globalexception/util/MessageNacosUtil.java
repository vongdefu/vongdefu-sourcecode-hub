package me.zeanzai.globalexception.util;

import cn.hutool.core.text.StrFormatter;
import me.zeanzai.globalexception.common.enums.NacosJson2ObjEnum;
import me.zeanzai.globalexception.config.NacosConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/22
 */
@Component
public class MessageNacosUtil {

    @Autowired
    private static NacosConfig nacosConfig;

    @Autowired
    public void setNacosConfig(NacosConfig nacosConfig) {
        MessageNacosUtil.nacosConfig = nacosConfig;
    }


    public static String getExcptMsg(String excptCode) {
        return StrFormatter.format(getMessageMap().get(excptCode), null);
    }

    public static String getExcptMsg(String excptCode, String... args) {
        return StrFormatter.format(getMessageMap().get(excptCode), args);
    }

    public static Map<String, String> getMessageMap(){
        Map<String, String> messageMap = null;
        String language = LocaleContextHolder.getLocale().getLanguage();
        if ("".equals(language) || "zh".equals(language)) {
            messageMap = nacosConfig.getNacosJson2Object(NacosJson2ObjEnum.MESSAGE_ZH_CN, Map.class);
        } else {
            messageMap = nacosConfig.getNacosJson2Object(NacosJson2ObjEnum.MESSAGE_EN_US, Map.class);
        }
        return messageMap;
    }
}
