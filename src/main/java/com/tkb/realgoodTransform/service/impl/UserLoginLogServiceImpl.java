package com.tkb.realgoodTransform.service.impl;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.UserLoginLogDao;
import com.tkb.realgoodTransform.model.UserLoginLog;
import com.tkb.realgoodTransform.service.UserLoginLogService;




@Service
public class UserLoginLogServiceImpl implements UserLoginLogService {
	
	@Autowired
	private UserLoginLogDao userLoginLogDao;

	@Override
	public void addUserLoginLog(String loginStatus, String account, String ip) {
		UserLoginLog userLoginLog = new UserLoginLog();
		userLoginLog.setStatus(loginStatus);
		userLoginLog.setAccount(account.toUpperCase());
		userLoginLog.setIp(ip);
		userLoginLog.setCreate_date(Date.valueOf(LocalDate.now()));
		userLoginLogDao.addUserLoginLog(userLoginLog);
	}

}
