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
		return lecturesPlaceDao.getList(lecturesPlace);
	}

	@Override
	public List<LecturesPlace> getList(int pageCount, int pageStart, LecturesPlace lecturesPlace) {
		return null;
	}

	@Override
	public Integer getCount(LecturesPlace lecturesPlace) {
		return null;
	}

	@Override
	public LecturesPlace getData(LecturesPlace lecturesPlace) {
		return null;
	}

	@Override
	public Integer getNextId() {
		return lecturesPlaceDao.getNextId();
	}

	@Override
	public void add(LecturesPlace lecturesPlace) {
		lecturesPlaceDao.add(lecturesPlace);
	}

	@Override
	public void update(LecturesPlace lecturesPlace) {
		lecturesPlaceDao.update(lecturesPlace);
	}

	@Override
	public void delete(Integer id) {
		lecturesPlaceDao.delete(id);
	}

	@Override
	public List<LecturesPlace> getPlaceList(LecturesPlace lecturesPlace) {
		return lecturesPlaceDao.getPlaceList(lecturesPlace);
	}

	@Override
	public List<LecturesPlace> getEventList(LecturesPlace lecturesPlace) {
		return lecturesPlaceDao.getEventList(lecturesPlace);
	}

	@Override
	public List<LecturesPlace> getAllEventList(LecturesPlace lecturesPlace) {
		return lecturesPlaceDao.getAllEventList(lecturesPlace);
	}
	
	@Override
	public List<LecturesPlace> getAllLecturesPlaces() {
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
