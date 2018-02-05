package com.test.cglib;

import org.springframework.cglib.proxy.Enhancer;

public class CglibTest {
	public static void main(String[] args) {
		Enhancer enhancer=new Enhancer();
		enhancer.setSuperclass(HelloConcrete.class);
		enhancer.setCallback(new MyMethodInterceptor());
		HelloConcrete hello=(HelloConcrete) enhancer.create();
		System.out.println(hello.sayHello("wl test"));
	}
}
