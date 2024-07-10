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
		// TODO Auto-generated method stub
		return schoolBulletinDao.getListByCategory(schoolBulletin);
	}

	@Override
	public List<SchoolBulletin> getList(int pageCount, int pageStart, SchoolBulletin schoolBulletin, int groupId) {
		// TODO Auto-generated method stub
		return schoolBulletinDao.getList(pageCount, pageStart, schoolBulletin, groupId);
	}

	@Override
	public List<SchoolBulletin> getIndexList(SchoolBulletin schoolBulletin) {
		// TODO Auto-generated method stub
		return schoolBulletinDao.getIndexList(schoolBulletin);
	}
	
	@Override
	public List<SchoolBulletin> getFrontList(SchoolBulletin schoolBulletin) {
		// TODO Auto-generated method stub
		return schoolBulletinDao.getFrontList(schoolBulletin);
	}

	@Override
	public List<SchoolBulletin> getFrontList(int pageCount, int pageStart, SchoolBulletin schoolBulletin,
			String search_sort) {
		// TODO Auto-generated method stub
		return schoolBulletinDao.getFrontList(pageCount, pageStart, schoolBulletin, search_sort);
	}

	@Override
	public List<SchoolBulletin> getIndexFrontList(int pageCount, int pageStart, SchoolBulletin schoolBulletin,
			String search_sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SchoolBulletin> getFrontList(int pageCount, int pageStart, SchoolBulletin schoolBulletin,
			List<Area>areaList, String search_sort) {
		// TODO Auto-generated method stub
		return schoolBulletinDao.getFrontList(pageCount, pageStart, schoolBulletin, areaList, search_sort);
	}

	@Override
	public Integer getCount(SchoolBulletin schoolBulletin) {
		// TODO Auto-generated method stub
		return schoolBulletinDao.getCount(schoolBulletin);
	}

	@Override
	public Integer getFrontCount(SchoolBulletin schoolBulletin) {
		// TODO Auto-generated method stub
		return schoolBulletinDao.getFrontCount(schoolBulletin);
	}

	@Override
	public SchoolBulletin getData(SchoolBulletin schoolBulletin) {
		// TODO Auto-generated method stub
		return schoolBulletinDao.getData(schoolBulletin);
	}

	@Override
	public SchoolBulletin getFrontData(SchoolBulletin schoolBulletin) {
		// TODO Auto-generated method stub
		return schoolBulletinDao.getFrontData(schoolBulletin);
	}

	@Override
	public Integer getNextId() {
		// TODO Auto-generated method stub
		return schoolBulletinDao.getNextId();
	}

	@Override
	public void add(SchoolBulletin schoolBulletin) {
		// TODO Auto-generated method stub
		schoolBulletinDao.add(schoolBulletin);
	}

	@Override
	public void update(SchoolBulletin schoolBulletin) {
		// TODO Auto-generated method stub
		schoolBulletinDao.update(schoolBulletin);
	}

	@Override
	public void updateClickRate(SchoolBulletin schoolBulletin) {
		// TODO Auto-generated method stub
		schoolBulletinDao.updateClickRate(schoolBulletin);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		schoolBulletinDao.delete(id);
	}

	@Override
	public List<SchoolBulletin> getAllList(SchoolBulletin schoolBulletin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateId(SchoolBulletin schoolBulletin) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer userAccount(User user) {
		// TODO Auto-generated method stub
		return schoolBulletinDao.userAccount(user);
	}

	@Override
	public List<SchoolBulletin> getRandomList(SchoolBulletin schoolBulletin) {
		// TODO Auto-generated method stub
		return schoolBulletinDao.getRandomList(schoolBulletin);
	}

	@Override
	public List<SchoolBulletin> getPrevList(SchoolBulletin schoolBulletin) {
		// TODO Auto-generated method stub
		return schoolBulletinDao.getPrevList(schoolBulletin);
	}

	@Override
	public List<SchoolBulletin> getNextList(SchoolBulletin schoolBulletin) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return schoolBulletinDao.getNormalAreaList();
	}

	@Override
	public void updateNormalAreaData(Area area) {
		// TODO Auto-generated method stub
		schoolBulletinDao.updateNormalAreaData(area);
	}

	
	
	
	
	
	
	
}
