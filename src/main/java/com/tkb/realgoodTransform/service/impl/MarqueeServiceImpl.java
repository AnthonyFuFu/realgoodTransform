package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.MarqueeDao;
import com.tkb.realgoodTransform.model.Marquee;
import com.tkb.realgoodTransform.service.MarqueeService;



@Service
public class MarqueeServiceImpl implements MarqueeService {

	@Autowired
	MarqueeDao marqueeDao;
	
	@Override
	public List<Marquee> getList(int pageCount, int pageStart, Marquee marquee) {
		return marqueeDao.getList(pageCount, pageStart, marquee);
	}

	@Override
	public List<Marquee> getFrontList(Marquee marquee) {
		return marqueeDao.getFrontList(marquee);
	}

	@Override
	public Integer getCount(Marquee marquee) {
		return marqueeDao.getCount(marquee);
	}

	@Override
	public Marquee getData(Marquee marquee) {
		return marqueeDao.getData(marquee);
	}

	@Override
	public Marquee getFrontData(Marquee marquee) {
		return null;
	}

	@Override
	public Integer getNextId() {
		return marqueeDao.getNextId();
	}

	@Override
	public void add(Marquee marquee) {
		marqueeDao.add(marquee);

	}

	@Override
	public void update(Marquee marquee) {
		marqueeDao.update(marquee);
	}

	@Override
	public void updateSort(Marquee marquee) {

	}

	@Override
	public void delete(Integer id) {
		marqueeDao.delete(id);
	}

	@Override
	public void updateClickRate(Marquee marquee) {
		marqueeDao.updateClickRate(marquee);

	}

	@Override
	public void resetSort() {
		marqueeDao.resetSort();

	}

	@Override
	public List<Map<String, Object>> getNormalBannerList() {
		return marqueeDao.getNormalBannerList();
	}

	@Override
	public void updateNormalData(Marquee marquee) {
		marqueeDao.updateNormalData(marquee);
	}

}
