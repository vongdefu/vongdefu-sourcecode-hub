package me.zeanzai.seatatccorder.controller;

import me.zeanzai.seatatcccommon.utils.ResponseResult;
import me.zeanzai.seatatccorder.model.OrderTb;
import me.zeanzai.seatatccorder.service.OrderTbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shawnwang
 */
@RestController
@RequestMapping("/order")
public class OrderTbController {

    @Autowired
    private OrderTbService orderTbService;

    @PostMapping("/deduct")
    public ResponseResult<OrderTb> deduct(Long productId, String userId, Long num) {
        return ResponseResult.success("200", "请求成功", orderTbService.create(productId, userId, num));
    }

    @PostMapping("/deductTcc")
    public ResponseResult<Void> deductTcc(Long productId, String userId, Long num) {
        orderTbService.createTcc(userId,productId,num);
        return ResponseResult.success("200", "请求成功", null);
    }
}
