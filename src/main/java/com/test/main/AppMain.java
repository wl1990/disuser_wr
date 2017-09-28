package com.test.main;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;

import com.alibaba.druid.support.http.StatViewServlet;
import com.test.env.SendMessage;
import com.test.filter.ExceptionTestFilter;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ImportResource("classpath:spring-mybatis.xml")
@SpringBootApplication(scanBasePackages={ "com.test.controller","com.test.service","com.test.handler","com.test.env","com.test.rabbitmq"})
@EnableSwagger2
public class AppMain {
	@Value("${app.name}")
	private String name;
	@Autowired
	private SendMessage sendMessage;
	
	@PostConstruct
	public void init(){
		sendMessage.send();
	}
	/**
	 * 加载过滤器，测试@ControllerAdvice
	 * @return
	 */
	@Bean  
    public FilterRegistrationBean  filterRegistrationBean() {  
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();  
        ExceptionTestFilter exceptiontestFilter = new ExceptionTestFilter();  
        registrationBean.setFilter(exceptiontestFilter);  
        List<String> urlPatterns = new ArrayList<String>();  
        urlPatterns.add("/testException");  
        registrationBean.setUrlPatterns(urlPatterns);  
        return registrationBean;  
    } 
	
	/**
	 * druid 监控
	 * @return
	 */
	@Bean
	public ServletRegistrationBean servletRegistrationBean(){
		return new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
	}
	@Bean
	public HttpMessageConverters restFileDownloadSupport(){
		ByteArrayHttpMessageConverter arrayHttpMessageConverter=new ByteArrayHttpMessageConverter();
		return new HttpMessageConverters(arrayHttpMessageConverter);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AppMain.class, args);
	}
	
}
