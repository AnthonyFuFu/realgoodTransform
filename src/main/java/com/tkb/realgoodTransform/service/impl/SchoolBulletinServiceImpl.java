package com.tkb.realgoodTransform.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.SchoolBulletinDao;
import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.Location;
import com.tkb.realgoodTransform.model.SchoolBulletin;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.SchoolBulletinService;


@Service
public class SchoolBulletinServiceImpl implements SchoolBulletinService {

	@Autowired
	SchoolBulletinDao schoolBulletinDao;
	
	@Override
	public List<SchoolBulletin> getListByCategory(SchoolBulletin schoolBulletin) {
		return schoolBulletinDao.getListByCategory(schoolBulletin);
	}

	@Override
	public List<SchoolBulletin> getList(int pageCount, int pageStart, SchoolBulletin schoolBulletin, int groupId) {
		return schoolBulletinDao.getList(pageCount, pageStart, schoolBulletin, groupId);
	}

	@Override
	public List<SchoolBulletin> getIndexList(SchoolBulletin schoolBulletin) {
		return schoolBulletinDao.getIndexList(schoolBulletin);
	}
	
	@Override
	public List<SchoolBulletin> getFrontList(SchoolBulletin schoolBulletin) {
		return schoolBulletinDao.getFrontList(schoolBulletin);
	}

	@Override
	public List<SchoolBulletin> getFrontList(int pageCount, int pageStart, SchoolBulletin schoolBulletin,
			String search_sort) {
		return schoolBulletinDao.getFrontList(pageCount, pageStart, schoolBulletin, search_sort);
	}

	@Override
	public List<SchoolBulletin> getIndexFrontList(int pageCount, int pageStart, SchoolBulletin schoolBulletin,
			String search_sort) {
		return null;
	}

	@Override
	public List<SchoolBulletin> getFrontList(int pageCount, int pageStart, SchoolBulletin schoolBulletin,
			List<Area>areaList, String search_sort) {
		return schoolBulletinDao.getFrontList(pageCount, pageStart, schoolBulletin, areaList, search_sort);
	}

	@Override
	public Integer getCount(SchoolBulletin schoolBulletin) {
		return schoolBulletinDao.getCount(schoolBulletin);
	}

	@Override
	public Integer getFrontCount(SchoolBulletin schoolBulletin) {
		return schoolBulletinDao.getFrontCount(schoolBulletin);
	}

	@Override
	public SchoolBulletin getData(SchoolBulletin schoolBulletin) {
		return schoolBulletinDao.getData(schoolBulletin);
	}

	@Override
	public SchoolBulletin getFrontData(SchoolBulletin schoolBulletin) {
		return schoolBulletinDao.getFrontData(schoolBulletin);
	}

	@Override
	public Integer getNextId() {
		return schoolBulletinDao.getNextId();
	}

	@Override
	public void add(SchoolBulletin schoolBulletin) {
		schoolBulletinDao.add(schoolBulletin);
	}

	@Override
	public void update(SchoolBulletin schoolBulletin) {
		schoolBulletinDao.update(schoolBulletin);
	}

	@Override
	public void updateClickRate(SchoolBulletin schoolBulletin) {
		schoolBulletinDao.updateClickRate(schoolBulletin);
	}

	@Override
	public void delete(Integer id) {
		schoolBulletinDao.delete(id);
	}

	@Override
	public List<SchoolBulletin> getAllList(SchoolBulletin schoolBulletin) {
		return null;
	}

	@Override
	public void updateId(SchoolBulletin schoolBulletin) {

	}

	@Override
	public Integer userAccount(User user) {
		return schoolBulletinDao.userAccount(user);
	}

	@Override
	public List<SchoolBulletin> getRandomList(SchoolBulletin schoolBulletin) {
		return schoolBulletinDao.getRandomList(schoolBulletin);
	}

	@Override
	public List<SchoolBulletin> getPrevList(SchoolBulletin schoolBulletin) {
		return schoolBulletinDao.getPrevList(schoolBulletin);
	}

	@Override
	public List<SchoolBulletin> getNextList(SchoolBulletin schoolBulletin) {
		return schoolBulletinDao.getNextList(schoolBulletin);
	}
	
	@Override
	public Integer getCountByApiLocation(SchoolBulletin schoolBulletin, List<Location> locationList) {
		return schoolBulletinDao.getCountByApiLocation(schoolBulletin, locationList);
	}

	@Override
	public List<SchoolBulletin> getListByApiLocation(int pageCount, int pageStart, SchoolBulletin schoolBulletin,
			List<Location> locationList, String search_sort) {
		return schoolBulletinDao.getListByApiLocation(pageCount, pageStart, schoolBulletin, locationList, search_sort);
	}

	
//	=========================
	
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		return schoolBulletinDao.getNormalList();
	}

	@Override
	public void updateNormalData(SchoolBulletin schoolBulletin) {
		schoolBulletinDao.updateNormalData(schoolBulletin);
	}

	@Override
	public List<Map<String, Object>> getNormalAreaList() {
		return schoolBulletinDao.getNormalAreaList();
	}

	@Override
	public void updateNormalAreaData(Area area) {
		schoolBulletinDao.updateNormalAreaData(area);
	}
	
}
