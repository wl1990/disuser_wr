package com.test.cglib;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class MyMethodInterceptor implements MethodInterceptor{

	@Override
	public Object intercept(Object obj, Method method, Object[] args,MethodProxy proxy) throws Throwable {
		System.out.println("---say before---");
		return proxy.invokeSuper(obj, args);
	}
	
}
