package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.LecturesPlaceDao;
import com.tkb.realgoodTransform.model.LecturesPlace;
import com.tkb.realgoodTransform.service.LecturesPlaceService;



@Service
public class LecturesPlaceServiceImpl implements LecturesPlaceService {

	@Autowired
	private LecturesPlaceDao lecturesPlaceDao;
	@Override
	public List<LecturesPlace> getList(LecturesPlace lecturesPlace) {
		// TODO Auto-generated method stub
		return lecturesPlaceDao.getList(lecturesPlace);
	}

	@Override
	public List<LecturesPlace> getList(int pageCount, int pageStart, LecturesPlace lecturesPlace) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getCount(LecturesPlace lecturesPlace) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LecturesPlace getData(LecturesPlace lecturesPlace) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNextId() {
		// TODO Auto-generated method stub
		return lecturesPlaceDao.getNextId();
	}

	@Override
	public void add(LecturesPlace lecturesPlace) {
		// TODO Auto-generated method stub
		lecturesPlaceDao.add(lecturesPlace);
	}

	@Override
	public void update(LecturesPlace lecturesPlace) {
		// TODO Auto-generated method stub
		lecturesPlaceDao.update(lecturesPlace);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		lecturesPlaceDao.delete(id);
	}

	@Override
	public List<LecturesPlace> getPlaceList(LecturesPlace lecturesPlace) {
		// TODO Auto-generated method stub
		return lecturesPlaceDao.getPlaceList(lecturesPlace);
	}

	@Override
	public List<LecturesPlace> getEventList(LecturesPlace lecturesPlace) {
		// TODO Auto-generated method stub
		return lecturesPlaceDao.getEventList(lecturesPlace);
	}

	@Override
	public List<LecturesPlace> getAllEventList(LecturesPlace lecturesPlace) {
		return lecturesPlaceDao.getAllEventList(lecturesPlace);
	}
	
	@Override
	public List<LecturesPlace> getAllLecturesPlaces() {
		// TODO Auto-generated method stub
		return lecturesPlaceDao.getAllLecturesPlaces();
	}

	@Override
	public List<Map<String, Object>> getNormalList() {
		return lecturesPlaceDao.getNormalList();
	}

	@Override
	public void updateNormalData(LecturesPlace lecturesPlace) {
		lecturesPlaceDao.updateNormalData(lecturesPlace);
	}


}
