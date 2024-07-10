package com.tkb.realgoodTransform.service.impl;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.dao.LecturesDao;
import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.Lectures;
import com.tkb.realgoodTransform.model.LecturesCategory;
import com.tkb.realgoodTransform.model.LecturesContent;
import com.tkb.realgoodTransform.model.LecturesPlace;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.LecturesCategoryService;
import com.tkb.realgoodTransform.service.LecturesContentService;
import com.tkb.realgoodTransform.service.LecturesPlaceService;
import com.tkb.realgoodTransform.service.LecturesService;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.CryptographyUtils;
import com.tkb.realgoodTransform.utils.UploadUtil;

import jakarta.servlet.http.HttpServletRequest;



@Service
public class LecturesServiceImpl extends BaseUtils implements LecturesService {

	@Value("${upload.file.path}")
	private String uploadFilePath; // 檔案上傳位置
	@Autowired
	private LecturesDao lecturesDao;
	@Autowired
	private LecturesCategoryService lecturesCategoryService;
	@Autowired
	private LecturesPlaceService lecturesPlaceService;
	@Autowired
	private LecturesContentService lecturesContentService;
	@Autowired
	private EditLogService editLogService;
	@Override
	public List<Lectures> getList(int pageCount, int pageStart, Lectures lectures) {
		// TODO Auto-generated method stub
		return lecturesDao.getList(pageCount, pageStart, lectures);
	}

	@Override
	public List<Lectures> getFrontList(Lectures lectures) {
		// TODO Auto-generated method stub
		return lecturesDao.getFrontList(lectures);
	}

	@Override
	public List<Lectures> getFrontList(int pageCount, int pageStart, Lectures lectures, String search_sort) {
		// TODO Auto-generated method stub
		return lecturesDao.getFrontList(pageCount, pageStart, lectures, search_sort);
	}

