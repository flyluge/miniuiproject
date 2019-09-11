/** 
 * projectName: restaurantmanage 
 * fileName: TableManagerServlet.java 
 * packageName: com.epoint.controller 
 * date: 2019年9月9日上午10:20:53 
 * copyright(c)  LUGE
 */
package com.epoint.action.tablemanager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epoint.action.BaseServlet;
import com.epoint.domain.Tableinfo;
import com.epoint.service.TableMngService;
import com.epoint.utils.FormValidation;
import com.epoint.utils.MyBeanUtils;

/**   
 * @title: TableManagerServlet.java 
 * @package com.epoint.controller 
 * @description: 餐桌管理Servlet
 * @author: luge
 * @date: 2019年9月9日 上午10:20:53 
 * @version: V1.0   
*/
public class TableManagerServlet extends BaseServlet{

	private static final long serialVersionUID = 1L;
	private TableMngService mngService=new TableMngService();
	/**
	  * 获取餐桌列表
	 *@title: getTableList 
	 *@description: 获取列表
	 *@author: luge
	 *@date: 2019年9月9日 上午10:36:34
	 *@param req
	 *@param resp
	 *@throws:
	 */
	public void getTableList(HttpServletRequest req, HttpServletResponse resp) {
		// 排序方式
		String sortOrder = req.getParameter("sortOrder");
		// 排序字段
		String sortField = req.getParameter("sortField");
		// 获取分页数据
		int pageIndex = Integer.parseInt(req.getParameter("pageIndex")) + 1;
		int pageSize = Integer.parseInt(req.getParameter("pageSize"));
		//获取查询条件
		String s1=req.getParameter("holdnum");
		String s2 = req.getParameter("isuse");
		Integer holdnum=null;
		Integer isuse=null;
		if(FormValidation.verifyNull(s1)) {
			holdnum=Integer.parseInt(req.getParameter("holdnum"));
		}
		if(FormValidation.verifyNull(s2)) {
			isuse=Integer.parseInt(req.getParameter("isuse"));
		}
		List<Tableinfo> list=mngService.findTableList(holdnum,isuse,sortField,sortOrder,pageIndex,pageSize);
		int totalcount=mngService.findTotalCount(holdnum,isuse);
		this.miniuiWrite(true, list, totalcount, resp);
	} 
	/**
	 *@title: deleteById 
	 *@description: 通过id删除数据
	 *@author: luge
	 *@date: 2019年9月9日 上午10:58:54
	 *@param req
	 *@param resp
	 *@throws:
	 */
	public void deleteById(HttpServletRequest req, HttpServletResponse resp) {
		String id=req.getParameter("tableno");
		String[] ids=id.split(",");
		String msg=mngService.deleteByIds(ids);
		if("".equals(msg)) {
			this.write(true, "删除成功", resp);
		}else {
			this.write(false, msg+"餐桌已被预约或在用餐中,无法将其删除", resp);
		}
	}
	/**
	 *@title: getId 
	 *@description: 获取餐桌编号
	 *@author: luge
	 *@date: 2019年9月9日 上午11:10:03
	 *@param req
	 *@param resp
	 *@throws:
	 */
	public void getId(HttpServletRequest req, HttpServletResponse resp) {
		//获取容纳人数
		String s1=req.getParameter("holdnum");
		Integer holdnum=null;
		if(FormValidation.verifyNull(s1)) {
			holdnum=Integer.parseInt(req.getParameter("holdnum"));
		}
		String id=mngService.getId(holdnum);
		this.write(true, id, resp);
	}
	/**
	 *@title: add 
	 *@description: 添加一条记录
	 *@author: luge
	 *@date: 2019年9月9日 下午2:14:02
	 *@param req
	 *@param resp
	 *@throws:
	 */
	public void add(HttpServletRequest req, HttpServletResponse resp) {
		Tableinfo tableinfo = MyBeanUtils.populate(Tableinfo.class, req.getParameterMap());
		if(mngService.add(tableinfo)) {
			this.write(true,"添加成功",resp);
		}else {
			this.write(false,"添加失败", resp);
		}
	}
}
