package com.tkb.realgoodTransform.service;

import java.util.List;

import com.tkb.realgoodTransform.model.GSATAdmit;

/**
 * 外特專區Service介面接口
 */
public interface GSATAdmitService {

	/**
	 * 取得外特專區資料清單(分頁)
	 */
	public List<GSATAdmit> getList(int pageCount, int pageStart, GSATAdmit gSATAdmit);
	
	/**
	 * 取得前台外特專區清單(依地區類別查詢)
	 */
	public List<GSATAdmit> getFrontList(GSATAdmit gSATAdmit);
	
	/**
	 * 取得外特專區總筆數
	 */
	public Integer getCount(GSATAdmit gSATAdmit);
	
	
	/**
	 * 取得單筆外特專區
	 */
	public GSATAdmit getData(GSATAdmit gSATAdmit);
	
	
	/**
	 * 取得下一筆ID
	 */
	public Integer getNextId();
	
	/**
	 * 新增外特專區
	 */
	public void add(GSATAdmit gSATAdmit);
	
	/**
	 * 修改外特專區
	 */
	public void update(GSATAdmit gSATAdmit);
	
	/**
	 * 修改外特專區點擊率
	 */
	public void updateClickRate(GSATAdmit gSATAdmit);
	
	/**
	 * 刪除外特專區
	 */
	public void delete(GSATAdmit gSATAdmit ,Integer id);			
	
}
