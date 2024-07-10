package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.AdmitDao;
import com.tkb.realgoodTransform.model.Admit;
import com.tkb.realgoodTransform.model.AdmitContent;
import com.tkb.realgoodTransform.model.AdmitContentOption;
import com.tkb.realgoodTransform.model.AdmitDetail;
import com.tkb.realgoodTransform.model.AdmitLog;
import com.tkb.realgoodTransform.service.AdmitService;



/**
 * 考取金榜Service實作類
 * 
 * @author
 * @version 創建時間：2016-05-17
 */
@Service
public class AdmitServiceImpl implements AdmitService {
	@Autowired
	private AdmitDao admitDao;

	@Override
	public List<Admit> getList(int pageCount, int pageStart, Admit admit) {
		return admitDao.getList(pageCount, pageStart, admit);
	}

	@Override
	public List<Admit> getFrontList() {
		return admitDao.getFrontList();
	}

	@Override
	public List<Admit> getIndexList() {
		return admitDao.getIndexList();
	}

	@Override
	public List<Admit> getFrontList(int pageCount, int pageStart, Admit admit) {
		return admitDao.getFrontList(pageCount, pageStart, admit);
	}

	@Override
	public Integer getCount(Admit admit) {
		return admitDao.getCount(admit);
	}

	@Override
	public Integer getFrontCount(Admit admit) {
		return admitDao.getFrontCount(admit);
	}

	@Override
	public Admit getData(Admit admit) {
		return admitDao.getData(admit);
	}

	@Override
	public Admit getFrontData(Admit admit) {
		return admitDao.getFrontData(admit);
	}

	@Override
	public List<Admit> getAllAdmitYear() {
		return admitDao.getAllAdmitYear();
	}

	@Override
	public Integer getCountListByCategory(int categoryId) {
		return admitDao.getCountListByCategory(categoryId);
	}

	@Override
	public Integer getNextId() {
		return admitDao.getNextId();
	}

	@Override
	public void add(Admit admit) {
		admitDao.add(admit);
	}

	@Override
	public void addContent(AdmitContent admitContent) {
		admitDao.addContent(admitContent);
	}

	@Override
	public void addContentOption(AdmitContentOption admitContentOption) {
		admitDao.addContentOption(admitContentOption);
	}

	@Override
	public void addDetail(AdmitDetail admitDetail) {
		admitDao.addDetail(admitDetail);
	}

	@Override
	public void update(Admit admit) {
		admitDao.update(admit);
	}

	@Override
	public void updateClickRate(Admit admit) {
		admitDao.updateClickRate(admit);
	}

	@Override
	public void delete(Integer id) {
		admitDao.delete(id);
	}

	@Override
	public void updateDisplayHide(Integer id) {
		admitDao.updateDisplayHide(id);
	}

	@Override
	public void updateDisplayShow(Integer id) {
		admitDao.updateDisplayShow(id);
	}

	@Override
	public Integer getNextAdmitLogId() {
		return admitDao.getNextAdmitLogId();
	}

	@Override
	public void addAdmitLog(AdmitLog admitLog) {
		admitDao.addAdmitLog(admitLog);
	}

	@Override
	public List<Admit> getList(Admit admit) {
		return admitDao.getList(admit);
	}

	@Override
	public void updateId(Admit admit) {
		admitDao.updateId(admit);
	}

	@Override
	public List<Map<String, Object>> getNormalList() {
		return admitDao.getNormalList();
	}

	@Override
	public void updateNormalData(Admit admit) {
		admitDao.updateNormalData(admit);
	}
}
