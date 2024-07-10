package com.tkb.realgoodTransform.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.dao.UserAccountDao;
import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.GroupUser;
import com.tkb.realgoodTransform.model.Groups;
import com.tkb.realgoodTransform.model.Location;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.model.UserAccount;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.GroupUserService;
import com.tkb.realgoodTransform.service.GroupsService;
import com.tkb.realgoodTransform.service.UserAccountService;
import com.tkb.realgoodTransform.utils.AreaLocationApi;

import jakarta.servlet.http.HttpServletRequest;


@Service
public class UserAccountServiceImpl implements UserAccountService {

	@Autowired
	private UserAccountDao userAccountDao;
	@Autowired
	private GroupsService groupsService;
	@Autowired
	private GroupUserService groupUserService;
	@Autowired
	private EditLogService editLogService;

	@Override
	//tkbApi新增全部使用者名單(初始化)
	public void insert(UserAccount userAccount) {
		userAccountDao.insert(userAccount);
	}

	@Override
	public int getCount(User user) {
		return userAccountDao.getCount(user);
	}

	@Override
	public List<User> getList(int pageCount, int pageStart, User user) {
		return userAccountDao.getList(pageCount, pageStart, user);
	}
	
	@Override
	public User getDataByAccount(User user) {
		return userAccountDao.getDataByAccount(user);
	}
	
	@Override
	public void getUpdatePageData(Model model, User user, Area area) {
		
		AreaLocationApi areaLocationApi = new AreaLocationApi();
		Location location = new Location();
		String website = "D";
		
		List<Groups> groupsList = groupsService.getGroupsList();
		List<Area> areaTempList = new ArrayList<>();
		List<Area> apiAreaList = areaLocationApi.jsonToAreaList(areaLocationApi.getApiArea());
		List<Area> areaList = apiAreaList;
		
		user = getDataById(user);
		user.setGroup_id(groupUserService.getGroupId(user.getId()));
		
		for(int i=0; i<areaList.size(); i++) {
			Area areaTemp = areaList.get(i);
			if(areaTemp != null) {
				String apiLocation = areaLocationApi.getApiLocation(website, areaTemp.getId().toString());
				if(apiLocation != null && !"".equals(apiLocation)) {
					areaTempList.add(areaTemp);
				}
			}
		}
		areaList = areaTempList;
		
		List<Location> allLocationList = areaLocationApi.jsonToLocationList(areaLocationApi.getApiLocation("0","1"));
		for(int i=0; i<allLocationList.size(); i++) {
			if(allLocationList.get(i).getId().toString().equals(user.getArea())) {
				location = allLocationList.get(i);
			}
		}
		
		if(user.getArea() == null) {
			area.setId(Integer.valueOf("0"));
		} else if ("0".equals(user.getArea())) {
			area.setId(Integer.valueOf("1"));
			Location tempLocation = new Location();
			tempLocation.setId(Integer.valueOf(user.getArea()));
			tempLocation.setName("全區");
			location = tempLocation;
		} else {
			for(int i=0; i<allLocationList.size(); i++) {
				if(allLocationList.get(i).getId().toString().equals(user.getArea())) {
					area.setId(Integer.valueOf(allLocationList.get(i).getArea_id().toString()));
					location = allLocationList.get(i);
				}
			}
		}
		
		model.addAttribute("user", user)
		.addAttribute("areaList", areaList)
		.addAttribute("area", area)
		.addAttribute("location", location)
		.addAttribute("groupsList", groupsList);
	}

	@Override
	public User getDataById(User user) {
		return userAccountDao.getDataById(user);
	}

	@Override
	public List<Location> getLocationList(HttpServletRequest request) {
		String id = request.getParameter("id");
		
		AreaLocationApi areaLocationApi = new AreaLocationApi();
		List<Location> locationList = new ArrayList<>();
		String website = "D";
		List<Location> tempLocationList = new ArrayList<>();

		if ("1".equals(id)) {
			Location tempLocation = new Location();
			tempLocation.setId(Integer.valueOf("0"));
			tempLocation.setName("全區");
			tempLocationList.add(tempLocation);
			locationList = tempLocationList;
		} else {
			String location = areaLocationApi.getApiLocation(website, id);
			List<Location> apiLocationList = areaLocationApi.jsonToLocationList(location);
			locationList = apiLocationList;
		}
		return locationList;
	}

	@Override
	public void updateUserInfo(Model model, User user, User userAccountSession) {
		user.setUpdate_by(userAccountSession.getAccount());
		
		//設定更新完回傳顯示頁面部門清單
		User userAccount = new User();
		userAccount.setDepartment_no(user.getDepartment_no());
		
		//更新group_user
		//先檢查使用者有沒有被加入過群組，沒有的話用add，已有群組的話用update
		GroupUser groupUser = new GroupUser();
		groupUser.setGroup_id(user.getGroup_id());
		groupUser.setUser_id(user.getId());
		groupUser.setCreate_by(userAccountSession.getAccount());
		groupUser.setUpdate_by(userAccountSession.getAccount());
		Integer count = groupUserService.checkUser(groupUser);
		if(count == 0) {
			//新增group_user
			groupUserService.add(groupUser);
			Gson gson = new Gson();
			String jsonString = gson.toJson(groupUser);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(groupUser.getUser_id());
			editLog.setFunction("GROUP_USER");
			editLog.setAction_type("ADD");
			editLog.setContent(jsonString);
			editLog.setCreate_by(userAccountSession.getAccount());
			editLogService.addLog(editLog);
		}else {
			//更新group_user
			groupUserService.update(groupUser);
			Gson gson = new Gson();
			String jsonString = gson.toJson(groupUser);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(groupUser.getUser_id());
			editLog.setFunction("GROUP_USER");
			editLog.setAction_type("UPDATE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(userAccountSession.getAccount());
			editLogService.addLog(editLog);
		}
		//更新user_account的地區
		userAccountDao.update(user);
		Gson gson = new Gson();
		String jsonString = gson.toJson(user);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(user.getId());
		editLog.setFunction("USER_ACCOUNT");
		editLog.setAction_type("UPDATE");
		editLog.setContent(jsonString);
		editLog.setCreate_by(userAccountSession.getAccount());
		editLogService.addLog(editLog);
		
		model.addAttribute("userAccount", userAccount);
	}

	@Override
	public List<String> getUserMenuList(User userAccountSession, String link) {
		return userAccountDao.getUserMenuList(userAccountSession, link);
	}

	@Override
	public User login(User user) {
		return userAccountDao.login(user);
	}

	@Override
	public void updateStatus(User user) {
		userAccountDao.updateStatus(user);
	}

	@Override
	public void scheduleUpdate(User user) {
		userAccountDao.scheduleUpdate(user);
	}
	
	@Override
	public Integer getNextId() {
		return userAccountDao.getNextId();
	};

	@Override
	public Map<String, Object> getLocationNoByAccount(String account) {
		return userAccountDao.getLocationNoByAccount(account);
	}

	@Override
	public void insertAndUpdate(User user) {
		userAccountDao.insertAndUpdate(user);
	}

	@Override
	public List<Map<String, Object>> getEmployeeList(User user) {
		return userAccountDao.getEmployeeList(user);
	}

	@Override
	public void updateLeaveUserStatus(String employee_no) {
		userAccountDao.updateLeaveUserStatus(employee_no);
	}

}
