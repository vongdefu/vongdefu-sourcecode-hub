package me.zeanzai.sharding.sql.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zeanzai.sharding.sql.domain.entity.OrderInfo;
import me.zeanzai.sharding.sql.domain.query.OrderInfoQuery;
import me.zeanzai.sharding.sql.domain.vo.OrderInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shawnwang
 */
@Mapper
public interface OrderInfoMapper {

    /**
     * 插入订单
     * @param orderInfo
     * @return
     */
    int insertSelective(OrderInfo orderInfo);

    /**
     * 修改订单状态订单
     * @param orderNo
     * @param status
     */
    void updateOrderStatus(@Param("orderNo") String orderNo, @Param("status")Integer status);

    /**
     * 根据条件分页查询订单列表
     * @param page
     * @param orderInfoQuery
     * @return
     */
    Page<OrderInfoVO> queryOrderInfoList(Page<OrderInfoVO> page, @Param("record") OrderInfoQuery orderInfoQuery);


    /**
     * 查询未支付的
     * @return
     */
    List<OrderInfo> queryNoPayOrderList();

    /**
     * 根据订单号查订单信息
     * @param orderNo
     * @return
     */
    OrderInfo getOrderInfoByNo(String orderNo);

    List<OrderInfo> queryAll();

}
