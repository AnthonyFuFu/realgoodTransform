package com.tkb.realgoodTransform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.MenuDao;
import com.tkb.realgoodTransform.model.Menu;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.MenuService;



/**
 * 選單Service實作類
 * @author Joshua
 * @version 創建時間：2022-01-25
 */
@Service
public class MenuServiceImpl implements MenuService {
	
	@Autowired
	MenuDao menuDao;

	@Override
	public Integer getCount(Menu menu) {
		return menuDao.getCount(menu);
	}

	@Override
	public List<Menu> getList(int pageCount, int pageStart, Menu menu) {
		return menuDao.getList(pageCount, pageStart, menu);
	}

	@Override
	public Integer getNextId() {
		return menuDao.getNextId();
	}

	@Override
	public void add(Menu menu) {
		menuDao.add(menu);
	}

	@Override
	public Menu getData(Menu menu) {
		return menuDao.getData(menu);
	}

	@Override
	public void update(Menu menu) {
		menuDao.update(menu);
	}

	@Override
	public List<Menu> getLayer1List(User user) {
		return menuDao.getLayer1List(user);
	}

	@Override
	public List<Menu> getLayer2List(Menu menu) {
		return menuDao.getLayer2List(menu);
	}

	@Override
	public List<Menu> getSubList(Menu menu) {
		return menuDao.getSubList(menu);
	}

	@Override
	public void delete(int id) {
		menuDao.delete(id);
	}

	@Override
	public List<Menu> getLayer2AllList(Menu menu) {
		return menuDao.getLayer2AllList(menu);
	}

	@Override
	public ResponseEntity<?> getMenuLayer2List(Menu menu, Integer group_id) {
		menu.setLayer("2");
		List<Map<String, Object>> menuLayer2List = menuDao.getMenuLayer2List(menu, group_id);
		if(menuLayer2List.size() > 0) {
			return new ResponseEntity<>(menuLayer2List, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@Override
	public Integer getMenuId(String link) {
		return menuDao.getMenuId(link);
	}

}
