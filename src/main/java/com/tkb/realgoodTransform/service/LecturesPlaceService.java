package com.tkb.realgoodTransform.service;



import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.LecturesPlace;



public interface LecturesPlaceService {
	/**
	 * 取得熱門講座地點資料清單
	 * 
	 * @param lecturesPlace
	 * @return List<LecturesPlace>
	 */
	public List<LecturesPlace> getList(LecturesPlace lecturesPlace);

	/**
	 * 取得熱門講座地點資料清單(分頁)
	 * 
	 * @param pageCount
	 * @param pageStart
	 * @param lecturesPlace
	 * @return List<LecturesPlace>
	 */
	public List<LecturesPlace> getList(int pageCount, int pageStart, LecturesPlace lecturesPlace);

	/**
	 * 取得熱門講座地點總筆數
	 * 
	 * @param lecturesPlace
	 * @return Integer
	 */
	public Integer getCount(LecturesPlace lecturesPlace);

	/**
	 * 取得單筆熱門講座地點
	 * 
	 * @param lecturesPlace
	 * @return LecturesPlace
	 */
	public LecturesPlace getData(LecturesPlace lecturesPlace);

	/**
	 * 取得下一筆ID
	 * 
	 * @return Integer
	 */
	public Integer getNextId();

	/**
	 * 新增熱門講座地點
	 * 
	 * @param lecturesPlace
	 */
	public void add(LecturesPlace lecturesPlace);

	/**
	 * 修改熱門講座地點
	 * 
	 * @param lecturesPlace
	 */
	public void update(LecturesPlace lecturesPlace);

	/**
	 * 刪除熱門講座地點
	 * 
	 * @param id
	 */
	public void delete(Integer id);

	/**
	 * 取得單筆熱門講座地點資料
	 * 
	 * @param lecturesPlace
	 * @return List<LecturesPlace>
	 */
	public List<LecturesPlace> getPlaceList(LecturesPlace lecturesPlace);

	/**
	 * 取得講座場次
	 * 
	 * @param lecturesPlace
	 * @return List<LecturesPlace>
	 */
	public List<LecturesPlace> getEventList(LecturesPlace lecturesPlace);

	/**
	 * 取得講座場次
	 * 
	 * @param lecturesPlace
	 * @return List<LecturesPlace>
	 */
	public List<LecturesPlace> getAllEventList(LecturesPlace lecturesPlace);

	/**
	 * 取得all熱門講座地點資料
	 * 
	 * @param lecturesPlace
	 * @return List<LecturesPlace>
	 */
	public List<LecturesPlace> getAllLecturesPlaces();
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(LecturesPlace lecturesPlace);
}
