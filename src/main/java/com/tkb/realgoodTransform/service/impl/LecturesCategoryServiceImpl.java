package com.tkb.realgoodTransform.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.dao.LecturesCategoryDao;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.LecturesCategory;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.LecturesCategoryService;


@Service
public class LecturesCategoryServiceImpl implements LecturesCategoryService {

	@Autowired
	private LecturesCategoryDao lecturesCategoryDao;
	@Autowired
	private EditLogService editLogService;
	@Override
	public List<LecturesCategory> getSubList(LecturesCategory lecturesCategory) {
		// TODO Auto-generated method stub修改頁面
		return lecturesCategoryDao.getSubList(lecturesCategory);
	}

	@Override
	public List<LecturesCategory> getLayerList(String layer, LecturesCategory lecturesCategory) {
		// TODO Auto-generated method stub
		return lecturesCategoryDao.getLayerList(layer, lecturesCategory);
	}

	@Override
	public List<LecturesCategory> getList(int pageCount, int pageStart, LecturesCategory lecturesCategory) {
		// TODO Auto-generated method stub
		return lecturesCategoryDao.getList(pageCount, pageStart, lecturesCategory);
	}

	@Override
	public Integer getCount(LecturesCategory lecturesCategory) {
		// TODO Auto-generated method stub
		return lecturesCategoryDao.getCount(lecturesCategory);
	}

	@Override
	public LecturesCategory getData(LecturesCategory lecturesCategory) {
		// TODO Auto-generated method stub
		return lecturesCategoryDao.getData(lecturesCategory);
	}

	@Override
	public Integer getNextId() {
		// TODO Auto-generated method stub
		return lecturesCategoryDao.getNextId();
	}

	@Override
	public void add(LecturesCategory lecturesCategory) {
		// TODO Auto-generated method stub
		lecturesCategoryDao.add(lecturesCategory);
	}

	@Override
	public void update(LecturesCategory lecturesCategory) {
		// TODO Auto-generated method stub
		lecturesCategoryDao.update(lecturesCategory);
	}

	@Override
	public void delete(LecturesCategory lecturesCategory, Integer id) {
		// TODO Auto-generated method stub
		lecturesCategoryDao.delete(lecturesCategory, id);

	}

	@Override
	public String checkLecturesCategory(LecturesCategory lecturesCategory) {
		// TODO Auto-generated method stub
		return lecturesCategoryDao.checkLecturesCategory(lecturesCategory);
	}

	@Override
	public void resetSort(LecturesCategory lecturesCategory) {
		// TODO Auto-generated method stub
		lecturesCategoryDao.resetSort(lecturesCategory);
	}

	@Override
	public void addSubmitFunction(LecturesCategory lecturesCategory, User user) {
		if(lecturesCategory.getParent_id() == null) {
			lecturesCategory.setParent_id(0);
		}
		lecturesCategory.setId(getNextId());
		lecturesCategory.setCreate_by(user.getAccount());
		lecturesCategory.setUpdate_by(user.getAccount());
		add(lecturesCategory);
		
	}

	@Override
	public ResponseEntity<?> checkLecturesCategoryFunction(LecturesCategory lecturesCategory, String layer, String lecturesCategoryName) {
		String msg = "";
		 lecturesCategory.setName(lecturesCategoryName);
		 lecturesCategory.setLayer(layer);
		 String returnLectureCategoryName = checkLecturesCategory(lecturesCategory);
		 if(returnLectureCategoryName == null) {
			 msg = "true";
		 }else {
			 msg = "false";
		 }
		return new ResponseEntity<String>(msg,HttpStatus.OK);
	}

	@Override
	public LecturesCategory updateFunction(LecturesCategory lecturesCategory) {
		lecturesCategory = getData(lecturesCategory);
		return lecturesCategory;
	}

	@Override
	public void updateSubmitFunction(LecturesCategory lecturesCategory, User user) {
		lecturesCategory.setUpdate_by(user.getAccount());
		update(lecturesCategory);
	}

	@Override
	public void deleteFunction(LecturesCategory lecturesCategory, String selectList, User user) throws IOException {
		String[] selectArray = selectList.split(",");
		Integer id = null;
		Integer count ;
		if(lecturesCategory.getParent_id() == null) {
			lecturesCategory.setParent_id(0);
		}
		for(int i = 0 ; i<selectArray.length ; i++) {
			id=Integer.valueOf(selectArray[i]);
			lecturesCategory.setId(id);
			count = deleteCheck(lecturesCategory);
			if(count != 0)throw new IOException();
			lecturesCategory = getData(lecturesCategory);
			Gson gson = new Gson();
			String jsonString = gson.toJson(lecturesCategory);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(lecturesCategory.getId());
			editLog.setFunction("LECTURES_CATEGORY");
			editLog.setAction_type("DELETE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			delete(lecturesCategory, id);
		}
		
	}

	@Override
	public ResponseEntity<?> checkData(LecturesCategory lecturesCategory,String checkList) {
		List<LecturesCategory>lecturesCategoryList = new ArrayList<>();
		String[] checkArray = checkList.split(",");
		String checkStr = "T";
		for (int i = 0; i<checkArray.length ; i++) {
			lecturesCategory.setParent_id(Integer.valueOf(checkArray[i]));
			lecturesCategoryList = getSubList(lecturesCategory);
			if(lecturesCategoryList.size()>0) {
				checkStr = "F";
				break;
			}
		}
		return new ResponseEntity<String>(checkStr,HttpStatus.OK);
	}

	@Override
	public void resetSortFunction(LecturesCategory lecturesCategory) {
		if(lecturesCategory.getParent_id() == null) {
			lecturesCategory.setParent_id(0);
		}
		resetSort(lecturesCategory);
		
	}

	@Override
	public List<LecturesCategory> getFrontList(LecturesCategory lecturesCategory) {
		// TODO Auto-generated method stub
		return lecturesCategoryDao.getFrontList(lecturesCategory);
	}

	@Override
	public List<LecturesCategory> getFrontLayerList(String layer) {
		// TODO Auto-generated method stub
		return lecturesCategoryDao.getFrontLayerList(layer);
	}

	@Override
	public Integer deleteCheck(LecturesCategory lecturesCategory) throws IOException {
		// TODO Auto-generated method stub
		return lecturesCategoryDao.deleteCheck(lecturesCategory);
	}

	
//	===============
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		return lecturesCategoryDao.getNormalList();
	}

	@Override
	public void updateNormalData(LecturesCategory lecturesCategory) {
		lecturesCategoryDao.updateNormalData(lecturesCategory);
	}


}
