package com.tkb.realgoodTransform.service;

import java.util.List;

import org.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.tkb.realgoodTransform.model.GroupAction;
import com.tkb.realgoodTransform.model.GroupUser;
import com.tkb.realgoodTransform.model.Groups;
import com.tkb.realgoodTransform.model.User;

import jakarta.servlet.http.HttpServletRequest;

public interface GroupsService {
	
	public Integer getCount(Groups groups);
	public List<Groups> getList(int pageCount, int pageStart, Groups groups);
	public Integer getNextId();
	public void add(Groups groups);
	public Groups getGroup(Groups groups);
	public void delete(Groups groups, HttpServletRequest request,GroupAction groupAction, User user);
	public List<Groups> getGroupsList();
	public void getUpdatePageData(Groups groups, GroupAction groupAction, Model model, HttpServletRequest request);
	public void updateSubmit(Groups groups, GroupAction groupAction, User user, JSONArray menu_list, Model model);
	public void addSubmit(Groups groups, GroupAction groupAction, JSONArray menu_list, User user, Model model);
	public ResponseEntity<?> checkData(GroupUser groupUser, HttpServletRequest request);

}
