package com.tkb.realgoodTransform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.UserDao;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.UserService;



/**
 * 使用者Service實作類
 * 
 * @author Ken
 * @version 創建時間：2016-02-24
 */
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	public User login(User user) {
		return userDao.login(user);
	}

	public Integer checkAccount(User user) {
		return userDao.checkAccount(user);
	}

	public Integer checkStatus(User user) {
		return userDao.checkStatus(user);
	}

	@Override
	public Integer getUserId(User user) {
		return userDao.getUserId(user);
	}


}
