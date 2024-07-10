package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.AdmitCategoryDao;
import com.tkb.realgoodTransform.model.AdmitCategory;
import com.tkb.realgoodTransform.service.AdmitCategoryService;



/**
 * 金榜類別Service實作類
 * 
 * @author
 * @version 創建時間：2016-05-17
 */
@Service
public class AdmitCategoryServiceImpl implements AdmitCategoryService {
	@Autowired
	private AdmitCategoryDao admitCategoryDao;

	@Override
	public List<AdmitCategory> getSubList(AdmitCategory admitCategory) {
		return admitCategoryDao.getSubList(admitCategory);
	}

	@Override
	public List<AdmitCategory> getLayerList(String layer, AdmitCategory admitCategory) {
		return admitCategoryDao.getLayerList(layer, admitCategory);
	}

	@Override
	public List<AdmitCategory> getList(int pageCount, int pageStart, AdmitCategory admitCategory) {
		return admitCategoryDao.getList(pageCount, pageStart, admitCategory);
	}

	@Override
	public Integer getCount(AdmitCategory admitCategory) {
		return admitCategoryDao.getCount(admitCategory);
	}

	@Override
	public AdmitCategory getData(AdmitCategory admitCategory) {
		return admitCategoryDao.getData(admitCategory);
	}

	@Override
	public Integer getNextId() {
		return admitCategoryDao.getNextId();
	}

	@Override
	public void add(AdmitCategory admitCategory) {
		admitCategoryDao.add(admitCategory);
	}

	@Override
	public void update(AdmitCategory admitCategory) {
		admitCategoryDao.update(admitCategory);
	}

	@Override
	public void delete(AdmitCategory admitCategory, Integer id) {
		admitCategoryDao.delete(admitCategory, id);
	}

	@Override
	public Integer getCountByName(String categoryName) {
		return admitCategoryDao.getCountByName(categoryName);
	}

	@Override
	public void resetSort(AdmitCategory admitCategory) {
		admitCategoryDao.resetSort(admitCategory);
	}

//	============
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		return admitCategoryDao.getNormalList();
	}

	@Override
	public void updateNormalData(AdmitCategory admitCategory) {
		admitCategoryDao.updateNormalData(admitCategory);
	}

}
