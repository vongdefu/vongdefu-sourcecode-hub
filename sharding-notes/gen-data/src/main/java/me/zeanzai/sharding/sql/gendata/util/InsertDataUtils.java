package me.zeanzai.sharding.sql.gendata.util;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author shawnwang
 */
public class InsertDataUtils {
    //起始id
    private long begin = 1;
    private long step = 10000;
    private long round = 1;
    //每次循环插入的数据量
    private long end = begin + step;
    private String url = "jdbc:mysql://192.168.10.105:3306/test?autoReconnect=true&useUnicode=true&characterEncoding" +
            "=utf-8&serverTimezone=GMT";
    private String user = "root";
    private String password = "root1003";
    //订单状态
    private Integer[] orderStatus = {10, 20, 30, 40, 50, 55, 60, 70};
    //订单状态
    private Integer[] payStatus = {1, 2, 3};
    //配送状态
    private Integer[] deliveryStatus = {0, 1, 2, 3};

    /**
     * 不带索引
     * 生成两张空表
     */
    public void createTableWithoutIndex() {
        Connection conn;
        PreparedStatement pstm;
        PreparedStatement pstm2;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //连接mysql
            conn = DriverManager.getConnection(url, user, password);
            //将自动提交关闭
            conn.setAutoCommit(false);
            String sql = "CREATE TABLE `order_infob` (\n" +
                    "  `id` bigint(32) NOT NULL AUTO_INCREMENT,\n" +
                    "  `order_no` varchar(32) NOT NULL COMMENT '订单号',\n" +
                    "  `order_amount` decimal(8,2) NOT NULL COMMENT '订单金额',\n" +
                    "  `merchant_id` bigint(32) NOT NULL COMMENT '商户ID',\n" +
                    "  `user_id` bigint(32) NOT NULL COMMENT '用户ID',\n" +
                    "  `order_freight` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '运费',\n" +
                    "  `order_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '订单状态,10待付款，20待接单，30已接单，40配送中，50已完成，55部分退款，60全部退款，70取消订单',\n" +
                    "  `trans_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '交易时间',\n" +
                    "  `pay_status` tinyint(3) NOT NULL DEFAULT '2' COMMENT '支付状态,1待支付,2支付成功,3支付失败',\n" +
                    "  `recharge_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '支付完成时间',\n" +
                    "  `pay_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '实际支付金额',\n" +
                    "  `pay_discount_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '支付优惠金额',\n" +
                    "  `address_id` bigint(32) NOT NULL COMMENT '收货地址ID',\n" +
                    "  `delivery_type` tinyint(3) NOT NULL DEFAULT '2' COMMENT '配送方式，1自提。2配送',\n" +
                    "  `delivery_status` tinyint(3) DEFAULT '0' COMMENT '配送状态，0 配送中，2已送达，3待收货，4已送达',\n" +
                    "  `delivery_expect_time` timestamp NULL DEFAULT NULL COMMENT '配送预计送达时间',\n" +
                    "  `delivery_complete_time` timestamp NULL DEFAULT NULL COMMENT '配送送达时间',\n" +
                    "  `delivery_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '配送运费',\n" +
                    "  `coupon_id` bigint(32) DEFAULT NULL COMMENT '优惠券id',\n" +
                    "  `cancel_time` timestamp NULL DEFAULT NULL COMMENT '订单取消时间',\n" +
                    "  `confirm_time` timestamp NULL DEFAULT NULL COMMENT '订单确认时间',\n" +
                    "  `remark` varchar(512) DEFAULT NULL COMMENT '订单备注留言',\n" +
                    "  `create_user` bigint(32) DEFAULT NULL COMMENT '创建用户',\n" +
                    "  `update_user` bigint(32) DEFAULT NULL COMMENT '更新用户',\n" +
                    "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                    "  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                    "  `delete_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记',\n" +
                    "  PRIMARY KEY (`id`,`order_no`),\n" +
                    "  UNIQUE KEY `uinx_order_no` (`order_no`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='订单表';";

            String sql2 = "CREATE TABLE `order_item_detailb` (\n" +
                    " `id` bigint(32) NOT NULL AUTO_INCREMENT,\n" +
                    " `order_no` varchar(32) NOT NULL COMMENT '订单号',\n" +
                    " `product_id` bigint(32) NOT NULL COMMENT '商品ID',\n" +
                    " `category_id` bigint(32) NOT NULL COMMENT '商品分类ID',\n" +
                    " `goods_num` int(8) NOT NULL DEFAULT '1' COMMENT '商品购买数量',\n" +
                    " `goods_price` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '商品单价',\n" +
                    " `goods_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '商品总价',\n" +
                    " `product_name` varchar(64) DEFAULT NULL COMMENT '商品名',\n" +
                    " `discount_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '商品优惠金额',\n" +
                    " `discount_id` bigint(32) DEFAULT NULL COMMENT '参与活动ID',\n" +
                    " `product_picture_url` varchar(128) DEFAULT NULL COMMENT '商品图片',\n" +
                    " `create_user` bigint(32) DEFAULT NULL COMMENT '创建用户',\n" +
                    " `update_user` bigint(32) DEFAULT NULL COMMENT '更新用户',\n" +
                    " `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                    " `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                    " `delete_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记',\n" +
                    " PRIMARY KEY (`id`) USING BTREE\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='订单明细表';";

            pstm = conn.prepareStatement(sql);
            pstm.execute();

            pstm2 = conn.prepareStatement(sql2);
            pstm2.execute();

            conn.commit();
            conn.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertBigData3() {
        //定义连接、statement对象
        Connection conn;
        PreparedStatement pstm;
        PreparedStatement pstm2;
        PreparedStatement pstm3;
        PreparedStatement pstm4;
        PreparedStatement pstm5;
        try {
            //加载jdbc驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //连接mysql
            conn = DriverManager.getConnection(url, user, password);
            //将自动提交关闭
            conn.setAutoCommit(false);
            //编写sql
            String sql = "INSERT INTO `order_infob` " +
                    "( `order_no`, `order_amount`, `merchant_id`, `user_id`,`address_id`,`order_status`," +
                    "`pay_status`,`delivery_status`,`trans_time`,`recharge_time`,`update_time`) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?);";
            String sql2 = "INSERT INTO `order_item_detailb` (`order_no`, `product_id`, `category_id`) VALUES (?,?,?)";
            String sql3 = "INSERT INTO `order_item_detailb` (`order_no`, `product_id`, `category_id`) VALUES (?,?,?)";
            String sql4 = "INSERT INTO `order_item_detailb` (`order_no`, `product_id`, `category_id`) VALUES (?,?,?)";
            String sql5 = "INSERT INTO `order_item_detailb` (`order_no`, `product_id`, `category_id`) VALUES (?,?,?)";

            //预编译sql
            pstm = conn.prepareStatement(sql);
            pstm2 = conn.prepareStatement(sql2);
            pstm3 = conn.prepareStatement(sql3);
            pstm4 = conn.prepareStatement(sql4);
            pstm5 = conn.prepareStatement(sql5);

            //开始总计时
            long bTime1 = System.currentTimeMillis();
            Long merId = CommonUtils.createNo(1000000000);

            //循环10次，每次 step 数据，一共[10*step]万
            for (int i = 0; i < round; i++) { // 10*20000 = 20万
                //开启分段计时，计1W数据耗时
                long bTime = System.currentTimeMillis();
                Long userId = CommonUtils.createNo(100000000);
                //开始循环
                while (begin < end) {
                    Long no = CommonUtils.createNo(1000000000);
                    String orderCode = GenerateOrderNoUtils.getOrderNo(no);
                    Long productId = CommonUtils.createNo(100000);
                    //赋值
                    pstm.setString(1, orderCode);
                    pstm.setBigDecimal(2, new BigDecimal(productId));
                    pstm.setLong(3, merId);
                    pstm.setLong(4, userId);
                    pstm.setLong(5, 2L);
                    pstm.setLong(6, CommonUtils.getStatus(Arrays.asList(orderStatus)));
                    pstm.setLong(7, CommonUtils.getStatus(Arrays.asList(payStatus)));
                    pstm.setLong(8, CommonUtils.getStatus(Arrays.asList(deliveryStatus)));
                    pstm.setDate(9, CommonUtils.getDate());
                    pstm.setDate(10, CommonUtils.getDate());
                    pstm.setDate(11, CommonUtils.getDate());
                    //执行sql
//                    pstm.execute();
                    pstm.addBatch();
                    pstm2.setString(1, orderCode);
                    pstm2.setLong(2, productId);
                    pstm2.setLong(3, productId);
//                    pstm2.execute();
                    pstm2.addBatch();

                    pstm3.setString(1, orderCode);
                    pstm3.setLong(2, productId);
                    pstm3.setLong(3, productId);
//                    pstm3.execute();
                    pstm3.addBatch();

                    pstm4.setString(1, orderCode);
                    pstm4.setLong(2, productId);
                    pstm4.setLong(3, productId);
//                    pstm4.execute();
                    pstm4.addBatch();

                    pstm5.setString(1, orderCode);
                    pstm5.setLong(2, productId);
                    pstm5.setLong(3, productId);
//                    pstm5.execute();
                    pstm5.addBatch();


                    begin++;
                }
                //执行批处理
                pstm.executeBatch();
                //执行批处理
                pstm2.executeBatch();
                //执行批处理
                pstm3.executeBatch();
                //执行批处理
                pstm4.executeBatch();
                //执行批处理
                pstm5.executeBatch();

                //提交事务
                conn.commit();

                //边界值
                end += step;
                //关闭分段计时
                long eTime = System.currentTimeMillis();
                //输出
                System.out.println("成功插入" + step + "条数据耗时：" + (eTime - bTime));
            }
            //关闭总计时
            long eTime1 = System.currentTimeMillis();
            //输出
            System.out.println("插入" + step * round + "条数据共耗时：" + (eTime1 - bTime1));
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }
}
