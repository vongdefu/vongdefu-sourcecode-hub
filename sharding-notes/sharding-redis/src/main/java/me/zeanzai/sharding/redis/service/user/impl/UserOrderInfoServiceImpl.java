package me.zeanzai.sharding.redis.service.user.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.zeanzai.sharding.redis.bean.request.order.UserOrderInfoRequestDTO;
import me.zeanzai.sharding.redis.common.util.RedisUtils;
import me.zeanzai.sharding.redis.constant.OrderConstant;
import me.zeanzai.sharding.redis.domain.entity.OrderInfo;
import me.zeanzai.sharding.redis.domain.entity.OrderItemDetail;
import me.zeanzai.sharding.redis.domain.query.OrderInfoQuery;
import me.zeanzai.sharding.redis.domain.valid.OrderValidation;
import me.zeanzai.sharding.redis.domain.vo.OrderInfoVO;
import me.zeanzai.sharding.redis.domain.vo.OrderItemDetailVO;
import me.zeanzai.sharding.redis.repository.OrderInfoRepository;
import me.zeanzai.sharding.redis.service.user.UserOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author shawnwang
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "orderInfo") // 添加缓存配置
public class UserOrderInfoServiceImpl implements UserOrderInfoService {

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private RedisUtils redisUtils;

    private final static Long FINISH = 50L;

    @Override
    public void generateOrder(UserOrderInfoRequestDTO userOrderInfoRequestDTO) {
        //添加订单信息
        OrderInfo orderInfo = userOrderInfoRequestDTO.getOrderInfo();
        //添加订单详情
        List<OrderItemDetail> orderItemDetailList = userOrderInfoRequestDTO.getOrderItemDetailList();
        orderInfoRepository.generateOrderInfo(orderInfo,orderItemDetailList);
    }

//    @Override
//    public Page<OrderInfoVO> queryOrderInfoList(OrderInfoQuery orderInfoQuery) {
//        OrderValidation.checkVerifyOrderQuery(orderInfoQuery);
//        Page<OrderInfoVO> page = new Page<OrderInfoVO>();
//        page.setCurrent(orderInfoQuery.getCurrent());
//        page.setSize(orderInfoQuery.getSize());
//        return orderInfoRepository.queryOrderInfoList(page, orderInfoQuery);
//    }


    @Override
    public Page<OrderInfoVO> queryOrderInfoList(OrderInfoQuery orderInfoQuery) {
        OrderValidation.checkVerifyOrderQuery(orderInfoQuery);
        Page<OrderInfoVO> page = new Page<OrderInfoVO>();
        page.setCurrent(orderInfoQuery.getCurrent());
        page.setSize(orderInfoQuery.getSize());
        //查询已完成的订单
        if (FINISH.equals(orderInfoQuery.getOrderStatus())) {
            //组装redisKey
            String redisKey = orderInfoQuery.getUserId() + orderInfoQuery.getCurrent().toString() + orderInfoQuery.getSize().toString();
            //获取redis缓存
            Object redisObject = redisUtils.get(redisKey);
            //redis为空则从数据库中查询
            if (Objects.isNull(redisObject)) {
                Page<OrderInfoVO> userOrderInfoVOPage = orderInfoRepository.queryOrderInfoList(page, orderInfoQuery);
                //设置redis缓存，过期时间为一小时
                redisUtils.set(redisKey, userOrderInfoVOPage, 3600L, TimeUnit.SECONDS);
                return userOrderInfoVOPage;
            }
            log.info("从redis中获取数据, key: {}", redisKey);
            return (Page<OrderInfoVO>) redisObject;
        }
        return orderInfoRepository.queryOrderInfoList(page, orderInfoQuery);
    }

    @Override
    @Cacheable  // 缓存结果
    public Page<OrderInfoVO> queryOrderInfoList2(OrderInfoQuery orderInfoQuery) {
        long start = System.currentTimeMillis();
        OrderValidation.checkVerifyOrderQuery(orderInfoQuery);
        Page<OrderInfoVO> page = new Page<OrderInfoVO>();
        page.setCurrent(orderInfoQuery.getCurrent());
        page.setSize(orderInfoQuery.getSize());

        Page<OrderInfoVO> orderInfoVOPage = orderInfoRepository.queryOrderInfoList(page, orderInfoQuery);
        log.info("查询时间为： {} ms", System.currentTimeMillis()-start);
        return orderInfoVOPage;
    }

    @Override
    public OrderItemDetailVO getOrderItemDetail(String orderNo) {
        return orderInfoRepository.getOrderItemDetail(orderNo);
    }



    @Override
    public void updateOrderStatus(String orderNo,Integer status) {
        orderInfoRepository.updateOrderStatus(orderNo,status);
    }

    @Override
    public void autoCancelOrder() {
        List<OrderInfo> orderInfos = orderInfoRepository.queryNoPayOrderList();
        for (OrderInfo orderInfo : orderInfos) {
            orderInfoRepository.updateOrderStatus(orderInfo.getOrderNo(), OrderConstant.OrderBaseStatus.CANCEL_ORDER);
        }

    }


}
