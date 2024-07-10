package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;



import org.springframework.ui.Model;

import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.Location;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.model.UserAccount;

import jakarta.servlet.http.HttpServletRequest;




public interface UserAccountService {
	
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
	 * @param userAccount
	 * @return UserAccount
	 */
	public User getDataByAccount(User user);

	/**
	 * 帳號功能頁
	 * @param model, user, area
	 * @return model
	 */
	public void getUpdatePageData(Model model, User user, Area area);
	
	/**
	 * 取得單筆帳號設定
	 * @param user
	 * @return User
	 */
	public User getDataById(User user);

	/**
	 * 取得地區清單
	 * @param request, area
	 * @return List<Area>
	 */
	public List<Location> getLocationList(HttpServletRequest request);

	/**
	 * 更新使用者資料
	 * @param model, user, groups
	 * @return model
	 */
	public void updateUserInfo(Model model, User user, User userAccountSession);
	
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
	 * 用員編取得location_no
	 * @param account
	 */
	Map<String, Object> getLocationNoByAccount(String account);
	
	/**
	 * 排程用來修改員工狀態為離職
	 * 
	 */
	public void updateLeaveUserStatus(String employee_no);

}
