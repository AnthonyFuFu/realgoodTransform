package com.tkb.realgoodTransform.dao;

import com.tkb.realgoodTransform.model.EditLog;

public interface EditLogDao {
	
	/**
	 * 新增各功能編輯LOG
	 * @param editLog
	 */
	public void addLog(EditLog editLog);
	
	/**
	 * 取得各功能編輯LOG下一筆ID
	 * @return Integer
	 */
	public Integer getNextLogId();
}
