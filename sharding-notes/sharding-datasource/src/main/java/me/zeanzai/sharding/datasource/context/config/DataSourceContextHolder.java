package me.zeanzai.sharding.datasource.context.config;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shawnwang
 */
@Slf4j
public class DataSourceContextHolder {

    public static final String MASTER = "MASTER";
    public static final String SLAVE = "SLAVE";

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void setDataSourceType(String dataSourceType) {
        if (dataSourceType == null) {
            log.error("dataSource为空");
            throw new NullPointerException();
        }
        log.info("设置dataSource：{}", dataSourceType);
        CONTEXT_HOLDER.set(dataSourceType);
    }

    /**
     * 默认写模式
     *
     * @return
     */
    public static String getDataSourceType() {
        return CONTEXT_HOLDER.get() == null ? MASTER : CONTEXT_HOLDER.get();
    }

    public static void removeDataSourceType() {
        CONTEXT_HOLDER.remove();
    }
}
