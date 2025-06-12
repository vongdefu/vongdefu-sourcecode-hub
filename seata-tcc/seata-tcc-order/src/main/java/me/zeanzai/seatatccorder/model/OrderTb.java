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
@Table(name = "ordertb")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "product_id")
    private Long productId;

    @Column
    private Long num;

    @Column
    private Integer status;

//    @Column(name = "create_time")
//    @CreatedDate
//    private Date createTime;
}
