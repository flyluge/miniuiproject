package com.epoint.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * 数据库连接工具类
 * @author Luge
 *
 */
public class JDBCUtils {
	private static DataSource dataSource;
	/**
	 * 绑定当前线程的Conncetion
	 */
	public static ThreadLocal<Connection> threadLocal=new ThreadLocal<Connection>();
	/**
	 * 初始化数据源
	 */
	static {
		try {
			Properties properties =new Properties();
			properties.load(JDBCUtils.class.getClassLoader().getResourceAsStream("jdbc.properties"));
			dataSource=DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	  *  关闭连接
	 * @param connection Connection对象
	 * @param preparedStatement PreparedStatement对象
	 * @param resultSet ResultSet对象
	 */
	public static void closeConnection(Connection conn) {
		try {
			//关闭连接
			conn.close();
			//释放当前线程绑定
			threadLocal.remove();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 *  从Druid数据库连接池中绑定当前线程的Connection对象
	 * @return Connection对象
	 */
	public static Connection getDruidConnection() {
		Connection conn=threadLocal.get();
		if(conn==null) {
			try {
				conn=dataSource.getConnection();
				threadLocal.set(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	/**
	 * 获取数据源 
	 * @return DataSource数据源对象
	 */
	public static DataSource getDataSource() {
		return dataSource;
	}
	/**
	 * 开启事务
	 * 获取当前线程的连接并关闭自动提交
	 * @throws SQLException
	 */
	public static void startTransaction() throws SQLException {
		getDruidConnection().setAutoCommit(false);
	}
	/**
	 * 提交事务
	 * @throws SQLException 
	 */
	public static void commitTransaction() throws SQLException {
		Connection conn = getDruidConnection();
		conn.commit();
		closeConnection(conn);
	}
	/**
	 * 回滚事务
	 * @throws SQLException
	 */
	public static void rollBackTransaction() throws SQLException {
		Connection conn = getDruidConnection();
		conn.rollback();
		closeConnection(conn);
	}
}
