package com.epoint.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
  * 表单验证类
 * @title: FormValidation.java 
 * @package com.epoint.utils 
 * @author: luge
 * @date: 2019年9月9日 下午6:48:23 
 * @version: V1.0
 */
public class FormValidation {
	/**
	 *@title: isValidDate 
	 *@description: 验证日期是否为指定格式
	 *@author: luge
	 *@date: 2019年9月9日 下午6:10:13
	 *@param str 
	 *@param pattern
	 *@return
	 */
    public static boolean isValidDate(String str,String pattern) {
    	//yyyy-MM-dd
        boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }
    /**
     * 正则表达式验证字符串
     * @param str 需要验证的字符串
     * @param pattern 正则表达式
     * @return true 验证成功
     * @return false 验证失败
     */
    public static boolean verifyStringByPattern(String str,String pattern) {
    	Pattern p=Pattern.compile(pattern);
    	Matcher m = p.matcher(str);
    	return m.matches();
    }
	/**
	 * 判断Obj数组中是否有空值或空串
	 * @param obj 需要验证的对象数组
	 * @return true:全部非空  false:存在空值或空串
	 */
	public static boolean verifyNull(Object... obj) {
		for (Object object : obj) {
			if(object==null||object.toString().trim().length()==0) {
				return false;
			}
		}
		return true;
	}
}
