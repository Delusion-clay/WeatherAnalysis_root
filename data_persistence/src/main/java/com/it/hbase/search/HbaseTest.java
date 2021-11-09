package com.it.hbase.search;


import com.it.hbase.exractor.SingleColumnMultiVersionRowExtrator;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import java.util.Map;
import java.util.Set;


public class HbaseTest {
    public static void main(String[] args) throws Exception {
        //新建表
        Set<String> rowKeySet = null;
        String hbase_table = "test:phone";
  /*      HBaseTableUtil.createTable(hbase_table, "cf", true, -1, 1, SpiltRegionUtil.getSplitKeysBydinct());

        //写入数据
        Put put = new Put("1111".getBytes());
        put.addColumn("cf".getBytes(),"aaa".getBytes(),"bbbbbb".getBytes());
        HBaseInsertHelper.put(hbase_table,put);
*/
        //查询
      HBaseSearchService hBaseSearchService = new HBaseSearchServiceImpl();
        Get get = new Get("18609765435".getBytes());
        get.setMaxVersions(100);
        SingleColumnMultiVersionRowExtrator scmvre = new SingleColumnMultiVersionRowExtrator("cf".getBytes(),"phone_mac".getBytes(),rowKeySet);
        rowKeySet = hBaseSearchService.search(hbase_table, get,scmvre);

        //  Map<String, String> search = hBaseSearchService.search(hbase_table, get, new MapRowExtrator());

   //     System.out.println(search);

    }
}
