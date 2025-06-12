package me.zeanzai.seatatccstorage.dao;


import me.zeanzai.seatatccstorage.model.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author shawnwang
 */

@Repository
public interface StorageRepository extends JpaRepository<Storage,Long> {

    Storage findByProductId(Long productId);

}
