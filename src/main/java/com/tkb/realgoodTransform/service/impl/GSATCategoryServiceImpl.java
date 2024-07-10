package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.GSATCategoryDao;
import com.tkb.realgoodTransform.model.GSATCategory;
import com.tkb.realgoodTransform.service.GSATCategoryService;

/**
 * 類別Service介面接口
 */
@Service
public class GSATCategoryServiceImpl implements GSATCategoryService{

	@Autowired
	private GSATCategoryDao gSATCategoryDao;
	
	@Override
	public List<GSATCategory> getSubList(GSATCategory gSATCategory) {
		return gSATCategoryDao.getSubList(gSATCategory);
	}
	@Override
	public List<GSATCategory> getLayerList(String layer, GSATCategory gSATCategory) {
		return gSATCategoryDao.getLayerList(layer, gSATCategory);
	}
	@Override
	public List<GSATCategory> getList(int pageCount, int pageStart, GSATCategory gSATCategory) {
		return gSATCategoryDao.getList(pageCount, pageStart, gSATCategory);
	}
	@Override
	public Integer getCount(GSATCategory gSATCategory) {
		return gSATCategoryDao.getCount(gSATCategory);
	}
	@Override
	public GSATCategory getData(GSATCategory gSATCategory) {
		return gSATCategoryDao.getData(gSATCategory);
	}
	@Override
	public Integer getNextId() {
		return gSATCategoryDao.getNextId();
	}
	@Override
	public void add(GSATCategory gSATCategory) {
		gSATCategoryDao.add(gSATCategory);
	}
	@Override
	public void update(GSATCategory gSATCategory) {
		gSATCategoryDao.update(gSATCategory);
	}
	@Override
	public void delete(GSATCategory gSATCategory, Integer id) {
		gSATCategoryDao.delete(gSATCategory, id);
	}	
	@Override
	public String checkGSATCategory(GSATCategory gSATCategory){
		return gSATCategoryDao.checkGSATCategory(gSATCategory);
	}
	@Override
	public void resetSort(GSATCategory gSATCategory){
		gSATCategoryDao.resetSort(gSATCategory);
	}
	@Override
	public Integer getCategoryId(String name) {
		return gSATCategoryDao.getCategoryId(name);
	}
	
//	======================
	@Override
	public List<Map<String, Object>> getNormalList() {
		return gSATCategoryDao.getNormalList();
	}
	@Override
	public void updateNormalData(GSATCategory gSATCategory) {
		gSATCategoryDao.updateNormalData(gSATCategory);
	}
	
	
}
