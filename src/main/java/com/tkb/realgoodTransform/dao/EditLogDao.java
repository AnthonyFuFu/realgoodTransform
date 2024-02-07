package com.tkb.realgoodTransform.dao;

import com.tkb.realgoodTransform.model.EditLog;

public interface EditLogDao {
	
	public void addLog(EditLog editLog);
	public Integer getNextLogId();
	
}
