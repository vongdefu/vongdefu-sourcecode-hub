package me.zeanzai.sharding.sql;

import me.zeanzai.sharding.sql.gendata.util.InsertDataUtils;

public class ShardingSqlGenDataApplication {


    public static void main(String[] args) {
        genData1();
    }

    public static void createTable(){
        InsertDataUtils insertDataUtils = new InsertDataUtils();
        insertDataUtils.createTableWithoutIndex();
    }


    public static void genData1(){
        InsertDataUtils insertDataUtils = new InsertDataUtils();
        insertDataUtils.insertBigData3();
    }



}
