package com.tkb.realgoodTransform.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.springframework.stereotype.Repository;

import com.article.api.client.ArticleAPIClient;
import com.article.api.client.model.ArticleAPIRequestModel;
import com.article.api.client.model.ArticleAPIResponseModel;
import com.tkb.api.client.util.Security;
import com.tkb.realgoodTransform.dao.ARTICLEAPIDao;


@Repository
public class ARTICLEAPIDaoImpl implements ARTICLEAPIDao {
	@Override
	public Map quoteArticleDate(String article_num, String articleAppName, String articleReqEncryptKey,
			String articleRepDecryptKey) throws Exception {

		Map map = new HashMap();
		map.put("ARTICLE_NUM", article_num);
		map.put("TYPE", 1);

		List<Map> list = new ArrayList<Map>();
		list.add(map);

		ArticleAPIRequestModel tArticleAPIRequestModel = new ArticleAPIRequestModel();
		tArticleAPIRequestModel.setAppName(articleAppName);
		tArticleAPIRequestModel.setService(1);
		tArticleAPIRequestModel.setMethod(29);
		tArticleAPIRequestModel.setVersion(1);
		tArticleAPIRequestModel.setDataList(list);

		ArticleAPIClient tArticleAPIClient = new ArticleAPIClient();

		try {
			String rep = tArticleAPIClient.sendRequest(tArticleAPIRequestModel, articleReqEncryptKey);
			rep = Security.decrypt(rep, articleRepDecryptKey);
			ArticleAPIResponseModel tResponse = new ArticleAPIResponseModel(rep);
			if (tResponse.getStatus() == 1) {
				if (tResponse.getDataList() != null && tResponse.getDataList().size() > 0) {
					return tResponse.getDataList().get(0);

				} else {
					return null;
				}

			} else {
				System.out.println("取資料時出現錯誤 api 回傳錯誤訊息" + tResponse.getMessage());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;

	}

}
