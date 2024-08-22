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
		return schoolBulletinContentDao.getList(schoolBulletinContent);
	}

	@Override
	public List<SchoolBulletinContent> getList(int pageCount, int pageStart,
			SchoolBulletinContent schoolBulletinContent) {
		return null;
	}

	@Override
	public Integer getCount(SchoolBulletinContent schoolBulletinContent) {
		return null;
	}

	@Override
	public SchoolBulletinContent getData(SchoolBulletinContent schoolBulletinContent) {
		return null;
	}

	@Override
	public Integer getNextId() {
		return schoolBulletinContentDao.getNextId();
	}

	@Override
	public void add(SchoolBulletinContent schoolBulletinContent) {
		schoolBulletinContentDao.add(schoolBulletinContent);
	}

	@Override
	public void update(SchoolBulletinContent schoolBulletinContent) {
		schoolBulletinContentDao.update(schoolBulletinContent);
	}

	@Override
	public void delete(Integer id) {
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
