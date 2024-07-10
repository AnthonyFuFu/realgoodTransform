package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.SchoolBulletinCategoryDao;
import com.tkb.realgoodTransform.model.SchoolBulletinCategory;
import com.tkb.realgoodTransform.service.SchoolBulletinCategoryService;



@Service
public class SchoolBulletinCategoryServiceImpl implements SchoolBulletinCategoryService {

	@Autowired
	SchoolBulletinCategoryDao schoolBulletinCategoryDao;
	
	@Override
	public List<SchoolBulletinCategory> getSubList(SchoolBulletinCategory schoolBulletinCategory) {
		// TODO Auto-generated method stub
		return schoolBulletinCategoryDao.getSubList(schoolBulletinCategory);
	}

	@Override
	public List<SchoolBulletinCategory> getLayerList(String layer, SchoolBulletinCategory schoolBulletinCategory) {
		// TODO Auto-generated method stub
		return schoolBulletinCategoryDao.getLayerList(layer, schoolBulletinCategory);
	}

	@Override
	public List<SchoolBulletinCategory> getLayer1List(SchoolBulletinCategory schoolBulletinCategory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SchoolBulletinCategory> getList(int pageCount, int pageStart,
			SchoolBulletinCategory schoolBulletinCategory) {
		// TODO Auto-generated method stub
		return schoolBulletinCategoryDao.getList(pageCount, pageStart, schoolBulletinCategory);
	}

	@Override
	public Integer getCount(SchoolBulletinCategory schoolBulletinCategory) {
		// TODO Auto-generated method stub
		return schoolBulletinCategoryDao.getCount(schoolBulletinCategory);
	}

	@Override
	public SchoolBulletinCategory getData(SchoolBulletinCategory schoolBulletinCategory) {
		// TODO Auto-generated method stub
		return schoolBulletinCategoryDao.getData(schoolBulletinCategory);
	}

	@Override
	public SchoolBulletinCategory checkRepeat(SchoolBulletinCategory schoolBulletinCategory) {
		// TODO Auto-generated method stub
		return schoolBulletinCategoryDao.checkRepeat(schoolBulletinCategory);
	}

	@Override
	public Integer getNextId() {
		// TODO Auto-generated method stub
		return schoolBulletinCategoryDao.getNextId();
	}

	@Override
	public void add(SchoolBulletinCategory schoolBulletinCategory) {
		// TODO Auto-generated method stub
		schoolBulletinCategoryDao.add(schoolBulletinCategory);

	}

	@Override
	public void update(SchoolBulletinCategory schoolBulletinCategory) {
		// TODO Auto-generated method stub
		schoolBulletinCategoryDao.update(schoolBulletinCategory);
	}

	@Override
	public void delete(SchoolBulletinCategory schoolBulletinCategory, Integer id) {
		// TODO Auto-generated method stub
		schoolBulletinCategoryDao.delete(schoolBulletinCategory, id);
	}

	@Override
	public void resetSort(SchoolBulletinCategory schoolBulletinCategory) {
		// TODO Auto-generated method stub
		schoolBulletinCategoryDao.resetSort(schoolBulletinCategory);

	}

	@Override
	public Integer deleteCheck(SchoolBulletinCategory schoolBulletinCategory) {
		// TODO Auto-generated method stub
		return schoolBulletinCategoryDao.deleteCheck(schoolBulletinCategory);
	}

	
//	===============
	
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		// TODO Auto-generated method stub
		return schoolBulletinCategoryDao.getNormalList();
	}

	@Override
	public void updateNormalData(SchoolBulletinCategory schoolBulletinCategory) {
		// TODO Auto-generated method stub
		schoolBulletinCategoryDao.updateNormalData(schoolBulletinCategory);
	}

}
