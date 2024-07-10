package com.tkb.realgoodTransform.dao;

import java.util.List;

import com.tkb.realgoodTransform.model.GSATWinnerContent;

/**
 * 學堂公告內容Dao介面接口
 */
public interface GSATWinnerContentDao {
	
	/**
	 * 取得學堂公告內容資料清單
	 */
	public List<GSATWinnerContent> getList(GSATWinnerContent gSATWinnerContent);
	
	/**
	 * 取得學堂公告內容資料清單(分頁)
	 */
	public List<GSATWinnerContent> getList(int pageCount, int pageStart, GSATWinnerContent gSATWinnerContent);
	
	/**
	 * 取得學堂公告內容總筆數
	 */
	public Integer getCount(GSATWinnerContent gSATWinnerContent);
	
	/**
	 * 取得單筆學堂公告內容
	 */
	public GSATWinnerContent getData(GSATWinnerContent gSATWinnerContent);
	
	/**
	 * 取得下一筆ID
	 */
	public Integer getNextId();
	
	/**
	 * 新增學堂公告內容
	 */
	public void add(GSATWinnerContent gSATWinnerContent);
	
	/**
	 * 修改學堂公告內容
	 */
	public void update(GSATWinnerContent gSATWinnerContent);
	
	/**
	 * 刪除學堂公告內容
	 */
	public void delete(Integer id);
	
}
