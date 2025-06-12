package me.zeanzai.nacosasconfiger.config;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.extern.slf4j.Slf4j;
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
 * @date 2023/4/21
 */
@Component
@Slf4j
public class NacosConfigLocalCatch implements InitializingBean {

    @Value("${spring.cloud.nacos.config.group}")
    private String group;

    // 注入 Nacos Config server 相关 springboot 配置
    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    // 根据 配置量 大小 ，初始化容量
    // key：dataId
    // value：具体配置数据实体对象
    private Map<String, Object> localCatchMap = new HashMap<>(16);

    private ConfigService configService;

    // 根据枚举 及 类型 获取 配置实体
    public <T> T getLocalCatchConfig(ENacosJsonDefinition eNacosJsonDefinition, Class<? extends T> cls) {
        Object data = this.localCatchMap.get(eNacosJsonDefinition.getDataId());
        // 判断data这个对象能不能被转化为cls这个类
        boolean instance = cls.isInstance(data);
        if(instance) {
            return (T) data;
        }
        throw new IllegalArgumentException("类型转换错误");
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        // 获取 所有 跟 Nacos 具体配置 映射的枚举实例
        ENacosJsonDefinition[] nacosJsonDefinitions = ENacosJsonDefinition.values();

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

        // 遍历 获取 配置 ，并且针对各配置 设置 动态监听对象
        for (ENacosJsonDefinition definition : nacosJsonDefinitions) {
            String dataId = definition.getDataId();

            // 初始化 获取具体配置
            String config = configService.getConfig(dataId, group, 1000);
            // 转换json保存到localCatchMap
            this.jsonToObject(config, definition);
            // 创建监听对象
            Listener listener = new NacosJsonConfigDataListener(definition);
            // 配置 具体配置项的监听
            configService.addListener(definition.getDataId(), group, listener);
        }
    }

    // 将获取到的json配置装换为具体对象存储到localCatchMap
    public void jsonToObject(String json, ENacosJsonDefinition eNacosJsonDefinition) {
        Object data = JSON.parseObject(json, eNacosJsonDefinition.getCls());
        log.info("Get Nacos Config Data, dataId = {} --- data = {}",
                eNacosJsonDefinition.getDataId(), data);
        this.localCatchMap.put(eNacosJsonDefinition.getDataId(), data);
    }

    // 配置用于异步监听的线程池
    private static ThreadPoolExecutor executor =
            new ThreadPoolExecutor(
                    2,
                    4,
                    1,
                    TimeUnit.SECONDS,
                    new LinkedBlockingDeque<>(100),
                    new ThreadPoolExecutor.CallerRunsPolicy()
            );

    // 具体监听类 动态监听Nacos配置
    public class NacosJsonConfigDataListener implements Listener {
        private ENacosJsonDefinition eNacosJsonDefinition;
        private NacosJsonConfigDataListener(ENacosJsonDefinition eNacosJsonDefinition) {
            this.eNacosJsonDefinition = eNacosJsonDefinition;
        }

        // 配置线程执行器 用于异步监听
        @Override
        public Executor getExecutor() {
            return executor;
        }

        // 动态监听到变动后的处理逻辑
        @Override
        public void receiveConfigInfo(String jsonStr) {
            jsonToObject(jsonStr, eNacosJsonDefinition);
        }
    }

}
