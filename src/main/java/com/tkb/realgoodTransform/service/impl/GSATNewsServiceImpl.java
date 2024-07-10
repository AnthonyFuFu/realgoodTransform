package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.GSATNewsDao;
import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.GSATNews;
import com.tkb.realgoodTransform.service.GSATNewsService;

/**
 * 外特高手經驗談Service實作類
 */
@Service
public class GSATNewsServiceImpl implements GSATNewsService {

	@Autowired
	private GSATNewsDao gSATNewsDao;

	@Override
	public List<GSATNews> getList(int pageCount, int pageStart, GSATNews gSATNews) {
		return gSATNewsDao.getList(pageCount, pageStart, gSATNews);
	}

	@Override
	public List<GSATNews> getFrontList(GSATNews gSATNews) {
		return gSATNewsDao.getFrontList(gSATNews);
	}

	@Override
	public List<GSATNews> getFrontList(int pageCount, int pageStart, GSATNews gSATNews, List<Area> areaList, String search_sort) {
		return gSATNewsDao.getFrontList(pageCount, pageStart, gSATNews, areaList, search_sort);
	}

	@Override
	public List<GSATNews> getFrontList() {
		return gSATNewsDao.getFrontList();
	}

	@Override
	public Integer getCount(GSATNews gSATNews) {
		return gSATNewsDao.getCount(gSATNews);
	}

	@Override
	public GSATNews getData(GSATNews gSATNews) {
		return gSATNewsDao.getData(gSATNews);
	}

	@Override
	public GSATNews getFrontData(GSATNews gSATNews) {
		return gSATNewsDao.getData(gSATNews);
	}

	@Override
	public Integer getNextId() {
		return gSATNewsDao.getNextId();
	}

	@Override
	public void add(GSATNews gSATNews) {
		gSATNewsDao.add(gSATNews);
	}

	@Override
	public void update(GSATNews gSATNews) {
		gSATNewsDao.update(gSATNews);
	}

	@Override
	public void updateClickRate(GSATNews gSATNews) {
		gSATNewsDao.updateClickRate(gSATNews);
	}

	@Override
	public void delete(GSATNews gSATNews, Integer id) {
		gSATNewsDao.delete(gSATNews, id);
	}

//	======================
	@Override
	public List<Map<String, Object>> getNormalList() {
		return gSATNewsDao.getNormalList();
	}
	@Override
	public void updateNormalData(GSATNews gSATNews) {
		gSATNewsDao.updateNormalData(gSATNews);
	}

}
