package com.tkb.realgoodTransform.dao;

import java.util.List;

import com.tkb.realgoodTransform.model.Groups;

public interface GroupsDao {

	public Integer getCount(Groups groups);
	public List<Groups> getList(int pageCount, int pageStart, Groups groups);
	public Integer getNextId();
	public void add(Groups groups);
	public Groups getGroup(Groups groups);
	public void delete(Groups groups);
	public List<Groups> getGroupsList();
	public void update(Groups groups);

}
