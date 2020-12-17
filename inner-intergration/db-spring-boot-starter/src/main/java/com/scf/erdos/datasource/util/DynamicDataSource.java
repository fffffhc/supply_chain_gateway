package com.scf.erdos.datasource.util;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.scf.erdos.datasource.constant.DataSourceKey;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Description : spring动态数据源（需要继承AbstractRoutingDataSource）
 * @author：bao-clm
 * @date: 2020/3/18
 * @version：1.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private Map<Object, Object> datasources;

    public DynamicDataSource() {
        datasources = new HashMap<>();

        super.setTargetDataSources(datasources);

    }

    public <T extends DataSource> void addDataSource(DataSourceKey key, T data) {
        datasources.put(key, data);
    }

    /**
     * 指定使用哪个数据源
     */
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getDataSourceKey();
    }

}