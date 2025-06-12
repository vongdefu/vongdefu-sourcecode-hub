package me.zeanzai.seatatccorder.dao;

import me.zeanzai.seatatccorder.model.TransactionalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author shawnwang
 */
@Repository
public interface TransactionalRecordRepository extends JpaRepository<TransactionalRecord,Long> {
    TransactionalRecord findByXid(String xid);
}
