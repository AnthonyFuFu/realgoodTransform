package com.tkb.realgoodTransform.dao;

import com.tkb.realgoodTransform.model.UserLoginLog;

/**
 * 使用者記錄Dao介面接口
 * @author Joshua
 * @version 創建時間：2022-04-11
 */
public interface UserLoginLogDao {
	
	/**
	 * 記錄登入Log
	 * @param userLoginLog
	 */
	public void addUserLoginLog(UserLoginLog userLoginLog);

}
