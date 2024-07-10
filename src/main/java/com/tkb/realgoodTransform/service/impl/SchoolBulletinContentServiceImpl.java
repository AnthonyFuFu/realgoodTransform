package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.SchoolBulletinContentDao;
import com.tkb.realgoodTransform.model.SchoolBulletinContent;
import com.tkb.realgoodTransform.service.SchoolBulletinContentService;


@Service
public class SchoolBulletinContentServiceImpl implements SchoolBulletinContentService {
	@Autowired
	SchoolBulletinContentDao schoolBulletinContentDao;
	@Override
	public List<SchoolBulletinContent> getList(SchoolBulletinContent schoolBulletinContent) {
		// TODO Auto-generated method stub
		return schoolBulletinContentDao.getList(schoolBulletinContent);
	}

	@Override
	public List<SchoolBulletinContent> getList(int pageCount, int pageStart,
			SchoolBulletinContent schoolBulletinContent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getCount(SchoolBulletinContent schoolBulletinContent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SchoolBulletinContent getData(SchoolBulletinContent schoolBulletinContent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNextId() {
		// TODO Auto-generated method stub
		return schoolBulletinContentDao.getNextId();
	}

	@Override
	public void add(SchoolBulletinContent schoolBulletinContent) {
		// TODO Auto-generated method stub
		schoolBulletinContentDao.add(schoolBulletinContent);
	}

	@Override
	public void update(SchoolBulletinContent schoolBulletinContent) {
		// TODO Auto-generated method stub
		schoolBulletinContentDao.update(schoolBulletinContent);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		schoolBulletinContentDao.delete(id);
	}

	
//	==================
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		return schoolBulletinContentDao.getNormalList();
	}

	@Override
	public void updateNormalData(SchoolBulletinContent schoolBulletinContent) {
		schoolBulletinContentDao.updateNormalData(schoolBulletinContent);
	}

}
