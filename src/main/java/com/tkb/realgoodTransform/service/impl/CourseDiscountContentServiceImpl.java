package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.CourseDiscountContentDao;
import com.tkb.realgoodTransform.model.CourseDiscountContent;
import com.tkb.realgoodTransform.service.CourseDiscountContentService;

@Service
public class CourseDiscountContentServiceImpl implements CourseDiscountContentService{

	@Autowired
	private CourseDiscountContentDao courseDiscountContentDao;

	@Override
	public List<CourseDiscountContent> getList(CourseDiscountContent courseDiscountContent) {
		return courseDiscountContentDao.getList(courseDiscountContent);
	}

	@Override
	public Integer getNextId() {
		return courseDiscountContentDao.getNextId();
	}

	@Override
	public void add(CourseDiscountContent courseDiscountContent) {
		courseDiscountContentDao.add(courseDiscountContent);
	}

	@Override
	public void update(CourseDiscountContent courseDiscountContent) {
		courseDiscountContentDao.update(courseDiscountContent);
	}

	@Override
	public void delete(Integer id) {
		courseDiscountContentDao.delete(id);
	}		
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		return courseDiscountContentDao.getNormalList();
	}

	@Override
	public void updateNormalData(CourseDiscountContent courseDiscountContent) {
		courseDiscountContentDao.updateNormalData(courseDiscountContent);
	}
	
}
