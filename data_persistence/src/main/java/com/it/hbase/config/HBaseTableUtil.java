package com.it.hbase.config;

import com.google.common.collect.Sets;
import com.it.hbase.split.SpiltRegionUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * @author:
 * @description:   hbase表创建
 * @Date:Created in 2019-03-29 22:15
 */
public class HBaseTableUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HBaseTableUtil.class);
    private static final String COPROCESSORCLASSNAME =  "org.apache.hadoop.hbase.coprocessor.AggregateImplementation";
    private static HBaseConf conf = HBaseConf.getInstance();

    private HBaseTableUtil(){}

    /**
     * 获取hbase 表连接
     * @param tableName
     * @return
     */
    public static Table getTable(String tableName){
        Table table =null;
        if(tableExists(tableName)){
            try {
                table = conf.getHconnection().getTable(TableName.valueOf(tableName));
            } catch (IOException e) {
                LOG.error(null,e);
            }
        }
        return table;
    }

    public static void close(Table table){
        if(table != null) {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * description:   判断   HBase中是否存在  名为  tableName 的表
     * @param tableName
     * @return  boolean
     * 2015-10-10 下午2:53:50
     */
    public static boolean tableExists(String tableName){

        boolean  isExists = false;
        try {
            isExists = conf.getHconnection().getAdmin().tableExists(TableName.valueOf(tableName));
        } catch (MasterNotRunningException e) {
            LOG.error("HBase  master  未运行 。 ", e);
        } catch (ZooKeeperConnectionException e) {
            LOG.error("zooKeeper 连接异常。 ", e);
        } catch (IOException e) {
            LOG.error("", e);
        }
        return isExists;
    }


    /**
     * 删除表
     * @param tableName
     * @return
     */
    public static boolean deleteTable(String tableName){

        boolean status = false;
        TableName name = TableName.valueOf(tableName);
        try {
            Admin admin = conf.getHconnection().getAdmin();
            System.out.println(admin.tableExists(name));

            if(admin.tableExists(name)){
                System.out.println(!admin.isTableDisabled(name));
                if(!admin.isTableDisabled(name)){
                    System.out.println("禁用表");
                    admin.disableTable(name);
                }
                System.out.println("删除表");
                admin.deleteTable(name);
            }else{
                LOG.warn(" HBase中不存在 表 " + tableName);
            }
            admin.close();
            status = true;

        } catch (MasterNotRunningException e) {
            LOG.error("HBase  master  未运行 。 ", e);
        } catch (ZooKeeperConnectionException e) {
            LOG.error("zooKeeper 连接异常。 ", e);
        } catch (IOException e) {
            LOG.error("", e);
        }
        return status;
    }


    /**
     * 清空表
     * @param tableName
     * @return
     */
    public static boolean truncateTable(String tableName){

        boolean status = false;
        TableName name = TableName.valueOf(tableName);

        try {
            Admin admin = conf.getHconnection().getAdmin();

            if(admin.tableExists(name)){

                if(admin.isTableAvailable(name)){
                    admin.disableTable(name);
                }
                admin.truncateTable(name, true);

            }else{
                LOG.warn(" HBase中不存在 表 " + tableName);
            }
            admin.close();
            status = true;

        } catch (MasterNotRunningException e) {
            LOG.error("HBase  master  未运行 。 ", e);
        } catch (ZooKeeperConnectionException e) {
            LOG.error("zooKeeper 连接异常。 ", e);
        } catch (IOException e) {
            LOG.error("", e);
        }
        return status;
    }

    /**
     * description:   创建HBase表
     * @param tableName
     * @param cf       列族名
     * @param inMemory
     * @param ttl      ttl < 0     则为永久保存
     */
    public static boolean createTable(String tableName, String cf, boolean inMemory, int ttl, int maxVersion){

        HTableDescriptor htd = createHTableDescriptor(tableName, cf, inMemory, ttl, maxVersion, COPROCESSORCLASSNAME);

        return createTable(htd);

    }

    public static boolean createTable(String tableName, String cf, boolean inMemory, int ttl, int maxVersion,  boolean useSNAPPY){


        HTableDescriptor htd = createHTableDescriptor(tableName, cf, inMemory, ttl, maxVersion, useSNAPPY , COPROCESSORCLASSNAME);

        return createTable(htd);

    }

    public static boolean createTable(String tableName, String cf, boolean inMemory, int ttl, int maxVersion,  boolean useSNAPPY, byte[][] splits){


        HTableDescriptor htd = createHTableDescriptor(tableName, cf, inMemory, ttl, maxVersion, useSNAPPY, COPROCESSORCLASSNAME);
        return createTable(htd , splits);

    }

    /**
     *
     * @param tableName    表名
     * @param cf           列簇
     * @param inMemory     是否存在内存
     * @param ttl          数据过期时间
     * @param maxVersion   最大版本
     * @param splits       分区
     * @return
     */
    public static boolean createTable(String tableName,
                                      String cf,
                                      boolean inMemory,
                                      int ttl,
                                      int maxVersion,
                                      byte[][] splits){

        //创建表描述类  设置表的结构和参数
        HTableDescriptor htd = createHTableDescriptor(tableName, cf, inMemory, ttl, maxVersion, COPROCESSORCLASSNAME);
        //通过HTableDescriptor 和 splits 分区策略来定义表
        return createTable(htd , splits);

    }

    public static List<String> listTables(){

        List<String> list = new ArrayList<String>();
        Admin admin = null;

        try {
            admin = conf.getHconnection().getAdmin();
            TableName[] listTableNames = admin.listTableNames();
            for( TableName t :  listTableNames ){
                list.add( t.getNameAsString() );
            }

        } catch(IOException e )  {
            LOG.error("创建HBase表失败。", e);
        }finally{
            try {
                if(admin!=null){
                    admin.close();
                }
            } catch (IOException e) {
                LOG.error("", e);
            }
        }

        return list;
    }

    /**
     * 列出所有表
     * @param reg
     * @return
     */
    public static List<String> listTables(String reg){

        List<String> list = new ArrayList<String>();
        Admin admin = null;

        try {
            admin = conf.getHconnection().getAdmin();
            TableName[] listTableNames = admin.listTableNames(reg);
            for(TableName t :  listTableNames){
                list.add(t.getNameAsString());
            }

        } catch(IOException e)  {
            LOG.error("创建HBase表失败。", e);
        }finally{
            try {
                if(admin!=null){
                    admin.close();
                }
            } catch (IOException e) {
                LOG.error("", e);
            }
        }

        return list;
    }


    /**
     * description:   创建HBase表
     * @param tableName
     * @param cf       列族名
     * @param inMemory
     * @param ttl      ttl < 0     则为永久保存
     */
    public static boolean  createTable(String tableName, String cf, boolean inMemory, int ttl , int maxVersion, String ... coprocessorClassNames){

        HTableDescriptor htd = createHTableDescriptor(tableName, cf, inMemory, ttl, maxVersion, coprocessorClassNames);
        return createTable(htd);
    }

    public static boolean  createTable( String tableName, String cf, boolean inMemory, int ttl, int maxVersion, boolean useSNAPPY, String ... coprocessorClassNames){

        HTableDescriptor htd = createHTableDescriptor(tableName, cf, inMemory, ttl, maxVersion, useSNAPPY, coprocessorClassNames);
        return createTable(htd);
    }

    public static boolean  createTable( String tableName,String cf,boolean inMemory, int ttl ,int maxVersion ,  boolean useSNAPPY ,byte[][] splits, String ... coprocessorClassNames){

        HTableDescriptor htd = createHTableDescriptor(tableName, cf, inMemory, ttl, maxVersion, useSNAPPY ,coprocessorClassNames);
        return createTable(htd,splits );
    }
    public static boolean  createTable(String tableName, String cf, boolean inMemory, int ttl, int maxVersion, byte[][] splits, String ... coprocessorClassNames){

        HTableDescriptor htd = createHTableDescriptor(tableName, cf, inMemory, ttl, maxVersion, coprocessorClassNames);
        return createTable(htd,splits );
    }


    /**
     * 通过HTableDescriptor 和 分区 来构建hbase
     * @param htd
     * @param splits
     * @return
     */
    public static boolean createTable(HTableDescriptor htd, byte[][] splits){

        Admin admin = null;

        try {
            //获取admin
            admin = conf.getHconnection().getAdmin();
            TableName tableName = htd.getTableName();
            boolean exist = admin.tableExists(tableName);
            if(exist){
                LOG.info("表"+tableName.getNameAsString() + "已经存在");
            }else{
                //使用Admin进行创建表
                //admin.createTable(htd, splits);
                admin.createTable(htd);
                LOG.info("表"+tableName.getNameAsString() + "创建成功");
            }
        } catch(IOException e )  {
            LOG.error("创建HBase表失败。", e);
            return false;
        }finally{
            try {
                if(admin!=null){
                    admin.close();
                }
            } catch (IOException e) {
                LOG.error("", e);
            }
        }
        return true;
    }

    public static boolean createTable(HTableDescriptor htd){

        Admin admin = null;

        try {
            admin = conf.getHconnection().getAdmin();
            if(admin.tableExists(htd.getTableName())){
                LOG.info("表" + htd.getTableName() + "已经存在");
            }else{
                admin.createTable(htd);
            }
        } catch(IOException e )  {
            LOG.error("创建HBase表失败。", e);
            return false;
        }finally{
            try {
                if(admin!=null){
                    admin.close();
                }
            } catch (IOException e) {
                LOG.error("", e);
            }
        }
        return true;
    }


    /**
     * 创建命名空间
     * @param nameSpace
     * @return
     */
    public static boolean createNameSpace(String nameSpace){

        Admin admin = null;
        try {
            admin = conf.getHconnection().getAdmin();
            //获取所有的命名空间
            NamespaceDescriptor[] listNamespaceDescriptors = admin.listNamespaceDescriptors();
            boolean exist = false;
            for(NamespaceDescriptor namespaceDescriptor : listNamespaceDescriptors){
                if(namespaceDescriptor.getName().equals(nameSpace)){
                    exist = true;
                }
            }
            if(!exist) admin.createNamespace(NamespaceDescriptor.create(nameSpace).build());
        } catch(IOException e )  {
            LOG.error("创建HBase命名空间失败。", e);
            return false;
        }finally{
            try {
                if(admin!=null){
                    admin.close();
                }
            } catch (IOException e) {
                LOG.error("", e);
            }
        }
        return true;
    }

    /**
     * description:  为 HBase中的表  tableName添加 协处理器  coprocessorClassName
     * @param tableName
     * @param coprocessorClassName    必须是已经存在与HBase集群中
     * @return  boolean
     */
    public static boolean addCoprocessorClassForTable(String tableName,String coprocessorClassName){

        boolean status = false;
        TableName name = TableName.valueOf(tableName);
        Admin admin = null;
        try {
            admin = conf.getHconnection().getAdmin();
            HTableDescriptor htd = admin.getTableDescriptor(name);
            if(!htd.hasCoprocessor(coprocessorClassName)){

                htd.addCoprocessor(coprocessorClassName);

                admin.disableTable(name);
                admin.modifyTable(name, htd);
                admin.enableTable(name);
            }else{
                LOG.warn(String.format("表 %s中已经存在协处理器%s", tableName, coprocessorClassName));
            }
            status = true;

        } catch (MasterNotRunningException e) {
            LOG.error("HBase  master  未运行 。 ", e);
        } catch (ZooKeeperConnectionException e) {
            LOG.error("zooKeeper 连接异常。 ", e);
        } catch (IOException e) {
            LOG.error("", e);
        }finally{
            try {
                if(admin!=null){
                    admin.close();
                }
            } catch (IOException e) {
                LOG.error("", e);
            }
        }

        return status;
    }

    /**
     * description:  为 HBase中的表  tableName添加  指定位置的 协处理器 jar
     * @param tableName
     * @param coprocessorClassName   jar中的具体的协处理器
     * @param jarPath     hdfs的路径
     * @param level       执行级别
     * @param kvs         运行参数    可以为 null
     * @return   boolean
     *
     */
    public static boolean addCoprocessorJarForTable(String  tableName, String coprocessorClassName,String jarPath,int level ,Map<String, String> kvs ){

        boolean status = false;
        TableName name = TableName.valueOf(tableName);
        Admin admin = null;
        try {

            admin = conf.getHconnection().getAdmin();
            HTableDescriptor htd = admin.getTableDescriptor(name);
            if(!htd.hasCoprocessor(coprocessorClassName)){
                admin.disableTable(name);
                htd.addCoprocessor(coprocessorClassName, new Path(jarPath), level, kvs);
                admin.modifyTable(name, htd);
                admin.enableTable(name);
            }else{
                LOG.warn(String.format("表 %s中已经存在协处理器%s", tableName, coprocessorClassName));
            }

            status = true;

        } catch (MasterNotRunningException e) {
            LOG.error("HBase  master  未运行 。 ", e);
        } catch (ZooKeeperConnectionException e) {
            LOG.error("zooKeeper 连接异常。 ", e);
        } catch (IOException e) {
            LOG.error("", e);
        }finally{
            try {
                if(admin!=null){
                    admin.close();
                }
            } catch (IOException e) {
                LOG.error("", e);
            }
        }

        return status;
    }

    /**
     *
     * @param tableName
     * @param cf
     * @param inMemory
     * @param ttl
     * @param maxVersion
     * @param coprocessorClassNames
     * @return
     */
    public static HTableDescriptor createHTableDescriptor( String tableName,String cf,boolean inMemory, int ttl ,int maxVersion ,String ... coprocessorClassNames ){

        return createHTableDescriptor(tableName, cf, inMemory, ttl, maxVersion, true , COPROCESSORCLASSNAME);
    }

    /**
     *  创建表描述类，通过此类设置hbase相关参数
     * @param tableName
     * @param cf
     * @param inMemory
     * @param ttl
     * @param maxVersion
     * @param useSNAPPY
     * @param coprocessorClassNames
     * @return
     */
    public static HTableDescriptor createHTableDescriptor( String tableName,String cf,boolean inMemory, int ttl ,int maxVersion , boolean useSNAPPY , String ... coprocessorClassNames ){


        // 1.创建命名空间
        String[] split = tableName.split(":");
        if(split.length==2){
            createNameSpace(split[0]);
        }

        // 2.表描述
        HTableDescriptor htd = new HTableDescriptor(TableName.valueOf(tableName));
  /*      for( String coprocessorClassName : coprocessorClassNames ){

            try {
                htd.addCoprocessor(coprocessorClassName);
            } catch (IOException e1) {
                LOG.error("为表" + tableName + " 添加协处理器失败。 ", e1);
            }
        }
*/

        // 创建HColumnDescriptor，范围更小了
        HColumnDescriptor hcd = new HColumnDescriptor(cf);
        if( maxVersion > 0 )
            //定义最大版本号
            hcd.setMaxVersions(maxVersion);

            /**
         * 设置布隆过滤器
         * 默认是NONE 是否使用布隆过虑及使用何种方式
         * 布隆过滤可以每列族单独启用
         * Default = ROW 对行进行布隆过滤。
         * 对 ROW，行键的哈希在每次插入行时将被添加到布隆。
         * 对 ROWCOL，行键 + 列族 + 列族修饰的哈希将在每次插入行时添加到布隆
         * 使用方法: create ‘table’,{BLOOMFILTER =>’ROW’}
         * 启用布隆过滤可以节省读磁盘过程，可以有助于降低读取延迟
         */

        hcd.setBloomFilterType(BloomType.ROWCOL);

        /**
         * hbase在LRU缓存基础之上采用了分层设计，整个blockcache分成了三个部分，分别是single、multi和inMemory。三者区别如下：
         * single：如果一个block第一次被访问，放在该优先队列中；
         * multi：如果一个block被多次访问，则从single队列转移到multi队列
         * inMemory：优先级最高，常驻cache，因此一般只有hbase系统的元数据，如meta表之类的才会放到inMemory队列中。普通的hbase列族也可以指定IN_MEMORY属性，方法如下：
         * create 'table', {NAME => 'f', IN_MEMORY => true}
         * 修改上表的inmemory属性，方法如下：
         * alter 'table',{NAME=>'f',IN_MEMORY=>true}
         */
        hcd.setInMemory(inMemory);
        hcd.setScope(1);

		/** 数据量大，边压边写也会提升性能的，毕竟IO是大数据的最严重的瓶颈，
		哪怕使用了SSD也是一样。众多的压缩方式中，推荐使用SNAPPY。从压缩率和压缩速度来看，
		性价比最高。  **/
        if(useSNAPPY)hcd.setCompressionType(Compression.Algorithm.SNAPPY);

        //默认为NONE
        //如果数据存储时设置了编码， 在缓存到内存中的时候是不会解码的，这样和不编码的情况相比，相同的数据块，编码后占用的内存更小， 即提高了内存的使用率
        //如果设置了编码，用户必须在取数据的时候进行解码， 因此在内存充足的情况下会降低读写性能。
        //在任何情况下开启PREFIX_TREE编码都是安全的
        //不要同时开启PREFIX_TREE和SNAPPY
        //通常情况下 SNAPPY并不能比 PREFIX_TREE取得更好的优化效果
        //hcd.setDataBlockEncoding(DataBlockEncoding.PREFIX_TREE);

        //默认为64k     65536
        //随着blocksize的增大， 系统随机读的吞吐量不断的降低，延迟也不断的增大，
        //64k大小比16k大小的吞吐量大约下降13%，延迟增大13%
        //128k大小比64k大小的吞吐量大约下降22%，延迟增大27%
        //对于随机读取为主的业务，可以考虑调低blocksize的大小

        //随着blocksize的增大， scan的吞吐量不断的增大，延迟也不断降低，
        //64k大小比16k大小的吞吐量大约增加33%，延迟降低24%
        //128k大小比64k大小的吞吐量大约增加7%，延迟降低7%
        //对于scan为主的业务，可以考虑调大blocksize的大小

        //如果业务请求以Get为主，则可以适当的减小blocksize的大小
        //如果业务是以scan请求为主，则可以适当的增大blocksize的大小
        //系统默认为64k, 是一个scan和get之间取的平衡值
        //hcd.setBlocksize(s)


        //设置表中数据的存储生命期，过期数据将自动被删除，
        // 例如如果只需要存储最近两天的数据，
        // 那么可以设置setTimeToLive(2 * 24 * 60 * 60)
        if( ttl < 0 ) ttl = HConstants.FOREVER;
        hcd.setTimeToLive(ttl);
        htd.addFamily( hcd);
        return htd;
    }


    public static boolean createTable(HBaseTableParam param){

        String nameSpace = param.getNameSpace();
        if(!"default".equalsIgnoreCase(nameSpace)){
            checkArgument(createNameSpace(nameSpace), String.format("创建命名空间%s失败。", nameSpace));
        }

        HTableDescriptor desc = createHTableDescriptor(param);
        byte[][] splits = param.getSplits();
        if(splits == null){
            return createTable(desc);
        }else{
            return createTable(desc, splits);
        }

    }


    public static HTableDescriptor createHTableDescriptor(HBaseTableParam param){

        String tableName = String.format("%s:%s", param.getNameSpace(), param.getTableName());
        HTableDescriptor htd = new HTableDescriptor(TableName.valueOf(tableName));

        for(String coprocessorClassName : param.getCoprocessorClazz()){
            try {
                htd.addCoprocessor(coprocessorClassName);
            } catch (IOException e) {
                LOG.error(String.format("为表  %s 添加协处理器失败。", tableName), e);
            }
        }

        HColumnDescriptor hcd = new HColumnDescriptor(param.getCf());
        hcd.setBloomFilterType(param.getBloomType());
        hcd.setMaxVersions(param.getMaxVersions());
        hcd.setScope(param.getReplicationScope());
        hcd.setBlocksize(param.getBlocksize());
        hcd.setInMemory(param.isInMemory());
        hcd.setTimeToLive(param.getTtl());

		/* 数据量大，边压边写也会提升性能的，毕竟IO是大数据的最严重的瓶颈，哪怕使用了SSD也是一样。众多的压缩方式中，推荐使用SNAPPY。从压缩率和压缩速度来看，性价比最高。  */
       // if(param.isUsePrefix_tree())hcd.setDataBlockEncoding(DataBlockEncoding.PREFIX_TREE);
        if(param.isUseSnappy())hcd.setCompressionType(Compression.Algorithm.SNAPPY);

        htd.addFamily( hcd);

        return htd;
    }


    public static void closeTable( Table table ){

        if( table != null ){
            try {
                table.close();
            } catch (IOException e) {
                LOG.error(" ", e);
            }
            table = null;
        }
    }


    public static byte[][] getSplitKeys() {
        //String[] keys = new String[]{"50|"};
        //String[] keys = new String[]{"25|","50|","75|"};
        //String[] keys = new String[]{"13|","26|","39|", "52|","65|","78|","90|"};
        String[] keys = new String[]{ "06|","13|","20|", "26|","33|", "39|","46|", "52|","58|", "65|","72|","78|", "84|","90|","95|"};
        //String[] keys = new String[]{"10|", "20|", "30|", "40|", "50|", "60|", "70|", "80|", "90|"};
        byte[][] splitKeys = new byte[keys.length][];
        TreeSet<byte[]> rows = new TreeSet<byte[]>(Bytes.BYTES_COMPARATOR);//升序排序
        for (int i = 0; i < keys.length; i++) {
            rows.add(Bytes.toBytes(keys[i]));
        }
        Iterator<byte[]> rowKeyIter = rows.iterator();
        int i = 0;
        while (rowKeyIter.hasNext()) {
        byte[] tempRow = rowKeyIter.next();
        rowKeyIter.remove();
        splitKeys[i] = tempRow;
        i++;
    }
        return splitKeys;
}


    public static class HBaseTableParam{

        private final String nameSpace; //命名空间
        private final String tableName; //表名
        private final String cf;        //列簇
        private Set<String>  coprocessorClazz = Sets.newHashSet("org.apache.hadoop.hbase.coprocessor.AggregateImplementation");
        private int maxVersions = 1;    //版本号 默认为1
        private BloomType bloomType = BloomType.ROWCOL;
        private boolean inMemory = false;
        private int replicationScope = 1;
        private boolean useSnappy = false; //默认不使用压缩
        private boolean usePrefix_tree = false;
        private int blocksize = 65536;
        private int ttl = HConstants.FOREVER;

        private byte[][] splits;

        public HBaseTableParam(String nameSpace, String tableName, String cf) {
            super();
            this.nameSpace = nameSpace == null ? "default" : nameSpace;
            this.tableName = tableName;
            this.cf = cf;
        }

        public String getNameSpace() {
            return nameSpace;
        }

        public String getTableName() {
            return tableName;
        }

        public String getCf() {
            return cf;
        }

        public Set<String> getCoprocessorClazz() {
            return coprocessorClazz;
        }

        public void clearCoprocessor(){
            coprocessorClazz.clear();
        }
        public void addCoprocessorClazz(String clazz) {
            this.coprocessorClazz.add(clazz);
        }

        public void addCoprocessorClazz(String ... clazz) {
            addCoprocessorClazz(Arrays.asList(clazz));
        }

        public void addCoprocessorClazz(Collection<String>  clazz) {
            this.coprocessorClazz.addAll(clazz);
        }

        public int getMaxVersions() {
            return maxVersions;
        }

        public void setMaxVersions(int maxVersions) {
            this.maxVersions = maxVersions <= 0 ? 1 : maxVersions;
        }

        public BloomType getBloomType() {
            return bloomType;
        }

        public void setBloomType(BloomType bloomType) {
            this.bloomType = bloomType == null ? BloomType.ROWCOL : bloomType;
        }

        public boolean isInMemory() {
            return inMemory;
        }

        public void setInMemory(boolean inMemory) {
            this.inMemory = inMemory;
        }

        public int getReplicationScope() {
            return replicationScope;
        }

        public void setReplicationScope(int replicationScope) {
            this.replicationScope = replicationScope < 0 ? 1 : replicationScope;
        }

        public boolean isUseSnappy() {
            return useSnappy;
        }

        /**
         * description:
         *   控制是否使用 snappy 压缩数据， 默认是不启用
         * @param useSnappy
         *
         */
        public void setUseSnappy(boolean useSnappy) {
            this.useSnappy = useSnappy;
        }

        public boolean isUsePrefix_tree() {
            return usePrefix_tree;
        }

        /**
         * description:
         * 控制是否使用数据编码  ， 默认是不使用
         *
         * 如果数据存储时设置了编码， 在缓存到内存中的时候是不会解码的，这样和不编码的情况相比，相同的数据块，编码后占用的内存更小， 即提高了内存的使用率
         * 如果设置了编码，用户必须在取数据的时候进行解码， 因此在内存充足的情况下会降低读写性能。
         * 在任何情况下开启PREFIX_TREE编码都是安全的
         * 不要同时开启PREFIX_TREE和SNAPPY
         * 通常情况下 SNAPPY并不能比 PREFIX_TREE取得更好的优化效果
         */
        public void setUsePrefix_tree(boolean usePrefix_tree) {
            this.usePrefix_tree = usePrefix_tree;
        }

        public int getBlocksize() {
            return blocksize;
        }

        /**
         * description:
         *
         *默认为64k     65536
         *随着blocksize的增大， 系统随机读的吞吐量不断的降低，延迟也不断的增大，
         *64k大小比16k大小的吞吐量大约下降13%，延迟增大13%
         *128k大小比64k大小的吞吐量大约下降22%，延迟增大27%
         *对于随机读取为主的业务，可以考虑调低blocksize的大小

         *随着blocksize的增大， scan的吞吐量不断的增大，延迟也不断降低，
         *64k大小比16k大小的吞吐量大约增加33%，延迟降低24%
         *128k大小比64k大小的吞吐量大约增加7%，延迟降低7%
         *对于scan为主的业务，可以考虑调大blocksize的大小

         *如果业务请求以Get为主，则可以适当的减小blocksize的大小
         *如果业务是以scan请求为主，则可以适当的增大blocksize的大小
         *系统默认为64k, 是一个scan和get之间取的平衡值
         *
         */
        public void setBlocksize(int blocksize) {
            this.blocksize = blocksize <= 0 ? 65536 : blocksize;
        }

        public int getTtl() {
            return ttl;
        }

        /**
         * description:
         *
         *    默认是永久保存 ，
         *
         * @param ttl                大于 零的整数    ，  <= 0 ? tt 为  永久保存
         */
        public void setTtl(int ttl) {
            this.ttl = ttl <= 0 ? HConstants.FOREVER : ttl;
        }

        public byte[][] getSplits() {
            return splits;
        }


/*        *//**
         * description:
         * 预分区的rowKey范围配置
         * @param splits
         *//*
        public void setSplits(byte[][] splits) {
            this.splits = splits;
        }*/

    }




    public static void main(String[] args) throws Exception{
        Admin admin = conf.getHconnection().getAdmin();
        System.out.println(deleteTable("bbb:aaaaa"));

//        HBaseTableUtil.deleteTable("tanslator");
//        HBaseTableUtil.deleteTable("ability");
//        HBaseTableUtil.deleteTable("task");
//        HBaseTableUtil.deleteTable("paper");

        //  HbaseSearchService hbaseSearchService=new HbaseSearchService();
        //  Map<String, String> stringStringMap = hbaseSearchService.get("countform:bsid","", new BaseMapRowExtrator());
        // Map<String, String> aaaaa = hbaseSearchService.get("countform:bsid", "aaaaa", new BaseMapRowExtrator());
        // System.out.println(aaaaa);
    }
}
