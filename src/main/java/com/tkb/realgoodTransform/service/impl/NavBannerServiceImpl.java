package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.NavBannerDao;
import com.tkb.realgoodTransform.model.NavBanner;
import com.tkb.realgoodTransform.service.NavBannerService;



@Service
public class NavBannerServiceImpl implements NavBannerService {
	@Autowired
	private NavBannerDao navBannerDao;
	
	

	@Override
	public List<NavBanner> getList(int pageCount, int pageStart, NavBanner navBanner) {
		return navBannerDao.getList(pageCount, pageStart, navBanner);
	}

	@Override
	public List<NavBanner> getFrontList(NavBanner navBanner) {
		return navBannerDao.getFrontList(navBanner);
	}

	@Override
	public List<NavBanner> getFrontType2List(NavBanner navBanner) {
		return navBannerDao.getFrontType2List(navBanner);
	}

	@Override
	public Integer getCount(NavBanner navBanner) {
		return navBannerDao.getCount(navBanner);
	}

	@Override
	public Integer getFrontCount(NavBanner navBanner) {
		return navBannerDao.getFrontCount(navBanner);
	}

	@Override
	public Integer getShowCount(NavBanner navBanner) {
		return null;
	}

	@Override
	public NavBanner getData(NavBanner navBanner) {
		return navBannerDao.getData(navBanner);
	}

	@Override
	public NavBanner getFrontData(NavBanner navBanner) {
		return null;
	}

	@Override
	public Integer getNextId() {
		
		return navBannerDao.getNextId();
	}

	@Override
	public void add(NavBanner navBanner) {
		navBannerDao.add(navBanner);
	}

	@Override
	public void update(NavBanner navBanner) {
		navBannerDao.update(navBanner);

	}

	@Override
	public void updateClickRate(NavBanner navBanner) {
		navBannerDao.updateClickRate(navBanner);
	}

	@Override
	public void updateSort(NavBanner navBanner) {

	}

	@Override
	public void delete(Integer id) {
		navBannerDao.delete(id);
	}

	@Override
	public void resetSort() {

	}
	@Override
	public List<NavBanner> getBrochureFrontList(NavBanner navBanner) {
		return navBannerDao.getBrochureFrontList(navBanner);
	}
	
	@Override
	public List<NavBanner> getBrochuregetFrontType2List(NavBanner navBanner) {
		return navBannerDao.getBrochuregetFrontType2List(navBanner);
	}

	@Override
	public List<Map<String, Object>> getNormalNavBannerList() {
		return navBannerDao.getNormalNavBannerList();
	}

	@Override
	public void updateNormalData(NavBanner navBanner) {
		navBannerDao.updateNormalData(navBanner);
	}


}
