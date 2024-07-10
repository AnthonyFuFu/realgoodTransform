package com.tkb.realgoodTransform.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.GSATWinnerContentDao;
import com.tkb.realgoodTransform.model.GSATWinnerContent;
import com.tkb.realgoodTransform.service.GSATWinnerContentService;

/**
 * 贏家經驗談內容Service實作類
 */
@Service
public class GSATWinnerContentServiceImpl implements GSATWinnerContentService {
	
	@Autowired
	private GSATWinnerContentDao gSATWinnerContentDao;

	@Override
	public List<GSATWinnerContent> getList(GSATWinnerContent gSATWinnerContent) {
		return gSATWinnerContentDao.getList(gSATWinnerContent);
	}

	@Override
	public List<GSATWinnerContent> getList(int pageCount, int pageStart, GSATWinnerContent gSATWinnerContent) {
		return gSATWinnerContentDao.getList(pageCount, pageStart, gSATWinnerContent);
	}

	@Override
	public Integer getCount(GSATWinnerContent gSATWinnerContent) {
		return gSATWinnerContentDao.getCount(gSATWinnerContent);
	}

	@Override
	public GSATWinnerContent getData(GSATWinnerContent gSATWinnerContent) {
		return gSATWinnerContentDao.getData(gSATWinnerContent);
	}

	@Override
	public Integer getNextId() {
		return gSATWinnerContentDao.getNextId();
	}

	@Override
	public void add(GSATWinnerContent gSATWinnerContent) {
		gSATWinnerContentDao.add(gSATWinnerContent);
	}

	@Override
	public void update(GSATWinnerContent gSATWinnerContent) {
		gSATWinnerContentDao.update(gSATWinnerContent);
	}

	@Override
	public void delete(Integer id) {
		gSATWinnerContentDao.delete(id);
	}

	

}
