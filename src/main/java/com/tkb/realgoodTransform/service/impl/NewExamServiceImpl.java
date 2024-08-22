package com.tkb.realgoodTransform.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.dao.NewExamDao;
import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.NewExam;
import com.tkb.realgoodTransform.model.NewExamCategory;
import com.tkb.realgoodTransform.model.NewExamContent;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.NewExamCategoryService;
import com.tkb.realgoodTransform.service.NewExamContentService;
import com.tkb.realgoodTransform.service.NewExamService;
import com.tkb.realgoodTransform.utils.CryptographyUtils;

import jakarta.servlet.http.HttpServletRequest;



@Service
public class NewExamServiceImpl implements NewExamService{

	@Autowired
	private NewExamDao newExamDao;
	@Autowired
	private NewExamCategoryService newExamCategoryService;
	@Autowired
	private NewExamContentService newExamContentService;
	@Autowired
	private EditLogService editLogService;
	@Override
	public List<NewExam> getList(int pageCount, int pageStart, NewExam newExam) {
		return newExamDao.getList(pageCount, pageStart, newExam);
	}

	@Override
	public List<NewExam> getFrontList(NewExam newExam) {
		return newExamDao.getFrontList(newExam);
	}

	@Override
	public List<NewExam> getFrontList(int pageCount, int pageStart, NewExam newExam, String search_sort) {
		return newExamDao.getFrontList(pageCount, pageStart, newExam, search_sort);
	}

	@Override
	public List<NewExam> getFrontList(int pageCount, int pageStart, NewExam newExam, List<Area> areaList,
			String search_sort) {
		return null;
	}

	@Override
	public List<NewExam> getFrontList() {
		return newExamDao.getFrontList();
	}

	@Override
	public Integer getCount(NewExam newExam) {
		return newExamDao.getCount(newExam);
	}

	@Override
	public Integer getFrontCount(NewExam newExam) {
		return newExamDao.getFrontCount(newExam);
	}

	@Override
	public Integer getFrontCount(NewExam newExam, List<Area> areaList) {
		return null;
	}

	@Override
	public NewExam getData(NewExam newExam) {
		return newExamDao.getData(newExam);
	}

	@Override
	public NewExam getFrontData(NewExam newExam) {
		return newExamDao.getFrontData(newExam);
	}

	@Override
	public Integer getNextId() {
		return newExamDao.getNextId();
	}

	@Override
	public void add(NewExam newExam) {
		newExamDao.add(newExam);
	}

	@Override
	public void update(NewExam newExam) {
		newExamDao.update(newExam);
	}

	@Override
	public void updateClickRate(NewExam newExam) {
		newExamDao.updateClickRate(newExam);
	}

	@Override
	public void delete(NewExam newExam,Integer id) {
		newExamDao.delete(newExam,id);
	}

	@Override
	public List<NewExam> getList(NewExam newExam) {
		return null;
	}

	@Override
	public void updateId(NewExam newExam) {

	}

	@Override
	public void updateIndex_Sort(NewExam newExam) {

	}

	@Override
	public List<NewExamCategory> add(NewExamCategory newExamCategory) {
		List<NewExamCategory>newExamCategoryList = new ArrayList<>();
		newExamCategory.setParent_id(0);
		newExamCategoryList = newExamCategoryService.getLayerList("1",newExamCategory);
		return newExamCategoryList;
	}

	@Override
	public void addSubmit(NewExam newExam, NewExamContent newExamContent, User user, HttpServletRequest request,Model model) {
		String message = "";
		try {
		String[] iconList = request.getParameter("iconList") == null ? null : request.getParameter("iconList").split(",;,");
		String[] contentTitleList = request.getParameter("contentTitleList") == null ? null : request.getParameter("contentTitleList").split(",;,");
		String[] contentContentList = request.getParameter("contentContentList") == null ? null : request.getParameter("contentContentList").split(",;,");
		
		if(iconList == null)throw new NullPointerException();
		if(contentTitleList == null)throw new NullPointerException();
		if(contentContentList == null)throw new NullPointerException();
		
		int id = getNextId();
		newExam.setId(id);
		newExam.setCreate_by(user.getAccount());
		newExam.setUpdate_by(user.getAccount());
		newExam.setEncrypturl(CryptographyUtils.encryptStr(String.valueOf(newExam.getId())));
		add(newExam);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(newExam);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(newExam.getId());
		editLog.setFunction("NEW_EXAM");
		editLog.setAction_type("ADD");
		editLog.setContent(jsonString);
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		
		if(iconList.length==contentTitleList.length && iconList.length==contentContentList.length) {
			newExamContent.setCreate_by(user.getAccount());
			newExamContent.setUpdate_by(user.getAccount());
			for(int i=0; i<iconList.length; i++) {
				newExamContent.setId(newExamContentService.getNextId());
				newExamContent.setNewExam_id(id);
				newExamContent.setIcon(iconList[i]);
				newExamContent.setTitle(contentTitleList[i]);
				newExamContent.setContent(contentContentList[i]);
				newExamContentService.add(newExamContent);
			}
		}
		message = "新增成功";
		} catch (NullPointerException ee) {
		ee.printStackTrace();
		message = "新增失敗,請輸入ICON,內容主題,內文";
		} catch (Exception e) {
		e.printStackTrace();
		message = "新增失敗";
		}
		model.addAttribute("message", message);
	}

