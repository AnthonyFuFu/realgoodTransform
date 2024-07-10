package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.AdmitContent;



/**
 * 考取金榜Service介面接口
 * @author 
 * @version 創建時間：2016-05-17
 */
public interface AdmitContentService {
	
	/**
	 * 取得金榜內容下一筆ID
	 * @return Integer
	 */
	public Integer getNextContentId();

	/**
	 * 取得前台金榜內容清單
	 * @param admitContent
	 * @return List<AdmitContent>
	 */
	public List<AdmitContent> getFrontContentList(AdmitContent admitContent);
	
	/**
	 * 取得金榜內容清單by金榜id
	 * @param admit_id
	 * @return List<AdmitContent>
	 */
	public List<AdmitContent> getContentListByAdmitId(Integer admit_id);
	
	/**
	 * 刪除金榜內容
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
	public void updateNormalData(AdmitContent admitContent);
	
}
