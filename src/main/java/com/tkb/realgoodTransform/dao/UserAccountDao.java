package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.model.UserAccount;

public interface UserAccountDao {

	// ======================================== 開發用method start ========================================= 
	public List<Map<String, Object>> getListForInsertData();
	public List<Map<String, Object>> getListForChecktData();
	public void insertForRemake(UserAccount userAccount);
	public void updateForRemake(UserAccount userAccount);
	// ======================================== 開發用method end =========================================
	
	

	/**
	 * 取得單筆帳號設定(依帳號)
	 * @param user
	 * @return User
	 */
	public User getDataByAccount(User user);
	
	
	
	public void insert(UserAccount userAccount);

	/**
	 * 取得使用者群組權限清單
	 * @param userAccountSession, link
	 * @return List<String>
	 */
	public List<String> getUserMenuList(User userAccountSession, String link);

	/**
	 * 驗證帳號密碼
	 * @param User user
	 * @return User
	 */
	public User login(User user);
	
}
