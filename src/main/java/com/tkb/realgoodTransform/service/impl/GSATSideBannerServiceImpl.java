package com.tkb.realgoodTransform.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.GSATSideBannerDao;
import com.tkb.realgoodTransform.model.NavBanner;
import com.tkb.realgoodTransform.service.GSATSideBannerService;

/**
 * 首頁Banner Service實作類
 */
@Service
public class GSATSideBannerServiceImpl implements GSATSideBannerService {

	@Autowired
	private GSATSideBannerDao gSATSideBannerDao;

	@Override
	public List<NavBanner> getList(int pageCount, int pageStart, NavBanner navBanner) {
		return gSATSideBannerDao.getList(pageCount, pageStart, navBanner);
	}

	@Override
	public List<NavBanner> getFrontList(NavBanner navBanner) {
		return gSATSideBannerDao.getFrontList(navBanner);
	}

	@Override
	public Integer getCount(NavBanner navBanner) {
		return gSATSideBannerDao.getCount(navBanner);
	}

	@Override
	public Integer getFrontCount(NavBanner navBanner) {
		return gSATSideBannerDao.getFrontCount(navBanner);
	}

	@Override
	public Integer getShowCount(NavBanner navBanner) {
		return gSATSideBannerDao.getShowCount(navBanner);
	}

	@Override
	public NavBanner getData(NavBanner navBanner) {
		return gSATSideBannerDao.getData(navBanner);
	}

	@Override
	public NavBanner getFrontData(NavBanner navBanner) {
		return gSATSideBannerDao.getData(navBanner);
	}

	@Override
	public Integer getNextId() {
		return gSATSideBannerDao.getNextId();
	}

	@Override
	public void add(NavBanner navBanner) {
		gSATSideBannerDao.add(navBanner);
	}

	@Override
	public void update(NavBanner navBanner) {
		gSATSideBannerDao.update(navBanner);
	}

	@Override
	public void updateClickRate(NavBanner navBanner) {
		gSATSideBannerDao.updateClickRate(navBanner);
	}

	@Override
	public void updateSort(NavBanner navBanner) {
		gSATSideBannerDao.updateSort(navBanner);
	}

	@Override
	public void delete(Integer id) {
		gSATSideBannerDao.delete(id);
	}

	@Override
	public void resetSort() {
		gSATSideBannerDao.resetSort();
	}

	@Override
	public List<NavBanner> getFrontType2List(NavBanner navBanner) {
		return gSATSideBannerDao.getFrontType2List(navBanner);
	}


}
