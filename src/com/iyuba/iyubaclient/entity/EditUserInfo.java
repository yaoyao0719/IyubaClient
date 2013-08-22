/**
 * 
 */
package com.iyuba.iyubaclient.entity;

/**
 * @author yao
 * 用户可编辑的参数
 */
public class EditUserInfo {
	private String edGender;//只能为1或2
	private int edBirthDay;//日，必须为数字
	private int edBirthYear;//年，必须为数字
	private int edBirthMonth;//月，必须为数字
	private String edResideCity;//出生市
	private String edResideDistrict;//出生区
	private String edZodiac;//生肖
	private String edConstellation;//星座
	public String getEdGender() {
		return edGender;
	}
	public void setEdGender(String edGender) {
		this.edGender = edGender;
	}
	public int getEdBirthDay() {
		return edBirthDay;
	}
	public void setEdBirthDay(int edBirthDay) {
		this.edBirthDay = edBirthDay;
	}
	public int getEdBirthYear() {
		return edBirthYear;
	}
	public void setEdBirthYear(int edBirthYear) {
		this.edBirthYear = edBirthYear;
	}
	public int getEdBirthMonth() {
		return edBirthMonth;
	}
	public void setEdBirthMonth(int edBirthMonth) {
		this.edBirthMonth = edBirthMonth;
	}
	public String getEdResideCity() {
		return edResideCity;
	}
	public void setEdResideCity(String edResideCity) {
		this.edResideCity = edResideCity;
	}
	public String getEdResideDistrict() {
		return edResideDistrict;
	}
	public void setEdResideDistrict(String edResideDistrict) {
		this.edResideDistrict = edResideDistrict;
	}
	public String getEdZodiac() {
		return edZodiac;
	}
	public void setEdZodiac(String edZodiac) {
		this.edZodiac = edZodiac;
	}
	public String getEdConstellation() {
		return edConstellation;
	}
	public void setEdConstellation(String edConstellation) {
		this.edConstellation = edConstellation;
	}
	
}
