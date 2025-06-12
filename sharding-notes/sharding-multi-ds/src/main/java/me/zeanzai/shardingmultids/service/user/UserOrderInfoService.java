package me.zeanzai.shardingmultids.service.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zeanzai.shardingmultids.bean.request.order.UserOrderInfoRequestDTO;
import me.zeanzai.shardingmultids.domain.query.OrderInfoQuery;
import me.zeanzai.shardingmultids.domain.vo.OrderInfoVO;
import me.zeanzai.shardingmultids.domain.vo.OrderItemDetailVO;

/**
 * @author shawnwang
 */
public interface UserOrderInfoService {
    /**
     * 用户提交订单
     *
     * @param userOrderInfoRequestDTO
     */
    void generateOrder(UserOrderInfoRequestDTO userOrderInfoRequestDTO);

    /**
     * 根据查询条件获得订单列表
     *
     * @param orderInfoQuery
     * @return
     */
    Page<OrderInfoVO> queryOrderInfoList(OrderInfoQuery orderInfoQuery);

    /**
     * 根据查询条件获得订单列表
     *
     * @param userOrderInfoQuery
     * @return
     */
//    Page<UserOrderVO> queryUserOrderInfoList(UserOrderInfoQuery userOrderInfoQuery);

    /**
     * 根据订单号查询订单详情
     *
     * @param orderNo
     * @return
     */
    OrderItemDetailVO getOrderItemDetail(String orderNo);

    /**
     * 用户取消订单
     *
     * @param orderNo
     * @param status
     */
    void updateOrderStatus(String orderNo, Integer status);


    /**
     * 自动取消未支付订单
     */
    void autoCancelOrder();

}
