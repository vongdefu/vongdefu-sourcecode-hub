package me.zeanzai.sharding.sql.gendata.util;

import me.zeanzai.sharding.sql.context.utils.GenerateOrderNoUtils;

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
    private long round = 10;
    //每次循环插入的数据量
    private long end = begin+step;
    private String url = "jdbc:mysql://192.168.56.10:3306/monomer_order?autoReconnect=true&useUnicode=true&characterEncoding" +
            "=utf-8&serverTimezone=GMT";
    private String user = "root";
    private String password = "root";
    //订单状态
    private Integer[] orderStatus = {10,20,30,40,50,55,60,70};
    //订单状态
    private Integer[] payStatus = {1,2,3};
    //配送状态
    private Integer[] deliveryStatus = {0,1,2,3};

    public static void main(String[] args) {
        InsertDataUtils insertDataUtils = new InsertDataUtils();
        insertDataUtils.insertBigData3();
    }

    public void insertBigData3() {
        //定义连接、statement对象
        Connection conn ;
        PreparedStatement pstm ;
        PreparedStatement pstm2 ;
        PreparedStatement pstm3 ;
        PreparedStatement pstm4 ;
        PreparedStatement pstm5 ;
        try {
            //加载jdbc驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //连接mysql
            conn = DriverManager.getConnection(url, user, password);
            //将自动提交关闭
            conn.setAutoCommit(false);
            //编写sql
            String sql = "INSERT INTO `order_info` " +
                    "( `order_no`, `order_amount`, `merchant_id`, `user_id`,`address_id`,`order_status`," +
                    "`pay_status`,`delivery_status`,`trans_time`,`recharge_time`,`update_time`) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?);";
            String sql2 ="INSERT INTO `order_item_detail` (`order_no`, `product_id`, `category_id`) VALUES (?,?,?)";
            String sql3 ="INSERT INTO `order_item_detail` (`order_no`, `product_id`, `category_id`) VALUES (?,?,?)";
            String sql4 ="INSERT INTO `order_item_detail` (`order_no`, `product_id`, `category_id`) VALUES (?,?,?)";
            String sql5 ="INSERT INTO `order_item_detail` (`order_no`, `product_id`, `category_id`) VALUES (?,?,?)";

            //预编译sql
            pstm = conn.prepareStatement(sql);
            pstm2 = conn.prepareStatement(sql2);
            pstm3 = conn.prepareStatement(sql3);
            pstm4 = conn.prepareStatement(sql4);
            pstm5 = conn.prepareStatement(sql5);

            //开始总计时
            long bTime1 = System.currentTimeMillis();
            Long merId = CommonUtils.createNo(1000000000);;
            //循环10次，每次 step 数据，一共[10*step]万
            for(int i=0;i<round;i++) { // 10*20000 = 20万
                //开启分段计时，计1W数据耗时
                long bTime = System.currentTimeMillis();
                Long userId = CommonUtils.createNo(100000000);;
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
                System.out.println("成功插入"+step+"条数据耗时："+(eTime-bTime));
            }
            //关闭总计时
            long eTime1 = System.currentTimeMillis();
            //输出
            System.out.println("插入"+step*round+"条数据共耗时："+(eTime1-bTime1));
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }
}
