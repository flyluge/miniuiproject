/** 
 * projectName: project 
 * fileName: ReserveMngService.java 
 * packageName: com.epoint.service 
 * date: 2019年9月9日上午10:26:06 
 * copyright(c)  LUGE
 */
package com.epoint.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epoint.dao.ReserveMngDao;
import com.epoint.dao.TableMngDao;
import com.epoint.dao.impl.ReserveMngDaoimpl;
import com.epoint.dao.impl.TableMngDaoimpl;
import com.epoint.domain.Reserveinfo;
import com.epoint.domain.Tableinfo;
import com.epoint.utils.FormValidation;
import com.epoint.utils.JDBCUtils;

/**   
 * @title: ReverseMngServiceimpl.java 
 * @package com.epoint.service.impl 
 * @description: 预约管理Service实现类
 * @author: luge
 * @date: 2019年9月9日 上午10:26:06 
 * @version: V1.0   
*/
public class ReserveMngService{
	
	private ReserveMngDao reserveMngDao=new ReserveMngDaoimpl();
	private TableMngDao tableMngDao=new TableMngDaoimpl();
	/**   
	 * @title: findStuMarkList
	 * @description: TODO
	 * @param sortField
	 * @param sortOrder
	 * @param pageIndex
	 * @param pageSize
	 * @return   
	 * @see com.epoint.service.ReserveMngService#findStuMarkList(java.lang.String, java.lang.String, int, int)     
	 */ 
	public List<Reserveinfo> findReserveList(String sortField, String sortOrder, int pageIndex, int pageSize) {
		StringBuilder sql=new StringBuilder("select * from reserveinfo");
		List<Object> list=new ArrayList<Object>();
		if(FormValidation.verifyNull(sortField,sortOrder)) {
			sql.append(" order by "+sortField+" "+sortOrder);
		}
		List<Reserveinfo> list2 = reserveMngDao.findByPageAndSql2(sql.toString(), pageIndex, pageSize, list);
		JDBCUtils.closeConnection(JDBCUtils.getDruidConnection());
		return list2;
	}
	/**   
	 * @title: findCount
	 * @description: TODO
	 * @return   
	 * @see com.epoint.service.ReserveMngService#findCount()     
	 */ 
	public int findCount() {
		int count=(int) reserveMngDao.findAllCount();
		JDBCUtils.closeConnection(JDBCUtils.getDruidConnection());
		return count;
	}
	/**   
	 * @title: deleteByIds
	 * @description: TODO
	 * @param ids
	 * @return   
	 * @see com.epoint.service.ReserveMngService#deleteByIds(java.lang.String[])     
	 */ 
	public boolean deleteByIds(String[] ids) {
		try {
			JDBCUtils.startTransaction();
			for(int i=0;i<ids.length;i++) {
				Reserveinfo r = reserveMngDao.findById(ids[i]);
				Tableinfo t = tableMngDao.findById(r.getTableno());
				t.setIsuse(0);
				tableMngDao.update(t);
				reserveMngDao.delete(ids[i]); 
			}
			JDBCUtils.commitTransaction();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**   
	 * @title: findTableno
	 * @description: TODO
	 * @param peoplenum
	 * @return   
	 * @see com.epoint.service.ReserveMngService#findTableno(java.lang.Integer)     
	 */ 
	public List<Tableinfo> findTableno(Integer peoplenum) {
		int num=2;
		if(peoplenum<=2) {
			num=2;
		}else if(peoplenum>2&&peoplenum<=4) {
			num=4;
		}else if(peoplenum<=6&&peoplenum>4) {
			num=6;
		}
		String sql="select tableno from tableinfo where holdnum>=? and isuse=0 order by holdnum asc";
		List<Tableinfo> tls = tableMngDao.findAllBySql(sql, num);
		JDBCUtils.closeConnection(JDBCUtils.getDruidConnection());
		return tls;
	}
	/**   
	   * 添加操作 若预约时间冲突 返回false  
	 * @param r
	 * @return   
	 * @see com.epoint.service.ReserveMngService#add(java.lang.String, java.lang.String, com.epoint.domain.Reserveinfo)     
	 */ 
	public boolean add(Reserveinfo r) {
		boolean flag=false;
		try {
			JDBCUtils.startTransaction();
			String sql="select * from reserveinfo where ((? between starttime and endtime) or (? between starttime and endtime) or(? <=starttime and ? >=endtime)) and tableno=?";
			List<Tableinfo> l = tableMngDao.findAllBySql(sql, r.getStarttime(),r.getEndtime(),r.getStarttime(),r.getEndtime(),r.getTableno());
			if(l==null||l.size()==0) {
				flag=reserveMngDao.add(r);
				JDBCUtils.commitTransaction();
			}else {
				JDBCUtils.rollBackTransaction();
				flag=false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return flag;
	}
	/**
	  * 通过id获取Reserveinfo
	 *@author: luge
	 *@date: 2019年9月10日 上午10:05:57
	 *@param mngService
	 *@return
	 *@throws: 
	 */ 
	public Reserveinfo findById(String reserveno) {
		Reserveinfo r = reserveMngDao.findById(reserveno);
		JDBCUtils.closeConnection(JDBCUtils.getDruidConnection());
		return r;
	}
	/**
	 * 更新
	 *@author: luge
	 *@date: 2019年9月10日 上午10:29:59
	 *@param r
	 *@return
	 *@throws: 
	 */ 
	public boolean update(Reserveinfo r) {
		boolean flag=false;
		try {
			JDBCUtils.startTransaction();
			reserveMngDao.update(r);
			if(r.getReservestatus()==1) {
				Tableinfo t = tableMngDao.findById(r.getTableno());
				t.setIsuse(1);
				tableMngDao.update(t);
			}
			JDBCUtils.commitTransaction();
			flag=true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return flag;
	} 

}
