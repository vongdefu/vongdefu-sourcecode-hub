package me.zeanzai.seataataccount.controller;

import me.zeanzai.seataataccount.entity.Account;
import me.zeanzai.seataataccount.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Account)表控制层
 *
 * @author makejava
 * @since 2023-03-19 14:25:36
 */
@RestController
@RequestMapping("account")
public class AccountController {
    /**
     * 服务对象
     */
    @Resource
    private AccountService accountService;

    /**
     * 分页查询
     *
     * @param account     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<Account>> queryByPage(Account account, PageRequest pageRequest) {
        return ResponseEntity.ok(this.accountService.queryByPage(account, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<Account> queryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.accountService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param account 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<Account> add(@RequestBody Account account) {
        return ResponseEntity.ok(this.accountService.insert(account));
    }

    /**
     * 编辑数据
     *
     * @param account 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<Account> edit(@RequestBody Account account) {
        return ResponseEntity.ok(this.accountService.update(account));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Long id) {
        return ResponseEntity.ok(this.accountService.deleteById(id));
    }

    @PostMapping("/deduct")
    public ResponseEntity<Boolean> deduct(@RequestParam("userId") String userId, @RequestParam("money") Long money) {
        return ResponseEntity.ok(this.accountService.deduct(userId, money));
    }

}

