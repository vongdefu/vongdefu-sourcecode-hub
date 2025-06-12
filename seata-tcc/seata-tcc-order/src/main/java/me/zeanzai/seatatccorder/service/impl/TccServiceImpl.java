package me.zeanzai.seatatccorder.service.impl;

import me.zeanzai.seatatcccommon.utils.ResponseResult;
import me.zeanzai.seatatccorder.dao.OrderTbRepository;
import me.zeanzai.seatatccorder.dao.TransactionalRecordRepository;
import me.zeanzai.seatatccorder.feign.AccountFeignClient;
import me.zeanzai.seatatccorder.feign.StorageFeignClient;
import me.zeanzai.seatatccorder.model.OrderTb;
import me.zeanzai.seatatccorder.model.TransactionalRecord;
import me.zeanzai.seatatccorder.service.TccService;
import me.zeanzai.seatatccorder.util.IdempotenUtil;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author shawnwang
 *
 * 两大异常：
 * 1. 空回滚： TC还没有执行一阶段锁定资源的操作，就执行二阶段的Cancel的操作。
 *      主要原因： 一阶段锁定资源的操作，由于网络拥堵或抖动问题，导致TC迟迟没有收到锁定资源的操作，但是此时TC已经收到了发起分布式事务的请求，
 *              也就是tc收到分布式事务的请求后，会等着一阶段的请求，如果等了一段时间后，还是没有收到一阶段的请求，那么TC会自动执行二阶段的cancel操作，
 *              此时，二阶段早于一阶段执行，这就是空回滚。
 *      解决方式： 很自然的就会想到，如果一阶段没有执行，当然不需要进行回滚了。
 *      实践过程： 一阶段中执行完成后，做一下记录； 二阶段回滚操作中读取这个标志，如果这个标志存在，那说明不需要进行回滚操作；
 *
 *
 * 2. 悬挂： TC执行完二阶段释放资源的操作后，才收到一阶段的锁定资源的操作请求。
 *      主要原因：
 *
 *
 *
 */
@Service
@Slf4j
public class TccServiceImpl implements TccService {

    @Autowired
    private StorageFeignClient storageFeignClient;

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Autowired
    private OrderTbRepository orderTbRepository;

    @Autowired
    private TransactionalRecordRepository transactionalRecordRepository;

    @Override
    @Transactional
    public boolean tryCreateOrder(BusinessActionContext businessActionContext, String userId, String orderId, Long productId, Long num) {

        log.info("--------开始第一阶段的事务，事务组XID：{}---------",businessActionContext.getXid());

        // 悬挂： 一阶段调用try方法锁定资源时，由于网络拥堵造成超时，TC会认定一阶段try阶段失败，就会自动执行二阶段的cancel方法，后面网络正常后，TC才收到拥堵在网络上的try方法的数据包，
        // 就会造成再也收不到第二阶段的cancel或commit的情况， 换句话说会造成本该执行的二阶段没有执行，有点像把二阶段悬挂起来的样子。
        // 解决方式： 在执行一阶段锁定资源时，要先判断一下二阶段的cancel方法【思考一下： 为什么不是commit方法】是否已经执行完成，执行完成的话，就直接放弃执行try方法就行了
        //          要想判断 二阶段cancel方法是否已经执行 ，可以在cancel方法执行后，设置一个标识，判断时，只需要在 一阶段的try方法 中获取这个标识并进行判断就行

//        TransactionalRecord byXid = transactionalRecordRepository.findByXid(businessActionContext.getXid());
//        if (Objects.nonNull(byXid)) {
//            return true;
//        }

//        Long aLong = IdempotenUtil.get(this.getClass(), businessActionContext.getXid());
//        if (Objects.nonNull(aLong)) {
//            return true;
//        }

        ResponseResult<Void> frozen1 = storageFeignClient.frozen(productId, num);
        if (Objects.isNull(frozen1)) {
            throw new RuntimeException("冻结失败");
        }

        Long cost = storageFeignClient.getByProductId(productId).getData().getPrice() * num;
        ResponseResult<Void> frozen = accountFeignClient.frozen(userId, cost);
        if (Objects.isNull(frozen)) {
            throw new RuntimeException("冻结失败");
        }

        OrderTb orderTb = OrderTb.builder().orderId(orderId)
                .userId(userId)
                .productId(productId)
                .num(num)
                .status(2)
                .build();
        saveOrderTb(orderTb);

        IdempotenUtil.add(this.getClass(), businessActionContext.getXid(), System.currentTimeMillis());

        return true;
    }

