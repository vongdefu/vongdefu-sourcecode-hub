package me.vongdefu.entity;

import lombok.*;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EquipmentEntity {

    /**
     *
     *
     * 1. 基本信息
     * 2. 设备链路信息
     * 3. 设备物理信息
     */
    /**
     * 水电表设备
     */
    private String recordId;
    private Integer usedState;/*激活状态，激活后会绑定产品的密钥、加密库、协议库等信息*/

    /**
     * 基本信息
     */
    private Integer equipmentId;/*设备ID*/
    private String bigCategoryName;/*产品大类*/
    private String smallCategoryName;/*产品小类*/
    private String itemTypeId;/*产品型号*/
    private String measureCategoryName;/*能源类型，水、电；*/
    private String equipmentNum;/*设备编号*/
    private String equipmentName;/*设备名称*/

    /**
     * 设备链路信息
     */
    private Integer activationMode;/*注册状态，是否注册到某一个采集器上*/
    private String secretKey;/*密钥*/
    private String ipcNum;/*工控机编号*/
    private String concentratorName;/*集中器名称*/
    private String collectorName;/*采集器名称*/

    /**
     * 设备物理信息
     */
    private String terminalPort;/*终端端口*/
    // 测量点号列表，相当于指令编号。
    // 比如对某一产品的读数进行测量，再比如，对某一产品的流速进行测量；
    // 再比如，获取某一产品的额定功率
    private List<Integer> measurePointList;


    /**
     * 位置信息
     */
    private String addressInfo; // 设备所属信息，如 某电表 属于 5号楼506室
}
