package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.ChosenArticleDao;
import com.tkb.realgoodTransform.model.ChosenArticle;
import com.tkb.realgoodTransform.service.ChosenArticleService;



/**
 * 精選文章Service實作類
 * 
 * @author
 * @version 創建時間：2016-07-05
 */
@Service
public class ChosenArticleServiceImpl implements ChosenArticleService {
	@Autowired
	private ChosenArticleDao chosenArticleDao;

	@Override
	public List<ChosenArticle> getList(int pageCount, int pageStart, ChosenArticle chosenArticle) {
		return chosenArticleDao.getList(pageCount, pageStart, chosenArticle);
	}

	@Override
	public List<ChosenArticle> getFrontList() {
		return chosenArticleDao.getFrontList();
	}

	@Override
	public List<ChosenArticle> getIndexList() {
		return chosenArticleDao.getIndexList();
	}

	@Override
	public List<ChosenArticle> getFrontList(int pageCount, int pageStart, ChosenArticle chosenArticle,String search_sort) {
		return chosenArticleDao.getFrontList(pageCount, pageStart, chosenArticle,search_sort);
	}

	@Override
	public List<ChosenArticle> getFrontList(ChosenArticle chosenArticle) {
		return chosenArticleDao.getFrontList(chosenArticle);
	}

	@Override
	public Integer getCount(ChosenArticle chosenArticle) {
		return chosenArticleDao.getCount(chosenArticle);
	}

	@Override
	public Integer getFrontCount(ChosenArticle chosenArticle) {
		return chosenArticleDao.getFrontCount(chosenArticle);
	}

	@Override
	public ChosenArticle getData(ChosenArticle chosenArticle) {
		return chosenArticleDao.getData(chosenArticle);
	}

	@Override
	public ChosenArticle getFrontData(ChosenArticle chosenArticle) {
		return chosenArticleDao.getFrontData(chosenArticle);
	}

	@Override
	public Integer getCountListByCategory(int categoryId) {
		return chosenArticleDao.getCountListByCategory(categoryId);
	}

	@Override
	public Integer getNextId() {
		return chosenArticleDao.getNextId();
	}

	@Override
	public void add(ChosenArticle chosenArticle) {
		chosenArticleDao.add(chosenArticle);
	}

	@Override
	public void update(ChosenArticle chosenArticle) {
		chosenArticleDao.update(chosenArticle);
	}

	@Override
	public void updateClickRate(ChosenArticle chosenArticle) {
		chosenArticleDao.updateClickRate(chosenArticle);
	}

	@Override
	public void delete(Integer id) {
		chosenArticleDao.delete(id);
	}

	@Override
	public void updateDisplayHide(Integer id) {
		chosenArticleDao.updateDisplayHide(id);
	}

	@Override
	public void updateDisplayShow(Integer id) {
		chosenArticleDao.updateDisplayShow(id);
	}

	@Override
	public Integer getCountListByTop(int articleId) {
		return chosenArticleDao.getCountListByTop(articleId);
	}

	@Override
	public List<ChosenArticle> getList(ChosenArticle chosenArticle) {
		return chosenArticleDao.getList(chosenArticle);
	}

	@Override
	public void updateId(ChosenArticle chosenArticle) {
		chosenArticleDao.updateId(chosenArticle);
	}

	@Override
	public List<ChosenArticle> getArticleListForUpdate() {
		return chosenArticleDao.getArticleListForUpdate();
	}

	@Override
	public void updateQuoteArticle(ChosenArticle chosenArticle) {
		chosenArticleDao.updateQuoteArticle(chosenArticle);
	}

	@Override
	public List<ChosenArticle> getPrevList(ChosenArticle chosenArticle) {
		return chosenArticleDao.getPrevList(chosenArticle);
	}

	@Override
	public List<ChosenArticle> getNextList(ChosenArticle chosenArticle) {
		return chosenArticleDao.getNextList(chosenArticle);
	}

	
//	=========
	
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		return chosenArticleDao.getNormalList();
	}

	@Override
	public void updateNormalData(ChosenArticle chosenArticle) {
		chosenArticleDao.updateNormalData(chosenArticle);
	}
}