    @Override
    @Transactional
    public boolean commit(BusinessActionContext businessActionContext) {
        log.info("--------开始第二阶段的commit事务，事务组XID：{}---------",businessActionContext.getXid());
        Long aLong = IdempotenUtil.get(this.getClass(), businessActionContext.getXid());
        if (Objects.isNull(aLong)) {    // 说明已经执行过try或者执行过cancel，不需要执行commit
            return true;
        }

        long productId = Long.parseLong(businessActionContext.getActionContext("productId").toString());
        long num = Long.parseLong(businessActionContext.getActionContext("num").toString());
        String orderId = businessActionContext.getActionContext("orderId").toString();
        String userId = businessActionContext.getActionContext("userId").toString();

        ResponseResult<Void> voidResponseResult = storageFeignClient.cleanFrozen(productId, num);
        if (Objects.isNull(voidResponseResult)) {
            throw new RuntimeException("提交失败");
        }

        ResponseResult<Void> voidResponseResult1 = accountFeignClient.cleanFrozen(userId, num * storageFeignClient.getByProductId(productId).getData().getPrice());
        if (Objects.isNull(voidResponseResult1)) {
            throw new RuntimeException("提交失败");
        }

        OrderTb orderTb = OrderTb.builder()
                .orderId(orderId)
                .userId(userId)
                .productId(productId)
                .num(num)
                .status(3).build();
        saveOrderTb(orderTb);

        IdempotenUtil.remove(this.getClass(), businessActionContext.getXid());
        return true;
    }

    @Override
    @Transactional
    public boolean rollback(BusinessActionContext businessActionContext) {

        TransactionalRecord record = TransactionalRecord.builder()
                .xid(businessActionContext.getXid())
                //cancel阶段
                .status(3)
                .build();
        transactionalRecordRepository.save(record);
        Long aLong = IdempotenUtil.get(this.getClass(), businessActionContext.getXid());
        if (Objects.isNull(aLong)) {    // 说明还没有执行一阶段
            return true;
        }

        log.info("--------开始第一阶段的事务，事务组XID：{}---------",businessActionContext.getXid());

        long productId = Long.parseLong(businessActionContext.getActionContext("productId").toString());
        long num = Long.parseLong(businessActionContext.getActionContext("num").toString());
        String orderId = businessActionContext.getActionContext("orderId").toString();
        String userId = businessActionContext.getActionContext("userId").toString();

        ResponseResult<Void> voidResponseResult = storageFeignClient.rollbackFrozen(productId, num);
        if (Objects.isNull(voidResponseResult)) {
            throw new RuntimeException("回滚失败");
        }

        ResponseResult<Void> voidResponseResult1 = accountFeignClient.rollbackFrozen(userId, num);
        if (Objects.isNull(voidResponseResult1)) {
            throw new RuntimeException("回滚失败");
        }

        OrderTb orderTb = OrderTb.builder()
                .orderId(orderId)
                .status(5).build();
        orderTbRepository.save(orderTb);

        IdempotenUtil.remove(this.getClass(), businessActionContext.getXid());

        return true;
    }

    private OrderTb saveOrderTb(OrderTb orderTb) {
        OrderTb byOrderId = orderTbRepository.findByOrderId(orderTb.getOrderId());
        if (Objects.isNull(byOrderId)) {
            return orderTbRepository.save(orderTb);
        } else {
            orderTb.setId(byOrderId.getId());
            return orderTbRepository.save(orderTb);
        }
    }


}
