package com.tkb.realgoodTransform.controller.front;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import com.tkb.realgoodTransform.model.Location;
import com.tkb.realgoodTransform.model.NavBanner;
import com.tkb.realgoodTransform.model.SchoolBulletin;
import com.tkb.realgoodTransform.model.SchoolBulletinCategory;
import com.tkb.realgoodTransform.model.SchoolBulletinContent;
import com.tkb.realgoodTransform.service.NavBannerService;
import com.tkb.realgoodTransform.service.SchoolBulletinCategoryService;
import com.tkb.realgoodTransform.service.SchoolBulletinContentService;
import com.tkb.realgoodTransform.service.SchoolBulletinService;
import com.tkb.realgoodTransform.utils.AreaLocationApi;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.CryptographyUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/schoolBulletin")
public class FrontSchoolBulletinController extends BaseUtils {

	private int pageNo; // 頁碼
	
	@Autowired
	private SchoolBulletinService schoolBulletinService;
	
	@Autowired
	private SchoolBulletinCategoryService schoolBulletinCategoryService;
	
	@Autowired
	private SchoolBulletinContentService schoolBulletinContentService;
	
	@Autowired
	private NavBannerService navBannerService;

	@RequestMapping("/index")
	public String index(Area area, SchoolBulletin schoolBulletin, SchoolBulletinCategory schoolBulletinCategory, HttpServletRequest request, Model model, @RequestParam(name="schoolBulletin.category",required = false) String category) {
		List<Area> areaList = new ArrayList<>();
		List<Area> areaInnerList = new ArrayList<>();
		List<SchoolBulletin> schoolBulletinList = new ArrayList<>();
		List<SchoolBulletinCategory> schoolBulletinCategoryList = new ArrayList<>();
		String sort = request.getParameter("sort") == null ? "" : request.getParameter("sort");
		String pageCountStr = request.getParameter("page_count") == null ? "" : request.getParameter("page_count");

		String website = "R";
		AreaLocationApi areaLocationApi = new AreaLocationApi();
		String apiArea = areaLocationApi.getApiArea();
		List<Area> apiAreaList = areaLocationApi.jsonToAreaList(apiArea);
		areaList = apiAreaList;

		// 地區第二層串接
		for (int i = 0; i < areaList.size(); i++) {
			Area areaTemp = new Area();
			areaTemp = areaList.get(i);
			String apiLocation = areaLocationApi.getApiLocation(website, areaList.get(i).getId().toString());

			if (apiLocation != null && !"".equals(apiLocation)) {
				List<Location> apiLocationList = areaLocationApi.jsonToLocationList(apiLocation);
				areaTemp.setLocationInnerList(apiLocationList);
				areaInnerList.add(i, areaTemp);
			} else {
				areaInnerList.add(i, new Area());
			}

		}
		schoolBulletinCategory.setParent_id(0);
		schoolBulletinCategory.setLayer("1");
		schoolBulletinCategoryList = schoolBulletinCategoryService.getLayerList("1", schoolBulletinCategory);
		pageTotalCount = schoolBulletinService.getFrontCount(schoolBulletin);
		if (!"".equals(pageCountStr)) {
			super.pageCount = Integer.valueOf(pageCountStr);
		} else {
			super.pageCount = 8;
		}
		
		pageNo = super.pageSetting(pageNo);
		schoolBulletinList = schoolBulletinService.getFrontList(pageCount, pageStart, schoolBulletin, sort);
		addModelAttribute(pageNo, model);
		model.addAttribute("areaList", areaList)
			 .addAttribute("areaInnerList", areaInnerList)
			 .addAttribute("schoolBulletinList", schoolBulletinList)
			 .addAttribute("schoolBulletinCategoryList", schoolBulletinCategoryList)
			 .addAttribute("category", category);
		return "front/schoolBulletin/list";
	}
	

