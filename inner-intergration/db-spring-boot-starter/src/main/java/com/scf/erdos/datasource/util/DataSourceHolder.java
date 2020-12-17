package com.scf.erdos.datasource.util;

import com.scf.erdos.datasource.constant.DataSourceKey;

/**
 * @Description : 动态数据源存放对象（当先执行线程-ThreadLocal），保存key值，用于动态切换使用
 * @author：bao-clm
 * @date: 2020/3/18
 * @version：1.0
 */
public class DataSourceHolder {

	//注意使用ThreadLocal，微服务下游建议使用信号量
    private static final ThreadLocal<DataSourceKey> dataSourceKey = new ThreadLocal<>();

    //得到当前的数据库连接
    public static DataSourceKey getDataSourceKey() {
        return dataSourceKey.get();
    }
    //设置当前的数据库连接
    public static void setDataSourceKey(DataSourceKey type) {
        dataSourceKey.set(type);
    }
    //清除当前的数据库连接
    public static void clearDataSourceKey() {
        dataSourceKey.remove();
    }

}