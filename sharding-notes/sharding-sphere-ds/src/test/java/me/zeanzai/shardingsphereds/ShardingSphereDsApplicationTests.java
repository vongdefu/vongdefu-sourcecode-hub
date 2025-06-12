package me.zeanzai.shardingsphereds;

import me.zeanzai.shardingsphereds.configs.ShardingUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class ShardingSphereDsApplicationTests {

    @Autowired
    private ShardingUtil shardingUtil;

    @Test
    void contextLoads() {
        shardingUtil.initTables("/sql/init.sql");
    }

}
