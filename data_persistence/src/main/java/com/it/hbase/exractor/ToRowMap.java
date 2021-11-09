package com.it.hbase.exractor;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 *         currentVersion 标识是否取多版本的数据，默认取当前版本
 *         对当前版本，返回row`#`qualifier->value的映射
 *         对多个版本，返回row`#`version`#`qualifier->value的映射
 */
public class ToRowMap implements RowExtractor<Map<String, String>> {

    private Boolean currentVersion;

    public ToRowMap() {
        this(true);
    }

    private ToRowMap(Boolean currentVersion) {
        this.currentVersion = currentVersion;
    }

    @Override
    public Map<String, String> extractRowData(Result result, int rowNum)
            throws IOException {
        if(result == null || result.isEmpty()) return null;

        final char HashTag = '#';

        HashMap<String, String> col2value = new HashMap<>();

        String rowKey = Bytes.toString(result.getRow());

        for (Cell cell : result.listCells()) {
            String value = Bytes.toString(cell.getValueArray(),
                    cell.getValueOffset(), cell.getValueLength());
            String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
            if (currentVersion)
                col2value.put(rowKey + HashTag + qualifier, value);
            else {
                long version = cell.getTimestamp();
                col2value.put(rowKey + HashTag + version + HashTag + qualifier, value);
            }
        }

        return col2value;
    }
}
