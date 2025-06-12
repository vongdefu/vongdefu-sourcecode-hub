package me.zeanzai.seatatccstorage.service;

import me.zeanzai.seatatcccommon.utils.ResponseResult;

/**
 * @author shawnwang
 */
public interface StorageService {
    ResponseResult<Void> deduct(Long productId, Long num);
    ResponseResult<Void> frozen(Long productId, Long num );
    ResponseResult<Void> cleanFrozen(Long productId,Long num);
    ResponseResult<Void> rollbackFrozen(Long productId,Long num);


}
