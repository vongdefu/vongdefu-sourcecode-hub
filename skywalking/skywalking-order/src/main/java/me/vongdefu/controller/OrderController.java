package me.vongdefu.controller;


import com.google.common.collect.Lists;
import me.vongdefu.model.Order;
import me.vongdefu.service.feign.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    //调用feign的接口
    @Autowired
    private ProductService productService;

    @GetMapping("/get/{id}")
    public Order get(@PathVariable(value = "id") Long id) {
        //todo 未整合db层，造数据
        return new Order(id,20201000L,"bccm",productService.listByIds(Lists.newArrayList(1L,2L,3L,4L)));
    }

    @GetMapping("/list")
    public Order listByIds() throws InterruptedException {
        Thread.sleep(2000);

        //todo 未整合db层，造数据
        return new Order(12L,20201000L,"bccm",productService.listByIds(Lists.newArrayList(1L,2L,
                3L,4L)));
    }
}

