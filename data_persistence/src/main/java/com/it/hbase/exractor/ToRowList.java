package com.it.hbase.exractor;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Hbase数据库中数据提取接口实现：
 * 提取result的rowKey，和每个cell的值作为一行数据，
 * 一个cell=(row, family:qualifier:value, version)
 *
 * @author
 *         <p>
 *         每行数据的格式为：{rowKey column${separator}value column${separator}value ...}
 *         其中，不同的列之间用空格分隔，同样列元素的描述符与值之间用${separator}分隔
 */
public class ToRowList implements RowExtractor<List<String>> {

    private Boolean currentVersion; //currentVersion为true:只取当前最新版本，false:取所有版本
    private char separator; //不同元素之间拼接时的分隔符，默认为`#`

    private ToRowList(Boolean currentVersion, char separator) {
        this.separator = separator;
        this.currentVersion = currentVersion;
    }

    public ToRowList(Boolean currentVersion) {
        this(currentVersion, '#');
    }

    public ToRowList() {
        this(true, '#');
    }

    /**
      * 对{当前版本}存放在list[0] = {rowKey` `column`#`value` `column`#`value ...}
      * 多版本的时候list({rowKey`#`version1` `column`#`value` `column`#`value ...},
      *     {rowKey`#`version2` `column`#`value` `column`#`value ...})
      *
      */
    @Override
    public List<String> extractRowData(Result result, int rowNum) throws IOException {
        if(result == null || result.isEmpty()) return null;

        final char SPACE = ' ';

        List<String> rows = new LinkedList<>();

        String rowKey = Bytes.toString(result.getRow()); //一个result是同一个rowKey的所有cells集合

        StringBuilder row = new StringBuilder(); //build rowKey` `column`#`value` `column`#`value ...
        row.append(rowKey).append(SPACE);

        Map<Long, String> version2qualifiersAndValues = new HashMap<>(); //用于处理不同版本的映射

        List<Cell> cells = result.listCells();
        for (Cell cell : cells) {
            String value = Bytes.toString(cell.getValueArray(),
                    cell.getValueOffset(), cell.getValueLength());
            String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));

            if (currentVersion) {
                row.append(qualifier).append(separator).append(value).append(SPACE);
            } else {
                Long version = cell.getTimestamp();
                String tmp = version2qualifiersAndValues.get(version);
                version2qualifiersAndValues.put(version,
                        StringUtils.isNotBlank(tmp) ? tmp + " " + qualifier + separator + value
                                : rowKey + separator + version + " " + qualifier + separator + value);
            }
        }

        if (currentVersion) {
            rows.add(row.toString());
        } else {
            for (String v : version2qualifiersAndValues.values()) {
                rows.add(v);
            }
        }

        return rows;
    }
}
