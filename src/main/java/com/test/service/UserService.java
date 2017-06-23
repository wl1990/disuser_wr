package com.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.test.dao.UserMapper;
import com.test.model.User;
import com.test.myenum.DataSource;

@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;
	
	@DataSource("write")
	public int insertUser(User user){
		return userMapper.insert(user);
	}
	
	@DataSource("read")
	public User getUserById(int id){
		return userMapper.selectByPrimaryKey(id);
	}

	@DataSource("read")
	public List<User> getUserList(Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		return userMapper.getUserList();
	}

	public int insertUserList(List<User> list) {
		return userMapper.insertUserList(list);
	}
}
