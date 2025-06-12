package me.zeanzai.seatatccstorage.service.impl;

import me.zeanzai.seatatccstorage.dao.StorageRepository;
import me.zeanzai.seatatccstorage.model.Storage;
import me.zeanzai.seatatcccommon.utils.ResponseResult;
import me.zeanzai.seatatccstorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author shawnwang
 */
@Service
public class StorageServiceImpl implements StorageService {
    @Autowired
    private StorageRepository storageRepository;

    @Override
    public ResponseResult<Void> deduct(Long productId, Long num) {
        Storage storage = storageRepository.findByProductId(productId);
        storage.setNum(storage.getNum()-num);
        storageRepository.save(storage);
        return ResponseResult.success("200","请求成功",null);
    }

    //    @Transactional
    @Override
    public ResponseResult<Void> frozen(Long productId, Long num) {
        Storage storage = storageRepository.findByProductId(productId);
        if (Objects.isNull(storage))
            return ResponseResult.success("1001","该商品无库存信息",null);
        if (storage.getNum()<num)
            return ResponseResult.success("1002","该商品库存不足",null);
        storage.setNum(storage.getNum()-num);
        storage.setFrozen(num);
        storageRepository.save(storage);
        return ResponseResult.success("200","请求成功",null);
    }

    @Override
    public ResponseResult<Void> cleanFrozen(Long productId, Long num) {
        Storage storage = storageRepository.findByProductId(productId);
        if (Objects.isNull(storage))
            return ResponseResult.success("1001","该商品无库存信息",null);
        //释放库存
        storage.setFrozen(storage.getFrozen()-num);
        storageRepository.save(storage);
        return ResponseResult.success("200","请求成功",null);
    }

    @Override
    public ResponseResult<Void> rollbackFrozen(Long productId, Long num) {
        Storage storage = storageRepository.findByProductId(productId);
        if (Objects.isNull(storage))
            return ResponseResult.success("1001","该商品无库存信息",null);
        storage.setNum(storage.getNum()+num);
        storage.setFrozen(storage.getFrozen()-num);
        storageRepository.save(storage);
        return ResponseResult.success("200","请求成功",null);
    }
}
