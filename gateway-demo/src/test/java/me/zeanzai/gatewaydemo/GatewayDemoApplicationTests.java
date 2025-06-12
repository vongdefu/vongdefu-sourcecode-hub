package me.zeanzai.gatewaydemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class GatewayDemoApplicationTests {

    @Test
    void contextLoads() {
        File file = new File("/Users/shawnwang/Downloads/宣萱心伴/正念冥想 copy");
        func(file);

    }

    public void func(File file) {

        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                func(f);
            }
            if (f.isFile()) {
                // 获取文件名中的所有数字
                String name = f.getName();
                String[] split = name.split("\\.");
                String s1 = split[0];
                String s = s1.replaceAll("[\\d]+", "").replace(" ", "");
                // 删除所有数字，重命名
                String newName = s + ".m4a";

                System.out.println(newName);
                File newFile = new File("/Users/shawnwang/Downloads/宣萱心伴/正念冥想 copy/" + newName);
                f.renameTo(newFile);
            }
        }
    }

}
