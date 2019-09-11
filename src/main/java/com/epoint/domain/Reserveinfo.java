package com.epoint.domain ;
import java.util.Date;
/**
  * 预约信息表实体类
 * @title: Reserveinfo.java 
 * @package com.epoint.domain 
 * @author: luge
 * @date: 2019年9月9日 上午10:11:00 
 * @version: V1.0
 */
public class Reserveinfo {
	private String reserveno;
	private String tableno;
	private Integer peoplenum;
	private Date starttime;
	private Date endtime;
	private Integer reservestatus;
	private String notes;
	public String getReserveno() {
		return reserveno;
	}
	public void setReserveno(String reserveno) {
		this.reserveno = reserveno;
	}
	public String getTableno() {
		return tableno;
	}
	public void setTableno(String tableno) {
		this.tableno = tableno;
	}
	public Integer getPeoplenum() {
		return peoplenum;
	}
	public void setPeoplenum(Integer peoplenum) {
		this.peoplenum = peoplenum;
	}
	public Date getStarttime() {
		return starttime;
	}
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	public Integer getReservestatus() {
		return reservestatus;
	}
	public void setReservestatus(Integer reservestatus) {
		this.reservestatus = reservestatus;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	@Override
	public String toString() {
		return "Reserveinfo [reserveno=" + reserveno + ", tableno=" + tableno + ", peoplenum=" + peoplenum
				+ ", starttime=" + starttime + ", endtime=" + endtime + ", reservestatus=" + reservestatus + ", notes="
				+ notes + "]";
	}
	
}
