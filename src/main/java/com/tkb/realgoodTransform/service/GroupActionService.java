package com.tkb.realgoodTransform.service;

import java.util.List;

import com.tkb.realgoodTransform.model.GroupAction;

public interface GroupActionService {
	
	public void add(GroupAction groupAction);
	public List<GroupAction> getData(GroupAction groupAction);
	public void delete(GroupAction groupAction);
	public void update(GroupAction groupAction);

}
