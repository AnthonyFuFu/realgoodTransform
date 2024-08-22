package com.tkb.realgoodTransform.controller.front;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;



import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.Lectures;
import com.tkb.realgoodTransform.model.LecturesCategory;
import com.tkb.realgoodTransform.model.LecturesContent;
import com.tkb.realgoodTransform.model.LecturesPlace;
import com.tkb.realgoodTransform.model.Location;
import com.tkb.realgoodTransform.model.NavBanner;
import com.tkb.realgoodTransform.service.LecturesCategoryService;
import com.tkb.realgoodTransform.service.LecturesContentService;
import com.tkb.realgoodTransform.service.LecturesPlaceService;
import com.tkb.realgoodTransform.service.LecturesService;
import com.tkb.realgoodTransform.service.NavBannerService;
import com.tkb.realgoodTransform.utils.AreaLocationApi;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.CryptographyUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FrontLecturesController extends BaseUtils {
	
	private int pageNo; // 頁碼
	
	@Autowired
	private LecturesService lecturesService;
	
	@Autowired
	private LecturesCategoryService lecturesCategoryService;
	
	@Autowired
	private LecturesContentService lecturesContentService;
	
	@Autowired
	private LecturesPlaceService lecturesPlaceService;
	
	@Autowired
	private NavBannerService navBannerService;

	@RequestMapping("/lectures/index")
	public String index(Lectures lectures, LecturesCategory lecturesCategory, HttpServletRequest request, Model model, @RequestParam(required = false) String category, @RequestParam(required = false) String fare) {
		List<Lectures> lecturesList = new ArrayList<>();
		List<LecturesCategory> lecturesCategoryList = new ArrayList<>();
		String sort = request.getParameter("sort") == null ? "" : request.getParameter("sort");
		String pageCountStr = request.getParameter("page_count") == null ? "" : request.getParameter("page_count");
		lecturesCategoryList = lecturesCategoryService.getFrontLayerList("1");
		pageTotalCount = lecturesService.getFrontCount(lectures);
		if (!"".equals(pageCountStr)) {
			super.pageCount = Integer.valueOf(pageCountStr);
		} else {
			super.pageCount = 8;
		}
		pageNo = super.pageSetting(pageNo);
		if (category != null && !"".equals(category)) {
			lectures.setCategory(category);
		}
		if (fare != null && !"".equals(fare)) {
			lectures.setFare(fare);
		}
		
		lecturesList = lecturesService.getFrontList(pageCount, pageStart, lectures, sort);
		addModelAttribute(pageNo, model);
		model.addAttribute("lecturesList", lecturesList)
			 .addAttribute("lecturesCategoryList", lecturesCategoryList)
			 .addAttribute("category", category)
			 .addAttribute("fare", fare);
		return "front/lectures/list";
	}

	@PostMapping("/lectures/toSearch")
	public ResponseEntity<?> toSearch(Lectures lectures, LecturesCategory lecturesCategory, HttpServletRequest request, Model model) {
		List<Lectures> lecturesList = new ArrayList<>();
		String lecturesCategory_id = request.getParameter("lecturesCategory_id") == "" ? "" : request.getParameter("lecturesCategory_id");
		String searchSort = request.getParameter("searchSort") == null ? "" : request.getParameter("searchSort");
		String pageCountStr = request.getParameter("page_count") == null ? "" : request.getParameter("page_count");
		String page_no = request.getParameter("page_no") == null ? "" : request.getParameter("page_no");
		String date = request.getParameter("date") == null ? "" : request.getParameter("date");
		String fare = request.getParameter("fare") == null ? "" : request.getParameter("fare");
		String week_begin_date = "", week_end_date = "";
		super.resetPage();
		if (!"".equals(lecturesCategory_id)) {
			lectures.setCategory(lecturesCategory_id);
		}
		if (!"".equals(fare)) {
			lectures.setFare(fare);
		}

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		week_begin_date = sdf.format(getFirstDayOfWeek(cal.getTime()));
		week_end_date = sdf.format(getLastDayOfWeek(cal.getTime()));

		lectures.setDate(date);
		lectures.setWeek_begin_date(week_begin_date);
		lectures.setWeek_end_date(week_end_date);

		if (!"".equals(pageCountStr)) {
			super.pageCount = Integer.valueOf(pageCountStr);
		} else {
			super.pageCount = 8;
		}

		pageTotalCount = lecturesService.getFrontCount(lectures);
		pageNo = super.pageSetting(Integer.valueOf(page_no));
		lecturesList = lecturesService.getFrontList(pageCount, pageStart, lectures, searchSort);

		if (lecturesList.size() > 0) {
			lecturesList.get(0).setPageCount(pageCount);
			lecturesList.get(0).setPageTotalCount(pageTotalCount);
			lecturesList.get(0).setTotalPage(totalPage);

			lecturesList.get(0).setLeftStartPage(leftStartPage);
			lecturesList.get(0).setLeftEndPage(leftEndPage);
			lecturesList.get(0).setRightStartPage(rightStartPage);
			lecturesList.get(0).setRightEndPage(rightEndPage);
			lecturesList.get(0).setLeftPageNum(leftPageNum);
			lecturesList.get(0).setRightPageNum(rightPageNum);
		}
		addModelAttribute(pageNo, model);
		return new ResponseEntity<>(lecturesList, HttpStatus.OK);
	}

	@GetMapping("/lectures/inside")
	public String inside(@RequestParam String str, @RequestParam(required = false) String print_id, Lectures lectures,
			LecturesCategory lecturesCategory, LecturesPlace lecturesPlace, LecturesContent lecturesContent, NavBanner navBanner, Model model, HttpServletRequest request) {
		List<LecturesContent> lecturesContentList = new ArrayList<>();
		List<LecturesPlace> lecturesPlaceList = new ArrayList<>();
		List<NavBanner> navBannerList = new ArrayList<>();
		List<NavBanner> navBannerType2List = new ArrayList<>();
		List<LecturesPlace> placeList = new ArrayList<>();
		List<Location> locationList = new ArrayList<>();

		String shareStr = "";
		String ParentCheck = "";
		AreaLocationApi areaLocationApi = new AreaLocationApi();
		String website = "R";
		List<LecturesPlace> placeMapList = new ArrayList<>();

//		//取得PO版人
//		String print_id = request.getParameter("print_id");
		// 判斷瀏覽器
		String ua = request.getHeader("User-Agent");
		String login_equipment = "";
		if (ua.indexOf("Android") > -1 || ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1) {
			login_equipment = "MOBILE";
		} else {
			login_equipment = "COM";
		}

		if ("".equals(str)) {
			return "front/lectures/list";
		}
		try {
			lectures.setId(Integer.valueOf(CryptographyUtils.staticdecryptStr(str)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		lectures = lecturesService.getFrontData(lectures);

		lecturesCategory.setId(Integer.valueOf(lectures.getCategory()));
		lecturesCategory = lecturesCategoryService.getData(lecturesCategory);

		lecturesService.updateClickRate(lectures);

		lecturesContent.setLectures_id(lectures.getId());
		lecturesContentList = lecturesContentService.getList(lecturesContent);

		lecturesPlace.setLectures_id(lectures.getId());
		lecturesPlaceList = lecturesPlaceService.getList(lecturesPlace);

		/* 設定星期 */
		for (int i = 0; i < lecturesPlaceList.size(); i++) {
			try {
				lecturesPlaceList.get(i).setWeek(date2Day(lecturesPlaceList.get(i).getPlaceDay().toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		placeList = lecturesPlaceService.getPlaceList(lecturesPlace);
		
		if (placeList.size() > 0 && placeList != null) {
			for (int i = 0; i < placeList.size(); i++) {
				ParentCheck = placeList.get(i).getPlaceName();
				if ("全省甄戰".equals(ParentCheck)) {
					String allLocation = areaLocationApi.getApiLocation(website, "1");
//					List<Location> locationList = new ArrayList<>();
					locationList = areaLocationApi.jsonToLocationList(allLocation);
					// 北到南排序
			        Collections.sort(locationList, new Comparator<Location>() {
			            @Override
			            public int compare(Location loc1, Location loc2) {
			                return Integer.compare(Integer.valueOf(loc1.getArea_id()), Integer.valueOf(loc2.getArea_id()));
			            }
			        });
					for (int j = 0; j < locationList.size(); j++) {
						LecturesPlace tempLecturesPlace = new LecturesPlace();
						tempLecturesPlace.setArea_id(locationList.get(j).getArea_id());
						tempLecturesPlace.setPlaceName(locationList.get(j).getName());
						tempLecturesPlace.setPlace_address(locationList.get(j).getAddress());
						tempLecturesPlace.setUrl(locationList.get(j).getUrl());
						placeMapList.add(tempLecturesPlace);
					}

				} else if ("北區".equals(ParentCheck) || "台北甄戰".equals(ParentCheck) || "台北".equals(ParentCheck)) {
					String northLocation = areaLocationApi.getApiLocation(website, "2");
					List<Location> northList = areaLocationApi.jsonToLocationList(northLocation);
					for (int j = 0; j < northList.size(); j++) {
						if (ParentCheck.equals(northList.get(j).getName().substring(0, 4)) || ParentCheck.equals(northList.get(j).getName().substring(0, 2))) {
							LecturesPlace tempLecturesPlace = new LecturesPlace();
							tempLecturesPlace.setArea_id(northList.get(j).getArea_id());
							tempLecturesPlace.setPlaceName(northList.get(j).getName());
							tempLecturesPlace.setPlace_address(northList.get(j).getAddress());
							tempLecturesPlace.setUrl(northList.get(j).getUrl());
							placeMapList.add(tempLecturesPlace);
						}
					}
				} else if ("桃竹區".equals(ParentCheck) || "桃園甄戰".equals(ParentCheck) || "桃園".equals(ParentCheck)
						|| "中壢甄戰".equals(ParentCheck) || "中壢".equals(ParentCheck) || "新竹甄戰".equals(ParentCheck)
						|| "新竹".equals(ParentCheck)) {
					String taozhuLocation = areaLocationApi.getApiLocation(website, "3");
					List<Location> taozhuList = areaLocationApi.jsonToLocationList(taozhuLocation);
					for (int j = 0; j < taozhuList.size(); j++) {
						if (ParentCheck.equals(taozhuList.get(j).getName().substring(0, 4)) || ParentCheck.equals(taozhuList.get(j).getName().substring(0, 2))) {
							LecturesPlace tempLecturesPlace = new LecturesPlace();
							tempLecturesPlace.setArea_id(taozhuList.get(j).getArea_id());
							tempLecturesPlace.setPlaceName(taozhuList.get(j).getName());
							tempLecturesPlace.setPlace_address(taozhuList.get(j).getAddress());
							tempLecturesPlace.setUrl(taozhuList.get(j).getUrl());
							placeMapList.add(tempLecturesPlace);
						}
					}
				} else if ("中區".equals(ParentCheck) || "台中甄戰".equals(ParentCheck) || "台中".equals(ParentCheck)
						|| "斗六甄戰".equals(ParentCheck) || "斗六".equals(ParentCheck) || "西屯甄戰".equals(ParentCheck)
						|| "西屯".equals(ParentCheck) || "虎尾甄戰".equals(ParentCheck) || "虎尾".equals(ParentCheck)
						|| "豐原門市".equals(ParentCheck) || "豐原".equals(ParentCheck)) {
					String centralLocation = areaLocationApi.getApiLocation(website, "4");
					List<Location> centralList = areaLocationApi.jsonToLocationList(centralLocation);
					for (int j = 0; j < centralList.size(); j++) {
						if (ParentCheck.equals(centralList.get(j).getName().substring(0, 4)) || ParentCheck.equals(centralList.get(j).getName().substring(0, 2))) {
							LecturesPlace tempLecturesPlace = new LecturesPlace();
							tempLecturesPlace.setArea_id(centralList.get(j).getArea_id());
							tempLecturesPlace.setPlaceName(centralList.get(j).getName());
							tempLecturesPlace.setPlace_address(centralList.get(j).getAddress());
							tempLecturesPlace.setUrl(centralList.get(j).getUrl());
							placeMapList.add(tempLecturesPlace);
						}
					}
				} else if ("嘉南區".equals(ParentCheck) || "嘉義甄戰".equals(ParentCheck) || "嘉義".equals(ParentCheck)
						|| "台南甄戰".equals(ParentCheck) || "台南".equals(ParentCheck)) {
					String chiaNanLocation = areaLocationApi.getApiLocation(website, "5");
					List<Location> chiaNanList = areaLocationApi.jsonToLocationList(chiaNanLocation);
					for (int j = 0; j < chiaNanList.size(); j++) {
						if (ParentCheck.equals(chiaNanList.get(j).getName().substring(0, 4)) || ParentCheck.equals(chiaNanList.get(j).getName().substring(0, 2))) {
							LecturesPlace tempLecturesPlace = new LecturesPlace();
							tempLecturesPlace.setArea_id(chiaNanList.get(j).getArea_id());
							tempLecturesPlace.setPlaceName(chiaNanList.get(j).getName());
							tempLecturesPlace.setPlace_address(chiaNanList.get(j).getAddress());
							tempLecturesPlace.setUrl(chiaNanList.get(j).getUrl());
							placeMapList.add(tempLecturesPlace);
						}
					}
				} else if ("高屏區".equals(ParentCheck) || "高雄甄戰".equals(ParentCheck) || "高雄".equals(ParentCheck)) {
					String kaoPingLocation = areaLocationApi.getApiLocation(website, "6");
					List<Location> kaoPingList = areaLocationApi.jsonToLocationList(kaoPingLocation);
					for (int j = 0; j < kaoPingList.size(); j++) {
						if (ParentCheck.equals(kaoPingList.get(j).getName().substring(0, 4)) || ParentCheck.equals(kaoPingList.get(j).getName().substring(0, 2))) {
							LecturesPlace tempLecturesPlace = new LecturesPlace();
							tempLecturesPlace.setArea_id(kaoPingList.get(j).getArea_id());
							tempLecturesPlace.setPlaceName(kaoPingList.get(j).getName());
							tempLecturesPlace.setPlace_address(kaoPingList.get(j).getAddress());
							tempLecturesPlace.setUrl(kaoPingList.get(j).getUrl());
							placeMapList.add(tempLecturesPlace);
						}
					}
				} else {
					
					Integer AddId = 999;
					LecturesPlace tempLecturesPlace = new LecturesPlace();
					tempLecturesPlace.setArea_id(String.valueOf(AddId));
					tempLecturesPlace.setPlaceName(ParentCheck);
//					tempLecturesPlace.setPlace_address(taozhuList.get(j).getAddress());
//					tempLecturesPlace.setUrl(taozhuList.get(j).getUrl());
					placeMapList.add(tempLecturesPlace);
					
				}
			}
			// 北到南排序
	        Collections.sort(placeMapList, new Comparator<LecturesPlace>() {
	            @Override
	            public int compare(LecturesPlace loc1, LecturesPlace loc2) {
	                return Integer.compare(Integer.valueOf(loc1.getArea_id()), Integer.valueOf(loc2.getArea_id()));
	            }
	        });
			placeList = placeMapList;
		}
		shareStr = str;

		navBannerList = navBannerService.getFrontList(navBanner);
		navBannerType2List = navBannerService.getFrontType2List(navBanner);// 取300*600
		
		model.addAttribute("lecturesContentList", lecturesContentList)
			 .addAttribute("lecturesPlaceList", lecturesPlaceList)
			 .addAttribute("placeList", placeList)
			 .addAttribute("locationList", locationList)
			 .addAttribute("navBannerList", navBannerList)
			 .addAttribute("navBannerType2List", navBannerType2List)
			 .addAttribute("lectures", lectures)
			 .addAttribute("lecturesCategory", lecturesCategory)
			 .addAttribute("placeday", lecturesPlace.getPlaceDay())
			 .addAttribute("begin_date", lectures.getBegin_date())
			 .addAttribute("update_date", lectures.getUpdate_date())
			 .addAttribute("print_id", print_id)
			 .addAttribute("login_equipment", login_equipment)
			 .addAttribute("shareStr", shareStr);
		return "front/lectures/inside";
	}

	/**
	 * 取得當前日期所在週的第一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek()); // Sunday
		return calendar.getTime();
	}

	/**
	 * 取得當前日期所在週的最後一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6); // Saturday
		return calendar.getTime();
	}

	/**
	 * 輸入日期，可以轉換成星期幾。
	 * 
	 * @param dateString日期字串
	 * @return 星期幾
	 * @throws ParseException 無法將字串轉換成java.util.Date類別
	 */
	public String date2Day(String dateString) throws ParseException {
		SimpleDateFormat dateStringFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dateStringFormat.parse(dateString);

		SimpleDateFormat date2DayFormat = new SimpleDateFormat("u");
		switch (date2DayFormat.format(date)) {
		case "1":
			return "(一)";
		case "2":
			return "(二)";
		case "3":
			return "(三)";
		case "4":
			return "(四)";
		case "5":
			return "(五)";
		case "6":
			return "(六)";
		case "7":
			return "(日)";
		}
		return date2DayFormat.format(date);
	}

	/**
	 * 取得地區地址
	 * 
	 * @throws Exception
	 */
	@PostMapping("/lectures/changePlaceMap")
	public ResponseEntity<?> changePlaceMap(HttpServletRequest request, Area area) throws Exception {

		String placeMap = request.getParameter("placeMap");
		AreaLocationApi areaLocationApi = new AreaLocationApi();
		String website = "R";
		
		List<Location> locationMapList = new ArrayList<>();
		List<Location> allLocationList = areaLocationApi.jsonToLocationList(areaLocationApi.getApiLocation(website, "1"));
		for (int i = 0; i < allLocationList.size(); i++) {
			if (allLocationList.get(i).getName().toString().equals(placeMap)) {
				locationMapList.add(allLocationList.get(i));
			}
		}
		return new ResponseEntity<>(locationMapList, HttpStatus.OK);
	}

	/**
	 * 取得場次
	 * 
	 * @throws Exception
	 */
	@PostMapping("/lectures/changePlace")
	public ResponseEntity<?> changePlace(HttpServletRequest request, LecturesPlace lecturesPlace) throws Exception {

		List<LecturesPlace> placeList = new ArrayList<>();
		String lectures_id = request.getParameter("lectures_id");
		String place = request.getParameter("place");
		
		if (lecturesPlace == null) {
			lecturesPlace = new LecturesPlace();
		}
		
		if ("全省甄戰".equals(place)) {
			lecturesPlace.setLectures_id(Integer.valueOf(lectures_id));
			lecturesPlace.setPlaceName(place);
			placeList = lecturesPlaceService.getEventList(lecturesPlace);
		} else {
			if ("台北甄戰".equals(place) || "桃園甄戰".equals(place) || "中壢甄戰".equals(place) || "新竹甄戰".equals(place)
					|| "台中甄戰".equals(place) || "斗六甄戰".equals(place) || "西屯甄戰".equals(place) || "虎尾甄戰".equals(place)
					|| "豐原門市".equals(place) || "嘉義甄戰".equals(place) || "台南甄戰".equals(place) || "高雄甄戰".equals(place)) {

				lecturesPlace.setLectures_id(Integer.valueOf(lectures_id));
				lecturesPlace.setPlaceName(place.substring(0, 4));
				placeList = lecturesPlaceService.getEventList(lecturesPlace);

				if (placeList.size() == 0) {
					lecturesPlace.setLectures_id(Integer.valueOf(lectures_id));
					lecturesPlace.setPlaceName(place.substring(0, 2));
					placeList = lecturesPlaceService.getEventList(lecturesPlace);
				}

			} else if ("0".equals(place)) {
				lecturesPlace.setLectures_id(Integer.valueOf(lectures_id));
				lecturesPlace.setPlaceName(place);
				placeList = lecturesPlaceService.getEventList(lecturesPlace);
			} else {
				lecturesPlace.setLectures_id(Integer.valueOf(lectures_id));
				lecturesPlace.setPlaceName(place.substring(0, 2));
				placeList = lecturesPlaceService.getEventList(lecturesPlace);
			}
		}
		
		return new ResponseEntity<>(placeList, HttpStatus.OK);
	}

	@PostMapping("/lectures/navBannerUpdateClickRate")
	public void navBannerUpdateClickRate(HttpServletRequest request, NavBanner navBanner) {
		String id = request.getParameter("id");
		navBanner.setId(Integer.valueOf(id));
		navBannerService.updateClickRate(navBanner);
	}

	@PostMapping("/lectures/lecturesMenu")
	@ResponseBody
	public String lecturesMenu(LecturesCategory lecturesCategory) throws IOException {
		List<LecturesCategory> lecturesCategoryList = new ArrayList<>();
		lecturesCategory.setParent_id(0);
		lecturesCategoryList = lecturesCategoryService.getLayerList("1", lecturesCategory);
		JSONArray tJSONArray = new JSONArray(lecturesCategoryList);
		return tJSONArray.toString();
	}

}
