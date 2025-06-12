package me.zeanzai.seatatccaccount.service.impl;

import me.zeanzai.seatatccaccount.dao.AccountRepository;
import me.zeanzai.seatatccaccount.model.Account;
import me.zeanzai.seatatccaccount.service.AccountService;
import me.zeanzai.seatatcccommon.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author shawnwang
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public ResponseResult<Void> deduct(String userId, Long price, Long num) {
        Account account = accountRepository.findByUserId(userId);
        account.setMoney(account.getMoney()-price*num);
        accountRepository.save(account);
        return ResponseResult.success("200","请求成功",null);
    }

    @Override
    public ResponseResult<Void> frozen(String userId, Long money) {
        Account account = accountRepository.findByUserId(userId);
        if (Objects.isNull(account)) {
            return ResponseResult.success("1001", "该用户没有账户信息", null);
        }
        if (account.getMoney() < money) {
            return ResponseResult.success("1002", "账户余额不足", null);
        }
        account.setMoney(account.getMoney() - money);
        account.setFrozen(money);
        accountRepository.save(account);
        return ResponseResult.success("200","请求成功",null);
    }

    @Override
    public ResponseResult<Void> cleanFrozen(String userId, Long money) {
        Account account = accountRepository.findByUserId(userId);
        if (Objects.isNull(account)) {
            return ResponseResult.success("1001", "该用户没有账户信息", null);
        }

        account.setFrozen(account.getFrozen() - money);
        accountRepository.save(account);
        return ResponseResult.success("200","请求成功",null);
    }

    @Override
    public ResponseResult<Void> rollbackFrozen(String userId, Long money) {
        Account account = accountRepository.findByUserId(userId);
        if (Objects.isNull(account)) {
            return ResponseResult.success("1001", "该用户没有账户信息", null);
        }

        account.setMoney(account.getMoney() + money);
        account.setFrozen(account.getFrozen() - money);
        accountRepository.save(account);
        return ResponseResult.success("200","请求成功",null);
    }
}
