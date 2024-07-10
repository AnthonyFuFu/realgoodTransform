package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.GSATDao;
import com.tkb.realgoodTransform.model.GSAT;
import com.tkb.realgoodTransform.service.GSATService;

/**
 * 外特專區Service實作類
 */
@Service
public class GSATServiceImpl implements GSATService {

	@Autowired
	private GSATDao gSATDao;

	@Override
	public List<GSAT> getList(int pageCount, int pageStart, GSAT gSAT) {
		return gSATDao.getList(pageCount, pageStart, gSAT);
	}

	@Override
	public Integer getCount(GSAT gSAT) {
		return gSATDao.getCount(gSAT);
	}

	@Override
	public List<GSAT> getCategoryList(GSAT gSAT) {
		return gSATDao.getCategoryList(gSAT);
	}

	@Override
	public GSAT getData(GSAT gSAT) {
		return gSATDao.getData(gSAT);
	}

	@Override
	public GSAT getFrontData(GSAT gSAT) {
		return gSATDao.getData(gSAT);
	}

	@Override
	public Integer getNextId() {
		return gSATDao.getNextId();
	}

	@Override
	public void add(GSAT gSAT) {
		gSATDao.add(gSAT);
	}

	@Override
	public void update(GSAT gSAT) {
		gSATDao.update(gSAT);
	}

	@Override
	public void updateClickRate(GSAT gSAT) {
		gSATDao.updateClickRate(gSAT);
	}

	@Override
	public void delete(GSAT gSAT, Integer id) {
		gSATDao.delete(gSAT, id);
	}
	
	
//	======================
	@Override
	public List<Map<String, Object>> getNormalList() {
		return gSATDao.getNormalList();
	}
	@Override
	public void updateNormalData(GSAT gSAT) {
		gSATDao.updateNormalData(gSAT);
	}

}
