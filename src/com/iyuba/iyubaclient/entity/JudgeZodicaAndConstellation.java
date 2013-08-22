/**
 * 
 */
package com.iyuba.iyubaclient.entity;

import java.util.Calendar;



/**
 * @author yao
 * 根据生日判定星座和生肖
 */
public class JudgeZodicaAndConstellation {
	public static final String[] zodiacArr = { "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊" };    
	   
	public static final String[] constellationArr = { "水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",    
	        "天蝎座", "射手座", "魔羯座" };    
	   
	public static final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };    
	   
	/**   
	 * 根据日期获取生肖   
	 * @return   
	 */   
	public static String date2Zodica(Calendar time) {    
	    return zodiacArr[time.get(Calendar.YEAR) % 12];    
	}    
	   
	/**   
	 * 根据日期获取星座   
	 * @param time   
	 * @return   
	 */   
	public static String date2Constellation(Calendar time) {    
	    int month = time.get(Calendar.MONTH);    
	    int day = time.get(Calendar.DAY_OF_MONTH);    
	    if (day < constellationEdgeDay[month]) {    
	        month = month - 1;    
	    }    
	    if (month >= 0) {    
	        return constellationArr[month];    
	    }    
	    //default to return 魔羯    
	    return constellationArr[11];    
	}   
	public static void main(String[] args){
		Calendar calendar=Calendar.getInstance();
		System.out.print("calendar=="+calendar);
		JudgeZodicaAndConstellation judgeZodicaAndConstellation=new JudgeZodicaAndConstellation();
		String aString=judgeZodicaAndConstellation.date2Constellation(calendar);
		String bString=judgeZodicaAndConstellation.date2Zodica(calendar);
		System.out.println(aString+"    "+bString);
	}
}
