package com.tkb.realgoodTransform.dao;

import java.util.Map;

/**
 * ARTICLEAPIDao介面接口
 * @author 
 * @version 創建時間：2016-07-12
 */
public interface ARTICLEAPIDao {

	public Map quoteArticleDate(String article_num, String appName, String reqEncryptKey, String repDecryptKey) throws Exception;
	
}
