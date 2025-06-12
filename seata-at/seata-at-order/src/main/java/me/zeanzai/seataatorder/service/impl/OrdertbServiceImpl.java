package me.zeanzai.seataatorder.service.impl;

import me.zeanzai.seataatorder.dao.OrdertbDao;
import me.zeanzai.seataatorder.entity.Ordertb;
import me.zeanzai.seataatorder.entity.Storage;
import me.zeanzai.seataatorder.feignservice.AccountFeignClient;
import me.zeanzai.seataatorder.feignservice.StorageFeignClient;
import me.zeanzai.seataatorder.service.OrdertbService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (Ordertb)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 14:53:29
 */
@Service("ordertbService")
public class OrdertbServiceImpl implements OrdertbService {
    @Resource
    private OrdertbDao ordertbDao;

    @Autowired
    private StorageFeignClient storageFeignClient;

    @Autowired
    private AccountFeignClient accountFeignClient;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Ordertb queryById(Long id) {
        return this.ordertbDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param ordertb     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Ordertb> queryByPage(Ordertb ordertb, PageRequest pageRequest) {
        long total = this.ordertbDao.count(ordertb);
        return new PageImpl<>(this.ordertbDao.queryAllByLimit(ordertb, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param ordertb 实例对象
     * @return 实例对象
     */
    @Override
    public Ordertb insert(Ordertb ordertb) {
        this.ordertbDao.insert(ordertb);
        return ordertb;
    }

    /**
     * 修改数据
     *
     * @param ordertb 实例对象
     * @return 实例对象
     */
    @Override
    public Ordertb update(Ordertb ordertb) {
        this.ordertbDao.update(ordertb);
        return this.queryById(ordertb.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.ordertbDao.deleteById(id) > 0;
    }

    @Override
    @GlobalTransactional
    public boolean createOrder(String userId, Long productId, Long num) {
        // 1. 扣减库存
        storageFeignClient.deduct(productId, num);

        // 2. 扣减余额
        ResponseEntity<Storage> storageResponseEntity = storageFeignClient.queryById(productId);
        Storage body = storageResponseEntity.getBody();
        accountFeignClient.deduct(userId, body.getPrice() * num);
        // 3. 创建订单
        Ordertb ordertb = new Ordertb();
        ordertb.setNum(num);
        ordertb.setUserId(userId);
        ordertb.setProductId(productId);
        ordertb.setStatus(2);
        return this.ordertbDao.insert(ordertb)>0;
    }


}
