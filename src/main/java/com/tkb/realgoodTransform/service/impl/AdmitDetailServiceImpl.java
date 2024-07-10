package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.AdmitDetailDao;
import com.tkb.realgoodTransform.model.AdmitDetail;
import com.tkb.realgoodTransform.model.AdmitDetailLog;
import com.tkb.realgoodTransform.service.AdmitDetailService;



/**
 * 考取金榜Service實作類
 * 
 * @author
 * @version 創建時間：2016-05-17
 */
@Service
public class AdmitDetailServiceImpl implements AdmitDetailService {
	
	@Autowired
	private AdmitDetailDao admitDetailDao;

	public Integer getNextDetailId() {
		return admitDetailDao.getNextDetailId();
	}

	public Integer getNextDetailLogId() {
		return admitDetailDao.getNextDetailLogId();
	}

	public AdmitDetail getData(AdmitDetail admitDetail) {
		return admitDetailDao.getData(admitDetail);
	}

	public List<AdmitDetail> getDetailList(AdmitDetail admitDetail) {
		return admitDetailDao.getDetailList(admitDetail);
	}

	public void updateDetail(int pId, String pName, String pType, String pAchievement, String pUser) {
		admitDetailDao.updateDetail(pId, pName, pType, pAchievement, pUser);
	}

	public void deleteDetail(int pId) {
		admitDetailDao.deleteDetail(pId);
	}

	public void addDetailLog(AdmitDetailLog admitDetailLog) {
		admitDetailDao.addDetailLog(admitDetailLog);
	}

	
//	===========
	
	
	
	@Override
	public List<Map<String, Object>> getNormalDetailList() {
		return admitDetailDao.getNormalDetailList();
	}

	@Override
	public void updateNormalDetailData(AdmitDetail admitDetail) {
		admitDetailDao.updateNormalDetailData(admitDetail);
	}

	@Override
	public List<Map<String, Object>> getNormalDetailLogList() {
		return admitDetailDao.getNormalDetailLogList();
	}

	@Override
	public void updateNormalDetailLogData(AdmitDetailLog admitDetailLog) {
		admitDetailDao.updateNormalDetailLogData(admitDetailLog);
	}

}
