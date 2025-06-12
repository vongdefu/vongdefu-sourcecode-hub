package me.zeanzai.seatatccorder.feign;

import me.zeanzai.seatatcccommon.utils.ResponseResult;
import me.zeanzai.seatatcccommon.model.Storage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author shawnwang
 */
@FeignClient(name = "seata-tcc-storage", contextId = "storage")
public interface StorageFeignClient {
    @PostMapping("/storage/getByProductId")
    ResponseResult<Storage> getByProductId(@RequestParam("productId") Long productId);
    @PostMapping("/storage/deduct")
    ResponseResult<Void> deduct(@RequestParam("productId") Long productId, @RequestParam("num") Long num);

    @PostMapping("/storage/frozen")
    ResponseResult<Void> frozen(@RequestParam("productId") Long productId, @RequestParam("num") Long num);

    @PostMapping("/storage/cleanFrozen")
    ResponseResult<Void> cleanFrozen(@RequestParam("productId") Long productId, @RequestParam("num") Long num);

    @PostMapping("/storage/rollbackFrozen")
    ResponseResult<Void> rollbackFrozen(@RequestParam("productId") Long productId, @RequestParam("num") Long num);
}
