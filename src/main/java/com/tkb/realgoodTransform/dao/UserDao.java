package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import org.apache.catalina.Cluster;

import com.tkb.realgoodTransform.model.User;



/**
 * 使用者Dao介面接口
 * @author Ken
 * @version 創建時間：2016-02-01
 */
public interface UserDao {

	/**
	 * 驗證帳號密碼
	 * @param account
	 * @param password
	 * @return User
	 */
	public User login(User user);
	
	/**
	 * 確認帳號是否存在
	 * @param user
	 * @return Integer
	 */
	public Integer checkAccount(User user);
	
	/**
	 * 確認帳號是否未鎖定
	 * @param user
	 * @return
	 */
	public Integer checkStatus(User user);
	
	/**
	 * 獲取使用者ID
	 * @param user
	 * @return Integer
	 */
	public Integer getUserId(User user);
	
}
