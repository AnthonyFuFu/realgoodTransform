package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.AdmitContentDao;
import com.tkb.realgoodTransform.model.AdmitContent;
import com.tkb.realgoodTransform.service.AdmitContentService;



/**
 * 考取金榜Service實作類
 * 
 * @author
 * @version 創建時間：2016-05-17
 */
@Service
public class AdmitContentServiceImpl implements AdmitContentService {
	@Autowired
	private AdmitContentDao admitContentDao;

	public Integer getNextContentId() {
		return admitContentDao.getNextContentId();
	}

	public List<AdmitContent> getFrontContentList(AdmitContent admitContent) {
		return admitContentDao.getFrontContentList(admitContent);
	}

	public List<AdmitContent> getContentListByAdmitId(Integer admit_id) {
		return admitContentDao.getContentListByAdmitId(admit_id);
	}
	public void delete(Integer id) {
		admitContentDao.delete(id);
	}

	
//	==========




	@Override
	public List<Map<String, Object>> getNormalList() {
		return admitContentDao.getNormalList();
	}

	@Override
	public void updateNormalData(AdmitContent admitContent) {
		admitContentDao.updateNormalData(admitContent);
	}

}
