package me.zeanzai.globalexception.config;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import me.zeanzai.globalexception.common.enums.NacosJson2ObjEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/22
 */
@Slf4j
@Component
public class NacosConfig implements InitializingBean {
    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Value("${spring.cloud.nacos.config.group}")
    private String group;

    private ConfigService configService;
    private Map<String, Object> localCatchMap = new HashMap<>();

    private static ThreadPoolExecutor executor =
            new ThreadPoolExecutor(
                    2,
                    4,
                    1,
                    TimeUnit.SECONDS,
                    new LinkedBlockingDeque<>(100),
                    new ThreadPoolExecutor.CallerRunsPolicy()
            );

    @Override
    public void afterPropertiesSet() throws Exception {
        // 获取 Nacos 服务端 具体配置
        String serverAddr = nacosConfigProperties.getServerAddr();
        String namespace = nacosConfigProperties.getNamespace();

        // 封装 Nacos server 配置参数
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(PropertyKeyConst.NAMESPACE, namespace);
        if (configService == null) {
            configService = NacosFactory.createConfigService(properties);
        }

        NacosJson2ObjEnum[] nacosJson2ObjEnums = NacosJson2ObjEnum.values();
        for (NacosJson2ObjEnum nacosJson2ObjEnum : nacosJson2ObjEnums) {
            String dataId = nacosJson2ObjEnum.getDataId();
            String config = configService.getConfig(dataId, group, 1000);

            jsonToObject(config, nacosJson2ObjEnum);

            configService.addListener(
                    nacosJson2ObjEnum.getDataId(),
                    group,
                    new NacosJsonConfigDataListener(nacosJson2ObjEnum)
            );
        }

    }

    public void jsonToObject(String json, NacosJson2ObjEnum eNacosJsonDefinition) {
        Object data = JSON.parseObject(json, eNacosJsonDefinition.getClz());
        log.info("Get Nacos Config Data, dataId = {} --- data = {}",
                eNacosJsonDefinition.getDataId(), data);
        localCatchMap.put(eNacosJsonDefinition.getDataId(), data);
    }

    public class NacosJsonConfigDataListener implements Listener {
        private NacosJson2ObjEnum nacosJson2ObjEnum;

        private NacosJsonConfigDataListener(NacosJson2ObjEnum nacosJson2ObjEnum) {
            this.nacosJson2ObjEnum = nacosJson2ObjEnum;
        }

        @Override
        public Executor getExecutor() {
            return executor;
        }

        @Override
        public void receiveConfigInfo(String configInfo) {
            jsonToObject(configInfo, nacosJson2ObjEnum);
        }
    }

    public <T> T getNacosJson2Object(NacosJson2ObjEnum nacosJson2ObjEnum, Class<? extends T> clz) {
        Object data = localCatchMap.get(nacosJson2ObjEnum.getDataId());
        if (clz.isInstance(data)) {
            return (T)data;
        }
        throw new IllegalArgumentException("转换类型失败");
    }


}
