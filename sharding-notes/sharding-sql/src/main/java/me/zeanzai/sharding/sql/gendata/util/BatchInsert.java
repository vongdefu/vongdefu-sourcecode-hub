package me.zeanzai.sharding.sql.gendata.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author shawnwang
 */
public class BatchInsert {
    public static void main(String[] args) {
        long startTimes = System.currentTimeMillis();
        int threadCount = 1;
        int total = 2;
        int every = total/threadCount;
        final CountDownLatch latch = new CountDownLatch(threadCount);

        // 预计创建 140 * 400 = 64000 万数据
        for(int i=0;i<threadCount;i++){

            // 每次 创建 10 万, 每个线程执行 20 次 ，即每个线程创建 400万数据

            new Thread(new WorkerT(CountNum.count, latch,i*every,(i+1)*every)).start();
        }
        try {
            latch.await();
            long endTimes = System.currentTimeMillis();

            System.out.println("==== 所有线程执行完毕:" + (endTimes - startTimes));
            System.out.println(CountNum.count.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

class CountNum {
    static AtomicLong count=new AtomicLong(0);

}

class WorkerT implements Runnable{
    //    private static AtomicLong count=new AtomicLong(0);
    int start = 0;
    int end = 0;
    CountDownLatch latch;

    AtomicLong countNum;
    public WorkerT(AtomicLong countNum, CountDownLatch latch, int start,int end){
        this.countNum = countNum;
        this.start = start;
        this.end = end;
        this.latch = latch;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            countNum.addAndGet(200000);
            System.out.println("线程" + Thread.currentThread().getName()+ "正在执行。。");
            InsertDataUtils insertDateUtils1 = new InsertDataUtils();
            insertDateUtils1.insertBigData3();
        }
        latch.countDown();
    }

}

