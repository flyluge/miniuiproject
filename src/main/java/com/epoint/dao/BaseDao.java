package com.epoint.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @description:Dao层所有接口的父接口 定义了一些基本的常用的增删改查方法
 * @author Luge
 */
public interface BaseDao<T> {
	/**
	 * 插入一条数据
	 *@title: add 
	 *@author: luge
	 *@date: 2019年9月9日 下午6:35:01
	 *@param t 封装数据的实体bean
	 *@return true:添加成功  false:添加失败
	 */
	boolean add(T t);
	/**
	 *  添加一条数据并返回主键
	 * @param t 插入的数据
	 * @return null:插入失败  或者返回插入后的主键
	 * 注意:只对自增长id有效
	 */
	Serializable addBackWithId(T t);
	/**
	 *根据主码删除一条数据
	 *@author: luge
	 *@date: 2019年9月9日 下午6:35:29
	 *@param id
	 *@return true false
	 */
	boolean delete(Serializable id);
	/**
	 *  根据主码更新一条数据
	 * @param t 带有主码的需要更新的数据
	 * @return true:更新成功
	 * 			false:更新的数据不存在
	 */
	boolean update(T t);
	/**
	  *查询所有数量
	 *@author: luge
	 *@date: 2019年9月9日 下午6:35:57
	 *@return 数据数量
	 */
	long findAllCount();
	/**
	 * 
	 *通过主码查询单条数据
	 *@author: luge
	 *@date: 2019年9月9日 下午6:36:09
	 *@param id
	 *@return 实体bean
	 */
	T findById(Serializable id);
	/**
	  *  条件查询单个
	  *  通过sql语句查询单条数据  
	 * @param sql sql语句
	 * @param param 查询参数
	 * @return 查询结果封装bean
	 */
	T findOneBySql(String sql,Object... param);
	/**
	 *  查询所有
	 * @return
	 */
	List<T> findAll();
	/**
	  * 条件查询多个
	  * 通过sql语句条件查询多条数据
	 *@author: luge
	 *@date: 2019年9月9日 下午6:36:50
	 *@param sql sql语句
	 *@param param
	 *@return 查询结果封装List<Bean>
	 *@throws:
	 */
	List<T> findAllBySql(String sql,Object... param);
	/**
	 * 条件查询数量
	 * @param t 封装条件的javabean
	 * @param predate 
	 * @param lastdate
	 * @return
	 */
	long findAllCount(T t,String predate,String lastdate);
	/**
	 * 
	 *条件查询数量
	 *@author: luge
	 *@date: 2019年9月9日 下午6:37:22
	 *@param sql sql语句
	 *@param obj 条件数组
	 *@return
	 */
	long findAllCount(String sql, Object... obj);
	/**
	 *条件查询数量
	 *@author: luge
	 *@date: 2019年9月9日 下午6:37:52
	 *@param sql sql语句
	 *@param list 条件集合
	 *@return
	 */
	long findAllCount2(String sql, List<Object> list);
	/**
	 * 分页查询所有
	 * @param currPage 当前页
	 * @param pageSize 页面大小
	 * @return
	 */
	List<T> findByPage(Integer currPage, Integer pageSize);
	/**
	 * 分页条件查询

	
	 */
	/**
	  * 分页条件查询
	 * @author: luge
	 * @date: 2019年9月9日 下午6:41:57
	 * @param t 封装了查询条件的实体bean
	 * @param pageIndex 页码(从1开始)
	 * @param pageSize 每页数量
	 * @param predate
	 * @param lastdate
	 * @param column 需要根据column列排序
	 * @param asc 排序规则 asc:正序  desc:逆序
	 * @return 条件分页数据
	 * @return
	 */
	List<T> findByPage(T t, int pageIndex, int pageSize,String predate,String lastdate, String column, String asc);
	/**
	  *  条件分页查询
	  *  通过sql语句与页码条件查询多条数据
	 * @param sql sql语句
	 * @param currPage 当前页
	 * @param pageSize 页面大小
	 * @param param 条件数组
	 * @return
	 */
	List<T> findByPageAndSql(String sql,Integer currPage, Integer pageSize,Object... param);
	/**
	 * 
	  *  条件分页查询
	  *  通过sql语句与页码条件查询多条数据
	 *@author: luge
	 *@date: 2019年9月9日 下午6:38:35
	 *@param sql sql语句
	 *@param pageIndex 当前页
	 *@param pageSize 页面大小
	 *@param list
	 *@return
	 *@throws:
	 */
	List<T> findByPageAndSql2(String sql, Integer pageIndex, Integer pageSize, List<Object> list);
}
