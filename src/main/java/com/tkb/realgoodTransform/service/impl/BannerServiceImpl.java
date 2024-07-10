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
		// TODO Auto-generated method stub
		return bannerDao.getList(pageCount, pageStart, banner);
	}

	@Override
	public List<Banner> getFrontList(Banner banner) {
		// TODO Auto-generated method stub
		return bannerDao.getFrontList(banner);
	}

	@Override
	public Integer getCount(Banner banner) {
		// TODO Auto-generated method stub
		return bannerDao.getCount(banner);
	}

	@Override
	public Integer getFrontCount(Banner banner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getShowCount(Banner banner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Banner getData(Banner banner) {
		// TODO Auto-generated method stub
		return bannerDao.getData(banner);
	}

	@Override
	public Banner getFrontData(Banner banner) {
		// TODO Auto-generated method stub
		return bannerDao.getFrontData(banner);
	}

	@Override
	public Integer getNextId() {
		// TODO Auto-generated method stub
		return bannerDao.getNextId();
	}

	@Override
	public void add(Banner banner) {
		// TODO Auto-generated method stub
		bannerDao.add(banner);
	}

	@Override
	public void update(Banner banner) {
		// TODO Auto-generated method stub
		bannerDao.update(banner);

	}

	@Override
	public void updateClickRate(Banner banner) {
		bannerDao.updateClickRate(banner);
	}

	@Override
	public void updateSort(Banner banner) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		bannerDao.delete(id);

	}

	@Override
	public void resetSort() {
		// TODO Auto-generated method stub
		bannerDao.resetSort();
	}

	
	
	
//	======================
	@Override
	public List<Map<String, Object>> getNormalList() {
		// TODO Auto-generated method stub
		return bannerDao.getNormalList();
	}
	@Override
	public void updateNormalData(Banner banner) {
		// TODO Auto-generated method stub
		bannerDao.updateNormalData(banner);
	}


}
