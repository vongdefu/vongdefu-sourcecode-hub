package me.zeanzai.seatatccorder.dao;

import me.zeanzai.seatatccorder.model.OrderTb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author shawnwang
 */
@Repository
public interface OrderTbRepository extends JpaRepository<OrderTb, Long> {

    /**
     * https://blog.csdn.net/cmx1060220219/article/details/106259423
     *
     * 可以写 hql 和 sql
     * @param orderId
     * @return
     */
    OrderTb findByOrderId(String orderId);
}
