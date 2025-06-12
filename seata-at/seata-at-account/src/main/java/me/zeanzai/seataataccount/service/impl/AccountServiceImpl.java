package me.zeanzai.seataataccount.service.impl;

import me.zeanzai.seataataccount.entity.Account;
import me.zeanzai.seataataccount.dao.AccountDao;
import me.zeanzai.seataataccount.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * (Account)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 14:25:36
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Resource
    private AccountDao accountDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Account queryById(Long id) {
        return this.accountDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param account     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Account> queryByPage(Account account, PageRequest pageRequest) {
        long total = this.accountDao.count(account);
        return new PageImpl<>(this.accountDao.queryAllByLimit(account, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param account 实例对象
     * @return 实例对象
     */
    @Override
    public Account insert(Account account) {
        this.accountDao.insert(account);
        return account;
    }

    /**
     * 修改数据
     *
     * @param account 实例对象
     * @return 实例对象
     */
    @Override
    public Account update(Account account) {
        this.accountDao.update(account);
        return this.queryById(account.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.accountDao.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public boolean deduct(String userId, Long money) {
        Account account = this.accountDao.queryByUserId(userId);
        if (Objects.isNull(account)) {
            return false;

        }
        account.setMoney(account.getMoney()-money);
        return this.accountDao.update(account)>0;
    }
}
