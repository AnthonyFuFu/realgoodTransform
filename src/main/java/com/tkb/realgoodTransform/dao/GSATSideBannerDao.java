package com.tkb.realgoodTransform.dao;

import java.util.List;

import com.tkb.realgoodTransform.model.NavBanner;

/**
 * 首頁Banner Dao介面接口
 */
public interface GSATSideBannerDao {
    
    /**
     * 取得首頁Banner 資料清單
     */
    public List<NavBanner> getList(int pageCount, int pageStart, NavBanner navBanner);
    
    /**
     * 取得首頁Banner 前台資料清單
     */
    public List<NavBanner> getFrontList(NavBanner navBanner);
    
    
    public List<NavBanner> getFrontType2List(NavBanner navBanner);
    /**
     * 取得首頁Banner 總筆數
     */
    public Integer getCount(NavBanner navBanner);
    
    /**
     * 取得前台首頁Banner 前台總筆數
     */
    public Integer getFrontCount(NavBanner navBanner);

    /**
     * 取得前台首頁Banner show總筆數
     */
    public Integer getShowCount(NavBanner navBanner);
    
    /**
     * 取得單筆首頁Banner
     */
    public NavBanner getData(NavBanner navBanner);
    
    /**
     * 取得前台單筆首頁Banner
     */
    public NavBanner getFrontData(NavBanner navBanner);
    
    /**
     * 取得下一筆ID
     */
    public Integer getNextId();
    
    /**
     * 新增首頁Banner
     */
    public void add(NavBanner navBanner);
    
    /**
     * 修改首頁Banner
     */
    public void update(NavBanner navBanner);
    
    /**
     * 修改首頁Banner 點擊率
     */
    public void updateClickRate(NavBanner navBanner);
    
    /**
     * 修改Banner排序
     */
    public void updateSort(NavBanner navBanner);
    
    /**
     * 刪除首頁Banner
     */
    public void delete(Integer id);
    
    /**
     * 重新排序Banner
     */
    public void resetSort();
    
}
