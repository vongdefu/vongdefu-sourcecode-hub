package me.vongdefu.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/11
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DeviceCommondInfoEntity implements Serializable {

    private String id;
    private String deviceTypeNo;
    private String deviceTypeName;
    private String deviceNo;
    private String deviceName;
    private String cmdInfo;

    private Integer recordId;/*设备配置ID*/
    private Integer buildingId;/*建筑ID*/
    private Integer equipmentId;
    private String equipmentName;/*设备名称*/
    private String concentratorName;/*集中器名称*/
    private String collectorName;/*采集器名称*/
    private Integer projectId;/*项目id*/
    private String configurationPlanName;/*配置方案的名称*/
    private Integer activeTaskId;
    private Integer selfcheckTaskId;



}
