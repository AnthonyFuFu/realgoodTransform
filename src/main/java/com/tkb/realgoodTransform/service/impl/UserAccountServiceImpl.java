package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.UserAccountDao;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.model.UserAccount;
import com.tkb.realgoodTransform.service.UserAccountService;

@Service
public class UserAccountServiceImpl implements UserAccountService {
	
	@Autowired
	private UserAccountDao userAccountDao;

	// ======================================== 開發用method start ========================================= 
	@Override
	public List<Map<String, Object>> getListForInsertData() {
		return userAccountDao.getListForInsertData();
	}
	@Override
	public List<Map<String, Object>> getListForChecktData() {
		return userAccountDao.getListForChecktData();
	}
	@Override
	public void insertForRemake(UserAccount userAccount) {
		userAccountDao.insertForRemake(userAccount);
	}
	@Override
	public void updateForRemake(UserAccount userAccount) {
		userAccountDao.updateForRemake(userAccount);
	}
	// ======================================== 開發用method end =========================================
	
	@Override
	public User getDataByAccount(User user) {
		return userAccountDao.getDataByAccount(user);
	}

	@Override
	public void insert(UserAccount userAccount) {
		userAccountDao.insert(userAccount);
	}

	@Override
	public List<String> getUserMenuList(User userAccountSession, String link) {
		return userAccountDao.getUserMenuList(userAccountSession, link);
	}

	@Override
	public User login(User user) {
		return userAccountDao.login(user);
	}

}
