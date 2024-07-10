package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.AdmitCategory;



/**
 * 金榜類別Service介面接口
 * @author 
 * @version 創建時間：2016-05-17
 */
public interface AdmitCategoryService {
	
	/**
	 * 取得子層類別資料清單
	 * @param admitCategory
	 * @return List<AdmitCategory>
	 */
	public List<AdmitCategory> getSubList(AdmitCategory admitCategory);
	
	/**
	 * 取得金榜類別資料清單(層級)
	 * @param admitCategory
	 * @return List<AdmitCategory>
	 */
	public List<AdmitCategory> getLayerList(String layer, AdmitCategory admitCategory);
	
	/**
	 * 取得金榜類別資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param admitCategory
	 * @return List<AdmitCategory>
	 */
	public List<AdmitCategory> getList(int pageCount, int pageStart, AdmitCategory admitCategory);
	
	/**
	 * 取得金榜類別總筆數
	 * @param admitCategory
	 * @return Integer
	 */
	public Integer getCount(AdmitCategory admitCategory);
	
	/**
	 * 取得單筆金榜類別
	 * @param admitCategory
	 * @return admitCategory
	 */
	public AdmitCategory getData(AdmitCategory admitCategory);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增金榜類別
	 * @param admitCategory
	 */
	public void add(AdmitCategory admitCategory);
	
	/**
	 * 修改金榜類別
	 * @param admitCategory
	 */
	public void update(AdmitCategory admitCategory);
	
	/**
	 * 刪除金榜類別
	 * @param id
	 */
	public void delete(AdmitCategory admitCategory, Integer id);
	
	/**
	 * 依名稱取得金榜類別筆數
	 * @param categoryName
	 * @return Integer
	 */
	public Integer getCountByName(String categoryName);
	
	/**
	 * 重新排序
	 */
	public void resetSort(AdmitCategory admitCategory);
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(AdmitCategory admitCategory);
	
}
