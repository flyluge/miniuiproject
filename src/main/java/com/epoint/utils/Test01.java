	/** 
	 * projectName: project 
	 * fileName: Test.java 
	 * packageName: com.epoint.utils 
	 * date: 2019年9月10日下午2:54:51 
	 * copyright(c)  LUGE
	 */
package com.epoint.utils;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**   
 * @title: Test.java 
 * @package com.epoint.utils 
 * @description: TODO
 * @author: luge
 * @date: 2019年9月10日 下午2:54:51 
 * @version: V1.0   
*/
public class Test01 {
	
	@Test
	public void test() {
		String json="[\r\n" + 
				"		             	{'ID': 1001, 'name': '张三', 'age': 24},\r\n" + 
				"		            	{'ID': 1002, 'name': '李四', 'age': 25},\r\n" + 
				"		            	{'ID': 1003, 'name': '王五', 'age': 22}\r\n" + 
				"		            ]" ;
		JSONArray obj2 = JSON.parseArray(json);
		System.out.println(obj2.toString());
		JSONObject obj=JSON.parseObject(obj2.get(0).toString());
		System.out.println(obj.get("ID"));
	}
}
