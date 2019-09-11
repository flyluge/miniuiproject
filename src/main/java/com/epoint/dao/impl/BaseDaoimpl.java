package com.epoint.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.epoint.dao.BaseDao;
import com.epoint.utils.FormValidation;
import com.epoint.utils.JDBCUtils;

/**
 * Dao层实现类的父类,改类封装了基础的增删改查
 * 
 * @author Luge
 * @param <T> 实体类型 要求: 
 * 			  1.表名与对象名一致(不区分大小写) 
 * 	          2.数据名与属性名一致(区分大小写) 
 *            3.属性一定要有get/set方法
 *            4.表一定要有主码
 */
public class BaseDaoimpl<T> implements BaseDao<T> {
	/**
	 * 创建QueryRunner
	 */
	protected QueryRunner queryRunner = new QueryRunner();
	/**
	 * T对应的class对象
	 */
	protected Class<? extends T> clazz;
	/**
	 * 表名
	 */
	private String tablename;
	/**
	 * 主码名称
	 */
	private Serializable idname;
	/**
	 * 实体类属性列表
	 */
	Field[] fields;
	/**
	 * 数据表属性列表
	 */
	List<String> columns = new ArrayList<String>();

	public BaseDaoimpl() {
		init();
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {
		// 获取T对应的class
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<? extends T>) type.getActualTypeArguments()[0];
		// 获取表名
		tablename = clazz.getSimpleName().toLowerCase();
		// 获取主码
		try {
			// 获取元数据
			Connection conn = JDBCUtils.getDruidConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			// 获取主码
			ResultSet r = metaData.getPrimaryKeys(null, null, tablename);
			if (r.next()) {
				idname = (Serializable) r.getObject(4);
			}
			// 获取属性
			ResultSet r2 = metaData.getColumns(conn.getCatalog(), null, tablename, null);
			while (r2.next()) {
				columns.add(r2.getString(4));
			}
			r.close();
			r2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 获取属性列表
		fields = clazz.getDeclaredFields();
	}

	/**
	 * 将属性转换为对应的method方法
	 * 
	 * @param attribute 属性名
	 * @return Method 方法对象
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public Method convertToMethod(String attribute) throws NoSuchMethodException, SecurityException {
		String method = "get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1, attribute.length());
		return clazz.getMethod(method);
	}

	/**
	 * 将list集合转为Object数组
	 * 
	 * @param list
	 * @return
	 */
	public Object[] convertListToObj(List<Object> list) {
		Object[] obj = new Object[list.size()];
		for (int i = 0; i < list.size(); i++) {
			obj[i] = list.get(i);
		}
		return obj;
	}

	/**
	 * 添加一条数据
	 */
	@Override
	public boolean add(T t) {
		if (t == null) {
			return false;
		}
		// 添加属性 添加通配符
		String parameters = "(";
		String wildcard = "(";
		// 属性值列表
		Object[] objs = new Object[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			String param = columns.get(i);
			if (i == (columns.size() - 1)) {
				parameters += param + ")";
				wildcard += "?)";
			} else {
				parameters += param + ",";
				wildcard += "?,";
			}
			try {
				Object obj = convertToMethod(param).invoke(t);
				objs[i] = obj;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 拼接sql
		String sql = "insert into " + tablename + parameters + "values" + wildcard;
		try {
			int flag = queryRunner.update(JDBCUtils.getDruidConnection(), sql, objs);
			if (flag > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Serializable addBackWithId(T t) {
		if (add(t)) {
			try {
				return queryRunner.query(JDBCUtils.getDruidConnection(), "select LAST_INSERT_ID()",
						new ScalarHandler<String>());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 根据主码删除一条数据
	 * 
	 * @param t 带有主码的需要删除的数据
	 * @return true:删除成功 false:删除的数据不存在
	 */
	@Override
	public boolean delete(Serializable id) {
		String sql = "delete from " + tablename + " where " + idname + "=?";
		try {
			int flag = queryRunner.update(JDBCUtils.getDruidConnection(), sql, id);
			if (flag > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据主码更新一条数据
	 * 
	 * @param t 带有主码的需要更新的数据
	 * @return true:更新成功 false:更新的数据不存在
	 */
	@Override
	public boolean update(T t) {
		if (t == null) {
			return false;
		}
		// 拼接sql
		String parameters = "";
		// 属性值列表
		Object[] objs = new Object[columns.size()];
		int index = 0;
		for (int i = 0; i < columns.size(); i++) {
			String param = columns.get(i);
			Object obj = null;
			try {
				obj = convertToMethod(param).invoke(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (param.equals(idname)) {
				objs[columns.size() - 1] = obj;
				continue;
			} else {
				objs[index] = obj;
				index++;
			}
			if (i != columns.size() - 1) {
				parameters += "`" + param + "` =?,";
			} else {
				parameters += "`" + param + "` =? ";
			}
		}
		String sql = "UPDATE `" + tablename + "` SET " + parameters + " WHERE `" + idname + "` = ?";
		try {
			int flag = queryRunner.update(JDBCUtils.getDruidConnection(), sql, objs);
			if (flag > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查询所有的数量
	 */
	@Override
	public long findAllCount() {
		String sql = "select count(*) from `" + tablename + "`";
		try {
			return queryRunner.query(JDBCUtils.getDruidConnection(), sql, new ScalarHandler<Long>());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 通过主码查询单条数据
	 */
	@Override
	public T findById(Serializable id) {
		String sql = "select * from " + tablename + " where " + idname + "=?";
		try {
			return (T) queryRunner.query(JDBCUtils.getDruidConnection(), sql, new BeanHandler<>(clazz), id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	@Override
	public List<T> findAll() {
		String sql = "select * from `" + tablename + "`";
		try {
			return (List<T>) queryRunner.query(JDBCUtils.getDruidConnection(), sql, new BeanListHandler<T>(clazz));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 分页查询
	 * 
	 * @param currPage 当前页
	 * @param pageSize 页面大小
	 * @return
	 */
	@Override
	public List<T> findByPage(Integer currPage, Integer pageSize) {
		if (currPage <= 0 || pageSize <= 0) {
			return null;
		}
		int index = (currPage - 1) * pageSize;
		String sql = "select * from `" + tablename + "` limit " + index + "," + pageSize;
		try {
			return (List<T>) queryRunner.query(JDBCUtils.getDruidConnection(), sql, new BeanListHandler<T>(clazz));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通过sql查询单条数据
	 */
	@Override
	public T findOneBySql(String sql, Object... param) {
		try {
			if(param==null) {
				return queryRunner.query(JDBCUtils.getDruidConnection(), sql, new BeanHandler<T>(clazz));
			}else {
				return queryRunner.query(JDBCUtils.getDruidConnection(), sql, new BeanHandler<T>(clazz), param);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通过sql语句条件查询
	 */
	@Override
	public List<T> findAllBySql(String sql, Object... obj) {
		try {
			if(obj==null) {
				return (List<T>) queryRunner.query(JDBCUtils.getDruidConnection(), sql, new BeanListHandler<T>(clazz));
			}else {
				return (List<T>) queryRunner.query(JDBCUtils.getDruidConnection(), sql, new BeanListHandler<T>(clazz),obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<T> findByPageAndSql(String sql, Integer currPage, Integer pageSize, Object... obj) {
		if (currPage <= 0 || pageSize <= 0) {
			return null;
		}
		int index = (currPage - 1) * pageSize;
		sql += " limit " + index + "," + pageSize;
		try {
			if(obj==null) {
				return (List<T>) queryRunner.query(JDBCUtils.getDruidConnection(), sql, new BeanListHandler<T>(clazz));
			}else {
				return (List<T>) queryRunner.query(JDBCUtils.getDruidConnection(), sql, new BeanListHandler<T>(clazz),
						obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<T> findByPageAndSql2(String sql, Integer currPage, Integer pageSize, List<Object> list) {
		Object[] obj = convertListToObj(list);
		return findByPageAndSql(sql, currPage, pageSize, obj);
	}

	@Override
	public List<T> findByPage(T t, int pageIndex, int pageSize, String predate, String lastdate, String column,
			String asc) {
		StringBuilder sql = new StringBuilder("select * from " + tablename + " where 1=1 ");
		// 创建有序列表 存存查询条件
		List<Object> qlist = new ArrayList<Object>();
		for (int i = 0; i < fields.length; i++) {
			// 获取get方法并实现
			String attr = fields[i].getName();
			String method = "get" + attr.substring(0, 1).toUpperCase() + attr.substring(1, attr.length());
			try {
				Object obj = clazz.getMethod(method).invoke(t);
				// 非空验证
				if (FormValidation.verifyNull(obj)) {
					sql.append(" and " + attr + " like ?");
					qlist.add("%" + obj + "%");
				}
				if ("java.util.Date".equals(fields[i].getGenericType().getTypeName())) {
					if (FormValidation.verifyNull(predate)) {
						sql.append(" and " + attr + " >= ?");
						qlist.add(predate);
					}
					if (FormValidation.verifyNull(lastdate)) {
						sql.append(" and " + attr + " <= ?");
						qlist.add(lastdate);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (FormValidation.verifyNull(column, asc)) {
			sql.append(" Order by " + column + " " + asc);
		}
		sql.append(" limit ?,?");
		pageIndex = (pageIndex - 1) * pageSize;
		qlist.add(pageIndex);
		qlist.add(pageSize);
		// 将qlist查询条件转为Object集合
		Object[] obj = new Object[qlist.size()];
		for (int i = 0; i < obj.length; i++) {
			obj[i] = qlist.get(i);
		}
		try {
			if (qlist.size() == 0) {
				return queryRunner.query(JDBCUtils.getDruidConnection(), sql.toString(), new BeanListHandler<T>(clazz));
			} else {
				return queryRunner.query(JDBCUtils.getDruidConnection(), sql.toString(), new BeanListHandler<T>(clazz),
						obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public long findAllCount(T t, String predate, String lastdate) {
		StringBuilder sql = new StringBuilder("select count(1) from " + tablename + " where 1=1 ");
		// 创建有序列表 存存查询条件
		List<Object> qlist = new ArrayList<Object>();
		for (int i = 0; i < fields.length; i++) {
			// 获取get方法并实现
			String attr = fields[i].getName();
			String method = "get" + attr.substring(0, 1).toUpperCase() + attr.substring(1, attr.length());
			try {
				Object obj = clazz.getMethod(method).invoke(t);
				// 非空验证
				if (FormValidation.verifyNull(obj)) {
					sql.append(" and " + attr + " like ?");
					qlist.add("%" + obj + "%");
				}
				if ("java.util.Date".equals(fields[i].getGenericType().getTypeName())) {
					if (FormValidation.verifyNull(predate)) {
						sql.append(" and " + attr + " >= ?");
						qlist.add(predate);
					}
					if (FormValidation.verifyNull(lastdate)) {
						sql.append(" and " + attr + " <= ?");
						qlist.add(lastdate);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 将qlist查询条件转为Object集合
		Object[] obj = new Object[qlist.size()];
		for (int i = 0; i < obj.length; i++) {
			obj[i] = qlist.get(i);
		}
		try {
			if (qlist.size() == 0) {
				return queryRunner.query(JDBCUtils.getDruidConnection(), sql.toString(), new ScalarHandler<Long>());
			} else {
				return queryRunner.query(JDBCUtils.getDruidConnection(), sql.toString(), new ScalarHandler<Long>(),
						obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public long findAllCount2(String sql, List<Object> list) {
		Object[] obj = convertListToObj(list);
		return findAllCount(sql, obj);
	}

	@Override
	public long findAllCount(String sql, Object... obj) {
		try {
			if(obj==null) {
				return queryRunner.query(JDBCUtils.getDruidConnection(), sql, new ScalarHandler<Long>());
			}else {
				return queryRunner.query(JDBCUtils.getDruidConnection(), sql, new ScalarHandler<Long>(), obj);
			}
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		return 0;
	}
}
