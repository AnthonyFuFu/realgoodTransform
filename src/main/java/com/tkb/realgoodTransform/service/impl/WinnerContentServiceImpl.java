package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.WinnerContentDao;
import com.tkb.realgoodTransform.model.WinnerContent;
import com.tkb.realgoodTransform.service.WinnerContentService;



@Service
public class WinnerContentServiceImpl implements WinnerContentService {
	
	@Autowired
	private WinnerContentDao winnerContentDao;
	
	@Override
	public List<WinnerContent> getList(WinnerContent winnerContent) {
		return winnerContentDao.getList(winnerContent);
	}

	@Override
	public List<WinnerContent> getList(int pageCount, int pageStart, WinnerContent winnerContent) {
		return null;
	}

	@Override
	public Integer getCount(WinnerContent winnerContent) {
		return null;
	}

	@Override
	public WinnerContent getData(WinnerContent winnerContent) {
		return null;
	}

	@Override
	public Integer getNextId() {
		return winnerContentDao.getNextId();
	}

	@Override
	public void add(WinnerContent winnerContent) {
		winnerContentDao.add(winnerContent);
	}

	@Override
	public void update(WinnerContent winnerContent) {
		winnerContentDao.update(winnerContent);

	}

	@Override
	public void delete(Integer id) {
		winnerContentDao.delete(id);

	}
	
//	==============

	@Override
	public List<Map<String, Object>> getNormalList() {
		return winnerContentDao.getNormalList();
	}

	@Override
	public void updateNormalData(WinnerContent winnerContent) {
		winnerContentDao.updateNormalData(winnerContent);
	}

}
