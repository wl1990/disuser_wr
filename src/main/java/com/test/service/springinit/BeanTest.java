package com.test.service.springinit;

import java.util.concurrent.Executor;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taobao.diamond.manager.DiamondManager;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;

public class BeanTest {
	public static void main(String[] args) {
		diamondtest();
	}
	
	public static void beaninittest(){
		ClassPathXmlApplicationContext ac=new ClassPathXmlApplicationContext("/spring/applicationContext.xml");
		InitBeanTest beanTest=(InitBeanTest) ac.getBean("initBeanTest");
		PostProcessor postProcessor=(PostProcessor) ac.getBean("postProcessor");
		ac.close();
	}
	public static void diamondtest(){
		DiamondManager manager=new DefaultDiamondManager("${your_config_data_id}",new ManagerListener(){

			@Override
			public Executor getExecutor() {
				return null;
			}

			@Override
			public void receiveConfigInfo(String configInfo) {
				System.out.println("------------------------------------receiver config:"+configInfo);
			}
			
		});
	}
}
