package me.zeanzai.seataataccount.service;

import me.zeanzai.seataataccount.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (Account)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 14:25:36
 */
public interface AccountService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Account queryById(Long id);

    /**
     * 分页查询
     *
     * @param account     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<Account> queryByPage(Account account, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param account 实例对象
     * @return 实例对象
     */
    Account insert(Account account);

    /**
     * 修改数据
     *
     * @param account 实例对象
     * @return 实例对象
     */
    Account update(Account account);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    boolean deduct(String userId, Long money);

}
