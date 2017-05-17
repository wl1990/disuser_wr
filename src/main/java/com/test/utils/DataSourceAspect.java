package com.test.utils;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.test.myenum.DataSource;

public class DataSourceAspect {
	 /**  
     * 在dao层方法之前获取datasource对象之前在切面中指定当前线程数据源路由的key  
     */  
     public void before(JoinPoint point)  
        {         
           Object target=point.getTarget();
           System.out.println(target.toString());
           String method=point.getSignature().getName();
           System.out.println("--method--"+method);
           Class<?>[] classz=target.getClass().getInterfaces();
           Class<?>[] parameterTypes=((MethodSignature)point.getSignature()).getMethod().getParameterTypes();
		try {
			Method m = target.getClass().getMethod(method, parameterTypes);
			System.out.println(m.getName());
			if(m!=null && m.isAnnotationPresent(DataSource.class)){
				DataSource data=m.getAnnotation(DataSource.class);
				System.out.println("user select data type:"+data.value());
				HandleDataSource.putDataSource(data.value());
			}else if(m!=null){
				System.out.println("default datasource read");
				HandleDataSource.putDataSource("read");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
           
        }  
	
}
