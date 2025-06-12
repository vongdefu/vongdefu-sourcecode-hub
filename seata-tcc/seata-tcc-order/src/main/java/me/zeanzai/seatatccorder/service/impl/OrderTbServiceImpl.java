package me.zeanzai.seatatccorder.service.impl;

import io.seata.spring.annotation.GlobalTransactional;
import me.zeanzai.seatatcccommon.model.Storage;
import me.zeanzai.seatatcccommon.utils.ResponseResult;
import me.zeanzai.seatatccorder.dao.OrderTbRepository;
import me.zeanzai.seatatccorder.feign.AccountFeignClient;
import me.zeanzai.seatatccorder.feign.StorageFeignClient;
import me.zeanzai.seatatccorder.model.OrderTb;
import me.zeanzai.seatatccorder.service.OrderTbService;
import me.zeanzai.seatatccorder.service.TccService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author shawnwang
 */
@Service
public class OrderTbServiceImpl implements OrderTbService {

    @Resource
    private StorageFeignClient storageFeignClient;

    @Resource
    private AccountFeignClient accountFeignClient;

    @Resource
    private OrderTbRepository orderTbRepository;

    @Resource
    private TccService tccService;

    /**
     * 1. 扣除库存
     * 2. 扣除账户金额
     * 3. 生成订单
     * @param productId
     * @param userId
     * @param num
     */
    @Transactional
    @Override
    public OrderTb create(Long productId, String userId, Long num) {
        // 1. 扣除库存
        storageFeignClient.deduct(productId, num);

        ResponseResult<Storage> responseResult = storageFeignClient.getByProductId(productId);
        Storage storage = responseResult.getData();

        // 2. 扣除金额
        accountFeignClient.deduct(userId, storage.getPrice(), num);

        // 3. 生成订单
        OrderTb ordertb = OrderTb.builder()
                .orderId("1")
                .userId(userId)
                .productId(productId)
                .num(num)
                .status(1)
                .build();
        OrderTb save = orderTbRepository.save(ordertb);
        return save;
    }

    @Override
    @GlobalTransactional
    public Void createTcc(String userId, Long productId, Long num) {
        tccService.tryCreateOrder(null, userId, UUID.randomUUID().toString(), productId, num);
        return null;
    }
}
