package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.AdmitContentOptionDao;
import com.tkb.realgoodTransform.model.AdmitContentOption;
import com.tkb.realgoodTransform.service.AdmitContentOptionService;



/**
 * 考取金榜Service實作類
 * @author 
 * @version 創建時間：2016-05-17
 */
@Service
public class AdmitContentOptionServiceImpl implements AdmitContentOptionService{

	@Autowired
	private AdmitContentOptionDao admitContentOptionDao;
	
	public Integer getNextContentOptionId() {
		return admitContentOptionDao.getNextContentOptionId();
	}

	public List<AdmitContentOption> getFrontContentOptionList(AdmitContentOption admitContentOption) {
		return admitContentOptionDao.getFrontContentOptionList(admitContentOption);
	}
	
	public List<AdmitContentOption> getContentOptionListByContentId(Integer content_id) {
		return admitContentOptionDao.getContentOptionListByContentId(content_id);
	}
	
	public void delete(Integer id) {
		admitContentOptionDao.delete(id);
	}

	
//	========
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		return admitContentOptionDao.getNormalList();
	}

	@Override
	public void updateNormalData(AdmitContentOption admitContentOption) {
		admitContentOptionDao.updateNormalData(admitContentOption);
	}
	

}
