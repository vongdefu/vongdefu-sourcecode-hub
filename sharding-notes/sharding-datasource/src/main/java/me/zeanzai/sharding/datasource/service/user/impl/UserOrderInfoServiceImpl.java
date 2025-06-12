package me.zeanzai.sharding.datasource.service.user.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.zeanzai.sharding.datasource.annotation.ReadOnly;
import me.zeanzai.sharding.datasource.bean.request.order.UserOrderInfoRequestDTO;
import me.zeanzai.sharding.datasource.common.util.RedisUtils;
import me.zeanzai.sharding.datasource.constant.OrderConstant;
import me.zeanzai.sharding.datasource.domain.entity.OrderInfo;
import me.zeanzai.sharding.datasource.domain.entity.OrderItemDetail;
import me.zeanzai.sharding.datasource.domain.query.OrderInfoBaseQuery;
import me.zeanzai.sharding.datasource.domain.query.OrderInfoQuery;
import me.zeanzai.sharding.datasource.domain.query.UserOrderInfoQuery;
import me.zeanzai.sharding.datasource.domain.valid.OrderValidation;
import me.zeanzai.sharding.datasource.domain.vo.OrderInfoVO;
import me.zeanzai.sharding.datasource.domain.vo.OrderItemDetailVO;
import me.zeanzai.sharding.datasource.domain.vo.UserOrderVO;
import me.zeanzai.sharding.datasource.repository.OrderInfoRepository;
import me.zeanzai.sharding.datasource.service.user.UserOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author shawnwang
 */
@Service
@Slf4j
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
    @ReadOnly
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

    /**
     * 根据用户id获得订单列表
     *
     * @param userOrderInfoQuery 入参
     * @return 出参
     */
//    @ReadOnly
//    @Override
//    public Page<UserOrderVO> queryUserOrderInfoList(UserOrderInfoQuery userOrderInfoQuery) {
//        //1.入参校验
//        OrderValidation.checkVerifyOrderQuery(userOrderInfoQuery);
//        //2.转换入参
//        OrderInfoBaseQuery orderInfoBaseQuery = orderConvertor.userQueryToBaseOrder(userOrderInfoQuery);
//        //3.查询已完成的订单
//        if (OrderEnums.FINISH.getCode().equals(userOrderInfoQuery.getOrderStatus())) {
//            //4.组装redisKey
//            String redisKey = userOrderInfoQuery.getUserId() + userOrderInfoQuery.getPageNo().toString() + userOrderInfoQuery.getPageSize().toString();
//            //5.获取redis缓存
//            Object redisObject = redisUtils.get(redisKey);
//            //6.redis为空则从数据库中查询
//            if (Objects.isNull(redisObject)) {
//                //7.查询订单列表
//                Page<OrderInfoVO> orderInfoVOPage = orderInfoRepository.queryOrderInfoList(orderInfoBaseQuery);
//                //8.转换出参
//                Page<UserOrderVO> userOrderVOPage = orderConvertor.OrderVoToUserInfoVo(orderInfoVOPage);
//                //9.设置redis缓存，过期时间为一小时
//                redisUtils.set(redisKey, userOrderVOPage, 3600L, TimeUnit.SECONDS);
//                return userOrderVOPage;
//            }
//            log.info("从redis中获取数据，key:" + redisKey);
//            return (Page<UserOrderVO>) redisObject;
//        }
//        return orderConvertor.OrderVoToUserInfoVo(orderInfoRepository.queryOrderInfoList(orderInfoBaseQuery));
//    }

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
