package com.cmdi.yjs.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringUtils {
	public static String printStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	
	/**
	 * 首字母大写
	 * @param str
	 * @return
	 */
	public static String capitalise(String str){
		if(str==null){
			return null;
		}
		return str.substring(0,1).toUpperCase()+str.substring(1); 
	}
	
	/**
	 * 把数组按指定分割符号链接
	 * @param ints
	 * @return
	 */
	public static String join(Integer[] ints,String joinChar){
		if(ints==null||ints.length==0){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(Integer i:ints){
			sb.append(i);
			sb.append(joinChar);
		}
		return sb.toString().substring(0,sb.length()-joinChar.length());
	}
	
	/**
	 * 把 1,2,3,4转成int数组
	 */
	public static Integer[] parseArr(String intStr){
		if(intStr == null || intStr.length()==0){
			return null;
		}
		String[] strs = intStr.split("[,]");
		Integer[] iarr = new Integer[strs.length];
		for(int i=0;i<strs.length;i++){
			iarr[i] = Integer.valueOf(strs[i]);
		}
		return iarr;
	}
}
