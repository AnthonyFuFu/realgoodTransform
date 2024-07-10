package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.ChosenArticleCategoryDao;
import com.tkb.realgoodTransform.model.ChosenArticleCategory;
import com.tkb.realgoodTransform.service.ChosenArticleCategoryService;



/**
 * 精選文章類別Service實作類
 * 
 * @author
 * @version 創建時間：2016-07-06
 */
@Service
public class ChosenArticleCategoryServiceImpl implements ChosenArticleCategoryService {
	@Autowired
	private ChosenArticleCategoryDao chosenArticleCategoryDao;

	@Override
	public List<ChosenArticleCategory> getSubList(ChosenArticleCategory chosenArticleCategory) {
		return chosenArticleCategoryDao.getSubList(chosenArticleCategory);
	}

	@Override
	public List<ChosenArticleCategory> getLayerList(String layer, ChosenArticleCategory chosenArticleCategory) {
		return chosenArticleCategoryDao.getLayerList(layer, chosenArticleCategory);
	}

	@Override
	public List<ChosenArticleCategory> getList(int pageCount, int pageStart,
			ChosenArticleCategory chosenArticleCategory) {
		return chosenArticleCategoryDao.getList(pageCount, pageStart, chosenArticleCategory);
	}

	@Override
	public Integer getCount(ChosenArticleCategory chosenArticleCategory) {
		return chosenArticleCategoryDao.getCount(chosenArticleCategory);
	}

	@Override
	public ChosenArticleCategory getData(ChosenArticleCategory chosenArticleCategory) {
		return chosenArticleCategoryDao.getData(chosenArticleCategory);
	}

	@Override
	public Integer getNextId() {
		return chosenArticleCategoryDao.getNextId();
	}

	@Override
	public void add(ChosenArticleCategory chosenArticleCategory) {
		chosenArticleCategoryDao.add(chosenArticleCategory);
	}

	@Override
	public void update(ChosenArticleCategory chosenArticleCategory) {
		chosenArticleCategoryDao.update(chosenArticleCategory);
	}

	@Override
	public void delete(Integer id) {
		chosenArticleCategoryDao.delete(id);
	}

	@Override
	public Integer getCountByName(String categoryName) {
		return chosenArticleCategoryDao.getCountByName(categoryName);
	}

	@Override
	public void resetSort(ChosenArticleCategory chosenArticleCategory) {
		chosenArticleCategoryDao.resetSort(chosenArticleCategory);
	}

	
//	==========
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		return chosenArticleCategoryDao.getNormalList();
	}

	@Override
	public void updateNormalData(ChosenArticleCategory chosenArticleCategory) {
		chosenArticleCategoryDao.updateNormalData(chosenArticleCategory);
	}

}
