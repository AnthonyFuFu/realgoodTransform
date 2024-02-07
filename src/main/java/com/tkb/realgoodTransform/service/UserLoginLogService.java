package com.tkb.realgoodTransform.service;

/**
 * 使用者記錄Service介面接口
 * @author Joshua
 * @version 創建時間：2022-04-11
 */
public interface UserLoginLogService {
	
	
	/**
	 * 記錄登入Log
	 * @param loginStatus, account, ip
	 */
	public void addUserLoginLog(String loginStatus, String account, String ip);


}
