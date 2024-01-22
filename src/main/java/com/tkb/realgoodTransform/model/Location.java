package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 地區
 */
@Data
public class Location {

	private Integer id; 						// 流水號
	private String name; 						// 學堂名稱
	private String area_id; 					// 地區id
	private String website_code; 				// 品牌代號
	private String tel; 						// 電話
	private String address; 					// 地址
	private String url; 						// google map 的 iframe url
	private String remark; 						// 備註
	private String status; 						// 狀態
	private String branch_no; 					// 館別代號
	private String create_by; 					// 創建者
	private Timestamp create_date; 				// 創建時間
	private String update_by; 					// 編輯者
	private Timestamp update_date; 				// 編輯時間
	private Integer sort;			 			// 排序
	private String file_info; 					// 立案資訊
	private String do_business_time; 			// 營業時間
	private String image; 						// 地區靜圖
	private String image_url; 					// 圖片靜圖
	private String share_url; 					// 分享地圖URL
	private String type; 						// 種類
	private String area_name; 					// 地區名稱
	private String website; 					// 品牌名稱

	private String area; 						// 地區
	private Integer rownum; 					// 自然編號
	private String random; 						// 是否亂數排序

	private Integer pageCount; 					// 每頁筆數
	private Integer pageTotalCount; 			// 總筆數
	private Integer totalPage; 					// 總頁數

	private String show_message;

	private List<Map> branchList; 				// api list
	private List<Location> locationInnerList;	// 第二層清單

}
