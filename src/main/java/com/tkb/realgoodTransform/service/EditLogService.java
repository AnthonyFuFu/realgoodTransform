package com.tkb.realgoodTransform.service;

import com.tkb.realgoodTransform.model.EditLog;

public interface EditLogService {
	
	/**
	 * 新增各功能編輯LOG
	 * @param editLog
	 */
	public void addLog(EditLog editlog);
	
	/**
	 * 取得各功能編輯LOG下一筆ID
	 * @return Integer
	 */
	public Integer getNextLogId();
}
