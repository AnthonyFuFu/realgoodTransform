package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.NewExamContentDao;
import com.tkb.realgoodTransform.model.NewExamContent;
import com.tkb.realgoodTransform.service.NewExamContentService;



@Service
public class NewExamContentServiceImpl implements NewExamContentService {

	@Autowired
	private NewExamContentDao newExamContentDao;
	@Override
	public List<NewExamContent> getList(NewExamContent newExamContent) {
		// TODO Auto-generated method stub
		return newExamContentDao.getList(newExamContent);
	}

	@Override
	public Integer getNextId() {
		// TODO Auto-generated method stub
		return newExamContentDao.getNextId();
	}

	@Override
	public void add(NewExamContent newExamContent) {
		// TODO Auto-generated method stub
		newExamContentDao.add(newExamContent);
	}

	@Override
	public void update(NewExamContent newExamContent) {
		// TODO Auto-generated method stub
		newExamContentDao.update(newExamContent);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		newExamContentDao.delete(id);
	}

//	============================
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		// TODO Auto-generated method stub
		return newExamContentDao.getNormalList();
	}

	@Override
	public void updateNormalData(NewExamContent newExamContent) {
		// TODO Auto-generated method stub
		newExamContentDao.updateNormalData(newExamContent);
	}

}
