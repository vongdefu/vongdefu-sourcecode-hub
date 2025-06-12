package me.zeanzai.seataatstorage.service.impl;

import me.zeanzai.seataatstorage.dao.StorageDao;
import me.zeanzai.seataatstorage.entity.Storage;
import me.zeanzai.seataatstorage.service.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * (Storage)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 12:29:23
 */
@Service("storageService")
public class StorageServiceImpl implements StorageService {
    @Resource
    private StorageDao storageDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Storage queryById(Long id) {
        return this.storageDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param storage     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Storage> queryByPage(Storage storage, PageRequest pageRequest) {
        long total = this.storageDao.count(storage);
        return new PageImpl<>(this.storageDao.queryAllByLimit(storage, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param storage 实例对象
     * @return 实例对象
     */
    @Override
    public Storage insert(Storage storage) {
        this.storageDao.insert(storage);
        return storage;
    }

    /**
     * 修改数据
     *
     * @param storage 实例对象
     * @return 实例对象
     */
    @Override
    public Storage update(Storage storage) {
        this.storageDao.update(storage);
        return this.queryById(storage.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.storageDao.deleteById(id) > 0;
    }

    /**
     * 扣减库存业务方法
     * 使用@Transactional开启一个本地事务，和正常流程并无差别
     * @param id 商品Id
     * @param num 扣减的数量
     */
    @Transactional
    @Override
    public boolean deduct(Long id, Long num) {
        //todo 模拟扣减库存，具体业务逻辑自己完善
        Storage storage = this.storageDao.queryById(id);
        if (Objects.isNull(storage))
            throw new RuntimeException();

        storage.setNum(storage.getNum()-num);

        return this.storageDao.update(storage) > 0;
    }

    @Override
    public Storage getStorageByProductId(Long productId) {
        return null;
    }


}
