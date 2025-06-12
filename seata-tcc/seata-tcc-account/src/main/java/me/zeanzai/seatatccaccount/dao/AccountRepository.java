package me.zeanzai.seatatccaccount.dao;

import me.zeanzai.seatatccaccount.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author shawnwang
 */
@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    Account findByUserId(String userId);
}
