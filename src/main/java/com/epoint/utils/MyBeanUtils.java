package com.epoint.utils;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
/**
 * 封装了BeanUtil里的populate
 * @author Luge
 */
public class MyBeanUtils {
	/**
	 * 注册转换器
	 * 设置基础数据类型默认值为null
	 */
	static {
		ConvertUtils.register(new LongConverter(null), Long.class);
		ConvertUtils.register(new ShortConverter(null), Short.class);
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		ConvertUtils.register(new DoubleConverter(null), Double.class);
		ConvertUtils.register(new FloatConverter(null), Float.class);
		ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
	}
	/**
	 * @param obj 需要被赋值的bean对象
	 * @param map 数据集合
	 */
	public static void populate(Object obj, Map<String, String[]> map) {
		try {
			ConvertUtils.register(new DateConverter(), java.util.Date.class);
			BeanUtils.populate(obj, map);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 将map中的数据封装到clazz对应的对象中并返回
	 * @param <T> bean对象的类型
	 * @param clazz 需要将数据加入的对象类型
	 * @param map 数据集合
	 * @return bean对象
	 */
	public static<T> T  populate(Class<T> clazz, Map<String, String[]> map) {
		try {
			T obj=clazz.newInstance();
			//创建日期转换类
			//注册转换器
			ConvertUtils.register(new MyDateConverter(), java.util.Date.class);
			BeanUtils.populate(obj, map);
			return obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}

}
