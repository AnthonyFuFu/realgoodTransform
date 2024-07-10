package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.WinnerContent;



public interface WinnerContentDao {
	/**
	 * 取得百官網公告內容資料清單
	 * @param winnerContent
	 * @return List<WinnerContent>
	 */
	public List<WinnerContent> getList(WinnerContent winnerContent);
	
	/**
	 * 取得百官網公告內容資料清單(分頁)
	 * @param winnerContent
	 * @param pageCount
	 * @param pageStart
	 * @return List<WinnerContent>
	 */
	public List<WinnerContent> getList(int pageCount, int pageStart, WinnerContent winnerContent);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增百官網公告內容
	 * @param winnerContent
	 */
	public void add(WinnerContent winnerContent);
	
	/**
	 * 修改百官網公告內容
	 * @param winnerContent
	 */
	public void update(WinnerContent winnerContent);
	
	/**
	 * 刪除百官網公告內容
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
	public void updateNormalData(WinnerContent winnerContent);
}