	@Override
	public void update(NewExam newExam, NewExamCategory newExamCategory, NewExamContent newExamContent,
			Model model) {
		Map<String, Object>map = new HashMap<>();
		List<NewExamCategory>newExamCategoryList = new ArrayList<>();
		List<NewExamContent>newExamContentList = new ArrayList<>();
		newExamCategory.setParent_id(0);
		newExamCategoryList = newExamCategoryService.getLayerList("1", newExamCategory);
		newExam = getData(newExam);
		newExamContent.setNewExam_id(newExam.getId());
		newExamContentList = newExamContentService.getList(newExamContent);
		map.put("newExamContentList", newExamContentList);
		map.put("newExamCategoryList", newExamCategoryList);
		map.put("newExam", newExam);
		model.addAttribute("newExamContentList", map.get("newExamContentList"));
		model.addAttribute("newExamCategoryList", map.get("newExamCategoryList"));
		model.addAttribute("newExam", map.get("newExam"));
	}

	@Override
	public void updateSubmit(NewExam newExam, NewExamContent newExamContent, User user, HttpServletRequest request,
			Model model) {
		List<NewExamContent>newExamContentList = new ArrayList<>();
		String message = "";
		try {
		String[] iconList = request.getParameter("iconList") == null ? null : request.getParameter("iconList").split(",;,");
		String[] contentTitleList = request.getParameter("contentTitleList") == null ? null : request.getParameter("contentTitleList").split(",;,");
		String[] contentContentList = request.getParameter("contentContentList") == null ? null : request.getParameter("contentContentList").split(",;,");
		String[] newExamContentIdList = request.getParameter("newExamContentIdList") == null ? null : request.getParameter("newExamContentIdList").split(",;,");
		String deleteIdList = "";
		
		if(iconList == null)throw new NullPointerException();
		if(contentTitleList == null)throw new NullPointerException();
		if(contentContentList == null)throw new NullPointerException();
		if(newExamContentIdList == null)throw new NullPointerException();
		
		
			if(iconList.length==contentTitleList.length && iconList.length==contentContentList.length && iconList.length==newExamContentIdList.length) {
			newExamContent.setNewExam_id(newExam.getId());
			newExamContentList = newExamContentService.getList(newExamContent);
			//取得刪除ID
			for(int i=0; i<newExamContentList.size(); i++) {
				for(int j=0; j<newExamContentIdList.length; j++) {
					if(String.valueOf(newExamContentList.get(i).getId()).equals(newExamContentIdList[j])) {
						break;
					} else {
						if(j == (newExamContentIdList.length-1)) {
							if("".equals(deleteIdList)) {
								deleteIdList += newExamContentList.get(i).getId();
							} else {
								deleteIdList = deleteIdList + ",;," + newExamContentList.get(i).getId();
							}
						}
					}
				}
			}
			//新增修改
			for(int i=0; i<iconList.length; i++) {
				newExamContent.setIcon(iconList[i]);
				newExamContent.setTitle(contentTitleList[i]);
				newExamContent.setContent(contentContentList[i]);
				newExamContent.setCreate_by(user.getAccount());
				newExamContent.setUpdate_by(user.getAccount());
				//新增
				if("0".equals(newExamContentIdList[i])) {
					newExamContent.setId(newExamContentService.getNextId());
					newExamContent.setNewExam_id(newExam.getId());
					newExamContentService.add(newExamContent);
				//修改
				} else {
					newExamContent.setId(Integer.valueOf(newExamContentIdList[i]));
					newExamContentService.update(newExamContent);
				}
			}
			//刪除
			String[] deleteIdArray = deleteIdList.split(",;,");
			if(!"".equals(deleteIdArray[0])) {
				for(int i=0; i<deleteIdArray.length; i++) {
					newExamContentService.delete(Integer.valueOf(deleteIdArray[i]));
				}
			}
		}
			newExam.setUpdate_by(user.getAccount());
			update(newExam);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(newExam);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(newExam.getId());
			editLog.setFunction("NEW_EXAM");
			editLog.setAction_type("UPDATE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			message = "修改成功";
		} catch (NullPointerException ee) {
			ee.printStackTrace();
			message = "修改失敗,請輸入ICON,內容主題,內文";
		} catch (Exception e) {
			e.printStackTrace();
			message = "修改失敗";
		}
		model.addAttribute("message", message);
	}

	@Override
	public void delete(NewExam newExam, NewExamContent newExamContent, String deleteList, User user) {
		List<NewExamContent>newExamContentList = new ArrayList<>();
		String[] deleteArray = deleteList.split(",");
		for(int i = 0; i<deleteArray.length; i++) {
			newExamContent.setNewExam_id(Integer.valueOf(deleteArray[i]));
			newExamContentList = newExamContentService.getList(newExamContent);
			for (int j = 0; j<newExamContentList.size(); j++) {
				newExamContentService.delete(newExamContentList.get(j).getId());
			}
		newExam.setId(Integer.valueOf(deleteArray[i]));
		newExam = getData(newExam);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(newExam);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(newExam.getId());
		editLog.setFunction("NEW_EXAM");
		editLog.setAction_type("DELETE");
		editLog.setContent(jsonString);
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		
		delete(newExam,Integer.valueOf(deleteArray[i]));
		}
	}

	@Override
	public List<Map<String, Object>> getNormalList() {
		return newExamDao.getNormalList();
	}

	@Override
	public void updateNormalData(NewExam newExam) {
		newExamDao.updateNormalData(newExam);
	}


	
}
