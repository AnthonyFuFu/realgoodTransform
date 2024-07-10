package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.GSATMenuDao;
import com.tkb.realgoodTransform.model.GSATMenu;
import com.tkb.realgoodTransform.service.GSATMenuService;

/**
 * 外特選單Service介面接口
 */
@Service
public class GSATMenuServiceImpl implements GSATMenuService {

	@Autowired
	private GSATMenuDao gSATMenuDao;

	@Override
	public List<GSATMenu> getSubList(GSATMenu gSATMenu) {
		return gSATMenuDao.getSubList(gSATMenu);
	}

	@Override
	public List<GSATMenu> getLayerList(String layer, GSATMenu gSATMenu) {
		return gSATMenuDao.getLayerList(layer, gSATMenu);
	}

	@Override
	public List<GSATMenu> getList(GSATMenu gSATMenu) {
		return gSATMenuDao.getList(gSATMenu);
	}

	@Override
	public List<GSATMenu> getList(int pageCount, int pageStart, GSATMenu gSATMenu) {
		return gSATMenuDao.getList(pageCount, pageStart, gSATMenu);
	}

	@Override
	public Integer getCount(GSATMenu gSATMenu) {
		return gSATMenuDao.getCount(gSATMenu);
	}

	@Override
	public GSATMenu getData(GSATMenu gSATMenu) {
		return gSATMenuDao.getData(gSATMenu);
	}

	@Override
	public Integer getNextId() {
		return gSATMenuDao.getNextId();
	}

	@Override
	public void add(GSATMenu gSATMenu) {
		gSATMenuDao.add(gSATMenu);
	}

	@Override
	public void update(GSATMenu gSATMenu) {
		gSATMenuDao.update(gSATMenu);
	}

	@Override
	public void delete(GSATMenu gSATMenu, Integer id) {
		gSATMenuDao.delete(gSATMenu, id);
	}

	@Override
	public String checkGSATMenu(GSATMenu gSATMenu) {
		return gSATMenuDao.checkGSATMenu(gSATMenu);
	}

	@Override
	public void resetSort(GSATMenu gSATMenu) {
		gSATMenuDao.resetSort(gSATMenu);
	}

	@Override
	public Integer getCategoryId(String name) {
		return gSATMenuDao.getCategoryId(name);
	}
	
	
//	======================
	@Override
	public List<Map<String, Object>> getNormalList() {
		return gSATMenuDao.getNormalList();
	}
	@Override
	public void updateNormalData(GSATMenu gSATMenu) {
		gSATMenuDao.updateNormalData(gSATMenu);
	}

}
