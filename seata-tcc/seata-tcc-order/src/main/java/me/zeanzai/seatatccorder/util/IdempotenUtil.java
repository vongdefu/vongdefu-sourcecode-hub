package me.zeanzai.seatatccorder.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shawnwang
 */
@Slf4j
public class IdempotenUtil {
    //HashBasedTable 可以用两个key标记一个值
    private static Table<Class<?>,String,Long> map= HashBasedTable.create();

    public static void add(Class<?> clazz,String xid,Long marker){
        map.put(clazz,xid,marker);
        log.info("---------执行add 方法： {}",map.toString());
    }

    public static Long get(Class<?> clazz,String xid){
        return map.get(clazz,xid);
    }

    public static void remove(Class<?> clazz,String xid){
        map.remove(clazz,xid);
    }
}
