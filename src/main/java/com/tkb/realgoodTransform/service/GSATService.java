package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.GSAT;

/**
 * 考試資訊Service介面接口
 */
public interface GSATService {

	/**
	 * 取得考試資訊資料清單(分頁)
	 */
	public List<GSAT> getList(int pageCount, int pageStart, GSAT gSAT);
	/**
	 * 取得考試資訊總筆數
	 */
	public Integer getCount(GSAT gSAT);
	
	/**
	 * 取得前台考試資訊相同類別文章
	 */
	public List<GSAT> getCategoryList(GSAT gSAT);	
	
	/**
	 * 取得單筆考試資訊
	 */
	public GSAT getData(GSAT gSAT);
	
	/**
	 * 取得前台單筆考試資訊
	 */
	public GSAT getFrontData(GSAT gSAT);
	
	/**
	 * 取得下一筆ID
	 */
	public Integer getNextId();
	
	/**
	 * 新增考試資訊
	 */
	public void add(GSAT gSAT);
	
	/**
	 * 修改考試資訊
	 */
	public void update(GSAT gSAT);
	
	/**
	 * 修改考試資訊點擊率
	 */
	public void updateClickRate(GSAT gSAT);
	
	/**
	 * 刪除考試資訊
	 */
	public void delete(GSAT gSAT ,Integer id);
	
	
//	==========================
	/**
	 * 獲取正是機Menu
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Menu
	 */
	public void updateNormalData(GSAT gSAT);
	
}
