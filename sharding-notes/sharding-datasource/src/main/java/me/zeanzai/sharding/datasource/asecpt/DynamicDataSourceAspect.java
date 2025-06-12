package me.zeanzai.sharding.datasource.asecpt;

import lombok.extern.slf4j.Slf4j;
import me.zeanzai.sharding.datasource.annotation.ReadOnly;
import me.zeanzai.sharding.datasource.context.config.DataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author shawnwang
 */
@Slf4j
@Aspect
@Component
public class DynamicDataSourceAspect implements Ordered {

    /**
     * 在service层方法获取datasource对象之前，在切面中指定当前线程数据源slave
     */
    @Before(value = "execution(* *(..)) && @annotation(readOnly)")
    public void before(JoinPoint point, ReadOnly readOnly) {
        log.info(point.getSignature().getName() + "走从库");
        DataSourceContextHolder.setDataSourceType(DataSourceContextHolder.SLAVE);
    }

    @After(value = "execution(* *(..)) && @annotation(readOnly)")
    public void restoreDataSource(JoinPoint point, ReadOnly readOnly) {
        log.info(point.getSignature().getName() + "清除数据源");
        //方法执行完后清除数据源。
        DataSourceContextHolder.removeDataSourceType();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
