package com.epoint.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
/**
 * 日期转换类
 * @author Luge
 */
public class MyDateConverter implements Converter{  
	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 重写convert
	 * 当日期为空时 设置日期为null
	 * 可转换类型:
	 * 	1. java.util.Date
	 *  2. java.sql.Date
	 *  3. java.sql.Timestamp
	 */
	public Object convert(@SuppressWarnings("rawtypes") Class type, Object value){  
        if(value == null||value.toString().trim().length()==0){  
            return null;  
        }else if(type == Timestamp.class){  
            return convertToDate(type, value, "yyyy-MM-dd HH:mm:ss"); 
        }else if(type == java.util.Date.class){ 
            return convertToDate(type, value, "yyyy-MM-dd HH:mm:ss");  
        }else if(type == java.sql.Date.class) {
        	return convertToDate(type, value, "yyyy-MM-dd");  
        }else if(type == String.class){ 
            return convertToString(type, value);  
        }  
        throw new ConversionException("不能转换 " + value.getClass().getName() + " 为 " + type.getName());  
    }  
    /**
     * 日期转换
     * @param type 转换为的日期格式
     * @param value 需要转换的数据
     * @param pattern 
     * @return
     */
    protected Object convertToDate(Class<?> type, Object value, String pattern) {  
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);  
        if(value instanceof String){  
            try{  
                if(value==null||value.toString().trim().length()==0){  
                    return null;
                }
                Date date = sdf.parse((String) value);  
                if(type.equals(Timestamp.class)){  
                    return new Timestamp(date.getTime());  
                }  
                if(type.equals(java.util.Date.class)){  
                	return date; 
                }  
                if(type.equals(java.sql.Date.class)){  
                	return new java.sql.Date(date.getTime());  
                }  
            }catch(Exception pe){  
                return null;  
            }  
        }else if(value instanceof Date){  
            return value;  
        }
		return null;
    }  
	/**
	 * 将日期转换为时间字符串  
	 * @param type 转换的类型
	 * @param value 转换的内容
	 * @return
	 */
    protected Object convertToString(@SuppressWarnings("rawtypes") Class type, Object value) {  
        if(value instanceof java.util.Date){  
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
              
            if (value instanceof Timestamp) {  
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            }   
            try{  
                return sdf.format(value);  
            }catch(Exception e){  
                throw new ConversionException("日期转换为字符串时出错！");  
            }  
        }else{  
            return value.toString();  
        }  
    } 
}  