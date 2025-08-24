package me.vongdefu;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class MainTest {
    public static void main(String[] args) throws InterruptedException {
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            ZooKeeper zooKeeper = new ZooKeeper("192.168.10.105:2181",
                    4000, event -> {
                if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
                    //如果收到了服务端的响应事件，连接成功
                    countDownLatch.countDown();

                    System.out.println("事件类型：" + event.getType());
                    System.out.println("当前线程：" + Thread.currentThread().getName());
                }
            });

            countDownLatch.await();
            //CONNECTED
            System.out.println(zooKeeper.getState());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
