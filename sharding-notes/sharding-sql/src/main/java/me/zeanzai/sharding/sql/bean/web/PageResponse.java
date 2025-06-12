package me.zeanzai.sharding.sql.bean.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import me.zeanzai.sharding.sql.constant.ResponseCode;

import java.util.List;

/**
 * @author shawnwang
 */
@Data
public class PageResponse extends BaseResponse{
    /**
     * 响应值
     */
    private String code;
    /**
     * 查询数据
     */
    private List<?> data;
    /**
     * 总的行数
     */
    private Long count;
    /**
     *响应信息
     */
    private String message;

    /**
     * 返回成功操作分页对象
     *
     * @param page 数据分页对象
     * @auther sunfeng
     */
    public static PageResponse success(Page page) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setCode(ResponseCode.SUCCESS);
        pageResponse.setCount(page.getTotal());
        pageResponse.setData(page.getRecords());
        return pageResponse;
    }


    /**
     * 返回异常数据查询响应对象
     *
     * @auther sunfeng
     */
    public static PageResponse error(String message) {
        PageResponse dataResponse = new PageResponse();
        dataResponse.setCode(ResponseCode.ERROR);
        dataResponse.setMessage(message);
        return dataResponse;
    }
}
