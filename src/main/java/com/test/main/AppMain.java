package com.test.main;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;

import com.test.filter.ExceptionTestFilter;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ImportResource("classpath:spring-mybatis.xml")
@SpringBootApplication(scanBasePackages={ "com.test.controller","com.test.service","com.test.handler"})
@EnableSwagger2
public class AppMain {
	
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
	@Bean
	public HttpMessageConverters restFileDownloadSupport(){
		ByteArrayHttpMessageConverter arrayHttpMessageConverter=new ByteArrayHttpMessageConverter();
		return new HttpMessageConverters(arrayHttpMessageConverter);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AppMain.class, args);
	}
	
	
}
