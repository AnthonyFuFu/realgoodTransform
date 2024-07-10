package com.tkb.realgoodTransform.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.GroupActionDao;
import com.tkb.realgoodTransform.model.GroupAction;
import com.tkb.realgoodTransform.service.GroupActionService;



@Service
public class GroupActionServiceImpl implements GroupActionService {
	
	@Autowired
	private GroupActionDao groupActionDao;

	@Override
	public void add(GroupAction groupAction) {
		groupActionDao.add(groupAction);
	}

	@Override
	public List<GroupAction> getData(GroupAction groupAction) {
		return groupActionDao.getData(groupAction);
	}
	
	@Override
	public void delete(GroupAction groupAction) {
		groupActionDao.delete(groupAction);
		
	}

	@Override
	public void update(GroupAction groupAction) {
		groupActionDao.update(groupAction);
	}

}
