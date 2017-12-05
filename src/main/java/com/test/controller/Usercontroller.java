package com.test.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.test.model.User;
import com.test.redis.dislock.MyDisLock;
import com.test.service.UserService;
import com.test.service.springinit.InitBeanTest;
import com.test.utils.NebulaException;

@RestController
public class Usercontroller {
	private static final Logger logger=LoggerFactory.getLogger(Usercontroller.class);
	@Autowired
	private UserService userService;
	@Autowired
	private MyDisLock myDisLock;
	@RequestMapping(value="/user",method=RequestMethod.POST)
	public JSONObject addUser(@RequestParam String username){
		 User user=new User();
		 user.setBirthday(new Date());
		 user.setName(username);
		 int f=userService.insertUser(user);
		 JSONObject j=new JSONObject();
    	 j.put("username", username);
    	 j.put("result", f);
    	 return j;
	}
	
	@RequestMapping(value="/adduser",method=RequestMethod.POST)
	public JSONObject addUser(@RequestBody User user){
		 int f=userService.insertUser(user);
		 JSONObject j=new JSONObject();
    	 j.put("username", user.getName());
    	 j.put("result", f);
    	 return j;
    	 // 1463270400000
    	 
	}
	
	/**
	 * multi user insert
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/addusers",method=RequestMethod.POST)
	public JSONObject addUsers(){
		 List<User> list=new ArrayList<User>();
		 User u1=new User();
		 u1.setBirthday(new Date());
		 u1.setName("6221");
		 User u2=new User();
		 u2.setBirthday(new Date());
		 u2.setName("6222");
		 User u3=new User();
		 u3.setBirthday(new Date());
		 u3.setName("6222");
		 list.add(u3);
		 list.add(u2);
		 list.add(u1);
		 int f=userService.insertUserList(list);
		 JSONObject j=new JSONObject();
    	 j.put("username", JSONObject.toJSONString(list));
    	 j.put("result", f);
    	 return j;
    	 // 1463270400000
    	 
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 * @throws NebulaException
	 */
	@RequestMapping(value="/user/{id}",method=RequestMethod.GET)
	public JSONObject getUser(@PathVariable("id") Integer id){
		User user=userService.getUserById(id);
    	 return (JSONObject) JSON.toJSON(user);
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 * @throws NebulaException
	 */
	@RequestMapping(value="/users",method=RequestMethod.GET)
	public JSONArray getUserList(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
		List<User> user=userService.getUserList(pageNum,pageSize);
    	 return  (JSONArray) JSON.toJSON(user);
	}
	
	/**
	 * @controllerAdvice+@ExceptionHandler 实现exception的统一处理
	 * 缺点 只能处理controller未处理的异常，对interceptor层的异常，spring框架层的异常，tomcat容器servlet 的异常无法处理
	 * @throws NebulaException
	 */
	@RequestMapping(value="/testException",method=RequestMethod.GET)
	public void testException() throws NebulaException{
    	throw new NebulaException(500,"exception test");
	}
	
	@RequestMapping(value="/testExceptionNoFilter",method=RequestMethod.GET)
	public void testExceptionNoFilter() throws NebulaException{
    	throw new NebulaException(500,"exception test");
	}
	
	@RequestMapping(value="/druid",method=RequestMethod.GET)
	public JSONArray testDruid() throws NebulaException{
		return getUserList(1,10);
	}
	
	@RequestMapping(value="/locktest/{uid}",method=RequestMethod.POST)
	public JSONArray testRedisLock(@PathVariable("uid") String uid){
		try{
			myDisLock.tryLock(uid,"123");
			User user=userService.getUserById(Integer.parseInt(uid));
			return  (JSONArray) JSON.toJSON(user);
		}catch(Exception e){
			
		}finally{
			myDisLock.releaseLock(uid.toString(),"123");
		}
		return null;
	}
}
