package com.tkb.realgoodTransform.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.dao.WinnerCategoryDao;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.model.Winner;
import com.tkb.realgoodTransform.model.WinnerCategory;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.WinnerCategoryService;



@Service
public class WinnerCategoryServiceImpl implements WinnerCategoryService {

	@Autowired
	WinnerCategoryDao winnerCategoryDao;
	@Autowired
	private EditLogService editLogService;
	
	@Override
	public List<WinnerCategory> getSubList(WinnerCategory winnerCategory) {
		return winnerCategoryDao.getSubList(winnerCategory);
	}

	@Override
	public List<WinnerCategory> getLayerList(String layer, WinnerCategory winnerCategory) {
		return winnerCategoryDao.getLayerList(layer, winnerCategory);
	}

	@Override
	public List<WinnerCategory> getLayerList(String layer) {
		return winnerCategoryDao.getLayerList(layer);
	}

	@Override
	public List<WinnerCategory> getList(int pageCount, int pageStart, WinnerCategory winnerCategory) {
		return winnerCategoryDao.getList(pageCount, pageStart, winnerCategory);
	}

	@Override
	public Integer getCount(WinnerCategory winnerCategory) {
		return winnerCategoryDao.getCount(winnerCategory);
	}

	@Override
	public WinnerCategory getData(WinnerCategory winnerCategory) {
		return winnerCategoryDao.getData(winnerCategory);
	}

	@Override
	public WinnerCategory checkRepeat(WinnerCategory winnerCategory) {
		return winnerCategoryDao.checkRepeat(winnerCategory);
	}

	@Override
	public Integer getNextId() {
		return winnerCategoryDao.getNextId();
	}

	@Override
	public void add(WinnerCategory winnerCategory) {
		winnerCategoryDao.add(winnerCategory);

	}

	@Override
	public void update(WinnerCategory winnerCategory) {
		winnerCategoryDao.update(winnerCategory);

	}

	@Override
	public void delete(WinnerCategory winnerCategory, Integer id) {
		winnerCategoryDao.delete(winnerCategory, id);

	}

	@Override
	public void resetSort(WinnerCategory winnerCategory) {
		winnerCategoryDao.resetSort(winnerCategory);

	}

	@Override
	public void addSubmitFunction(WinnerCategory winnerCategory, User user) {
		if(winnerCategory.getParent_id() == null) {
			winnerCategory.setParent_id(0);
		}
		winnerCategory.setId(getNextId());
		winnerCategory.setCreate_by(user.getAccount());
		winnerCategory.setUpdate_by(user.getAccount());
		add(winnerCategory);
	}

	@Override
	public String checkRepeatFunction(String name, String layer, WinnerCategory winnerCategory) {
		String checkStr = "T";
		if(winnerCategory == null) {
			winnerCategory = new WinnerCategory();
		}
		winnerCategory.setName(name);
		winnerCategory.setLayer(layer);
		winnerCategory = checkRepeat(winnerCategory);
		if(winnerCategory != null) {
			checkStr = "F";
		} else {
			checkStr = "T";
		}
		return checkStr;
	}

	@Override
	public WinnerCategory updateFunction(WinnerCategory winnerCategory) {
		winnerCategory = getData(winnerCategory);
		return winnerCategory;
	}

	@Override
	public void updateSubmitFunction(WinnerCategory winnerCategory, User user) {
		winnerCategory.setUpdate_by(user.getAccount());
		update(winnerCategory);
		
	}

	@Override
	public void deleteFunction(WinnerCategory winnerCategory, String deleteList, User user)throws IOException {
		if(winnerCategory.getParent_id() == null) {
			winnerCategory.setParent_id(0);
		}
		String[] deleteArray = deleteList.split(",");
		Integer id = null;
		Integer count;
		for(int i = 0; i<deleteArray.length; i++) {
			id = Integer.valueOf(deleteArray[i]);
			winnerCategory.setId(id);
			count = deleteCheck(winnerCategory);
			if(count != 0 )throw new IOException();
			winnerCategory = getData(winnerCategory);
			Gson gson = new Gson();
			String jsonString = gson.toJson(winnerCategory);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(winnerCategory.getId());
			editLog.setFunction("WINNER_CATEGORY");
			editLog.setAction_type("DELETE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			delete(winnerCategory, id);
		}
	}
	
	public Integer deleteCheck(WinnerCategory winnerCategory) throws IOException{
		return winnerCategoryDao.deleteCheck(winnerCategory);
	}

	@Override
	public void resetSortFunction(WinnerCategory winnerCategory) {
		if(winnerCategory.getParent_id() == null) {
			winnerCategory.setParent_id(0);
		}
		resetSort(winnerCategory);
		
	}

	@Override
	public String checkDataFunction(WinnerCategory winnerCategory, Winner winner, String checkList) {
		List<WinnerCategory>winnerCategoryList = new ArrayList<>();
		String[] checkArray = checkList.split(",");
		String checkStr = "T";
		for(int i = 0 ; i<checkArray.length ; i++) {
			winnerCategory.setParent_id(Integer.valueOf(checkArray[i]));
			winnerCategoryList = getSubList(winnerCategory);
		}
		if( winnerCategoryList.size()>0) {
			checkStr = "F";
		}
		return checkStr;
	}

	@Override
	public String getCategoryName(String category, WinnerCategory winnerCategory) {
		return winnerCategoryDao.getCategoryName(category, winnerCategory);
	}

	
//	======
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		return winnerCategoryDao.getNormalList();
	}

	@Override
	public void updateNormalData(WinnerCategory winerCategory) {
		winnerCategoryDao.updateNormalData(winerCategory);
	}

}
