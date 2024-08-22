package com.tkb.realgoodTransform.service;

import java.util.Map;

/**
 * ARTICLEAPIService介面接口
 * @author 
 * @version 創建時間：2016-07-12
 */
public interface ARTICLEAPIService {

	/**
	 * 取得文章資料
	 * @param article_num
	 * @param appName
	 * @param reqEncryptKey
	 * @param repDecryptKey
	 * @return quoteArticleDate
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map quoteArticleDate(String article_num, String appName, String reqEncryptKey, String repDecryptKey) throws Exception;
	
}
