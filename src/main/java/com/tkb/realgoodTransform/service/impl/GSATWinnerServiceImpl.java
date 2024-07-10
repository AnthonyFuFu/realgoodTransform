package com.tkb.realgoodTransform.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.GSATWinnerDao;
import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.GSATWinner;
import com.tkb.realgoodTransform.service.GSATWinnerService;

/**
 * 外特高手經驗談Service實作類
 */
@Service
public class GSATWinnerServiceImpl implements GSATWinnerService{

	@Autowired
	private GSATWinnerDao gSATWinnerDao;

	@Override
	public List<GSATWinner> getList(int pageCount, int pageStart, GSATWinner gSATWinner) {
		return gSATWinnerDao.getList(pageCount, pageStart, gSATWinner);
	}
	@Override
	public List<GSATWinner> getFrontList(GSATWinner gSATWinner) {
		return gSATWinnerDao.getFrontList(gSATWinner);
	}
	@Override
	public List<GSATWinner> getFrontList(int pageCount, int pageStart, GSATWinner gSATWinner) {
		return gSATWinnerDao.getFrontList(pageCount, pageStart, gSATWinner);
	}
	@Override
	public List<GSATWinner> getFrontList(int pageCount, int pageStart, GSATWinner gSATWinner, List<Area> areaList, String search_sort) {
		return gSATWinnerDao.getFrontList(pageCount, pageStart, gSATWinner, areaList, search_sort);
	}
	@Override
	public List<GSATWinner> getFrontList() {
		return gSATWinnerDao.getFrontList();
	}
	@Override
	public Integer getCount(GSATWinner gSATWinner) {
		return gSATWinnerDao.getCount(gSATWinner);
	}
	@Override
	public Integer getFrontCount(GSATWinner gSATWinner) {
		return gSATWinnerDao.getFrontCount(gSATWinner);
	}
	@Override
	public GSATWinner getData(GSATWinner gSATWinner) {
		return gSATWinnerDao.getData(gSATWinner);
	}
	@Override
	public GSATWinner getFrontData(GSATWinner gSATWinner) {
		return gSATWinnerDao.getData(gSATWinner);
	}
	@Override
	public Integer getNextId() {
		return gSATWinnerDao.getNextId();
	}
	@Override
	public void add(GSATWinner gSATWinner) {
		gSATWinnerDao.add(gSATWinner);
	}
	@Override
	public void update(GSATWinner gSATWinner) {
		gSATWinnerDao.update(gSATWinner);
	}
	@Override
	public void updateClickRate(GSATWinner gSATWinner) {
		gSATWinnerDao.updateClickRate(gSATWinner);
	}
	
	@Override
	public void delete(GSATWinner gSATWinner ,Integer id) {
		gSATWinnerDao.delete(gSATWinner,id);
	}
	

	
}
