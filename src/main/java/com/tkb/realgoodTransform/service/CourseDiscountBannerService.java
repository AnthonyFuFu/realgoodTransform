package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.CourseDiscountBanner;



/**
 * 課程優惠Banner Service介面接口
 * @author Felix
 * @version 創建時間：2022-03-11
 */
public interface CourseDiscountBannerService {
	/**
	 * 取得課程優惠Banner 資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param courseDiscountBanner
	 * @return List<CourseDiscountBanner>
	 */
	public List<CourseDiscountBanner> getList(int pageCount, int pageStart, CourseDiscountBanner courseDiscountBanner);
	
	/**
	 * 取得前台課程優惠Banner 清單(依地區類別查詢)
	 * @param pageCount
	 * @param pageStart
	 * @param courseDiscountBanner
	 * @return List<CourseDiscountBanner>
	 */
	public List<CourseDiscountBanner> getFrontList(CourseDiscountBanner courseDiscountBanner);
	
	
	/**
	 * 取得課程優惠Banner 總筆數
	 * @param courseDiscountBanner
	 * @return Integer
	 */
	public Integer getCount(CourseDiscountBanner courseDiscountBanner);
	
	/**
	 * 取得前台課程優惠Banner 總筆數
	 * @param courseDiscountBanner
	 * @return Integer
	 */
	public Integer getFrontCount(CourseDiscountBanner courseDiscountBanner);

	/**
	 * 取得前台課程優惠Banner show總筆數
	 * @param courseDiscountBanner
	 * @return Integer
	 */
	public Integer getShowCount(CourseDiscountBanner courseDiscountBanner);
	
	
	/**
	 * 取得單筆課程優惠Banner 
	 * @param courseDiscountBanner
	 * @return CourseDiscountBanner
	 */
	public CourseDiscountBanner getData(CourseDiscountBanner courseDiscountBanner);
	
	/**
	 * 取得前台單筆課程優惠Banner 
	 * @param courseDiscountBanner
	 * @return CourseDiscountBanner
	 */
	public CourseDiscountBanner getFrontData(CourseDiscountBanner courseDiscountBanner);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增課程優惠Banner 
	 * @param courseDiscountBanner
	 */
	public void add(CourseDiscountBanner courseDiscountBanner);
	
	/**
	 * 修改課程優惠Banner 
	 * @param courseDiscountBanner
	 */
	public void update(CourseDiscountBanner courseDiscountBanner);
	
	/**
	 * 修改課程優惠Banner 點擊率
	 * @param courseDiscountBanner
	 */
	public void updateClickRate(CourseDiscountBanner courseDiscountBanner);
	
	/**
	 * 修改Banner排序
	 * @param courseDiscountBanner
	 */
	public void updateSort(CourseDiscountBanner courseDiscountBanner);
	/**
	 * 刪除課程優惠Banner 
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 重新排序Banner
	 * @param banner
	 */
	public void resetSort();	
	
	
	
//	=======================
	/**
	 * 獲取正是機courseBannerlist
	 */
	public List<Map<String, Object>> getNormalCourseBannerList();
	/**
	 * 更新增增正是機
	 * @param courseDiscountBanner
	 */
	public void updateNormalData(CourseDiscountBanner courseDiscountBanner);
	
}
