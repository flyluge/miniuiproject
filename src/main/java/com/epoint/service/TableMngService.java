	/** 
	 * projectName: project 
	 * fileName: TableMngService.java 
	 * packageName: com.epoint.service 
	 * date: 2019年9月9日上午10:22:09 
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
import com.epoint.domain.Tableinfo;
import com.epoint.utils.FormValidation;
import com.epoint.utils.JDBCUtils;

/**   
 * @title: TableMngServiceimpl.java 
 * @package com.epoint.service.impl 
 * @description: 餐桌管理实现类
 * @author: luge
 * @date: 2019年9月9日 上午10:22:09 
 * @version: V1.0   
*/
public class TableMngService{
	
	private TableMngDao tabelMngDao=new TableMngDaoimpl();
	
	private ReserveMngDao reserveMngDao=new ReserveMngDaoimpl();
	/**   
	 * @title: findTableList
	 * @description: TODO
	 * @param holdnum
	 * @param isuse
	 * @param sortField
	 * @param sortOrder
	 * @param pageIndex
	 * @param pageSize
	 * @return   
	 * @see com.epoint.service.TableMngService#findTableList(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, int, int)     
	 */ 
	public List<Tableinfo> findTableList(Integer holdnum, Integer isuse, String sortField, String sortOrder,
			int pageIndex, int pageSize) {
		StringBuilder sql=new StringBuilder("select * from tableinfo where 1=1");
		//参数列表
		List<Object> list=new ArrayList<Object>();
		if(FormValidation.verifyNull(holdnum)) {
			sql.append(" and holdnum like ?");
			list.add("%"+holdnum+"%");
		}
		if(FormValidation.verifyNull(isuse)) {
			sql.append(" and isuse=?");
			list.add(isuse);
		}
		if(FormValidation.verifyNull(sortField,sortOrder)) {
			sql.append(" Order by "+sortField+" "+sortOrder);
		}
		List<Tableinfo> list2 = tabelMngDao.findByPageAndSql2(sql.toString(), pageIndex, pageSize, list);
		JDBCUtils.closeConnection(JDBCUtils.getDruidConnection());
		return list2;
	}

	/**   
	 * @title: findTotalCount
	 * @description: TODO
	 * @param holdnum
	 * @param isuse
	 * @return   
	 * @see com.epoint.service.TableMngService#findTotalCount(java.lang.Integer, java.lang.Integer)     
	 */ 
	public int findTotalCount(Integer holdnum, Integer isuse) {
		StringBuilder sql=new StringBuilder("select count(1) from tableinfo where 1=1");
		//参数列表
		List<Object> list=new ArrayList<Object>();
		if(FormValidation.verifyNull(holdnum)) {
			sql.append(" and holdnum like ?");
			list.add("%"+holdnum+"%");
		}
		if(FormValidation.verifyNull(isuse)) {
			sql.append(" and isuse=?");
			list.add(isuse);
		}
		long count = tabelMngDao.findAllCount2(sql.toString(), list);
		JDBCUtils.closeConnection(JDBCUtils.getDruidConnection());
		return (int)count;
	}

	/**   
	 * @title: deleteByIds
	 * @description: 删除多条数据
	 * @param ids
	 * @return   
	 * @see com.epoint.service.TableMngService#deleteByIds(java.lang.String[])     
	 */ 
	public String deleteByIds(String[] ids) {
		String msg="";
		try {
			JDBCUtils.startTransaction();
			for(int i=0;i<ids.length;i++) {
				String sql="select count(1) from reserveinfo where tableno=? and (reservestatus=1||reservestatus=0)";
				long count = reserveMngDao.findAllCount(sql, ids[i]);
				if(count!=0) {
					msg+=ids[i];
					JDBCUtils.rollBackTransaction();
					return msg;
				}
				tabelMngDao.delete(ids[i]); 
			}
			JDBCUtils.commitTransaction();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**   
	 * @title: getId
	 * @description: TODO
	 * @return   
	 * @see com.epoint.service.TableMngService#getId()     
	 */ 
	public String getId(Integer holdnum) {
		String sql="select max(tableno) tableno from tableinfo where holdnum = ?";
		Tableinfo t = tabelMngDao.findOneBySql(sql,holdnum);
		String maxid=null;
		if(t.getTableno()!=null) {
			String tn = t.getTableno();
			maxid=String.valueOf(Integer.parseInt(tn)+1);
		}else {
			maxid=holdnum.toString()+"01";
		}
		JDBCUtils.closeConnection(JDBCUtils.getDruidConnection());
		return maxid;
	}

	/**   
	 * @title: add
	 * @description: 添加
	 * @param tableinfo
	 * @return   
	 * @see com.epoint.service.TableMngService#add(com.epoint.domain.Tableinfo)     
	 */ 
	public boolean add(Tableinfo tableinfo) {
		try {
			JDBCUtils.startTransaction();
			tabelMngDao.add(tableinfo);
			JDBCUtils.commitTransaction();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
