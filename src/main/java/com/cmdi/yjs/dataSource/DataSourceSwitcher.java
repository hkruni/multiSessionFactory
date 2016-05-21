package com.cmdi.yjs.dataSource;


/**
 * 数据源选择器
 * 
 * @author liuyu
 */
public class DataSourceSwitcher {
	@SuppressWarnings("rawtypes")
	private static final ThreadLocal contextHolder = new ThreadLocal();

	@SuppressWarnings("unchecked")
	public static void setDataSource(String dataSource) {
		System.out.println("setDataSource " + dataSource);
		contextHolder.set(dataSource);
	}

	public static void setWrite(){
		System.out.println("setWrite");
		clearDataSource();
    }
	
	public static void setRead() {
		System.out.println("setRead");
		setDataSource("read");
	}
	
	public static String getDataSource() {
		System.out.println("getDataSource");
		String dataSource =  (String) contextHolder.get();
		System.out.println(dataSource);
		
		return dataSource;
	}

	public static void clearDataSource() {
		System.out.println("clearDataSource");
		contextHolder.remove();
	}
}

