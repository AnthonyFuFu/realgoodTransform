package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.LecturesContentDao;
import com.tkb.realgoodTransform.model.LecturesContent;
import com.tkb.realgoodTransform.service.LecturesContentService;



@Service
public class LecturesContentServiceImpl implements LecturesContentService {

	@Autowired
	private LecturesContentDao lecturesContentDao;
	@Override
	public List<LecturesContent> getList(LecturesContent lecturesContent) {
		// TODO Auto-generated method stub
		return lecturesContentDao.getList(lecturesContent);
	}

	@Override
	public List<LecturesContent> getList(int pageCount, int pageStart, LecturesContent lecturesContent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getCount(LecturesContent lecturesContent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LecturesContent getData(LecturesContent lecturesContent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNextId() {
		// TODO Auto-generated method stub
		return lecturesContentDao.getNextId();
	}

	@Override
	public void add(LecturesContent lecturesContent) {
		// TODO Auto-generated method stub
		lecturesContentDao.add(lecturesContent);
	}

	@Override
	public void update(LecturesContent lecturesContent) {
		// TODO Auto-generated method stub
		lecturesContentDao.update(lecturesContent);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		lecturesContentDao.delete(id);;
	}

//	================
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		return lecturesContentDao.getNormalList();
	}

	@Override
	public void updateNormalData(LecturesContent lecturesContent) {
		lecturesContentDao.updateNormalData(lecturesContent);
	}

}
