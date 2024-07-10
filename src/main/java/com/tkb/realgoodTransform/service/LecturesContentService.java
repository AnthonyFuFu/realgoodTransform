package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.LecturesContent;



public interface LecturesContentService {
	/**
	 * 取得熱門講座內容資料清單
	 * @param lecturesContent
	 * @return List<LecturesContent>
	 */
	public List<LecturesContent> getList(LecturesContent lecturesContent);
	
	/**
	 * 取得熱門講座內容資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param lecturesContent
	 * @return List<LecturesContent>
	 */
	public List<LecturesContent> getList(int pageCount, int pageStart, LecturesContent lecturesContent);
	
	/**
	 * 取得熱門講座內容總筆數
	 * @param lecturesContent
	 * @return Integer
	 */
	public Integer getCount(LecturesContent lecturesContent);
	
	/**
	 * 取得單筆熱門講座內容
	 * @param lecturesContent
	 * @return LecturesContent
	 */
	public LecturesContent getData(LecturesContent lecturesContent);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增熱門講座內容
	 * @param lecturesContent
	 */
	public void add(LecturesContent lecturesContent);
	
	/**
	 * 修改熱門講座內容
	 * @param lecturesContent
	 */
	public void update(LecturesContent lecturesContent);
	
	/**
	 * 刪除熱門講座內容
	 * @param id
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
	public void updateNormalData(LecturesContent lecturesContent);
	
	
}
