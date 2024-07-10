package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.Marquee;



public interface MarqueeDao {
		
		/**
		 * 取得首頁Marquee 資料清單
		 * @param marquee
		 * @return List<Marquee>
		 */
		public List<Marquee> getList(int pageCount, int pageStart, Marquee marquee);
		
		/**
		 * 取得首頁Marquee 總筆數
		 * @param marquee
		 * @return Integer
		 */
		public Integer getCount(Marquee marquee);
		
		/**
		 * 取得單筆首頁Marquee 
		 * @param marquee
		 * @return Marquee
		 */
		public Marquee getData(Marquee marquee);
		
		/**
		 * 取得下一筆ID
		 * @return Integer
		 */
		public Integer getNextId();
		
		/**
		 * 新增首頁Marquee 
		 * @param marquee
		 */
		public void add(Marquee marquee);
		
		/**
		 * 修改首頁Marquee 
		 * @param marquee
		 */
		public void update(Marquee marquee);
		
		/**
		 * 刪除首頁Marquee 
		 * @param id
		 */
		public void delete(Integer id);
		
		/**
		 * 重新排序Marquee
		 * @param marquee
		 */
		public void resetSort();
		
		/**
		 * 取得首頁跑馬燈清單
		 * @param marquee
		 * @return List<Marquee>
		 */
		public List<Marquee> getFrontList(Marquee marquee);
		
		/**
		 * 更新點擊綠
		 */
		public void updateClickRate(Marquee marquee);
		
		
//		=======================
		/**
		 * 獲取正是機Bannerlist
		 */
		public List<Map<String, Object>> getNormalBannerList();
		/**
		 * 更新增增正是機Banner
		 * @param Banner
		 */
		public void updateNormalData(Marquee marquee);
}
