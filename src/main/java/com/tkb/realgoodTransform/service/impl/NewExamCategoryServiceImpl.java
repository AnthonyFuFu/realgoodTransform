package com.tkb.realgoodTransform.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.dao.NewExamCategoryDao;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.NewExamCategory;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.NewExamCategoryService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class NewExamCategoryServiceImpl extends BaseUtils implements NewExamCategoryService {
	private int pageNo;					//頁碼
	
	@Autowired
	private NewExamCategoryDao newExamCategoryDao;
	
	@Autowired
	private EditLogService editLogService;

	@Override
	public void index(NewExamCategory newExamCategory, Integer menu_id, Model model, HttpServletRequest request) {
		List<NewExamCategory> newExamCategoryList = new ArrayList<>();
		//從layout選單取得目前頁面的menu.id
		Integer menu_id_hidden = request.getParameter("menu_id") == null ? 0 : Integer.valueOf(request.getParameter("menu_id"));
		
		newExamCategory = newExamCategory == null ? new NewExamCategory() : newExamCategory;

		pageNo = setPage(pageNo, request);
		pageTotalCount = getCount(newExamCategory);
		pageNo = super.pageSetting(pageNo);
		newExamCategoryList = newExamCategoryDao.getList(pageCount, pageStart, newExamCategory);
		
		addModelAttribute(pageNo, model);
		model.addAttribute("newExamCategory", newExamCategory)
			 .addAttribute("newExamCategoryList",newExamCategoryList)
			 .addAttribute("menu_id", menu_id)
			 .addAttribute("menu_id_hidden", menu_id_hidden);
	}

	@Override
	public Integer getCount(NewExamCategory newExamCategory) {
		Integer count = Optional.ofNullable(newExamCategoryDao.getCount(newExamCategory)).orElse(null);
		return count;
	}

	@Override
	public void addPage(NewExamCategory newExamCategory, Model model, HttpServletRequest request) {
		Integer menu_id_hidden = request.getParameter("menu_id_hidden") == null ? 0 : Integer.valueOf(request.getParameter("menu_id_hidden"));
		model.addAttribute("newExamCategory", newExamCategory)
			 .addAttribute("menu_id_hidden", menu_id_hidden);	
	}

	@Override
	public ResponseEntity<?> checkNewExamCategory(String newExamCategoryName, Integer layer, HttpServletRequest request) {
		NewExamCategory newExamCategory = new NewExamCategory();
		newExamCategory.setName(newExamCategoryName);
		newExamCategory.setLayer(String.valueOf(layer));
		List<NewExamCategory> checkList = newExamCategoryDao.checkNewExamCategory(newExamCategory);
		if(checkList.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	@Override
	public void add(NewExamCategory newExamCategory, User user, Model model) {
		String message = "";
		try {
			if(null == newExamCategory.getParent_id())newExamCategory.setParent_id(0);
			newExamCategory.setCreate_by(user.getAccount());
			newExamCategory.setId(newExamCategoryDao.getNextId());
			newExamCategoryDao.add(newExamCategory);
			Gson gson = new Gson();
			String jsonString = gson.toJson(newExamCategory);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(newExamCategory.getId());
			editLog.setFunction("NEW_EXAM_CATEGORY");
			editLog.setAction_type("ADD");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			message = "新增成功";
		} catch (Exception e) {
			e.printStackTrace();
			message = "新增失敗";
		}
		model.addAttribute("parent_id", newExamCategory.getParent_id());
		model.addAttribute("layer", newExamCategory.getLayer());
		model.addAttribute("link", "index");
		model.addAttribute("message", message);
		
	}

	@Override
	public ResponseEntity<?> checkData(String checkList) {
		String[] checkArray = checkList.split(",");
		String checkResult = "T";
		for (String s : checkArray) {
			NewExamCategory newExamCategory = new NewExamCategory();
			newExamCategory.setParent_id(Integer.parseInt(s));
			List<NewExamCategory> subList = getSubList(newExamCategory);
			if(subList.size() > 0) {
				checkResult = "T";
				break;
			}else {
				checkResult = "F";
			}
		}

		if ("T".equals(checkResult)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@Override
	public List<NewExamCategory> getSubList(NewExamCategory newExamCategory) {
		return newExamCategoryDao.getSubList(newExamCategory);
	}

	@Override
	public ResponseEntity<?> delete(String deleteList,User user) {
		String[] deleteArray = deleteList.split(",");
		Integer count ;
		for(int i = 0 ; i<deleteArray.length ; i++) {
			NewExamCategory newExamCategory = new NewExamCategory();
			newExamCategory.setId(Integer.valueOf(deleteArray[i]));
			count = deleteCheck(newExamCategory);
			if(count != 0 ) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			newExamCategory = newExamCategoryDao.getData(Integer.valueOf(deleteArray[i]));
			Gson gson = new Gson();
			String jsonString = gson.toJson(newExamCategory);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(newExamCategory.getId());
			editLog.setFunction("NEW_EXAM_CATEGORY");
			editLog.setAction_type("DELETE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			newExamCategoryDao.delete(newExamCategory);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	public Integer deleteCheck(NewExamCategory newExamCategory) {
		return newExamCategoryDao.deleteCheck(newExamCategory);
	}

	@Override
	public ResponseEntity<?> resetSort(Integer parent_id,User user) {
		try {
			newExamCategoryDao.resetSort(parent_id);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(0);
			editLog.setFunction("NEW_EXAM_CATEGORY");
			editLog.setAction_type("UPDATE");
			editLog.setContent("重新排序");
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void updatePage(Integer id, Model model) {
		NewExamCategory newExamCategory = newExamCategoryDao.getData(id);
		model.addAttribute("newExamCategory", newExamCategory);
	}

	@Override
	public void updateSubmit(NewExamCategory newExamCategory, User user, Model model) {
		String message = "";
		try {
			newExamCategory.setUpdate_by(user.getAccount());
			newExamCategoryDao.update(newExamCategory);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(newExamCategory);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(newExamCategory.getId());
			editLog.setFunction("NEW_EXAM_CATEGORY");
			editLog.setAction_type("UPDATE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			message = "更新成功";
		} catch (Exception e) {
			e.printStackTrace();
			message = "更新失敗";
		}
		model.addAttribute("parent_id", newExamCategory.getParent_id());
		model.addAttribute("layer", newExamCategory.getLayer());
		model.addAttribute("link", "index");
		model.addAttribute("message", message);
	}

	@Override
	public List<NewExamCategory> getLayerList(String layer, NewExamCategory newExamCategory) {
		// TODO Auto-generated method stub
		return newExamCategoryDao.getLayerList(layer, newExamCategory);
	}

	@Override
	public List<Map<String, Object>> getNewExamMenuMenu(NewExamCategory newExamCategory) {
		return newExamCategoryDao.getNewExamMenuMenu(newExamCategory);	
	}

	@Override
	public List<Map<String, Object>> getNormalList() {
		return newExamCategoryDao.getNormalList();
	}

	@Override
	public void updateNormalData(NewExamCategory newExamCategory) {
		newExamCategoryDao.updateNormalData(newExamCategory);
	}

}
