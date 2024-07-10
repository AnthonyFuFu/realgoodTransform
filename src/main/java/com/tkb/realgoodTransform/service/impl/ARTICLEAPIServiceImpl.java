package com.tkb.realgoodTransform.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.ARTICLEAPIDao;
import com.tkb.realgoodTransform.service.ARTICLEAPIService;



@Service
public class ARTICLEAPIServiceImpl implements ARTICLEAPIService {

	@Autowired
	private ARTICLEAPIDao articleapiDao;
	
	@Override
	public Map quoteArticleDate(String article_num, String appName, String reqEncryptKey, String repDecryptKey) throws Exception {
		
		return articleapiDao.quoteArticleDate(article_num, appName, reqEncryptKey, repDecryptKey);

	}

	
}
