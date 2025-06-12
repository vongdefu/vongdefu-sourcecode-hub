package me.zeanzai.seatatccaccount.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author shawnwang
 */
@Data
@Entity
@Table(name = "account")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 用 户userId
     */
    @Column
    private String userId;
    /**
     * 余额，单位分
     */
    @Column
    private Long money;
    /**
     * 创建时间
     */
    @Column
    private Date createTime;

    @Column
    private Long frozen;
}
