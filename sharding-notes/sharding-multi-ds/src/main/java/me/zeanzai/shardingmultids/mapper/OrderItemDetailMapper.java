package me.zeanzai.shardingmultids.mapper;

import me.zeanzai.shardingmultids.domain.dto.OrderItemDetailDto;
import me.zeanzai.shardingmultids.domain.entity.OrderItemDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shawnwang
 */
@Mapper
public interface OrderItemDetailMapper {
    /**
     * 获取订单详情
     * @param orderNos
     * @return
     */
    List<OrderItemDetailDto> getOrderItemDetailList(@Param("orderNo") String orderNo);

    /**
     * 插入订单明细
     * @param record
     * @return
     */
    int insertSelective(OrderItemDetail record);

    /**
     * 修改订单明细
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(OrderItemDetail record);
}
