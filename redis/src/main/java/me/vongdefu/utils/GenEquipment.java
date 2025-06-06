package me.vongdefu.utils;


import me.vongdefu.entity.EquipmentEntity;

import java.util.ArrayList;
import java.util.List;

public class GenEquipment {
    public static List<EquipmentEntity> genDeviceChain(int num) {
        List<EquipmentEntity> allEquipment = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            EquipmentEntity equipment = new EquipmentEntity();
            equipment.setRecordId("equipment_"+String.format("%09d", i));
            equipment.setUsedState(1);
            equipment.setEquipmentId(123);
            equipment.setBigCategoryName("产品大类");
            equipment.setSmallCategoryName("产品小类");
            equipment.setItemTypeId("dafadfas");
            equipment.setEquipmentNum("设备编号"+String.format("%09d", i));
            equipment.setEquipmentName("设备名称");

            equipment.setActivationMode(1);
            equipment.setSecretKey("secretkey");
            equipment.setIpcNum("ipc_"+String.format("%02d", i%10));
            equipment.setConcentratorName("3号集中器");
            equipment.setCollectorName("5号采集器");

            equipment.setTerminalPort("80801");
            List measurePoint = new ArrayList();
            measurePoint.add(101);
            measurePoint.add(205);
            equipment.setMeasurePointList(measurePoint);

            equipment.setAddressInfo("SZN5#506");// 城市代码+所在校区编号+楼栋编号+宿舍编号
            allEquipment.add(equipment);
        }
        return allEquipment;
    }

}
