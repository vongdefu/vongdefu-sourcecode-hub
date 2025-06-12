package me.zeanzai.seataatorder.service;

import me.zeanzai.seataatorder.entity.Ordertb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (Ordertb)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 14:53:29
 */
public interface OrdertbService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Ordertb queryById(Long id);

    /**
     * 分页查询
     *
     * @param ordertb     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<Ordertb> queryByPage(Ordertb ordertb, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param ordertb 实例对象
     * @return 实例对象
     */
    Ordertb insert(Ordertb ordertb);

    /**
     * 修改数据
     *
     * @param ordertb 实例对象
     * @return 实例对象
     */
    Ordertb update(Ordertb ordertb);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    boolean createOrder(String userId, Long productId, Long num);

}
