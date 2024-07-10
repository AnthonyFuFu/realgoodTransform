package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.Menu;
import com.tkb.realgoodTransform.model.User;



/**
 * 選單Dao介面接口
 * @author Joshua
 * @version 創建時間：2022-01-25
 */
public interface MenuDao {
	
	/**
	 * 取得選單總筆數
	 * @param menu
	 * @return Integer
	 */
	public Integer getCount(Menu menu);
	
	/**
	 * 取得選單資料清單(分頁)
	 * @param menu
	 * @param pageCount
	 * @param pageStart
	 * @return List<Menu>
	 */
	public List<Menu> getList(int pageCount, int pageStart, Menu menu);

	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增選單
	 * @param menu
	 */
	public void add(Menu menu);

	/**
	 * 取得單筆選單
	 * @param menu
	 * @return Menu
	 */
	public Menu getData(Menu menu);

	/**
	 * 修改選單
	 * @param menu
	 */
	public void update(Menu menu);

	/**
	 * 取得選單第一層資料清單
	 * @param user
	 * @return List<Menu>
	 */
	public List<Menu> getLayer1List(User user);

	/**
	 * 取得選單第二層資料清單
	 * @param actionList
	 * @return List<Menu>
	 */
	public List<Menu> getLayer2List(Menu menu);

	/**
	 * 取得子層選單資料清單
	 * @param menu
	 * @return List<Menu>
	 */
	public List<Menu> getSubList(Menu menu);

	/**
	 * 刪除選單
	 * @param id
	 */
	public void delete(int id);
	
	/**
	 * 取得選單第二層全部資料
	 * @param menu
	 * @return List<Menu>
	 */
	public List<Menu> getLayer2AllList(Menu menu);
	
	public List<Map<String, Object>> getMenuLayer2List(Menu menu, Integer group_id);
	
	
	/**
	 * 拿舊大碩的菜單id放在網址用
	 * @param String link
	 * @return Integer
	 */
	public Integer getMenuId(String link);

}
