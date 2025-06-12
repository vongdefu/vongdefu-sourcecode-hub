package me.zeanzai.sharding.sql.gendata.util;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * @author shawnwang
 */
public class CommonUtils {
    /**
     * 生成一个编号
     *
     * @return
     */
    public static Long createNo(Integer i) {
        Random random = new Random();
        String methodNo = "" + random.nextInt(i);
        return Long.valueOf(methodNo);
    }

    public static Integer getStatus(List<Integer> list) {
        Random random = new Random();
        int n = random.nextInt(list.size());
        return list.get(n);
    }

    public static Date getDate() {
        Random rand = new Random();
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 0, 1);
        long start = cal.getTimeInMillis();
        cal.set(2021, 0, 1);
        long end = cal.getTimeInMillis();
        Date d = new Date(start + (long) (rand.nextDouble() * (end - start)));
        return d;
    }

}
