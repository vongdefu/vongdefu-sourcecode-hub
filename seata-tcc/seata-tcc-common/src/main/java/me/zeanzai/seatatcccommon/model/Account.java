package me.zeanzai.seatatcccommon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author shawnwang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private Long id;
    /**
     * 用 户userId
     */
    private String userId;
    /**
     * 余额，单位分
     */
    private Long money;
    /**
     * 创建时间
     */
    private Date createTime;
    private Long frozen;
}
