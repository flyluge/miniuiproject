package com.epoint.controller.filter;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 通用的编码过滤器
 * @author Luge
 */
public class EncodingFilter implements Filter {
    /**
     *  默认设置过滤器使用的编码为utf-8
     */
    private String charsetName = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	//获取初始化编码 若不为空则修改
    	String encoding = filterConfig.getInitParameter("encoding");
    	if(encoding!=null) {
    		charsetName=encoding;
    	}
    }
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	// 转型为与协议相关对象
        HttpServletRequest req = (HttpServletRequest) request;
        // 获取请求方法
        String method = req.getMethod();
        String s="post";
        if (s.equalsIgnoreCase(method)) {
            // 解决post
            req.setCharacterEncoding(charsetName);
        } else {
            // 解决get请求
            req = new EncodingRequest(req, charsetName);
        }
        // 解决响应乱码
        // 设置响应数据和响应的页面编码格式
        response.setContentType("text/html; charset=" + charsetName);
        chain.doFilter(req, response);
    }
}
/**
 * get请求乱码的解决方式
 * @author Luge
 *
 */
class EncodingRequest extends HttpServletRequestWrapper {
    private HttpServletRequest request;
    private String charsetName;
    private Map<String, String[]> map;
    private Enumeration<String> names;

    public EncodingRequest(HttpServletRequest request, String charsetName) {
        super(request);
        this.request = request;
        this.charsetName = charsetName;
        map = getParameterMap();
        names = Collections.enumeration(map.keySet());
    }
    @Override
   /**
    * @title: getParameterNames
    * @description: 处理get请求中参数的键的乱码问题
    * @see javax.servlet.ServletRequestWrapper#getParameterNames()
    */
    public Enumeration<String> getParameterNames() {
        return names;
    }
    @Override
    public String getParameter(String name) {
        // 通过getParameterMap方法完成
        String[] values = getParameterValues(name);
        if (values == null) {
            return null;
        }
        return values[0];
    }
    @Override
    public String[] getParameterValues(String name) {
        String[] values = map.get(name);
        if(values != null && values.length > 0){
            for (int i = 0; i < values.length; i++) {
                try {
                    // values是一个地址
                    values[i] = new String(values[i].getBytes("ISO-8859-1"), charsetName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return values;
    }
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String[]> map = new HashMap<>(parameterMap.size());
        for (Map.Entry<String, String[]> entries : parameterMap.entrySet()) {
        	//获取参数的key
            String key = entries.getKey();
            String[] values = entries.getValue();
            try {
                //处理get请求中参数的键的乱码问题
                key = new String(key.getBytes("ISO-8859-1"), charsetName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //处理get请求中参数的值的乱码问题
            if(values != null && values.length > 0){
                for (int i = 0; i < values.length; i++) {
                    try {
                        // values是一个地址
                        values[i] = new String(values[i].getBytes("ISO-8859-1"), charsetName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            map.put(key, values);
        }
        return map;
    }
}
