package com.tkb.realgoodTransform.controller.admin;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;



import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.Location;
import com.tkb.realgoodTransform.model.SchoolBulletin;
import com.tkb.realgoodTransform.model.SchoolBulletinCategory;
import com.tkb.realgoodTransform.model.SchoolBulletinContent;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.SchoolBulletinCategoryService;
import com.tkb.realgoodTransform.service.SchoolBulletinContentService;
import com.tkb.realgoodTransform.service.SchoolBulletinService;
import com.tkb.realgoodTransform.utils.AreaLocationApi;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.CryptographyUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class SchoolBulletinController extends BaseUtils {
	
	private int pageNo; // 頁碼
	
	@Value("${upload.file.path}")
	private String uploadFilePath; // 檔案上傳位置

	@Autowired
	private SchoolBulletinService schoolBulletinService;
	
	@Autowired
	private SchoolBulletinCategoryService schoolBulletinCategoryService;
	
	@Autowired
	private SchoolBulletinContentService schoolBulletinContentService;
	
	@Autowired
	private EditLogService editLogService;

	/**
	 * 清單頁面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/schoolBulletin/index", method = { RequestMethod.POST, RequestMethod.GET })
	public String index(@ModelAttribute SchoolBulletin schoolBulletin, Model model, HttpServletRequest request, HttpSession session, @SessionAttribute("userAccountSession") User user) {
		List<SchoolBulletin> schoolBulletinList = new ArrayList<>();
		
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		if(title!=null) {
			schoolBulletin.setTitle(title);
		}
		
		schoolBulletin.setAccount(user.getAccount());
		schoolBulletin.setArea_id(Integer.valueOf(user.getArea()));
		
		if (pageNo != 0) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();
		int groupId = schoolBulletinService.userAccount(user);
		pageTotalCount = schoolBulletinService.getCount(schoolBulletin);
		pageNo = super.pageSetting(pageNo);
		schoolBulletinList = schoolBulletinService.getList(pageCount, pageStart, schoolBulletin, groupId);
		AreaLocationApi areaLocationApi = new AreaLocationApi();
		List<Location> allLocationList = areaLocationApi.jsonToLocationList(areaLocationApi.getApiLocation("0", "1"));
		
		Gson gson = new Gson();
		String schoolBulletinListJson = gson.toJson(schoolBulletinList);
		Type listType = new TypeToken<List<Map<String, String>>>(){}.getType();
		List<Map<String, String>> bulletinList = new Gson().fromJson(schoolBulletinListJson, listType);
		List<SchoolBulletin> convertedSBList = new ArrayList<>();
		
		SimpleDateFormat  inputFormat = new SimpleDateFormat("MMM dd, yyyy");
		
		for (Map<String, String> bulletinMap : bulletinList) {
		    String area = (String) bulletinMap.get("area");
		    if ("0".equals(area)) {
		    	bulletinMap.put("area_name", "全區");
			} else {
				for(int i=0; i<allLocationList.size(); i++) {
					if(allLocationList.get(i).getId().toString().equals(area)) {
						bulletinMap.put("area_name",allLocationList.get(i).getName());
					}
				}
			}
		    SchoolBulletin tempSchoolBulletin = new SchoolBulletin();
		    tempSchoolBulletin.setId(Integer.parseInt(bulletinMap.get("id")));
		    tempSchoolBulletin.setTitle(bulletinMap.get("title"));
		    tempSchoolBulletin.setArea(bulletinMap.get("area"));
		    tempSchoolBulletin.setCategory(bulletinMap.get("category"));
		    tempSchoolBulletin.setImage(bulletinMap.get("image"));
			
		    try {
		        Date inputBeginDate = inputFormat.parse(bulletinMap.get("begin_date").toString());
				java.sql.Date beginSqlDate = new java.sql.Date(inputBeginDate.getTime());

				Date inputEndDate = inputFormat.parse(bulletinMap.get("end_date"));
				java.sql.Date endDate = new java.sql.Date(inputEndDate.getTime());

				Date inputCreateDate = inputFormat.parse(bulletinMap.get("create_date"));
				java.sql.Date createDate = new java.sql.Date(inputCreateDate.getTime());

				Date inputUpdateDate = inputFormat.parse(bulletinMap.get("update_date"));
				java.sql.Date updateDate = new java.sql.Date(inputUpdateDate.getTime());

				tempSchoolBulletin.setBegin_date(beginSqlDate);
				tempSchoolBulletin.setEnd_date(endDate);
				tempSchoolBulletin.setCreate_date(createDate);
				tempSchoolBulletin.setUpdate_date(updateDate);
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    tempSchoolBulletin.setClick_rate(Integer.parseInt(bulletinMap.get("click_rate")));
		    tempSchoolBulletin.setCreate_by(bulletinMap.get("create_by"));
		    tempSchoolBulletin.setUpdate_by(bulletinMap.get("update_by"));
		    tempSchoolBulletin.setEncrypturl(bulletinMap.get("encrypturl"));
		    tempSchoolBulletin.setCategory_name(bulletinMap.get("category_name"));
		    tempSchoolBulletin.setArea_name(bulletinMap.get("area_name"));
		    
		    convertedSBList.add(tempSchoolBulletin);
		}
		schoolBulletinList = convertedSBList;
		
		model.addAttribute("schoolBulletinList", schoolBulletinList)
			 .addAttribute("pageNo", pageNo)
			 .addAttribute("pageTotalCount", pageTotalCount)
			 .addAttribute("pageCount", pageCount)
			 .addAttribute("totalPage", totalPage)
			 .addAttribute("leftStartPage", leftStartPage)
			 .addAttribute("leftPageNum", leftPageNum)
			 .addAttribute("rightPageNum", rightPageNum)
			 .addAttribute("leftEndPage", leftEndPage)
			 .addAttribute("rightStartPage", rightStartPage)
			 .addAttribute("rightEndPage", rightEndPage);

		return "admin/schoolBulletin/schoolBulletinList";
	}

	/**
	 * 新增頁面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/schoolBulletin/add", method = { RequestMethod.POST })
	public String add(@ModelAttribute SchoolBulletin schoolBulletin, Model model, @ModelAttribute Area area, @ModelAttribute SchoolBulletinContent schoolBulletinContent, @SessionAttribute("userAccountSession") User user, @ModelAttribute SchoolBulletinCategory schoolBulletinCategory,HttpServletRequest request) {

		AreaLocationApi areaLocationApi = new AreaLocationApi();
		List<Area>areaTempList = new ArrayList<>();
		List<Area> areaList = new ArrayList<>();
		List<SchoolBulletinCategory> schoolBulletinCategoryList = new ArrayList<>();
		List<SchoolBulletinContent> schoolBulletinContentList = new ArrayList<>();
		String website = "R";
		String judgeSchoolChange = "";
		Location location = new Location();
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		
			String apiArea = areaLocationApi.getApiArea();
			List<Area> apiAreaList = areaLocationApi.jsonToAreaList(apiArea);
			areaList = apiAreaList;
			
			for(int i=0; i<areaList.size(); i++) {
				Area areaTemp = areaList.get(i);
				if(areaTemp != null) {
					String apiLocation = areaLocationApi.getApiLocation(website, areaTemp.getId().toString());
					if(apiLocation != null && !"".equals(apiLocation)) {
						areaTempList.add(areaTemp);
					}
				}
			}
			areaList = areaTempList;
			
		schoolBulletinCategory.setParent_id(0);
		schoolBulletinCategoryList = schoolBulletinCategoryService.getLayerList("1", schoolBulletinCategory);
		


		if ("0".equals(user.getArea())) {
			area.setId(Integer.valueOf("1"));
			Location tempLocation = new Location();
			tempLocation.setId(Integer.valueOf(user.getArea()));
			tempLocation.setName("全區");
			location = tempLocation;
		} else {
			List<Location> allLocationList = areaLocationApi.jsonToLocationList(areaLocationApi.getApiLocation("0", "1"));
			for (int i = 0; i < allLocationList.size(); i++) {
				if (allLocationList.get(i).getId().toString().equals(user.getArea())) {
					area.setId(Integer.valueOf(allLocationList.get(i).getArea_id().toString()));
					location = allLocationList.get(i);
				}
			}
		}
		
		if(!user.getArea().equals("0")){
			judgeSchoolChange = "false";
		}
		
		model.addAttribute("user", user)
			 .addAttribute("areaList", areaList)
			 .addAttribute("schoolBulletinCategoryList", schoolBulletinCategoryList)
			 .addAttribute("area", area)
			 .addAttribute("location", location)
			 .addAttribute("searchTitle", title)
			 .addAttribute("judgeSchoolChange", judgeSchoolChange);

		return "admin/schoolBulletin/schoolBulletinForm";
	}

	/**
	 * 新增資料
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "schoolBulletin/addSubmit", method = { RequestMethod.POST })
	public String addSubmit(Model model, @SessionAttribute("userAccountSession") User user,
			@ModelAttribute SchoolBulletin schoolBulletin, @ModelAttribute SchoolBulletinContent schoolBulletinContent,
			HttpServletRequest request) {
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");

		try {
			String[] iconList = request.getParameter("iconList") == null ? null : request.getParameter("iconList").split(",;,");
			String[] contentTitleList = request.getParameter("contentTitleList") == null ? null : request.getParameter("contentTitleList").split(",;,");
			String[] contentContentList = request.getParameter("contentContentList") == null ? null : request.getParameter("contentContentList").split(",;,");

			if (iconList == null) throw new NullPointerException();
			if (contentTitleList == null) throw new NullPointerException();
			if (contentContentList == null) throw new NullPointerException();

			int id = schoolBulletinService.getNextId();
			schoolBulletin.setId(id);
			schoolBulletin.setCreate_by(user.getAccount());
			schoolBulletin.setUpdate_by(user.getAccount());
			schoolBulletin.setEncrypturl(CryptographyUtils.encryptStr(String.valueOf(schoolBulletin.getId())));
			schoolBulletinService.add(schoolBulletin);

			Gson gson = new Gson();
			String jsonString = gson.toJson(schoolBulletin);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(schoolBulletin.getId());
			editLog.setFunction("SCHOOL_BULLETIN");
			editLog.setAction_type("ADD");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);

			if (iconList.length == contentTitleList.length && iconList.length == contentContentList.length) {
				schoolBulletinContent.setCreate_by(user.getAccount());
				schoolBulletinContent.setUpdate_by(user.getAccount());

				for (int i = 0; i < iconList.length; i++) {
					schoolBulletinContent.setId(schoolBulletinContentService.getNextId());
					schoolBulletinContent.setSchool_bulletin_id(id);
					schoolBulletinContent.setIcon(iconList[i]);
					schoolBulletinContent.setTitle(contentTitleList[i]);
					schoolBulletinContent.setContent(contentContentList[i]);
					schoolBulletinContentService.add(schoolBulletinContent);
				}

			}
			schoolBulletin.setShow_message("新增公告成功");
		} catch (NullPointerException e) {
			e.printStackTrace();
			schoolBulletin.setShow_message("新增失敗,請輸入ICON,內容主題,內文");
			return "admin/schoolBulletin/toMessagePage";
		}
		model.addAttribute("title",title);
		return "admin/schoolBulletin/toMessagePage";

	}

	/**
	 * 修改頁面
	 * 
	 * @return
	 */
	@RequestMapping(value = "schoolBulletin/update", method = { RequestMethod.GET, RequestMethod.POST })
	public String update(Model model, @SessionAttribute("userAccountSession") User user,
			@ModelAttribute SchoolBulletin schoolBulletin,
			@ModelAttribute SchoolBulletinCategory schoolBulletinCategory,
			@ModelAttribute SchoolBulletinContent schoolBulletinContent, @ModelAttribute Area area,
			HttpServletRequest request, HttpSession session) {
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		AreaLocationApi areaLocationApi = new AreaLocationApi();
		List<Area> areaList = new ArrayList<>();
		List<Area>areaTempList = new ArrayList<>();
		List<SchoolBulletinCategory> schoolBulletinCategoryList = new ArrayList<>();
		List<SchoolBulletinContent> schoolBulletinContentList = new ArrayList<>();
		String website = "D";
		String judgeSchoolChange = "";
		Location location = new Location();
		
			String apiArea = areaLocationApi.getApiArea();
			List<Area> apiAreaList = areaLocationApi.jsonToAreaList(apiArea);
			areaList = apiAreaList;
			
			for(int i=0; i<areaList.size(); i++) {
				Area areaTemp = areaList.get(i);
				if(areaTemp != null) {
					String apiLocation = areaLocationApi.getApiLocation(website, areaTemp.getId().toString());
					if(apiLocation != null && !"".equals(apiLocation)) {
						areaTempList.add(areaTemp);
					}
				}
			}
			areaList = areaTempList;
		schoolBulletinCategory.setParent_id(0);
		schoolBulletinCategoryList = schoolBulletinCategoryService.getLayerList("1", schoolBulletinCategory);

		schoolBulletin = schoolBulletinService.getData(schoolBulletin);

		schoolBulletinContent.setSchool_bulletin_id(schoolBulletin.getId());
		schoolBulletinContentList = schoolBulletinContentService.getList(schoolBulletinContent);


		if ("0".equals(schoolBulletin.getArea())) {
			area.setId(Integer.valueOf("1"));
			Location tempLocation = new Location();
			tempLocation.setId(Integer.valueOf(schoolBulletin.getArea()));
			tempLocation.setName("全區");
			location = tempLocation;
		} else {
			List<Location> allLocationList = areaLocationApi.jsonToLocationList(areaLocationApi.getApiLocation("0", "1"));
			for (int i = 0; i < allLocationList.size(); i++) {
				if (allLocationList.get(i).getId().toString().equals(schoolBulletin.getArea())) {
					area.setId(Integer.valueOf(allLocationList.get(i).getArea_id().toString()));
					location = allLocationList.get(i);
				}
			}
		}
		
		if(!user.getArea().equals("0")){
			judgeSchoolChange = "false";
		}
		
		model.addAttribute("user", user)
			 .addAttribute("areaList", areaList)
			 .addAttribute("schoolBulletinCategoryList", schoolBulletinCategoryList)
			 .addAttribute("schoolBulletinContentList", schoolBulletinContentList)
			 .addAttribute("schoolBulletin", schoolBulletin)
			 .addAttribute("area", area)
			 .addAttribute("location", location)
		     .addAttribute("searchTitle", title)
		     .addAttribute("judgeSchoolChange", judgeSchoolChange);
		
		return "admin/schoolBulletin/schoolBulletinForm";
	}

	/**
	 * 修改資料
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "schoolBulletin/updateSubmit", method = { RequestMethod.POST })
	public String updateSubmit(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute SchoolBulletin schoolBulletin, @ModelAttribute SchoolBulletinContent schoolBulletinContent,
			@SessionAttribute("userAccountSession") User user,Model model) throws IOException {
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		try {
			List<SchoolBulletinContent> schoolBulletinContentList = new ArrayList<>();
			String[] iconList = request.getParameter("iconList") == null ? null : request.getParameter("iconList").split(",;,");
			String[] contentTitleList = request.getParameter("contentTitleList") == null ? null : request.getParameter("contentTitleList").split(",;,");
			String[] contentContentList = request.getParameter("contentContentList") == null ? null : request.getParameter("contentContentList").split(",;,");
			String[] schoolBulletinContentIdList = request.getParameter("schoolBulletinContentIdList") == null ? null : request.getParameter("schoolBulletinContentIdList").split(",;,");
			String deleteIdList = "";

			if (iconList == null) throw new NullPointerException();
			if (contentTitleList == null) throw new NullPointerException();
			if (contentContentList == null) throw new NullPointerException();
			if (schoolBulletinContentIdList == null) throw new NullPointerException();

			if (iconList.length == contentTitleList.length && iconList.length == contentContentList.length && iconList.length == schoolBulletinContentIdList.length) {

				schoolBulletinContent.setSchool_bulletin_id(schoolBulletin.getId());
				schoolBulletinContentList = schoolBulletinContentService.getList(schoolBulletinContent);

				// 取得刪除ID
				for (int i = 0; i < schoolBulletinContentList.size(); i++) {
					for (int j = 0; j < schoolBulletinContentIdList.length; j++) {
						if (String.valueOf(schoolBulletinContentList.get(i).getId()).equals(schoolBulletinContentIdList[j])) {
							break;
						} else {
							if (j == (schoolBulletinContentIdList.length - 1)) {
								if ("".equals(deleteIdList)) {
									deleteIdList += schoolBulletinContentList.get(i).getId();
								} else {
									deleteIdList = deleteIdList + "," + schoolBulletinContentList.get(i).getId();
								}
							}
						}
					}
				}
				// 新增修改
				for (int i = 0; i < iconList.length; i++) {
					schoolBulletinContent.setIcon(iconList[i]);
					schoolBulletinContent.setTitle(contentTitleList[i]);
					schoolBulletinContent.setContent(contentContentList[i]);
					schoolBulletinContent.setCreate_by(user.getAccount());
					schoolBulletinContent.setUpdate_by(user.getAccount());
					// 新增
					if ("0".equals(schoolBulletinContentIdList[i])) {
						schoolBulletinContent.setId(schoolBulletinContentService.getNextId());
						schoolBulletinContent.setSchool_bulletin_id(schoolBulletin.getId());
						schoolBulletinContentService.add(schoolBulletinContent);
						// 修改
					} else {
						schoolBulletinContent.setId(Integer.valueOf(schoolBulletinContentIdList[i]));
						schoolBulletinContentService.update(schoolBulletinContent);
					}

				}

				// 刪除
				String[] deleteIdArray = deleteIdList.split(",;,");
				if (!"".equals(deleteIdArray[0])) {
					for (int i = 0; i < deleteIdArray.length; i++) {
						schoolBulletinContentService.delete(Integer.valueOf(deleteIdArray[i]));
					}
				}
			}
			schoolBulletin.setUpdate_by(user.getAccount());

			schoolBulletinService.update(schoolBulletin);

			Gson gson = new Gson();
			String jsonString = gson.toJson(schoolBulletin);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(schoolBulletin.getId());
			editLog.setFunction("SCHOOL_BULLETIN");
			editLog.setAction_type("UPDATE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);

			schoolBulletin.setShow_message("修改公告成功");
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			schoolBulletin.setShow_message("新增失敗,請輸入ICON,內容主題,內文");
		} catch (Exception e) {
			e.printStackTrace();
			schoolBulletin.setShow_message("修改公告失敗");
		}
		model.addAttribute("title", title);
		return "admin/schoolBulletin/toMessagePage";
	}

	/**
	 * 刪除百官公告
	 * 
	 * @return
	 */
	@RequestMapping(value = "schoolBulletin/delete", method = { RequestMethod.POST })
	public String delete(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SchoolBulletin schoolBulletin, @ModelAttribute SchoolBulletinContent schoolBulletinContent,
			@SessionAttribute("userAccountSession") User user,Model model) throws IOException {
		List<SchoolBulletinContent> schoolBulletinContentList = new ArrayList<>();
		String selectList = request.getParameter("deleteList");
		String[] selectArray = selectList.split(",");
		for (int i = 0; i < selectArray.length; i++) {
			schoolBulletinContent.setSchool_bulletin_id(Integer.parseInt(selectArray[i]));
			schoolBulletinContentList = schoolBulletinContentService.getList(schoolBulletinContent);
			for (int j = 0; j < schoolBulletinContentList.size(); j++) {
				schoolBulletinContentService.delete(schoolBulletinContentList.get(j).getId());
			}
			schoolBulletin.setId(Integer.valueOf(selectArray[i]));
			schoolBulletin = schoolBulletinService.getData(schoolBulletin);

			Gson gson = new Gson();
			String jsonString = gson.toJson(schoolBulletin);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(schoolBulletin.getId());
			editLog.setFunction("SCHOOL_BULLETIN");
			editLog.setAction_type("DELETE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			schoolBulletinService.delete(Integer.valueOf(selectArray[i]));
			model.addAttribute("message", "刪除成功");
		}
		return "admin/schoolBulletin/toMessagePage";
	}

	/**
	 * 取得第二層地區
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "schoolBulletin/changeArea", method = { RequestMethod.POST })
	public ResponseEntity<?> changeArea(HttpServletRequest request, HttpServletResponse response, @ModelAttribute Area area) throws IOException {
		
		AreaLocationApi areaLocationApi = new AreaLocationApi();
		List<Location> locationList = new ArrayList<>();
		String id = request.getParameter("id");
		String website = "D";
		List<Location> tempLocationList = new ArrayList<>();
		
		if ("1".equals(id)) {
			Location tempLocation = new Location();
			tempLocation.setId(Integer.valueOf("0"));
			tempLocation.setName("全區");
			tempLocationList.add(tempLocation);
			locationList = tempLocationList;
		} else {
			String location = areaLocationApi.getApiLocation(website, id);
			List<Location> apiLocationList = areaLocationApi.jsonToLocationList(location);
			locationList = apiLocationList;
		}
		
		return new ResponseEntity<String>(new JSONArray(locationList).toString(), HttpStatus.OK);
	}
	
	
	
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/schoolBulletin/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		List<Map<String, Object>> getNormalContentList = new ArrayList<>();
		List<Map<String, Object>> getNormalAreaList = new ArrayList<>();
//		getNormalBannerList = schoolBulletinService.getNormalList();
//		getNormalContentList = schoolBulletinContentService.getNormalList();
		getNormalAreaList = schoolBulletinService.getNormalAreaList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		
			//schoolBulletin這個table的
//			for(Map<String, Object> map : getNormalBannerList) {
//				SchoolBulletin schoolBulletin = new SchoolBulletin();
//				
//				if(map.get("ID") != null) {
//					schoolBulletin.setId(Integer.valueOf(map.get("ID").toString()));
//				}
//				if(map.get("TITLE") != null) {
//					schoolBulletin.setTitle(map.get("TITLE").toString());
//				}
//				if(map.get("AREA") != null) {
//					schoolBulletin.setArea(map.get("AREA").toString());
//				}
//				if(map.get("CATEGORY") != null) {
//					schoolBulletin.setCategory(map.get("CATEGORY").toString());
//				}
//				if(map.get("IMAGE") != null) {
//					schoolBulletin.setImage(map.get("IMAGE").toString());
//				}
//				if(map.get("BEGIN_DATE") != null) {
//					schoolBulletin.setBegin_date(new java.sql.Date(sdf.parse(map.get("BEGIN_DATE").toString()).getTime()));
//				}
//				if(map.get("END_DATE") != null) {
//					schoolBulletin.setEnd_date(new java.sql.Date(sdf.parse(map.get("END_DATE").toString()).getTime()));
//				}
//				if(map.get("CLICK_RATE") != null) {
//					schoolBulletin.setClick_rate(Integer.valueOf(map.get("CLICK_RATE").toString()));
//				}
//				if(map.get("CREATE_BY") != null) {
//					schoolBulletin.setCreate_by(map.get("CREATE_BY").toString());
//				}
//				if(map.get("CREATE_DATE") != null) {
//					schoolBulletin.setCreate_date(new java.sql.Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
//				}
//				if(map.get("UPDATE_BY") != null) {
//					schoolBulletin.setUpdate_by(map.get("UPDATE_BY").toString());
//				}
//				if(map.get("UPDATE_DATE") != null) {
//					schoolBulletin.setUpdate_date(new java.sql.Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
//				}
//				if(map.get("CONTENT") != null) {
//					schoolBulletin.setContent(map.get("CONTENT").toString());
//				}
//				if(map.get("ENCRYPTURL") != null) {
//					schoolBulletin.setEncrypturl(map.get("ENCRYPTURL").toString());
//				}
//				if(map.get("PRODUCT_CATEGORY") != null) {
//					schoolBulletin.setProduct_category(map.get("PRODUCT_CATEGORY").toString());
//				}
//				schoolBulletinService.updateNormalData(schoolBulletin);
//				
//			}
			
			
			
			
			//school_bulletin_content這個table的
//			for(Map<String, Object> map : getNormalContentList) {
//				SchoolBulletinContent schoolBulletinContent = new SchoolBulletinContent();
//				
//				if(map.get("ID") != null) {
//					schoolBulletinContent.setId(Integer.valueOf(map.get("ID").toString()));
//				}
//				if(map.get("SCHOOL_BULLETIN_ID") != null) {
//					schoolBulletinContent.setSchool_bulletin_id(Integer.valueOf(map.get("SCHOOL_BULLETIN_ID").toString()));
//				}
//				if(map.get("ICON") != null) {
//					schoolBulletinContent.setIcon(map.get("ICON").toString());
//				}
//				if(map.get("TITLE") != null) {
//					schoolBulletinContent.setTitle(map.get("TITLE").toString());
//				}
//				if(map.get("CONTENT") != null) {
//					schoolBulletinContent.setContent(map.get("CONTENT").toString());
//				}
//				if(map.get("CREATE_BY") != null) {
//					schoolBulletinContent.setCreate_by(map.get("CREATE_BY").toString());
//				}
//				if(map.get("CREATE_DATE") != null) {
//					schoolBulletinContent.setCreate_date(new java.sql.Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
//				}
//				if(map.get("UPDATE_BY") != null) {
//					schoolBulletinContent.setUpdate_by(map.get("UPDATE_BY").toString());
//				}
//				if(map.get("UPDATE_DATE") != null) {
//					schoolBulletinContent.setUpdate_date(new java.sql.Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
//				}
//				schoolBulletinContentService.updateNormalData(schoolBulletinContent);
//				
//			}
			
			
			
			
			
			for(Map<String, Object> map : getNormalAreaList) {
				Area area = new Area();
				
				if(map.get("ID") != null) {
					area.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("PARENT_ID") != null) {
					
					area.setParent_id(Integer.valueOf(map.get("PARENT_ID").toString()));
				}
				if(map.get("NAME") != null) {
					
					area.setName(map.get("NAME").toString());
				}
				if(map.get("LAYER") != null) {
					area.setLayer(map.get("LAYER").toString());
					
				}
				
				if(map.get("CREATE_BY") != null) {
					area.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					area.setCreate_date(new java.sql.Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					area.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					area.setUpdate_date(new java.sql.Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("MAPURL") != null) {
					area.setMapUrl(map.get("MAPURL").toString());
					
				}
				schoolBulletinService.updateNormalAreaData(area);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
	
	
}
