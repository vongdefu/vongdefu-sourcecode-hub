package me.zeanzai.seatatccstorage.controller;

import me.zeanzai.seatatccstorage.model.Storage;
import me.zeanzai.seatatcccommon.utils.ResponseResult;
import me.zeanzai.seatatccstorage.dao.StorageRepository;
import me.zeanzai.seatatccstorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shawnwang
 */
@RestController
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private StorageService storageService;

    @PostMapping("/deduct")
    public ResponseResult<Void> deduct(Long productId, Long num) {
        return storageService.deduct(productId, num);
    }

    @PostMapping("/getByProductId")
    public ResponseResult<Storage> getByProductId(Long productId){
        return ResponseResult.success("200","请求成功",storageRepository.findByProductId(productId));

    }

    /**
     * 冻结库存
     */
    @PostMapping("/frozen")
    public ResponseResult<Void> frozen(Long productId, Long num ){
        return storageService.frozen(productId,num);
    }

    /**
     * 释放库存
     */
    @PostMapping("/cleanFrozen")
    public ResponseResult<Void> cleanFrozen(Long productId,Long num){
        return storageService.cleanFrozen(productId,num);
    }

    /**
     * 恢复库存
     */
    @PostMapping("/rollbackFrozen")
    public ResponseResult<Void> rollbackFrozen(Long productId,Long num){
        return storageService.rollbackFrozen(productId,num);
    }


}
