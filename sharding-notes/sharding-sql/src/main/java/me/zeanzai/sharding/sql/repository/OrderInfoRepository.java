package me.zeanzai.sharding.sql.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zeanzai.sharding.sql.context.utils.GenerateOrderNoUtils;
import me.zeanzai.sharding.sql.domain.dto.OrderItemDetailDto;
import me.zeanzai.sharding.sql.domain.entity.OrderInfo;
import me.zeanzai.sharding.sql.domain.entity.OrderItemDetail;
import me.zeanzai.sharding.sql.domain.query.OrderInfoQuery;
import me.zeanzai.sharding.sql.domain.vo.OrderInfoVO;
import me.zeanzai.sharding.sql.domain.vo.OrderItemDetailVO;
import me.zeanzai.sharding.sql.mapper.OrderInfoMapper;
import me.zeanzai.sharding.sql.mapper.OrderItemDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shawnwang
 */
@Repository
public class OrderInfoRepository {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderItemDetailMapper orderItemDetailMapper;

    /**
     * 查询用户订单列表
     * @param page
     * @param userOrderInfoQuery
     * @return
     */
    public Page<OrderInfoVO> queryOrderInfoList(Page page,OrderInfoQuery userOrderInfoQuery) {
        // 查询该用户订单信息
        Page<OrderInfoVO> orderInfoVOPage = orderInfoMapper.queryOrderInfoList(page, userOrderInfoQuery);
        return orderInfoVOPage;
    }

    /**
     * 根据订单号查询订单明细
     * @param orderNo
     * @return
     */
    public OrderItemDetailVO getOrderItemDetail(String orderNo) {
        OrderItemDetailVO orderItemDetailVO = new OrderItemDetailVO();
        //查询订单基础信息
        OrderInfo orderInfo = orderInfoMapper.getOrderInfoByNo(orderNo);
        orderItemDetailVO.setOrderInfo(orderInfo);
        // 根据订单号查询出所有的订单详细信息
        List<OrderItemDetailDto> orderItemDetailList = orderItemDetailMapper.getOrderItemDetailList(orderNo);
        orderItemDetailVO.setOrderItemDetails(orderItemDetailList);
        return orderItemDetailVO;
    }

    /**
     * 插入订单信息
     * @param orderInfo
     */
    @Transactional(rollbackFor = Exception.class)
    public void generateOrderInfo(OrderInfo orderInfo, List<OrderItemDetail> orderItemDetailList) {
        //生成订单号
        String orderCode = GenerateOrderNoUtils.getOrderNo(orderInfo.getUserId());
        orderInfo.setOrderNo(orderCode);
        orderInfoMapper.insertSelective(orderInfo);
        for (OrderItemDetail orderItemDetail : orderItemDetailList) {
            orderItemDetail.setOrderNo(orderCode);
            orderItemDetailMapper.insertSelective(orderItemDetail);
        }
    }

    /**
     * 取消订单
     * @param orderNo
     */
    public void updateOrderStatus(String orderNo, Integer status) {
        orderInfoMapper.updateOrderStatus(orderNo, status);
    }

    /**
     * 根据商户查询订单列表
     * @param page
     * @param orderInfoQuery
     * @return
     */
    public Page<OrderInfoVO> queryOrderInfoByMerchant(Page page, OrderInfoQuery orderInfoQuery){
        return orderInfoMapper.queryOrderInfoList(page, orderInfoQuery);
    }

    public List<OrderInfo> queryNoPayOrderList(){
        return orderInfoMapper.queryNoPayOrderList();
    }

    public List<OrderInfo> queryAll() {
        return orderInfoMapper.queryAll();
    }








}

