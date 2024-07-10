package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.AdmitContentOption;



/**
 * 考取金榜Service介面接口
 * @author 
 * @version 創建時間：2016-05-17
 */
public interface AdmitContentOptionService {
	
	/**
	 * 取得金榜內容項目下一筆ID
	 * @return Integer
	 */
	public Integer getNextContentOptionId();

	/**
	 * 取得前台金榜內容項目清單
	 * @param admitContentOption
	 * @return List<AdmitContentOption>
	 */
	public List<AdmitContentOption> getFrontContentOptionList(AdmitContentOption admitContentOption);
	
	/**
	 * 取得金榜內容項目清單BY content_id
	 * @param content_id
	 * @return List<AdmitContentOption>
	 */
	public List<AdmitContentOption> getContentOptionListByContentId(Integer content_id);
	
	/**
	 * 刪除金榜內容項目
	 */
	public void delete(Integer id);
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(AdmitContentOption admitContentOption);
	
}
