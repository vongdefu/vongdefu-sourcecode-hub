package me.zeanzai.seataatorder.controller;

import me.zeanzai.seataatorder.entity.Ordertb;
import me.zeanzai.seataatorder.service.OrdertbService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Ordertb)表控制层
 *
 * @author makejava
 * @since 2023-03-19 14:53:29
 */
@RestController
@RequestMapping("ordertb")
public class OrdertbController {
    /**
     * 服务对象
     */
    @Resource
    private OrdertbService ordertbService;

    /**
     * 分页查询
     *
     * @param ordertb     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<Ordertb>> queryByPage(Ordertb ordertb, PageRequest pageRequest) {
        return ResponseEntity.ok(this.ordertbService.queryByPage(ordertb, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<Ordertb> queryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.ordertbService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param ordertb 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<Ordertb> add(@RequestBody Ordertb ordertb) {
        return ResponseEntity.ok(this.ordertbService.insert(ordertb));
    }

    /**
     * 编辑数据
     *
     * @param ordertb 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<Ordertb> edit(@RequestBody Ordertb ordertb) {
        return ResponseEntity.ok(this.ordertbService.update(ordertb));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Long id) {
        return ResponseEntity.ok(this.ordertbService.deleteById(id));
    }

    @PostMapping("/createorder")
    public ResponseEntity<Boolean> createorder(@RequestParam("userId") String userId, @RequestParam("productId") Long productId, @RequestParam("num") Long num) {
        return ResponseEntity.ok(this.ordertbService.createOrder(userId, productId, num));
    }

}

