package me.zeanzai.seatatccorder.feign;

import me.zeanzai.seatatcccommon.utils.ResponseResult;
import me.zeanzai.seatatcccommon.model.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author shawnwang
 */
@FeignClient(name = "seata-tcc-account", contextId = "account")
public interface AccountFeignClient {

    @PostMapping("/account/getByUserId")
    public ResponseResult<Account> getByUserId(@RequestParam("userId") String userId);

    @PostMapping("/account/deduct")
    ResponseResult<Void> deduct(@RequestParam("userId") String userId, @RequestParam("price") Long price, @RequestParam("num") Long num);

    @PostMapping("/account/frozen")
    ResponseResult<Void> frozen(@RequestParam("userId") String userId, @RequestParam("money") Long money);

    @PostMapping("/account/cleanFrozen")
    ResponseResult<Void> cleanFrozen(@RequestParam("userId") String userId, @RequestParam("money") Long money);

    @PostMapping("/account/rollbackFrozen")
    ResponseResult<Void> rollbackFrozen(@RequestParam("userId") String userId, @RequestParam("money") Long money);
}
