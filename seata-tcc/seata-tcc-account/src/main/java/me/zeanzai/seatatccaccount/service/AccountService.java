package me.zeanzai.seatatccaccount.service;

import me.zeanzai.seatatcccommon.utils.ResponseResult;

/**
 * @author shawnwang
 */
public interface AccountService {
    ResponseResult<Void> deduct(String userId, Long price, Long num);

    ResponseResult<Void> frozen(String userId, Long money);
    ResponseResult<Void> cleanFrozen(String userId, Long money);
    ResponseResult<Void> rollbackFrozen(String userId, Long money);
}
