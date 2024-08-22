package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.BannerDao;
import com.tkb.realgoodTransform.model.Banner;
import com.tkb.realgoodTransform.service.BannerService;

@Service
public class BannerServiceImpl implements BannerService {
	@Autowired
	private BannerDao bannerDao;
	
	@Override
	public List<Banner> getList(int pageCount, int pageStart, Banner banner) {
		return bannerDao.getList(pageCount, pageStart, banner);
	}

	@Override
	public List<Banner> getFrontList(Banner banner) {
		return bannerDao.getFrontList(banner);
	}

	@Override
	public Integer getCount(Banner banner) {
		return bannerDao.getCount(banner);
	}

	@Override
	public Integer getFrontCount(Banner banner) {
		return null;
	}

	@Override
	public Integer getShowCount(Banner banner) {
		return null;
	}

	@Override
	public Banner getData(Banner banner) {
		return bannerDao.getData(banner);
	}

	@Override
	public Banner getFrontData(Banner banner) {
		return bannerDao.getFrontData(banner);
	}

	@Override
	public Integer getNextId() {
		return bannerDao.getNextId();
	}

	@Override
	public void add(Banner banner) {
		bannerDao.add(banner);
	}

	@Override
	public void update(Banner banner) {
		bannerDao.update(banner);

	}

	@Override
	public void updateClickRate(Banner banner) {
		bannerDao.updateClickRate(banner);
	}

	@Override
	public void updateSort(Banner banner) {

	}

	@Override
	public void delete(Integer id) {
		bannerDao.delete(id);

	}

	@Override
	public void resetSort() {
		bannerDao.resetSort();
	}
	
//	======================
	@Override
	public List<Map<String, Object>> getNormalList() {
		return bannerDao.getNormalList();
	}
	@Override
	public void updateNormalData(Banner banner) {
		bannerDao.updateNormalData(banner);
	}
	
}
