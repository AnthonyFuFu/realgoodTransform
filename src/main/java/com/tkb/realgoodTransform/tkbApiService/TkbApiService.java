package com.tkb.realgoodTransform.tkbApiService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.model.UserAccount;

public interface TkbApiService {

	//所有員工資料
	public void UserMasterList() throws Exception;

	/**
	 * ERP取得部門清單
	 * @param userAccount
	 * @param appName
	 * @param reqEncryptKey
	 * @param repDecryptKey
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> departmentList(UserAccount userAccount, String appName, String reqEncryptKey,
			String repDecryptKey);

	/**
	 * ERP取得單位清單
	 * @param userAccount
	 * @param appName
	 * @param reqEncryptKey
	 * @param repDecryptKey
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> unitList(UserAccount userAccount, String appName, String reqEncryptKey, String repDecryptKey);
	
	/**
	 * 登入
	 * @param User user
	 * @return Map userMasterList
	 */
	@SuppressWarnings("rawtypes")
	public Map UserMasterList(User user);

	/**
	 * 查當天新進員工清單
	 * @param beginDate
	 * @return newUserList
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> newUserList(LocalDate beginDate);
	
	/**
	 * 離職員工清單
	 * @param beginDate
	 * @return leaveUserList
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> leaveUser(LocalDate beginDate);

	/**
	 * 
	 * @param beginDate 
	 * @return modifiedUserList
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getModifiedUserList(LocalDate beginDate);
	
}
