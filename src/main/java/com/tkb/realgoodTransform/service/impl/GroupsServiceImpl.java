package com.tkb.realgoodTransform.service.impl;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.dao.GroupsDao;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.GroupAction;
import com.tkb.realgoodTransform.model.GroupUser;
import com.tkb.realgoodTransform.model.Groups;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.GroupActionService;
import com.tkb.realgoodTransform.service.GroupUserService;
import com.tkb.realgoodTransform.service.GroupsService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class GroupsServiceImpl implements GroupsService {
	
	@Autowired
	private GroupsDao groupsDao;
	@Autowired
	private GroupActionService groupActionService;
	@Autowired
	private GroupUserService groupUserService;
	@Autowired
	private EditLogService editLogService;

	@Override
	public Integer getCount(Groups groups) {
		return groupsDao.getCount(groups);
	}

	@Override
	public List<Groups> getList(int pageCount, int pageStart, Groups groups) {
		return groupsDao.getList(pageCount, pageStart, groups);
	}

	@Override
	public Integer getNextId() {
		return groupsDao.getNextId();
	}

	@Override
	public void add(Groups groups) {
		groupsDao.add(groups);
	}

	@Override
	public Groups getGroup(Groups groups) {
		return groupsDao.getGroup(groups);
	}

	@Override
	public void delete(Groups groups, HttpServletRequest request,GroupAction groupAction, User user) {
		String deleteString = request.getParameter("deleteList");
		String[] deleteArray = deleteString.split(",");
		for(String deleteId : deleteArray) {
			groups.setId(Integer.valueOf(deleteId));
			groupAction.setGroup_id(groups.getId());
			
			Gson gson1 = new Gson();
			String jsonString1 = gson1.toJson(groupAction);
			EditLog editLog1 = new EditLog();
			int logId1 = editLogService.getNextLogId();
			editLog1.setId(logId1);
			editLog1.setData_id(groupAction.getGroup_id());
			editLog1.setFunction("GROUP_ACTION");
			editLog1.setAction_type("DELETE");
			editLog1.setContent(jsonString1);
			editLog1.setCreate_by(user.getAccount());
			editLogService.addLog(editLog1);
			
			groupActionService.delete(groupAction);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(groups);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(groups.getId());
			editLog.setFunction("GROUPS");
			editLog.setAction_type("DELETE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			groupsDao.delete(groups);
		}
	}

	@Override
	public List<Groups> getGroupsList() {
		return groupsDao.getGroupsList();
	}

	@Override
	public void getUpdatePageData(Groups groups, GroupAction groupAction, Model model, HttpServletRequest request) {
		groups = getGroup(groups);
		groupAction.setGroup_id(groups.getId());//將前台綁定在group的id設到group_action，取得權限清單
		List<GroupAction> groupActionList = groupActionService.getData(groupAction);
		StringBuffer menu_list = new StringBuffer();
		groupActionList.stream().forEach(gAction -> {
			menu_list.append(String.valueOf(gAction.getAciton_id() + ","));
		});
		if(!menu_list.toString().equals("")) {
			groups.setMenu_list(menu_list.toString().substring(0, menu_list.toString().length()-1));
		}
		model.addAttribute("groups", groups);
	}

	@Override
	public void updateSubmit(Groups groups, GroupAction groupAction, User user, JSONArray menu_list, Model model) {
		String message = "";
		try {
			groupAction.setGroup_id(groups.getId());
			//先刪除
			groupActionService.delete(groupAction);
			//後更新
			for(int i=0 ; i<menu_list.length() ; i++) {
			JSONObject jsonObj = menu_list.getJSONObject(i);
			Integer menu_id = Integer.parseInt(jsonObj.get("menu_id").toString());
			Integer action_val = Integer.parseInt(jsonObj.get("action_val").toString());
			String title = jsonObj.get("title").toString();
			if(action_val == 1) {
				groupAction = new GroupAction();
				groupAction.setGroup_id(groups.getId());
				groupAction.setAciton_id(menu_id);
				groupAction.setCreate_by(user.getAccount());
				groupAction.setUpdate_by(user.getAccount());
				groupActionService.update(groupAction);
				
				Gson gson1 = new Gson();
				String jsonString1 = gson1.toJson(groupAction);
				EditLog editLog1 = new EditLog();
				int logId1 = editLogService.getNextLogId();
				editLog1.setId(logId1);
				editLog1.setData_id(groupAction.getGroup_id());
				editLog1.setFunction("GROUP_ACTION");
				editLog1.setAction_type("UPDATE");
				editLog1.setContent(jsonString1);
				editLog1.setCreate_by(user.getAccount());
				editLogService.addLog(editLog1);
				
				groups.setName(title);
				groups.setUpdate_by(user.getAccount());
				groupsDao.update(groups);
				
				Gson gson = new Gson();
				String jsonString = gson.toJson(groups);
				EditLog editLog = new EditLog();
				int logId = editLogService.getNextLogId();
				editLog.setId(logId);
				editLog.setData_id(groups.getId());
				editLog.setFunction("GROUPS");
				editLog.setAction_type("UPDATE");
				editLog.setContent(jsonString);
				editLog.setCreate_by(user.getAccount());
				editLogService.addLog(editLog);
			}
		}
			message = "更新成功";
		} catch (Exception e) {
			e.printStackTrace();
			message = "更新失敗";
		}
		model.addAttribute("link", "index");
		model.addAttribute("message", message);
	}

	@Override
	public void addSubmit(Groups groups, GroupAction groupAction, JSONArray menu_list, User user, Model model) {
		String message = "";
		try {
			Integer nextId = getNextId();
				groups.setId(nextId);		//新增群組，設定群組id
				groupAction.setGroup_id(nextId);	//新增群組功能，將群組id對照到group_action的id
				groups.setCreate_by(user.getAccount());
				groups.setUpdate_by(user.getAccount());
				//新增群組
				add(groups);
				
				Gson gson = new Gson();
				String jsonString = gson.toJson(groups);
				EditLog editLog = new EditLog();
				int logId = editLogService.getNextLogId();
				editLog.setId(logId);
				editLog.setData_id(groups.getId());
				editLog.setFunction("GROUPS");
				editLog.setAction_type("ADD");
				editLog.setContent(jsonString);
				editLog.setCreate_by(user.getAccount());
				editLogService.addLog(editLog);
		for(int i=0 ; i<menu_list.length() ; i++) {
			JSONObject jsonObj = menu_list.getJSONObject(i);
			Integer menu_id = Integer.parseInt(jsonObj.get("menu_id").toString());
			Integer action_val = Integer.parseInt(jsonObj.get("action_val").toString());
			
			if(action_val == 1) {
				groupAction = new GroupAction();
				groupAction.setGroup_id(groups.getId());
				groupAction.setAciton_id(menu_id);
				groupAction.setCreate_by(user.getAccount());
				groupAction.setUpdate_by(user.getAccount());
				groupActionService.add(groupAction);
				
				Gson gson1 = new Gson();
				String jsonString1 = gson1.toJson(groupAction);
				EditLog editLog1 = new EditLog();
				int logId1 = editLogService.getNextLogId();
				editLog1.setId(logId1);
				editLog1.setData_id(groupAction.getGroup_id());
				editLog1.setFunction("GROUP_ACTION");
				editLog1.setAction_type("ADD");
				editLog1.setContent(jsonString1);
				editLog1.setCreate_by(user.getAccount());
				editLogService.addLog(editLog1);
			}
		}
		message = "新增成功";
		} catch (Exception e) {
			e.printStackTrace();
			message = "新增失敗";
		}
		model.addAttribute("link", "index");
		model.addAttribute("message", message);
	}

	@Override
	public ResponseEntity<?> checkData(GroupUser groupUser, HttpServletRequest request) {
		String checkList = request.getParameter("checkList");
		String[] checkArray = checkList.split(",");
		String checkStr = "T";
		for (String checkId : checkArray) {
			groupUser.setGroup_id(Integer.valueOf(checkId));
			List<GroupUser> groupData = groupUserService.getData(groupUser);
			if(groupData.size() > 0) {
				checkStr = "F";
				break;
			}
		}
		return checkStr.equals("T") ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.OK);
	}

}
