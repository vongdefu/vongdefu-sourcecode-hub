package me.vongdefu.service.feign;


import me.vongdefu.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "skywalking-product")
public interface ProductService {

    /**
     * 获取列表
     * @param ids
     * @return
     */
    @PostMapping("/product/list")
    List<Product> listByIds(@RequestBody List<Long> ids);
}

