package me.zeanzai.seatatccaccount.controller;

import me.zeanzai.seatatccaccount.dao.AccountRepository;
import me.zeanzai.seatatccaccount.model.Account;
import me.zeanzai.seatatccaccount.service.AccountService;
import me.zeanzai.seatatcccommon.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shawnwang
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @PostMapping("/getByUserId")
    public ResponseResult<Account> getByProductId(String userId){
        return ResponseResult.success("200","请求成功",accountRepository.findByUserId(userId));
    }

    @PostMapping("/deduct")
    public ResponseResult<Void> deduct(String userId, Long price, Long num) {
        return accountService.deduct(userId, price, num);
    }

    /**
     * 冻结账户上的钱
     * @param userId
     * @param money
     * @return
     */
    @PostMapping("/frozen")
    public ResponseResult<Void> frozen(String userId, Long money) {
        return accountService.frozen(userId, money);
    }

    @PostMapping("/cleanFrozen")
    public ResponseResult<Void> cleanFrozen(String userId, Long money){
        return accountService.cleanFrozen(userId,money);
    }

    @PostMapping("/rollbackFrozen")
    public ResponseResult<Void> rollbackFrozen(String userId, Long money){
        return accountService.rollbackFrozen(userId,money);
    }


}
