package com.tkb.realgoodTransform.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.CourseDiscountCategoryDao;
import com.tkb.realgoodTransform.model.CourseDiscountCategory;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.CourseDiscountCategoryService;

import jakarta.servlet.http.HttpServletRequest;



@Service
public class CourseDiscountCategoryServiceImpl implements CourseDiscountCategoryService {
	@Autowired
	CourseDiscountCategoryDao courseDiscountCategoryDao;
	
	@Override
	public List<CourseDiscountCategory> getSubList(CourseDiscountCategory courseDiscountCategory) {
		return courseDiscountCategoryDao.getSubList(courseDiscountCategory);
	}

	@Override
	public List<CourseDiscountCategory> getLayerList(String layer, CourseDiscountCategory courseDiscountCategory) {
		return courseDiscountCategoryDao.getLayerList(layer, courseDiscountCategory);
	}

	@Override
	public List<CourseDiscountCategory> getList(int pageCount, int pageStart,
			CourseDiscountCategory courseDiscountCategory) {
		return courseDiscountCategoryDao.getList(pageCount, pageStart, courseDiscountCategory);
	}

	@Override
	public Integer getCount(CourseDiscountCategory courseDiscountCategory) {
		return courseDiscountCategoryDao.getCount(courseDiscountCategory);
	}

	@Override
	public CourseDiscountCategory getData(CourseDiscountCategory courseDiscountCategory) {
		return courseDiscountCategoryDao.getData(courseDiscountCategory);
	}

	@Override
	public Integer getNextId() {
		return courseDiscountCategoryDao.getNextId();
	}

	@Override
	public void add(CourseDiscountCategory courseDiscountCategory) {
		courseDiscountCategoryDao.add(courseDiscountCategory);
	}

	@Override
	public void update(CourseDiscountCategory courseDiscountCategory) {
		courseDiscountCategoryDao.update(courseDiscountCategory);
	}

	@Override
	public void delete(CourseDiscountCategory courseDiscountCategory, Integer id) {
		courseDiscountCategoryDao.delete(courseDiscountCategory, id);
	}

	@Override
	public String checkCourseDiscountCategory(CourseDiscountCategory courseDiscountCategory) {
		return courseDiscountCategoryDao.checkCourseDiscountCategory(courseDiscountCategory);
	}

	@Override
	public void resetSort(CourseDiscountCategory courseDiscountCategory) {
		courseDiscountCategoryDao.resetSort(courseDiscountCategory);
	}
	
	@Override
	public Integer deleteCheck(CourseDiscountCategory courseDiscountCategory) {
		return courseDiscountCategoryDao.deleteCheck(courseDiscountCategory);
	}
	
	@Override
	public void addSubmitFunction(CourseDiscountCategory courseDiscountCategory, User user) {
		if(courseDiscountCategory.getParent_id() == null) {
			courseDiscountCategory.setParent_id(0);
		}
		courseDiscountCategory.setId(getNextId());
		courseDiscountCategory.setCreate_by(user.getAccount());
		courseDiscountCategory.setUpdate_by(user.getAccount());
		add(courseDiscountCategory);
		
	}

	@Override
	public String checkCourseDiscountCategoryFunction(CourseDiscountCategory courseDiscountCategory,
			HttpServletRequest request) {
		String courseDiscountCategoryName = request.getParameter("courseDiscountCategoryName");
		String layer = request.getParameter("layer");
		String msg = "";
		courseDiscountCategory.setName(courseDiscountCategoryName);
		courseDiscountCategory.setLayer(layer);
		String returnCourseDiscountCategoryName = checkCourseDiscountCategory(courseDiscountCategory);
		if(returnCourseDiscountCategoryName == null) {
			msg = "true";
		}else {
			msg = "false";
		}
		return msg;
	}

	@Override
	public CourseDiscountCategory updateFunction(CourseDiscountCategory courseDiscountCategory) {
		courseDiscountCategory = getData(courseDiscountCategory);
		return courseDiscountCategory;
	}

	@Override
	public void updateSubmitFunction(CourseDiscountCategory courseDiscountCategory, User user) {
		courseDiscountCategory.setUpdate_by(user.getAccount());
		update(courseDiscountCategory);
		
	}

	@Override
	public String checkDataFunction(CourseDiscountCategory courseDiscountCategory, String deleteList) {
		List<CourseDiscountCategory>courseDiscountCategoryList = new ArrayList<>();
		String[] checkArray = deleteList.split(",");
		String checkStr = "T";
		for(int i = 0; i<checkArray.length; i++) {
			courseDiscountCategory.setParent_id(Integer.valueOf(checkArray[i]));
			courseDiscountCategoryList = getSubList(courseDiscountCategory);
		}
		if(courseDiscountCategoryList.size()>0) {
			checkStr = "F";
		}
		return checkStr;
	}

	
//	=============
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		return courseDiscountCategoryDao.getNormalList();
	}

	@Override
	public void updateNormalData(CourseDiscountCategory courseDiscountCategory) {
		courseDiscountCategoryDao.updateNormalData(courseDiscountCategory);
	}

}
