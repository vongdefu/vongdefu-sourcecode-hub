package me.zeanzai.seatatccorder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author shawnwang
 */
@Data
@Entity
@Table(name = "transactional_record")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Long id;

    @Column
    private String xid;

    @Column
    private Integer status;
}
