package me.zeanzai.sharding.sql.service.user.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zeanzai.sharding.sql.bean.request.order.UserOrderInfoRequestDTO;
import me.zeanzai.sharding.sql.constant.OrderConstant;
import me.zeanzai.sharding.sql.domain.entity.OrderInfo;
import me.zeanzai.sharding.sql.domain.entity.OrderItemDetail;
import me.zeanzai.sharding.sql.domain.query.OrderInfoQuery;
import me.zeanzai.sharding.sql.domain.valid.OrderValidation;
import me.zeanzai.sharding.sql.domain.vo.OrderInfoVO;
import me.zeanzai.sharding.sql.domain.vo.OrderItemDetailVO;
import me.zeanzai.sharding.sql.repository.OrderInfoRepository;
import me.zeanzai.sharding.sql.service.user.UserOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shawnwang
 */
@Service
public class UserOrderInfoServiceImpl implements UserOrderInfoService {

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Override
    public void generateOrder(UserOrderInfoRequestDTO userOrderInfoRequestDTO) {
        //添加订单信息
        OrderInfo orderInfo = userOrderInfoRequestDTO.getOrderInfo();
        //添加订单详情
        List<OrderItemDetail> orderItemDetailList = userOrderInfoRequestDTO.getOrderItemDetailList();
        orderInfoRepository.generateOrderInfo(orderInfo,orderItemDetailList);
    }

    @Override
    public Page<OrderInfoVO> queryOrderInfoList(OrderInfoQuery orderInfoQuery) {
        OrderValidation.checkVerifyOrderQuery(orderInfoQuery);
        Page<OrderInfoVO> page = new Page<OrderInfoVO>();
        page.setCurrent(orderInfoQuery.getCurrent());
        page.setSize(orderInfoQuery.getSize());
        return orderInfoRepository.queryOrderInfoList(page, orderInfoQuery);
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

    @Override
    public List<OrderInfo> getAll() {
        return orderInfoRepository.queryAll();
    }
}
