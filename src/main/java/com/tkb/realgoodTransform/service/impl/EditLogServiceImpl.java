package com.tkb.realgoodTransform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.EditLogDao;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.service.EditLogService;

@Service
public class EditLogServiceImpl implements EditLogService {
	
	@Autowired
	private EditLogDao editLogDao;
	
	@Override
	public void addLog(EditLog editlog) {
		editLogDao.addLog(editlog);
	}

	@Override
	public Integer getNextLogId() {
		return editLogDao.getNextLogId();
	}

}