	@PostMapping("/toSearch")
	public ResponseEntity<?> toSearch(HttpServletRequest request, SchoolBulletin schoolBulletin, SchoolBulletinCategory schoolBulletinCategory, Area area) {
		List<SchoolBulletin> schoolBulletinList = new ArrayList<>();
		List<Location> locationList = new ArrayList<>();
		String layer = request.getParameter("layer") == null ? "" : request.getParameter("layer");
		String area_idajax = request.getParameter("area_idajax") == "" ? "" : request.getParameter("area_idajax");
		String category_id = request.getParameter("category_id") == "" ? "" : request.getParameter("category_id");
		String searchSort = request.getParameter("searchSort") == null ? "" : request.getParameter("searchSort");
		String pageCountStr = request.getParameter("page_count") == null ? "" : request.getParameter("page_count");
		String page_no = request.getParameter("page_no") == null ? "" : request.getParameter("page_no");
		String date = request.getParameter("date") == null ? "" : request.getParameter("date");
		String week_begin_date = "", week_end_date = "";
		AreaLocationApi areaLocationApi = new AreaLocationApi();
		String website = "D";

		super.resetPage();
		if (!"".equals(category_id)) {
			schoolBulletin.setCategory(category_id);
		}

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		week_begin_date = sdf.format(getFirstDayOfWeek(cal.getTime()));
		week_end_date = sdf.format(getLastDayOfWeek(cal.getTime()));

		schoolBulletin.setDate(date);
		schoolBulletin.setWeek_begin_date(week_begin_date);
		schoolBulletin.setWeek_end_date(week_end_date);

		if (!"".equals(pageCountStr)) {
			super.pageCount = Integer.valueOf(pageCountStr);
		} else {
			super.pageCount = 8;
		}
		if ("1".equals(layer)) {
			if (!"".equals(area_idajax)) {
				if (Integer.valueOf(area_idajax) <= 6) {
					String apiLocation = areaLocationApi.getApiLocation(website, area_idajax);
					List<Location> apiLocationList = areaLocationApi.jsonToLocationList(apiLocation);
					locationList = apiLocationList;
				} else {
					String allLocation = areaLocationApi.getApiLocation(website, "1");
					List<Location> allLocationList = areaLocationApi.jsonToLocationList(allLocation);
					for (int i = 0; i < allLocationList.size(); i++) {
						if (allLocationList.get(i).getId().toString().equals(area_idajax)) {
							String apiLocation = areaLocationApi.getApiLocation(website, allLocationList.get(i).getArea_id().toString());
							List<Location> apiLocationList = areaLocationApi.jsonToLocationList(apiLocation);
							locationList = apiLocationList;
						}
					}
				}
			}
			pageTotalCount = schoolBulletinService.getCountByApiLocation(schoolBulletin, locationList);
			pageNo = super.pageSetting(Integer.valueOf(page_no));
			schoolBulletinList = schoolBulletinService.getListByApiLocation(pageCount, pageStart, schoolBulletin, locationList, searchSort);

		} else {

			if (!"".equals(area_idajax)) {
				schoolBulletin.setArea(area_idajax);
			}

			pageTotalCount = schoolBulletinService.getFrontCount(schoolBulletin);
			pageNo = super.pageSetting(Integer.valueOf(page_no));
			schoolBulletinList = schoolBulletinService.getFrontList(pageCount, pageStart, schoolBulletin, searchSort);

		}
		if (schoolBulletinList.size() > 0) {
			schoolBulletinList.get(0).setPageCount(pageCount);
			schoolBulletinList.get(0).setPageTotalCount(pageTotalCount);
			schoolBulletinList.get(0).setTotalPage(totalPage);

			schoolBulletinList.get(0).setLeftStartPage(leftStartPage);
			schoolBulletinList.get(0).setLeftEndPage(leftEndPage);
			schoolBulletinList.get(0).setRightStartPage(rightStartPage);
			schoolBulletinList.get(0).setRightEndPage(rightEndPage);
			schoolBulletinList.get(0).setLeftPageNum(leftPageNum);
			schoolBulletinList.get(0).setRightPageNum(rightPageNum);
		}
		return new ResponseEntity<>(schoolBulletinList, HttpStatus.OK);
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


	@GetMapping("/inside")
	public String inside(@RequestParam String str, SchoolBulletin schoolBulletin, SchoolBulletinContent schoolBulletinContent, NavBanner navBanner, Model model, HttpServletRequest request) {
		List<SchoolBulletin> schoolBulletinList = new ArrayList<>();
		List<SchoolBulletin> prevList = new ArrayList<>();
		List<SchoolBulletin> nextList = new ArrayList<>();
		List<SchoolBulletin> randomList = new ArrayList<>();
		List<SchoolBulletin> schoolBulletinRandomList = new ArrayList<>();
		List<SchoolBulletinContent> schoolBulletinContentList = new ArrayList<>();
		List<NavBanner> navBannerList = new ArrayList<>();
		List<NavBanner> navBannerType2List = new ArrayList<>();
		// 取得PO版人
		String print_id = request.getParameter("print_id");
		// 判斷瀏覽器
		String ua = request.getHeader("User-Agent");
		String login_equipment = "";
		if (ua.indexOf("Android") > -1 || ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1) {
			login_equipment = "MOBILE";
		} else {
			login_equipment = "COM";
		}

		String shareStr = "";
		if ("".equals(str)) {
			return "front/schoolBulletin/list";
		}
		try {
			schoolBulletin.setId(Integer.valueOf(CryptographyUtils.staticdecryptStr(str)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		schoolBulletin = schoolBulletinService.getFrontData(schoolBulletin);
		schoolBulletinList = schoolBulletinService.getIndexList(schoolBulletin);
		schoolBulletinService.updateClickRate(schoolBulletin);
		shareStr = str;
		schoolBulletinContent.setSchool_bulletin_id(schoolBulletin.getId());
		schoolBulletinContentList = schoolBulletinContentService.getList(schoolBulletinContent);
		randomList = schoolBulletinService.getIndexList(schoolBulletin);
		for (int i = 0; i < randomList.size(); i++) {
			if (schoolBulletin.getId().equals(randomList.get(i).getId())) {
				randomList.remove(i);
			}
		}
		int randomNumber;
		if (randomList.size() < 3) {
			randomNumber = randomList.size();
		} else {
			randomNumber = 3;
		}
		Collections.shuffle(randomList);
		schoolBulletinRandomList = randomList.subList(0, randomNumber);
		prevList = schoolBulletinService.getPrevList(schoolBulletin);
		nextList = schoolBulletinService.getNextList(schoolBulletin);
		navBannerList = navBannerService.getFrontList(navBanner);
		navBannerType2List = navBannerService.getFrontType2List(navBanner);// 取300*600
		model.addAttribute("schoolBulletinList", schoolBulletinList)
			 .addAttribute("prevList", prevList)
			 .addAttribute("login_equipment", login_equipment)
			 .addAttribute("print_id", print_id)
			 .addAttribute("nextList", nextList)
			 .addAttribute("schoolBulletin", schoolBulletin)
			 .addAttribute("schoolBulletinRandomList", schoolBulletinRandomList)
			 .addAttribute("schoolBulletinContentList", schoolBulletinContentList)
			 .addAttribute("navBannerList", navBannerList)
			 .addAttribute("navBannerType2List", navBannerType2List)
			 .addAttribute("begin_date", schoolBulletin.getCreate_date())
			 .addAttribute("update_date", schoolBulletin.getUpdate_date())
			 .addAttribute("shareStr", shareStr);
		return "front/schoolBulletin/inside";
	}
	
	@PostMapping("/navBannerUpdateClickRate")
	public void navBannerUpdateClickRate(@RequestParam String id, NavBanner navBanner) {
		navBanner.setId(Integer.valueOf(id));
		navBannerService.updateClickRate(navBanner);
	}

	@PostMapping("/schoolBulletinMenu")
	@ResponseBody
	public String schoolBulletinMenu(SchoolBulletinCategory schoolBulletinCategory) throws IOException {
		List<SchoolBulletinCategory> schoolBulletinCategoryList = new ArrayList<>();

		schoolBulletinCategory.setParent_id(0);
		schoolBulletinCategory.setLayer("1");
		schoolBulletinCategoryList = schoolBulletinCategoryService.getLayerList("1", schoolBulletinCategory);

		JSONArray tJSONArray = new JSONArray(schoolBulletinCategoryList);
		return tJSONArray.toString();
	}
	
}
