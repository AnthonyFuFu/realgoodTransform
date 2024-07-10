package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.model.UserAccount;




public interface UserAccountDao {
	
	// tkbApi新增全部使用者名單(初始化)
	public void insert(UserAccount userAccount);

	/**
	 * 取得帳號設定總筆數
	 * @param user
	 * @return Integer
	 */
	public int getCount(User user);
	
	/**
	 * 取得帳號設定資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param user
	 * @return List<User>
	 */
	public List<User> getList(int pageCount, int pageStart, User user);
	
	/**
	 * 取得單筆帳號設定(依帳號)
	 * @param user
	 * @return User
	 */
	public User getDataByAccount(User user);
	
	/**
	 * 取得單筆帳號設定
	 * @param user
	 * @return User
	 */
	public User getDataById(User user);

	/**
	 * 更新帳號資訊
	 * @param user
	 */
	public void update(User user);
	
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
	
	/**
	 * 更新員工狀態
	 * @param user
	 */
	public void updateStatus(User user);
	
	/**
	 * 更新員工資訊
	 * @param user
	 */
	public void scheduleUpdate(User user);
	
	public Integer getNextId();

	Map<String, Object> getLocationNoByAccount(String account);
	
	
	/***
	 * 排成更新員工資料
	 * 修改跟新增一起
	 */
	public void insertAndUpdate(User user);
	
	/***
	 * 獲取該事業群及單位底下的員工
	 * @param user
	 */
	public List<Map<String, Object>> getEmployeeList(User user);
	
	/**
	 * 排程用來修改員工狀態為離職
	 * 
	 */
	public void updateLeaveUserStatus(String employee_no);
}
