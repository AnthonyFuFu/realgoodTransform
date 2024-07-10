package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.AdmitDetail;
import com.tkb.realgoodTransform.model.AdmitDetailLog;



public interface AdmitDetailDao {
	
	/**
	 * 取得金榜榜單明細下一筆ID
	 * @return Integer
	 */
	public Integer getNextDetailId();
	
	/**
	 * 取得榜單LOG下一筆ID
	 * @return Integer
	 */
	public Integer getNextDetailLogId();
	
	/**
	 * 取得單筆
	 * @param admitDetail
	 * @return admitDetail
	 */
	public AdmitDetail getData(AdmitDetail admitDetail);

	/**
	 * 取得匯入榜單清單
	 * @param admitDetail
	 * @return List<AdmitDetail>
	 */
	public List<AdmitDetail> getDetailList(AdmitDetail admitDetail);
	
	/**
	 * 更新單筆榜單資料
	 */
	public void updateDetail(int pId,String pName,String pType,String pAchievement,String pUser);
	
	/**
	 * 刪除單筆榜單資料
	 */
	public void deleteDetail(int pId);
	
	/**
	 *  新增榜單LOG
	 */
	public void addDetailLog(AdmitDetailLog admitDetailLog);
	
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalDetailList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalDetailData(AdmitDetail admitDetail);
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalDetailLogList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalDetailLogData(AdmitDetailLog admitDetailLog);
	
}
