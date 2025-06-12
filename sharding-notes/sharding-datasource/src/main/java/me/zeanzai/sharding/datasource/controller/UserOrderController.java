package me.zeanzai.sharding.datasource.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.zeanzai.sharding.datasource.bean.request.order.UserOrderInfoRequestDTO;
import me.zeanzai.sharding.datasource.bean.web.DataResponse;
import me.zeanzai.sharding.datasource.bean.web.OperationResponse;
import me.zeanzai.sharding.datasource.bean.web.PageResponse;
import me.zeanzai.sharding.datasource.common.enums.OrderCode;
import me.zeanzai.sharding.datasource.domain.query.OrderInfoQuery;
import me.zeanzai.sharding.datasource.domain.query.UserOrderInfoQuery;
import me.zeanzai.sharding.datasource.domain.vo.OrderInfoVO;
import me.zeanzai.sharding.datasource.domain.vo.OrderItemDetailVO;
import me.zeanzai.sharding.datasource.domain.vo.UserOrderVO;
import me.zeanzai.sharding.datasource.exception.BaseException;
import me.zeanzai.sharding.datasource.service.user.UserOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author shawnwang
 */
@Slf4j
@RestController
@RequestMapping(value = "/user/order")
public class UserOrderController {

    @Autowired
    private UserOrderInfoService userOrderInfoService;

    /**
     * 创建订单
     * @param userOrderInfoRequestDTO
     * @return
     */
    @PostMapping("/generateOrder")
    public OperationResponse generateOrder(@RequestBody UserOrderInfoRequestDTO userOrderInfoRequestDTO){
        try{
            //开始计时
            long bTime = System.currentTimeMillis();
            userOrderInfoService.generateOrder(userOrderInfoRequestDTO);
            //关闭分段计时
            long eTime = System.currentTimeMillis();
            //输出
            log.info("生成用户订单耗时："+(eTime-bTime) + "ms");
            return OperationResponse.success(OrderCode.ADD_ORDER_SUCCESS.getDesc());
        }catch (Exception e){
            log.info(e.getMessage());
            return OperationResponse.error(OrderCode.ADD_ORDER_ERROR.getDesc());
        }
    }

    /**
     * 查询订单列表
     * @param orderInfoQuery
     * @return
     */
    @GetMapping("/queryOrderInfoList")
    public PageResponse queryOrderInfoList(@RequestBody OrderInfoQuery orderInfoQuery){
        //开始计时
        long bTime = System.currentTimeMillis();
        try{
            Page<OrderInfoVO> orderInfoPage = userOrderInfoService.queryOrderInfoList(orderInfoQuery);
            //关闭分段计时
            long eTime = System.currentTimeMillis();
            //输出
            log.info("查询用户订单耗时："+(eTime-bTime) + "ms");
            return PageResponse.success(orderInfoPage);
        }catch (Exception e){
            return  PageResponse.error(e.getMessage());
        }


    }

//    @GetMapping("/queryUserOrderInfoList")
//    public PageResponse queryUserOrderInfoList(@RequestBody UserOrderInfoQuery userOrderInfoQuery) {
//        try {
//            long startTime = System.currentTimeMillis();
//            Page<UserOrderVO> userOrderVOPage = userOrderInfoService.queryUserOrderInfoList(userOrderInfoQuery);
//            long endTime = System.currentTimeMillis();
//            log.info("查询用户订单耗时：" + (endTime - startTime));
//            return PageResponse.success(userOrderVOPage);
//        } catch (BaseException e) {
//            return PageResponse.error(e.getMessage());
//        } catch (Exception e) {
//            log.error("queryOrderInfoList error: {}", e.getMessage(), e);
//            return PageResponse.error(e.getMessage());
//        }
//
//
//    }

    /**
     * 查询订单明细
     * @param orderNo
     * @return
     */
    @GetMapping("/getOrderItemDetail")
    public DataResponse getOrderItemDetail(@RequestParam("orderNo")String orderNo){
        try{
            //开始计时
            long bTime = System.currentTimeMillis();
            OrderItemDetailVO orderItemDetailVOList = userOrderInfoService.getOrderItemDetail(orderNo);
            //关闭分段计时
            long eTime = System.currentTimeMillis();
            //输出
            log.info("查询用户订单明细耗时："+(eTime-bTime) + "ms");
            return DataResponse.success(orderItemDetailVOList);
        }catch (Exception e){
            return DataResponse.error(OrderCode.QUERY_ORDER_ERROR.getDesc());
        }

    }

    /**
     * 变更订单--取消订单，确认收货。
     * @param orderNo
     * @return
     */
    @PutMapping("/updateOrderStatus")
    public OperationResponse updateOrderStatus(@RequestParam("orderNo")String orderNo, @RequestParam("status")Integer status){
        try{
            //开始计时
            long bTime = System.currentTimeMillis();
            userOrderInfoService.updateOrderStatus(orderNo,status);
            //关闭分段计时
            long eTime = System.currentTimeMillis();
            //输出
            log.info("取消用户订单耗时："+(eTime-bTime) + "ms");
            return OperationResponse.success(OrderCode.DELETE_ORDER_SUCCESS.desc);
        }catch (Exception e){
            log.info(e.getMessage());
            return OperationResponse.error(OrderCode.DELETE_ORDER_ERROR.getDesc());
        }
    }

}