	@Override
	public List<Lectures> getIndexFrontList(int pageCount, int pageStart, Lectures lectures, String search_sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Lectures> getFrontList(int pageCount, int pageStart, Lectures lectures, List<Area> areaList,
			String search_sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getCount(Lectures lectures) {
		// TODO Auto-generated method stub
		return lecturesDao.getCount(lectures);
	}

	@Override
	public Integer getFrontCount(Lectures lectures) {
		// TODO Auto-generated method stub
		return lecturesDao.getFrontCount(lectures);
	}

	@Override
	public Integer getFrontCount(Lectures lectures, List<Area> areaList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Lectures getData(Lectures lectures) {
		// TODO Auto-generated method stub
		return lecturesDao.getData(lectures);
	}

	@Override
	public Lectures getFrontData(Lectures lectures) {
		// TODO Auto-generated method stub
		return lecturesDao.getFrontData(lectures);
	}

	@Override
	public Integer getNextId() {
		// TODO Auto-generated method stub
		return lecturesDao.getNextId();
	}

	@Override
	public void add(Lectures lectures) {
		// TODO Auto-generated method stub
		lecturesDao.add(lectures);
	}

	@Override
	public void update(Lectures lectures) {
		// TODO Auto-generated method stub
		lecturesDao.update(lectures);
	}

	@Override
	public void updateClickRate(Lectures lectures) {
		// TODO Auto-generated method stub
		lecturesDao.updateClickRate(lectures);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		lecturesDao.delete(id);
	}

	@Override
	public Integer checkTopCount(Lectures lectures) {
		// TODO Auto-generated method stub
		return lecturesDao.checkTopCount(lectures);
	}

	@Override
	public List<Lectures> getList(Lectures lectures) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateId(Lectures lectures) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<LecturesCategory> add(LecturesCategory lecturesCategory, LecturesPlace lecturesPlace,Lectures lectures) {
		List<LecturesCategory>lecturesCategoryList = new ArrayList<>();
		lecturesCategory.setParent_id(0);
		lecturesCategoryList = lecturesCategoryService.getLayerList("1", lecturesCategory);
		return lecturesCategoryList;
	}

	@Override
	public void addSubmit(Lectures lectures, LecturesContent lecturesContent, LecturesPlace lecturesPlace, User user,
			HttpServletRequest request, Model model)  {
		try {
		String[] iconList = request.getParameter("iconList") == null ? null : request.getParameter("iconList").split(",:;,");
		String[] contentTitleList = request.getParameter("contentTitleList") == null ? null : request.getParameter("contentTitleList").split(",:;,");
		String[] contentContentList = request.getParameter("contentContentList") == null ? null : request.getParameter("contentContentList").split(",:;,");
		
		String[] placeNameList = request.getParameter("placeNameList") == null ? null : request.getParameter("placeNameList").split(",:;,");
		String[] placeEventList = request.getParameter("placeEventList") == null ? null : request.getParameter("placeEventList").split(",:;,");
		String[] placeDayList = request.getParameter("placeDayList") == null ? null : request.getParameter("placeDayList").split(",:;,");	
		
		if (iconList == null)throw new NullPointerException();
		if (contentTitleList == null)throw new NullPointerException();
		if (contentContentList == null)throw new NullPointerException();
		if (placeNameList == null)throw new NullPointerException();
		if (placeEventList == null)throw new NullPointerException();
		if (placeDayList == null)throw new NullPointerException();
		
		String checkStatus = "T";
		SimpleDateFormat sdfhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		if("T".equals(checkStatus)) {
		int id = getNextId();
		if(iconList.length==contentTitleList.length && iconList.length==contentContentList.length) {
			lectures.setId(id);
			lectures.setCreate_by(user.getAccount());
			lectures.setUpdate_by(user.getAccount());
			lectures.setEncrypturl(CryptographyUtils.encryptStr(String.valueOf(lectures.getId())));
			add(lectures);
			
			lecturesContent.setCreate_by(user.getAccount());
			lecturesContent.setUpdate_by(user.getAccount());
			
			for(int i = 0; i<iconList.length; i++) {
				lecturesContent.setId(lecturesContentService.getNextId());
				lecturesContent.setLectures_id(id);
				lecturesContent.setIcon(iconList[i]);
				lecturesContent.setTitle(contentTitleList[i]);
				lecturesContent.setContent(contentContentList[i]);
				lecturesContentService.add(lecturesContent);
			}
		}
		if(placeNameList.length==placeEventList.length && placeNameList.length==placeDayList.length) {
			lecturesPlace.setCreate_by(user.getAccount());
			lecturesPlace.setUpdate_by(user.getAccount());
			
			for(int i = 0; i<placeNameList.length; i++) {
				lecturesPlace.setId(lecturesPlaceService.getNextId());
				lecturesPlace.setLectures_id(id);
				lecturesPlace.setPlaceName(placeNameList[i]);
				lecturesPlace.setPlaceDay(placeDayList[i]);
				lecturesPlace.setPlaceEvent(placeEventList[i]);
				lecturesPlaceService.add(lecturesPlace);
			}
		}
		}
				Gson gson = new Gson();
				String jsonString = gson.toJson(lectures);
				EditLog editLog = new EditLog();
				int logId = editLogService.getNextLogId();
				editLog.setId(logId);
				editLog.setData_id(lectures.getId());
				editLog.setFunction("LECTURES");
				editLog.setAction_type("ADD");
				editLog.setContent(jsonString);
				editLog.setCreate_by(user.getAccount());
				editLogService.addLog(editLog);
		
			model.addAttribute("message", "新增成功");
		
		}  catch (NullPointerException ee) {
			model.addAttribute("message", "新增失敗,請輸入ICON,內容主題,內文");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "新增失敗");
		}
	}
	@Override
	public Map<String, Object> update(Lectures lectures, LecturesCategory lecturesCategory,
			LecturesContent lecturesContent, LecturesPlace lecturesPlace) {
		Map<String, Object>map = new HashMap<>();
		List<LecturesCategory>lecturesCategoryList = new ArrayList<>();
		List<LecturesContent>lecturesContentList = new ArrayList<>();
		List<LecturesPlace>lecturesPlaceList = new ArrayList<>();
		lecturesCategory.setParent_id(0);
		lecturesCategoryList = lecturesCategoryService.getLayerList("1", lecturesCategory);
		
		lectures = getData(lectures);
		lecturesContent.setLectures_id(lectures.getId());
		lecturesContentList = lecturesContentService.getList(lecturesContent);
		
		lecturesPlace.setLectures_id(lectures.getId());
		lecturesPlaceList = lecturesPlaceService.getList(lecturesPlace);
		
		lecturesCategory.setId(Integer.valueOf(lectures.getCategory()));
		lecturesCategory = lecturesCategoryService.getData(lecturesCategory);
		map.put("lecturesCategoryList", lecturesCategoryList);
		map.put("lectures", lectures);
		map.put("lecturesContentList", lecturesContentList);
		map.put("lecturesPlaceList", lecturesPlaceList);
		map.put("lecturesCategory", lecturesCategory);
		return map;
	}

	@Override
	public void updateSubmit(Lectures lectures, LecturesContent lecturesContent, LecturesPlace lecturesPlace, User user,
			HttpServletRequest request, Model model) {
		try {
		String[] iconList = request.getParameter("iconList") == null ? null : request.getParameter("iconList").split(",:;,");
		String[] contentTitleList = request.getParameter("contentTitleList") == null ? null : request.getParameter("contentTitleList").split(",:;,");
		String[] contentContentList = request.getParameter("contentContentList") == null ? null : request.getParameter("contentContentList").split(",:;,");
		String[] lecturesContentIdList = request.getParameter("lecturesContentIdList") == null ? null : request.getParameter("lecturesContentIdList").split(",:;,");
		String deleteIdList = "";
		
		String[] placeNameList = request.getParameter("placeNameList") == null ? null : request.getParameter("placeNameList").split(",:;,");
		String[] placeEventList = request.getParameter("placeEventList") == null ? null : request.getParameter("placeEventList").split(",:;,");
		String[] placeDayList = request.getParameter("placeDayList") == null ? null : request.getParameter("placeDayList").split(",:;,");	
		String[] lecturesPlaceIdList = request.getParameter("lecturesPlaceIdList") == null ? null : request.getParameter("lecturesPlaceIdList").split(",:;,");
		String deletePlaceIdList = "";
		
		if (iconList == null)throw new NullPointerException();
		if (contentTitleList == null)throw new NullPointerException();
		if (contentContentList == null)throw new NullPointerException();
		if (lecturesContentIdList == null)throw new NullPointerException();
		if (placeNameList == null)throw new NullPointerException();
		if (placeEventList == null)throw new NullPointerException();
		if (placeDayList == null)throw new NullPointerException();
		if (lecturesPlaceIdList == null)throw new NullPointerException();
		
		UploadUtil uploadUtil = new UploadUtil();
		String deletePhoto = request.getParameter("photo");
		String checkStatus = "T";
		SimpleDateFormat sdfhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
			if(iconList.length==contentTitleList.length && iconList.length==contentContentList.length && iconList.length==lecturesContentIdList.length) {
			List<LecturesContent>lecturesContentList = new ArrayList<>();
			lecturesContent.setLectures_id(lectures.getId());
			lecturesContentList = lecturesContentService.getList(lecturesContent);
			
			//取得刪除ID
			for(int i=0; i<lecturesContentList.size(); i++) {
				for(int j=0; j<lecturesContentIdList.length; j++) {
					if(String.valueOf(lecturesContentList.get(i).getId()).equals(lecturesContentIdList[j])) {
						break;
					} else {
						if(j == (lecturesContentIdList.length-1)) {
							if("".equals(deleteIdList)) {
								deleteIdList += lecturesContentList.get(i).getId();
							} else {
								deleteIdList = deleteIdList + "," + lecturesContentList.get(i).getId();
							}
						}
					}
				}
			}
			//新增修改
			for(int i=0; i<iconList.length; i++) {
				lecturesContent.setIcon(iconList[i]);
				lecturesContent.setTitle(contentTitleList[i]);
				lecturesContent.setContent(contentContentList[i]);
				lecturesContent.setCreate_by(user.getAccount());
				lecturesContent.setUpdate_by(user.getAccount());
				//新增
				if("0".equals(lecturesContentIdList[i])) {
					lecturesContent.setId(lecturesContentService.getNextId());
					lecturesContent.setLectures_id(lectures.getId());
					lecturesContentService.add(lecturesContent);
				//修改
				} else {
					lecturesContent.setId(Integer.valueOf(lecturesContentIdList[i]));
					lecturesContentService.update(lecturesContent);
				}
			}
			
			//刪除
			String[] deleteIdArray = deleteIdList.split(",");
			if(!"".equals(deleteIdArray[0])) {
				for(int i=0; i<deleteIdArray.length; i++) {
					lecturesContentService.delete(Integer.valueOf(deleteIdArray[i]));
				}
			}
		}
		//講座地點修改
		if(placeNameList.length==placeEventList.length && placeNameList.length==placeDayList.length && placeNameList.length==lecturesPlaceIdList.length) {
			List<LecturesPlace>lecturesPlaceList = new ArrayList<>();
			lecturesPlace.setLectures_id(lectures.getId());
			lecturesPlaceList = lecturesPlaceService.getList(lecturesPlace);
			
			//取得刪除ID
			for(int i=0; i<lecturesPlaceList.size(); i++) {
				for(int j=0; j<lecturesPlaceIdList.length; j++) {
					if(String.valueOf(lecturesPlaceList.get(i).getId()).equals(lecturesPlaceIdList[j])) {
						break;
					} else {
						if(j == (lecturesPlaceIdList.length-1)) {
							if("".equals(deletePlaceIdList)) {
								deletePlaceIdList += lecturesPlaceList.get(i).getId();
							} else {
								deletePlaceIdList = deletePlaceIdList + "," + lecturesPlaceList.get(i).getId();
							}
						}
					}
				}
			}
			//新增修改
			for(int i=0; i<placeNameList.length; i++) {
				lecturesPlace.setPlaceName(placeNameList[i]);
				lecturesPlace.setPlaceDay(placeDayList[i]);
				lecturesPlace.setPlaceEvent(placeEventList[i]);
				lecturesPlace.setCreate_by(user.getAccount());
				lecturesPlace.setUpdate_by(user.getAccount());
				//新增
				if("0".equals(lecturesPlaceIdList[i])) {
					lecturesPlace.setId(lecturesPlaceService.getNextId());
					lecturesPlace.setLectures_id(lectures.getId());
					lecturesPlaceService.add(lecturesPlace);
				//修改
				} else {
					lecturesPlace.setId(Integer.valueOf(lecturesPlaceIdList[i]));
					lecturesPlaceService.update(lecturesPlace);
				}
			}
			
			//刪除
			String[] deletePlaceIdArray = deletePlaceIdList.split(",");
			if(!"".equals(deletePlaceIdArray[0])) {
				for(int i=0; i<deletePlaceIdArray.length; i++) {
					lecturesPlaceService.delete(Integer.valueOf(deletePlaceIdArray[i]));
				}
			}
			
		}
		
		if("T".equals(checkStatus)) {
			
			lectures.setUpdate_by(user.getAccount());
			update(lectures);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(lectures);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(lectures.getId());
			editLog.setFunction("LECTURES");
			editLog.setAction_type("UPDATE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			model.addAttribute("message", "修改成功");
		}else {
			model.addAttribute("message", "修改失敗，圖片固定300x184px");
		}
		} catch (NullPointerException ee) {
			model.addAttribute("message", "新增失敗,請輸入ICON,內容主題,內文");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "修改失敗");
		}
	}

	@Override
	public void delete(Lectures lectures, LecturesContent lecturesContent, LecturesPlace lecturesPlace, HttpServletRequest request, User user) {
		String deleteList = request.getParameter("deleteList");
		String[] deleteArray = deleteList.split(",");
		List<LecturesContent>lecturesContentList = new ArrayList<>();
		List<LecturesPlace>lecturesPlaceList = new ArrayList<>();
		for(int i = 0; i<deleteArray.length; i++) {
			lecturesContent.setLectures_id(Integer.valueOf(deleteArray[i]));
			lecturesContentList = lecturesContentService.getList(lecturesContent);
			for(int j=0; j<lecturesContentList.size(); j++) {
				lecturesContentService.delete(lecturesContentList.get(j).getId());
			}
			lecturesPlace.setLectures_id(Integer.valueOf(deleteArray[i]));
			lecturesPlaceList = lecturesPlaceService.getList(lecturesPlace);
			for(int j=0; j<lecturesPlaceList.size(); j++) {
				lecturesPlaceService.delete(lecturesPlaceList.get(j).getId());
			}			
			lectures.setId(Integer.valueOf(deleteArray[i]));
			lectures = getData(lectures);
			try {
				UploadUtil.delete(uploadFilePath + "images/lectures/" + lectures.getPhoto());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(lectures);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(lectures.getId());
			editLog.setFunction("LECTURES");
			editLog.setAction_type("DELETE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			delete(Integer.valueOf(deleteArray[i]));
		}
	}

	@Override
	public ResponseEntity<?> checkTopCountFunction(Lectures lectures) throws IOException {
		String msg = "";
		Integer returnLecturesTop = checkTopCount(lectures);
		if(returnLecturesTop >= 4) {
			msg = "true";
			return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
		}else {
			msg = "false";
			return new ResponseEntity<String>(msg, HttpStatus.OK);
		}
		
	}

	@Override
	public ResponseEntity<?> changeCategory(String id, LecturesCategory lecturesCategory) {
		if(id != null && !"".equals(id)) {
			lecturesCategory.setParent_id(Integer.valueOf(id));
		}
		List<LecturesCategory>lectureCategoryList = new ArrayList<>();
		lectureCategoryList = lecturesCategoryService.getLayerList("2", lecturesCategory);
		if(lectureCategoryList.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			return new ResponseEntity<>(lectureCategoryList,HttpStatus.OK);
		}
		
	}

	@Override
	public List<Lectures> getFreeCourse() {
		return lecturesDao.getFreeCourse();
	}
	
	@Override
	public List<Map<String, Object>> getPlaceName(Integer id){
		return lecturesDao.getPlaceName(id);
	}

//	============
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		return lecturesDao.getNormalList();
	}

	@Override
	public void updateNormalData(Lectures lectures) {
		lecturesDao.updateNormalData(lectures);
	}

	
	

}
