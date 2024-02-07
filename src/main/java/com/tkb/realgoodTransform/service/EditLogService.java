package com.tkb.realgoodTransform.service;

import com.tkb.realgoodTransform.model.EditLog;

public interface EditLogService {
	
	public void addLog(EditLog editlog);
	public Integer getNextLogId();
	
}
