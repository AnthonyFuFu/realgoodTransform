package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.Admit;
import com.tkb.realgoodTransform.model.AdmitContent;
import com.tkb.realgoodTransform.model.AdmitContentOption;
import com.tkb.realgoodTransform.model.AdmitDetail;
import com.tkb.realgoodTransform.model.AdmitLog;



public interface AdmitDao {

	/**
	 * 取得考取金榜資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param admit
	 * @return List<Admit>
	 */
	public List<Admit> getList(int pageCount, int pageStart, Admit admit);
	
	/**
	 * 取得首頁考取金榜清單
	 * @param pageCount
	 * @param pageStart
	 * @param admit
	 * @return List<Admit>
	 */
	public List<Admit> getFrontList();
    /**
     * 取得首頁考取金榜清單
     * @param pageCount
     * @param pageStart
     * @param admit
     * @return List<Admit>
     */
  public List<Admit> getIndexList();	
  
	/**
	 * 取得前台熱門講座資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param admit
	 * @return List<Admit>
	 */
	public List<Admit> getFrontList(int pageCount, int pageStart, Admit admit);
	
	/**
	 * 取得熱門講座總筆數
	 * @param admit
	 * @return Integer
	 */
	public Integer getCount(Admit admit);
	
	/**
	 * 取得前台學堂公告總筆數
	 * @param admit
	 * @return Integer
	 */
	public Integer getFrontCount(Admit admit);
	
	/**
	 * 取得單筆金榜
	 * @param admit
	 * @return admit
	 */
	public Admit getData(Admit admit);
	
	/**
	 * 取得前台單筆金榜
	 * @param admit
	 * @return admit
	 */
	public Admit getFrontData(Admit admit);
	
	/**
	 * 取得不重複的榜單年度
	 * @return List<Admit>
	 */
	public List<Admit> getAllAdmitYear();
	
	/**
	 * 依分類取得榜單數量
	 * @return Integer
	 */
	public Integer getCountListByCategory(int categoryId);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();

	/**
	 * 新增金榜
	 * @param admit
	 */
	public void add(Admit admit);
	
	/**
	 * 新增金榜內容
	 * @param admitContent
	 */
	public void addContent(AdmitContent admitContent);
	
	/**
	 * 新增金榜內容項目
	 * @param admitContentOption
	 */
	public void addContentOption(AdmitContentOption admitContentOption);
	
	/**
	 * 新增金榜榜單明細
	 * @param admitDetail
	 */
	public void addDetail(AdmitDetail admitDetail);
	
	/**
	 * 修改熱門講座
	 * @param admit
	 */
	public void update(Admit admit);
	
	/**
	 * 修改熱門講座點擊率
	 * @param admit
	 */
	public void updateClickRate(Admit admit);
	
	/**
	 * 刪除考取金榜
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 考取金榜下架
	 * @param id
	 */
	public void updateDisplayHide(Integer id);
	
	/**
	 * 考取金榜上架
	 * @param id
	 */
	public void updateDisplayShow(Integer id);
	
	/**
	 * 新增考取金榜Log
	 * @param admitLog
	 */
	public void addAdmitLog(AdmitLog admitLog);
	
	/**
	 * 取得金榜Log下一筆ID
	 * @return Integer
	 */
	public Integer getNextAdmitLogId();
	/**
     * 取得考取金榜資料清單
     * @param admit
     * @return List<Admit>
     */
  public List<Admit> getList(Admit admit);
      /**
   * 修改熱門講座
   * @param admit
   */
  public void updateId(Admit admit);
  
  
  
  
  
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(Admit admit);
}
