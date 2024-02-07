package com.tkb.realgoodTransform.service;

import java.util.List;

import com.tkb.realgoodTransform.model.GroupUser;

public interface GroupUserService {
	
	public void add(GroupUser groupUser);
	public void update(GroupUser groupUser);
	public Integer checkUser(GroupUser groupUser);
	public Integer getGroupId(Integer id);
	public List<GroupUser> getData(GroupUser groupUser);

}
