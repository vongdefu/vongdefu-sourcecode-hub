package me.vongdefu.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.vongdefu.entity.DeviceCommondInfoEntity;
import me.vongdefu.entity.EquipmentEntity;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class AppWarmUp {

    @Resource
    private RedisTemplate redisTemplate;


    private List<DeviceCommondInfoEntity> createDeviceCmd(int num){
        List<DeviceCommondInfoEntity> deviceCommondInfoEntities = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            DeviceCommondInfoEntity deviceCommondInfoEntity = new DeviceCommondInfoEntity();
            deviceCommondInfoEntity.setId(String.valueOf(i));
            deviceCommondInfoEntity.setDeviceTypeNo("typeNo: "+i);
            deviceCommondInfoEntity.setDeviceTypeName("typeName: "+i);
            deviceCommondInfoEntity.setDeviceNo("deviceNo: "+i);
            deviceCommondInfoEntity.setDeviceName("deviceName: "+i);

            deviceCommondInfoEntity.setCmdInfo("id="+i+"&typeNo="+i+"&typeName="+i+"&deviceNo="+i+"&deviceName="+i);
            deviceCommondInfoEntities.add(deviceCommondInfoEntity);
        }
        return deviceCommondInfoEntities;
    }

    /**
     * 从db中获取设备信息，然后放入redis中
     */
    @PostConstruct
    public void genDeviceInfoList(){
        log.info("自动从db中设备信息，然后放到redis中");
        List<DeviceCommondInfoEntity> deviceCmd = createDeviceCmd(1000);
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.rightPushAll("devicelist", deviceCmd);
    }

    /**
     * 从db中获取设备链路信息，并放入redis中的hash中，以【alldevicelist:equipmentlist:设备的recordId】作为key。
     */
    @PostConstruct
    public void putDeviceChainToRedis() throws JsonProcessingException {
        log.info("从db中获取设备链路信息，并放入redis");
        // 删除已有的
        redisTemplate.delete("alldevicelist:equipmentlist");


        // 模拟从db中获取设备链路信息
        List<EquipmentEntity> equipmentEntityList = GenEquipment.genDeviceChain(100);

        for (int i = 0; i < equipmentEntityList.size(); i++) {
            EquipmentEntity equipment = equipmentEntityList.get(i);
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(objectMapper.writeValueAsString(equipment), Map.class);
            redisTemplate.opsForHash().putAll("alldevicelist:equipmentlist:"+ equipment.getRecordId(), map);
        }

    }

    @PostConstruct
    public void putDeviceChainToRedisUseRedisList() throws JsonProcessingException {
        log.info("从db中获取设备链路信息，并放入redis");

        // 模拟从db中获取设备链路信息
        List<EquipmentEntity> equipmentEntityList = GenEquipment.genDeviceChain(1000);

        redisTemplate.opsForList().rightPushAll("equipmentlist",equipmentEntityList);

    }





}
