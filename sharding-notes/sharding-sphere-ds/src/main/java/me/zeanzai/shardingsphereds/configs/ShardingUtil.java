package me.zeanzai.shardingsphereds.configs;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/19
 */
@Component
public class ShardingUtil {
    private static final Logger logger = LoggerFactory.getLogger(ShardingUtil.class);

    @Autowired
    private ShardingStrategyConfig shardingStrategyConfig;

    public void initTables(String sqlPath) {
        //1.校验sqlPath
        if (StringUtils.isBlank(sqlPath)) {
            throw new IllegalArgumentException("sqlPath cannot be empty");
        }
        Optional.of(shardingStrategyConfig)
                .map(ShardingStrategyConfig::getDatasources)
                .ifPresent(dataSources -> {
                    //逻辑库的索引
                    AtomicInteger dbIndex = new AtomicInteger();
                    //2.依次处理每个数据源（物理库）
                    dataSources.stream().filter(Objects::nonNull).forEach(dataSourceConfig -> {
                        //3.创建数据源，建库用临时DataSource
                        MysqlDataSource dataSource = null;
                        try {
                            dataSource = buildDataSource(dataSourceConfig);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

                        //4.获取配置的数据库名称
                        String originDbName = shardingStrategyConfig.getOriginDbName();
                        logger.info("originDbName：[{}]，prepare to process the data source：[{}]", originDbName, dataSource.getUrl());

                        //5.每个数据源需要创建多少个逻辑库 逻辑库总数/物理库总数 = 每个物理库上应该创建多少个逻辑库
                        int dbNumPerDataSource = shardingStrategyConfig.getDbNum() / dataSources.size();

                        //6.依次创建逻辑库
                        for (int i = 0; i < dbNumPerDataSource; i++) {
                            //7.创建逻辑数据库
                            String targetDbName = createDatabase(dbIndex, jdbcTemplate, originDbName);
                            //8.在指定的逻辑库中创建table
                            createTables(sqlPath, dataSource, targetDbName);
                        }
                    });
                });
    }

    /**
     * 在指定的逻辑db中创建table
     */
    private void createTables(String sqlPath, MysqlDataSource dataSource, String targetDbName) {
        //1.指定建表的逻辑库
        dataSource.setDatabaseName(targetDbName);
        //2.创建建表使用的jdbcTemplate
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            //3.拼接建表sql
            String finalTableSql = splicingFinalTableSql(sqlPath);
            //4.执行建表sql
            jdbcTemplate.execute(finalTableSql);
            logger.info("DB[{}] create table complete...", targetDbName);
        } catch (Exception e) {
            logger.error("createTables fail,e:[{}]",e.getMessage());
        }
    }

    /**
     * 创建逻辑库
     */
    private String createDatabase(AtomicInteger dbIndex, JdbcTemplate jdbcTemplate, String originDbName) {
        //1.拼接要创建的目标数据库名称
        String targetDbName = originDbName + shardingStrategyConfig.getDbSuffix() + (dbIndex.getAndIncrement());
        //2.创建逻辑库
        jdbcTemplate.execute(String.format("CREATE DATABASE IF NOT EXISTS `%s` DEFAULT CHARACTER SET UTF8", targetDbName));
        logger.info("DB[{}]create db complete...", targetDbName);
        return targetDbName;
    }

    /**
     * 拼接建表sql
     */
    private String splicingFinalTableSql(String sqlPath) throws IOException {
        StringBuilder finalSqlBuilder = new StringBuilder();
        //1.使用流读取sqlPath文件中配置的sql语句
        InputStream inputStream = ShardingUtil.class.getResourceAsStream(sqlPath);
        if (Objects.isNull(inputStream)) {
            throw new IOException("The specified sqlPath file does not exist");
        }
        //2.将流转换成字符串
        String sqlText = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        for (int i = 0; i < shardingStrategyConfig.getTableNumPerDb(); i++) {
            int tableIndex = i;
            //3.依次处理每条sql
            Optional.of(sqlText.split(";"))
                    .ifPresent(sqls -> Arrays.stream(sqls)
                            .filter(Objects::nonNull)
                            .forEach(sql -> {
                                //4.依次处理配置的表后缀，拼接出相应后缀名的sql
                                shardingStrategyConfig.getTableSuffix().forEach(tableSuffix -> {
                                    //5.为sql中的表名拼接后缀
                                    String finalTableSql = splicingTableSuffix(sql, tableSuffix + tableIndex);
                                    //6.将最终要执行的sql统一拼接起来
                                    finalSqlBuilder.append(finalTableSql);
                                });
                            }));
        }
        return finalSqlBuilder.toString();
    }

    /**
     * 为sql中的表名拼接后缀
     */
    private String splicingTableSuffix(String sql, String suffix) {
        //1.编写匹配表名的正则表达式，(?i)表示忽略大小写，(?s)表示开启单行模式，即多行sql就像在同一行一样，方便匹配
        Pattern tablePattern = Pattern.compile("(?i)(?s)CREATE\\s+TABLE\\s+(\\S+)");
        StringBuilder sb = new StringBuilder(sql);
        //2.使用正则表达式来匹配表名
        Matcher matcher = tablePattern.matcher(sb);
        if (matcher.find()) {
            //3.在表名后边，拼接上指定的后缀名
            sb.insert(matcher.end(), suffix)
                    .append(";")
                    .append("\n\n");
            logger.debug("match to table:[{}]", matcher.group(1));
            return sb.toString();
        }
        return "";
    }

    /**
     * 创建数据源
     */
    private MysqlDataSource buildDataSource(DataSourceConfig dataSourceConfig) throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(dataSourceConfig.getHostName());
        dataSource.setPort(dataSourceConfig.getPort());
        dataSource.setUser(dataSourceConfig.getUsername());
        dataSource.setPassword(dataSourceConfig.getPassword());
        dataSource.setCharacterEncoding("utf-8");
        dataSource.setUseSSL(false);
        //开启批处理
        dataSource.setAllowMultiQueries(Boolean.TRUE);
        return dataSource;
    }


}
