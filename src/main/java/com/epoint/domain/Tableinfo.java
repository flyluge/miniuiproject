package com.epoint.domain ;
/**
 * @title: Tableinfo.java 
 * @package com.epoint.domain 
 * @description: 餐桌信息表实体类
 * @author: luge
 * @date: 2019年9月9日 上午10:10:35 
 * @version: V1.0
 */
public class Tableinfo {
	private String tableno;
	private Integer holdnum;
	private Integer isuse;
	private String notes;
	
	public String getTableno() {
		return tableno;
	}
	public void setTableno(String tableno) {
		this.tableno = tableno;
	}
	public Integer getHoldnum() {
		return holdnum;
	}
	public void setHoldnum(Integer holdnum) {
		this.holdnum = holdnum;
	}
	public Integer getIsuse() {
		return isuse;
	}
	public void setIsuse(Integer isuse) {
		this.isuse = isuse;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	@Override
	public String toString() {
		return "Tableinfo [tableno=" + tableno + ", holdnum=" + holdnum + ", isuse=" + isuse + ", notes=" + notes + "]";
	}
}
