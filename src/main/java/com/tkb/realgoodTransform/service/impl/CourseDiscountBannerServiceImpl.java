package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.CourseDiscountBannerDao;
import com.tkb.realgoodTransform.model.CourseDiscountBanner;
import com.tkb.realgoodTransform.service.CourseDiscountBannerService;



/**
 * 首頁Banner Service實作類
 * @author Felix
 * @version 創建時間：2022-03-11
 */
@Service
public class CourseDiscountBannerServiceImpl implements CourseDiscountBannerService {

	@Autowired
	private CourseDiscountBannerDao courseDiscountBannerDao;
	
	@Override
	public List<CourseDiscountBanner> getList(int pageCount, int pageStart, CourseDiscountBanner courseDiscountBanner) {
		return courseDiscountBannerDao.getList(pageCount, pageStart, courseDiscountBanner);
	}

	@Override
	public List<CourseDiscountBanner> getFrontList(CourseDiscountBanner courseDiscountBanner) {
		return courseDiscountBannerDao.getFrontList(courseDiscountBanner);
	}

	@Override
	public Integer getCount(CourseDiscountBanner courseDiscountBanner) {
		// TODO Auto-generated method stub
		return courseDiscountBannerDao.getCount(courseDiscountBanner);
	}

	@Override
	public Integer getFrontCount(CourseDiscountBanner courseDiscountBanner) {
		// TODO Auto-generated method stub
		return courseDiscountBannerDao.getFrontCount(courseDiscountBanner);
	}

	@Override
	public Integer getShowCount(CourseDiscountBanner courseDiscountBanner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CourseDiscountBanner getData(CourseDiscountBanner courseDiscountBanner) {
		// TODO Auto-generated method stub
		return courseDiscountBannerDao.getData(courseDiscountBanner);
	}

	@Override
	public CourseDiscountBanner getFrontData(CourseDiscountBanner courseDiscountBanner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNextId() {
		// TODO Auto-generated method stub
		return courseDiscountBannerDao.getNextId();
	}

	@Override
	public void add(CourseDiscountBanner courseDiscountBanner) {
		// TODO Auto-generated method stu
		courseDiscountBannerDao.add(courseDiscountBanner);

	}

	@Override
	public void update(CourseDiscountBanner courseDiscountBanner) {
		courseDiscountBannerDao.update(courseDiscountBanner);

	}

	@Override
	public void updateClickRate(CourseDiscountBanner courseDiscountBanner) {
		// TODO Auto-generated method stub
		courseDiscountBannerDao.updateClickRate(courseDiscountBanner);
	}

	@Override
	public void updateSort(CourseDiscountBanner courseDiscountBanner) {
		// TODO Auto-generated method stub
		courseDiscountBannerDao.updateSort(courseDiscountBanner);
	}

	@Override
	public void delete(Integer id) {
		courseDiscountBannerDao.delete(id);

	}

	@Override
	public void resetSort() {
		courseDiscountBannerDao.resetSort();

	}

	@Override
	public List<Map<String, Object>> getNormalCourseBannerList() {
		return courseDiscountBannerDao.getNormalCourseBannerList();
	}

	@Override
	public void updateNormalData(CourseDiscountBanner courseDiscountBanner) {
		courseDiscountBannerDao.updateNormalData(courseDiscountBanner);
	}

}
