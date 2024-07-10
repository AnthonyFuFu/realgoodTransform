package com.tkb.realgoodTransform.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.GSATAdmitDao;
import com.tkb.realgoodTransform.model.GSATAdmit;
import com.tkb.realgoodTransform.service.GSATAdmitService;

/**
 * 外特高手經驗談Service實作類
 */
@Service
public class GSATAdmitServiceImpl implements GSATAdmitService{

	@Autowired
	private GSATAdmitDao gSATAdmitDao;

	@Override
	public List<GSATAdmit> getList(int pageCount, int pageStart, GSATAdmit gSATAdmit) {
		return gSATAdmitDao.getList(pageCount, pageStart, gSATAdmit);
	}
	@Override
	public List<GSATAdmit> getFrontList(GSATAdmit gSATAdmit) {
		return gSATAdmitDao.getFrontList(gSATAdmit);
	}
	@Override
	public Integer getCount(GSATAdmit gSATAdmit) {
		return gSATAdmitDao.getCount(gSATAdmit);
	}
	@Override
	public GSATAdmit getData(GSATAdmit gSATAdmit) {
		return gSATAdmitDao.getData(gSATAdmit);
	}
	
	@Override
	public Integer getNextId() {
		return gSATAdmitDao.getNextId();
	}
	@Override
	public void add(GSATAdmit gSATAdmit) {
		gSATAdmitDao.add(gSATAdmit);
	}
	@Override
	public void update(GSATAdmit gSATAdmit) {
		gSATAdmitDao.update(gSATAdmit);
	}
	@Override
	public void updateClickRate(GSATAdmit gSATAdmit) {
		gSATAdmitDao.updateClickRate(gSATAdmit);
	}
	@Override
	public void delete(GSATAdmit gSATAdmit ,Integer id) {
		gSATAdmitDao.delete(gSATAdmit,id);
	}
	
	public GSATAdmit getFrontData(GSATAdmit gSATAdmit) {
		return gSATAdmitDao.getData(gSATAdmit);
	}
	
}
