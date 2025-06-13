package com.example.safeapi.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult {

    private String code;

    /** 结果 */
    private String msg;
}
