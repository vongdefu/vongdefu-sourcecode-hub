package me.vongdefu.entity;


import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NiotCommandEntity {
    private Integer recordId;
    private String  bigCategory;
    private String bigCategoryName;
    private String  smallCategory;
    private String smallCategoryName;
    private String  protocolLibName;// 协议库编号
    private String encryptionLibName;// 加密库编号
    private Integer returnDataType;
    private Integer agreement;
    private String remark;
}
