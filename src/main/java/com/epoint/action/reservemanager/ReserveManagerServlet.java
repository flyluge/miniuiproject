/** 
 * projectName: project 
 * fileName: ReverseManagerServlet.java 
 * packageName: com.epoint.controller 
 * date: 2019年9月9日上午10:25:09 
 * copyright(c)  LUGE
 */
package com.epoint.action.reservemanager;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import com.epoint.action.BaseServlet;
import com.epoint.domain.Reserveinfo;
import com.epoint.domain.Tableinfo;
import com.epoint.service.ReserveMngService;
import com.epoint.utils.FormValidation;

/**   
 * @title: ReverseManagerServlet.java 
 * @package com.epoint.controller 
 * @description: 预约管理Servlet
 * @author: luge
 * @date: 2019年9月9日 上午10:25:09 
 * @version: V1.0   
*/
public class ReserveManagerServlet extends BaseServlet{

	private static final long serialVersionUID = 1L;
	private ReserveMngService mngService=new ReserveMngService();
	/**
	 *@title: getReverseList 
	 *@description: 获取预约信息
	 *@author: luge
	 *@date: 2019年9月9日 下午2:35:11
	 *@param req
	 *@param resp
	 *@throws:
	 */
	public void getReverseList(HttpServletRequest req, HttpServletResponse resp) {
		// 排序方式
		String sortOrder = req.getParameter("sortOrder");
		// 排序字段
		String sortField = req.getParameter("sortField");
		// 获取分页数据
		int pageIndex = Integer.parseInt(req.getParameter("pageIndex")) + 1;
		int pageSize = Integer.parseInt(req.getParameter("pageSize"));
		
		List<Reserveinfo> list=mngService.findReserveList(sortField,sortOrder,pageIndex,pageSize);
		int totalcount=mngService.findCount();
		this.miniuiWrite(true, list, totalcount, resp);
	}
	/**
	 *@title: deleteById 
	 *@description: 通过id删除
	 *@author: luge
	 *@date: 2019年9月9日 下午3:00:18
	 *@param req
	 *@param resp
	 *@throws:
	 */
	public void deleteById(HttpServletRequest req, HttpServletResponse resp) {
		String id=req.getParameter("reserveno");
		String[] ids=id.split(",");
		if(mngService.deleteByIds(ids)) {
			this.write(true, "删除成功", resp);
		}else {
			this.write(false, "删除失败", resp);
		}
	}
	/**
	 *@title: getId 
	 *@description: 获取主码
	 *@author: luge
	 *@date: 2019年9月9日 下午3:16:24
	 *@param req
	 *@param resp
	 *@throws:
	 */
	public void getId(HttpServletRequest req, HttpServletResponse resp) {
		String id=UUID.randomUUID().toString().replace("-","");
		this.write(true,id, resp);
	}
	/**
	 *@title: findTabel 
	 *@description: 获取桌子
	 *@author: luge
	 *@date: 2019年9月9日 下午4:01:24
	 *@param req
	 *@param resp
	 *@throws:
	 */
	public void findTable(HttpServletRequest req, HttpServletResponse resp) {
		String num = req.getParameter("peoplenum");
		Integer peoplenum=null;
		if(FormValidation.verifyNull(num)) {
			peoplenum=Integer.parseInt(num);
		}
		List<Tableinfo> tls=mngService.findTableno(peoplenum);
		if(tls==null||tls.size()==0) {
			this.write(false, "不存在对应的餐桌", resp);
		}else {
			this.write(true, tls, resp);
		}
	}
	/**
	  *预约新增
	 *@author: luge
	 *@date: 2019年9月9日 下午5:02:35
	 *@param req
	 *@param resp
	 *@throws:
	 */
	public void add(HttpServletRequest req, HttpServletResponse resp) {
		DateConverter d=new DateConverter();
		d.setPattern("yyyy-MM-dd HH:mm");
		ConvertUtils.register(d, java.util.Date.class);
		Reserveinfo r=new Reserveinfo();
		try {
			BeanUtils.populate(r,req.getParameterMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(mngService.add(r)) {
			this.write(true, "添加成功", resp);
		}else {
			this.write(false,"餐桌已被预订", resp);
		}
	}
	/**
	  * 通过id查询预约信息
	 *@author: luge
	 *@date: 2019年9月10日 上午10:04:09
	 *@throws:
	 */
	public void findById(HttpServletRequest req, HttpServletResponse resp) {
		String reserveno=req.getParameter("reserveno");
		Reserveinfo r=mngService.findById(reserveno);
		this.write(true, r, resp);
	}
	/**
	  * 更新
	 *@author: luge
	 *@date: 2019年9月10日 上午10:04:09
	 *@throws:
	 */
	public void update(HttpServletRequest req, HttpServletResponse resp) {
		DateConverter d=new DateConverter();
		d.setPattern("yyyy-MM-dd HH:mm");
		ConvertUtils.register(d, java.util.Date.class);
		Reserveinfo r=new Reserveinfo();
		try {
			BeanUtils.populate(r, req.getParameterMap());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		if(mngService.update(r)) {
			this.write(true, "更新成功", resp);
		}else {
			this.write(false, "更新失败", resp);
		}
	}
}
