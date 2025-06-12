package me.zeanzai.globalexception.bean.user;

import lombok.Data;
import me.zeanzai.globalexception.common.constant.ResponseStatusCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/22
 */
@Data
public class UserDTO {
    @NotEmpty(message = ResponseStatusCode.EE2001)
    private String name;

    @NotBlank(message = ResponseStatusCode.EE2002)
    private String address;


    private String sex;
    private Integer age;
    private Long phonenum;
}
