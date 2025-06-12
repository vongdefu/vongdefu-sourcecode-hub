package me.zeanzai.sharding.redis.domain.valid;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import me.zeanzai.sharding.redis.domain.query.OrderInfoQuery;
import me.zeanzai.sharding.redis.exception.BaseException;

/**
 * @author shawnwang
 */
public class OrderValidation {

    /**
     * 检查校验订单查询参数
     * @param orderInfoQuery
     */
    public static void checkVerifyOrderQuery(OrderInfoQuery orderInfoQuery) {
        if (ObjectUtils.isEmpty(orderInfoQuery)) {
            throw new BaseException("入参对象不能为空");
        }

        if (ObjectUtils.isEmpty(orderInfoQuery.getSize())) {
            throw new BaseException("条数不能为空");
        }

        if (ObjectUtils.isEmpty(orderInfoQuery.getCurrent())) {
            throw new BaseException("当前页不能为空");
        }

        if (ObjectUtils.isEmpty(orderInfoQuery.getUserId())) {
            throw new BaseException("用户id不能为空");
        }
    }

}

