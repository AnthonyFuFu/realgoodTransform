package com.tkb.realgoodTransform.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.dao.WinnerDao;
import com.tkb.realgoodTransform.model.CourseOverviewCourseCategory;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.model.Winner;
import com.tkb.realgoodTransform.model.WinnerCategory;
import com.tkb.realgoodTransform.model.WinnerContent;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.WinnerCategoryService;
import com.tkb.realgoodTransform.service.WinnerContentService;
import com.tkb.realgoodTransform.service.WinnerService;
import com.tkb.realgoodTransform.utils.CryptographyUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class WinnerServiceImpl implements WinnerService {

	@Autowired
	private WinnerDao winnerDao;
	@Autowired
	private WinnerCategoryService winnerCategoryService;
	@Autowired
	private WinnerContentService winnerContentService;
	@Autowired
	private EditLogService editLogService;

	@Override
	public List<Winner> getList(int pageCount, int pageStart, Winner winner) {
		return winnerDao.getList(pageCount, pageStart, winner);
	}

	@Override
	public List<Winner> getList(Winner winner) {
		return winnerDao.getList(winner);
	}

	@Override
	public List<Winner> getSubList(Winner winner) {
		return winnerDao.getSubList(winner);
	}

	@Override
	public List<Winner> getFrontList(Winner winner) {
		return winnerDao.getFrontList(winner);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<Map> getIndexList(Winner winner) {
		return null;
	}

	@Override
	public List<Winner> getFrontList(int pageCount, int pageStart, Winner winner, String search_sort) {
		return winnerDao.getFrontList(pageCount, pageStart, winner, search_sort);
	}

	@Override
	public List<Winner> getFrontList(int pageCount, int pageStart, Winner winner,
			List<WinnerCategory> winnerCategoryList, String search_sort) {
		return winnerDao.getFrontList(pageCount, pageStart, winner, winnerCategoryList, search_sort);
	}

	@Override
	public Integer getCount(Winner winner) {
		return winnerDao.getCount(winner);
	}

	@Override
	public Integer getUseCount_parent(int count) {
		return winnerDao.getUseCount_parent(count);
	}

	@Override
	public Integer getUseCount_child(int count) {
		return winnerDao.getUseCount_child(count);
	}

	@Override
	public Integer getFrontCount(Winner winner) {
		return winnerDao.getFrontCount(winner);
	}

	@Override
	public Integer getFrontCount(Winner winner, List<WinnerCategory> winnerCategoryList) {
		return winnerDao.getFrontCount(winner, winnerCategoryList);
	}

	@Override
	public Winner getData(Winner winner) {
		return winnerDao.getData(winner);
	}

	@Override
	public Winner getFrontData(Winner winner) {
		return winnerDao.getFrontData(winner);
	}

	@Override
	public Integer getNextId() {
		return winnerDao.getNextId();
	}

	@Override
	public void add(Winner winner) {
		winnerDao.add(winner);
	}

	@Override
	public void update(Winner winner) {
		winnerDao.update(winner);
	}

	@Override
	public void updateClickRate(Winner winner) {
		winnerDao.updateClickRate(winner);
	}

	@Override
	public void delete(Integer id) {
		winnerDao.delete(id);
	}

	@Override
	public List<Winner> getRandomList(Winner winner) {
		return winnerDao.getRandomList(winner);
	}

	@Override
	public List<Winner> getAllList(Winner winner) {
		return null;
	}

	@Override
	public void updateId(Winner winner) {

	}

	@Override
	public List<Winner> getVideoYearList(Winner winner) {
		return winnerDao.getVideoYearList(winner);
	}

	@Override
	public List<Winner> getVideoList(Winner winner) {
		return winnerDao.getVideoList(winner);
	}

	@Override
	public List<Winner> getVideoMainList(Winner winner) {
		return winnerDao.getVideoMainList(winner);
	}

	@Override
	public List<Winner> getVideoCategoryList(Winner winner) {
		return winnerDao.getVideoCategoryList(winner);
	}

	@Override
	public List<Winner> getVideoIndexList(Winner winner) {
		return winnerDao.getVideoIndexList(winner);
	}

	@Override
	public List<Winner> getVideoHotList(Winner winner) {
		return winnerDao.getVideoHotList(winner);
	}

	@Override
	public ResponseEntity<?> changeCategoryTwo(String id, WinnerCategory winnerCategory) {
		if (id != null && !"".equals(id))
			winnerCategory.setParent_id(Integer.valueOf(id));
		List<WinnerCategory> winnerCategoryList = new ArrayList<>();
		winnerCategoryList = winnerCategoryService.getLayerList("2", winnerCategory);
		if (winnerCategoryList.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(winnerCategoryList, HttpStatus.OK);
		}
	}

	@Override
	public List<WinnerCategory> addFunction(WinnerCategory winnerCategory) {
		List<WinnerCategory> winnerCategoryList = new ArrayList<>();
		winnerCategory.setParent_id(0);
		winnerCategoryList = winnerCategoryService.getLayerList("1", winnerCategory);
		return winnerCategoryList;
	}

	@Override
	public void addSubmitFunction(Winner winner, WinnerContent winnerContent, User user, HttpServletRequest request)
			throws NullPointerException {
		String[] iconList = request.getParameter("iconList") == null ? null
				: request.getParameter("iconList").split(",");
		String[] contentTitleList = request.getParameter("contentTitleList") == null ? null
				: request.getParameter("contentTitleList").split(",");
		String[] contentContentList = request.getParameter("contentContentList") == null ? null
				: request.getParameter("contentContentList").split(",p_a,");

		if (iconList == null)
			throw new NullPointerException();
		if (contentTitleList == null)
			throw new NullPointerException();
		if (contentContentList == null)
			throw new NullPointerException();

		int id = getNextId();
		winner.setId(id);
		winner.setEncrypturl(CryptographyUtils.encryptStr(String.valueOf(winner.getId())));
		winner.setPhoto(winner.getPhoto());
		winner.setImage(winner.getImage());
		winner.setCreate_by(user.getAccount());
		winner.setUpdate_by(user.getAccount());
		add(winner);
		if (iconList.length == contentTitleList.length && iconList.length == contentContentList.length) {
			winnerContent.setCreate_by(user.getAccount());
			winnerContent.setUpdate_by(user.getAccount());
			for (int i = 0; i < iconList.length; i++) {
				winnerContent.setId(winnerContentService.getNextId());
				winnerContent.setWinner_id(id);
				winnerContent.setIcon(iconList[i]);
				winnerContent.setTitle(contentTitleList[i]);
				winnerContent.setContent(contentContentList[i]);
				winnerContentService.add(winnerContent);
			}
		}
	}

	@Override
	public Map<String, Object> updateFunction(Winner winner, WinnerCategory winnerCategory,
			WinnerContent winnerContent) {
		Map<String, Object> map = new HashMap<>();
		List<WinnerCategory> winnerCategoryList = new ArrayList<>();
		List<WinnerContent> winnerContentList = new ArrayList<>();
		winnerCategory.setParent_id(0);
		winnerCategoryList = winnerCategoryService.getLayerList("1", winnerCategory);

		winner = getData(winner);

		winnerContent.setWinner_id(winner.getId());
		winnerContentList = winnerContentService.getList(winnerContent);

		winnerCategory.setId(Integer.valueOf(winner.getCategory()));
		winnerCategory = winnerCategoryService.getData(winnerCategory);
		map.put("winnerCategoryList", winnerCategoryList);
		map.put("winner", winner);
		map.put("winnerContentList", winnerContentList);
		map.put("winnerCategory", winnerCategory);
		return map;
	}

	@Override
	public void updateSubmitFunction(Winner winner, WinnerContent winnerContent, User user, HttpServletRequest request)
			throws NullPointerException {
		List<WinnerContent> winnerContentList = new ArrayList<>();
		String[] iconList = request.getParameter("iconList") == null ? null
				: request.getParameter("iconList").split(",");
		String[] contentTitleList = request.getParameter("contentTitleList") == null ? null
				: request.getParameter("contentTitleList").split(",");
		String[] contentContentList = request.getParameter("contentContentList") == null ? null
				: request.getParameter("contentContentList").split(",p_a,");
		String[] winnerContentIdList = request.getParameter("winnerContentIdList") == null ? null
				: request.getParameter("winnerContentIdList").split(",");
		String deleteIdList = "";

		if (iconList == null)
			throw new NullPointerException();
		if (contentTitleList == null)
			throw new NullPointerException();
		if (contentContentList == null)
			throw new NullPointerException();
		if (winnerContentIdList == null)
			throw new NullPointerException();

		if (iconList.length == contentTitleList.length && iconList.length == contentContentList.length
				&& iconList.length == winnerContentIdList.length) {
			winnerContent.setWinner_id(winner.getId());
			winnerContentList = winnerContentService.getList(winnerContent);

			// 取得刪除ID
			for (int i = 0; i < winnerContentList.size(); i++) {
				for (int j = 0; j < winnerContentIdList.length; j++) {
					if (String.valueOf(winnerContentList.get(i).getId()).equals(winnerContentIdList[j])) {
						break;
					} else {
						if (j == (winnerContentIdList.length - 1)) {
							if ("".equals(deleteIdList)) {
								deleteIdList += winnerContentList.get(i).getId();
							} else {
								deleteIdList = deleteIdList + "," + winnerContentList.get(i).getId();
							}
						}
					}
				}
			}
		}
		// 新增修改
		for (int i = 0; i < iconList.length; i++) {
			winnerContent.setIcon(iconList[i]);
			winnerContent.setTitle(contentTitleList[i]);
			winnerContent.setContent(contentContentList[i]);
			winnerContent.setCreate_by(user.getAccount());
			winnerContent.setUpdate_by(user.getAccount());
			// 新增
			if ("0".equals(winnerContentIdList[i])) {
				winnerContent.setId(winnerContentService.getNextId());
				winnerContent.setWinner_id(winner.getId());
				winnerContentService.add(winnerContent);
				// 修改
			} else {
				winnerContent.setId(Integer.valueOf(winnerContentIdList[i]));
				winnerContentService.update(winnerContent);
			}

		}

		// 刪除
		String[] deleteIdArray = deleteIdList.split(",");
		if (!"".equals(deleteIdArray[0])) {
			for (int i = 0; i < deleteIdArray.length; i++) {
				winnerContentService.delete(Integer.valueOf(deleteIdArray[i]));
			}
		}
		winner.setImage(winner.getImage());
		winner.setPhoto(winner.getPhoto());
		winner.setUpdate_by(user.getAccount());
		update(winner);
	}

	@Override
	public void deleteFunction(WinnerContent winnerContent, Winner winner, String selectList, User user) {
		List<WinnerContent> winnerContentList = new ArrayList<>();
		String[] selectArray = selectList.split(",");
		for (int i = 0; i < selectArray.length; i++) {
			winnerContent.setWinner_id(Integer.valueOf(selectArray[i]));
			winnerContentList = winnerContentService.getList(winnerContent);

			for (int j = 0; j < winnerContentList.size(); j++) {
				winnerContentService.delete(winnerContentList.get(j).getId());
			}
			winner.setId(Integer.valueOf(selectArray[i]));
			winner = getData(winner);

			Gson gson = new Gson();
			String jsonString = gson.toJson(winner);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(winner.getId());
			editLog.setFunction("WINNER");
			editLog.setAction_type("DELETE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);

			delete(Integer.valueOf(selectArray[i]));
		}

	}

	@Override
	public Map<String, Object> viewFunction(Winner winner, WinnerContent winnerContent) {
		List<WinnerContent> winnerContentList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		winner = getData(winner);
		winnerContent.setWinner_id(winner.getId());
		winnerContentList = winnerContentService.getList(winnerContent);
		map.put("winner", winner);
		map.put("winnerContentList", winnerContentList);
		return map;
	}

	@Override
	public List<Winner> getFrontVideoList(Winner winner) {
		return winnerDao.getFrontVideoList(winner);
	}

	@Override
	public List<Winner> getRecommendVideoList(Winner winner) {
		return winnerDao.getRecommendVideoList(winner);
	}

	@Override
	public List<Winner> getPrevList(Winner winner) {
		return winnerDao.getPrevList(winner);
	}

	@Override
	public List<Winner> getNextList(Winner winner) {
		return winnerDao.getNextList(winner);
	}

	@Override
	public Winner getCourseOverviewVideoData(Winner winner, CourseOverviewCourseCategory courseOverviewCourseCategory) {
		return winnerDao.getCourseOverviewVideoData(winner,courseOverviewCourseCategory);
	}

	@Override
	public List<Map<String, Object>> getCourseOverviewVideoList(Winner winner,
			CourseOverviewCourseCategory courseOverviewCourseCategory) {
		return winnerDao.getCourseOverviewVideoList(winner,courseOverviewCourseCategory);

	}

	
//	========
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		return winnerDao.getNormalList();
	}

	@Override
	public void updateNormalData(Winner winner) {
		winnerDao.updateNormalData(winner);
	}

}
