package com.tkb.realgoodTransform.controller.front;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.tkb.realgoodTransform.model.NavBanner;
import com.tkb.realgoodTransform.model.NewExam;
import com.tkb.realgoodTransform.model.NewExamCategory;
import com.tkb.realgoodTransform.model.NewExamContent;
import com.tkb.realgoodTransform.service.NavBannerService;
import com.tkb.realgoodTransform.service.NewExamCategoryService;
import com.tkb.realgoodTransform.service.NewExamContentService;
import com.tkb.realgoodTransform.service.NewExamService;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.CryptographyUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FrontNewExamController extends BaseUtils{
	
	private int pageNo;
	private String shareStr;
	
	@Autowired
	private NewExamService newExamService;
	
	@Autowired
	private NewExamCategoryService newExamCategoryService;
	
	@Autowired
	private NewExamContentService newExamContentService;
	
	@Autowired
	private NavBannerService navBannerService;
	
	@RequestMapping("/newExam/index")
	public String index(NewExam newExam, NewExamCategory newExamCategory, Model model, HttpServletRequest request,@RequestParam(name="newExam.category",required = false)String category) {
		List<NewExam> newExamList = new ArrayList<>();
		List<NewExamCategory> newExamCategoryList = new ArrayList<>();
		String sort = request.getParameter("sort") == null ? "" : request.getParameter("sort");
		String pageCountStr = request.getParameter("page_count") == null ? "" : request.getParameter("page_count");

		
		newExamCategory.setParent_id(0);
		newExamCategoryList = newExamCategoryService.getLayerList("1", newExamCategory);
		pageTotalCount = newExamService.getFrontCount(newExam);
		if (!"".equals(pageCountStr)) {
			super.pageCount = Integer.valueOf(pageCountStr);
		} else {
			super.pageCount = 8;
		}
		if(category != null && !"".equals(category)) {
			newExam.setCategory(category);
		}
		pageNo = super.pageSetting(pageNo);
		newExamList = newExamService.getFrontList(pageCount, pageStart, newExam, sort);
		addModelAttribute(pageNo, model);
		model.addAttribute("newExamList", newExamList)
			 .addAttribute("newExamCategoryList", newExamCategoryList)
			 .addAttribute("category", category);
		return "front/newExam/list";
	}

	@PostMapping("/newExam/toSearch")
	public ResponseEntity<?> toSearch(NewExam newExam, NewExamCategory newExamCategory, HttpServletRequest request,
			Model model) throws IOException {
		String category_id = request.getParameter("category_id") == "" ? "" : request.getParameter("category_id");
		String searchSort = request.getParameter("searchSort") == null ? "" : request.getParameter("searchSort");
		String pageCountStr = request.getParameter("page_count") == null ? "" : request.getParameter("page_count");
		String page_no = request.getParameter("page_no") == null ? "" : request.getParameter("page_no");
		String date = request.getParameter("date") == null ? "" : request.getParameter("date");
		String week_begin_date = "", week_end_date = "";

		super.resetPage();
		List<NewExam> newExamList = new ArrayList<>();
		if (!"".equals(category_id)) {
			newExam.setCategory(category_id);
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		week_begin_date = sdf.format(getFirstDayOfWeek(cal.getTime()));
		week_end_date = sdf.format(getLastDayOfWeek(cal.getTime()));

		newExam.setDate(date);
		newExam.setWeek_begin_date(week_begin_date);
		newExam.setWeek_end_date(week_end_date);

		if (!"".equals(pageCountStr)) {
			super.pageCount = Integer.valueOf(pageCountStr);
		} else {
			super.pageCount = 8;
		}
		pageTotalCount = newExamService.getFrontCount(newExam);
		pageNo = super.pageSetting(Integer.valueOf(page_no));
		newExamList = newExamService.getFrontList(pageCount, pageStart, newExam, searchSort);
		if (newExamList.size() > 0) {
			newExamList.get(0).setPageCount(pageCount);
			newExamList.get(0).setPageTotalCount(pageTotalCount);
			newExamList.get(0).setTotalPage(totalPage);

			newExamList.get(0).setLeftStartPage(leftStartPage);
			newExamList.get(0).setLeftEndPage(leftEndPage);
			newExamList.get(0).setRightStartPage(rightStartPage);
			newExamList.get(0).setRightEndPage(rightEndPage);
			newExamList.get(0).setLeftPageNum(leftPageNum);
			newExamList.get(0).setRightPageNum(rightPageNum);
		}
		addModelAttribute(pageNo, model);
		return new ResponseEntity<>(newExamList, HttpStatus.OK);
	}

	@RequestMapping("/newExam/toEncrypt")
	@ResponseBody
	public String toEncrypt(HttpServletRequest request) throws IOException {
		
		int id = request.getParameter("id") == null ? 0 : Integer.valueOf(request.getParameter("id"));
		return CryptographyUtils.encryptStr(String.valueOf(id));
		
	}

	@PostMapping("/newExam/newExamMenu")
	@ResponseBody
	public String newExamMenu(NewExamCategory newExamCategory) throws IOException {
		List<NewExamCategory> newExamCategoryList = new ArrayList<>();

		newExamCategory.setParent_id(0);
		newExamCategoryList = newExamCategoryService.getLayerList("1", newExamCategory);

		JSONArray tJSONArray = new JSONArray(newExamCategoryList);
		return tJSONArray.toString();
	}
	
	
	@GetMapping("/newExam/inside")
	public String inside(@RequestParam String str, NewExam newExam,NewExamContent newExamContent,Model model,NavBanner navBanner,HttpServletRequest request) {
		List<NewExam>newExamRandomList = new ArrayList<>();
		List<NewExamContent>newExamContentList = new ArrayList<>();
		List<NavBanner>navBannerList = new ArrayList<>();
		List<NavBanner>navBannerType2List = new ArrayList<>();
		//取得PO版人
			String print_id = request.getParameter("print_id");
			//判斷瀏覽器
			String ua = request.getHeader("User-Agent");
			String login_equipment = "";
			if(ua.indexOf("Android")>-1||ua.indexOf("iPhone")>-1||ua.indexOf("iPad")>-1) {
				login_equipment = "MOBILE";
			}else{
				login_equipment = "COM";
			}
		
		if("".equals(str)) {
			return "front/newExam/list";
		}
		try {
			newExam.setId(Integer.valueOf(CryptographyUtils.staticdecryptStr(str)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		newExam = newExamService.getFrontData(newExam);
		newExamService.updateClickRate(newExam);
		shareStr = str;
		newExamContent.setNewExam_id(newExam.getId());
		newExamContentList = newExamContentService.getList(newExamContent);
		newExamRandomList = newExamService.getFrontList(newExam);
		
		navBannerList = navBannerService.getFrontList(navBanner);
		navBannerType2List = navBannerService.getFrontType2List(navBanner);
		model.addAttribute("shareStr", shareStr).addAttribute("newExam", newExam)
		     .addAttribute("newExamContentList", newExamContentList)
		     .addAttribute("newExam", newExam)
		     .addAttribute("begin_date", newExam.getBegin_date())
		     .addAttribute("update_date", newExam.getUpdate_date())
		     .addAttribute("newExamRandomList", newExamRandomList)
		     .addAttribute("navBannerList", navBannerList)
		     .addAttribute("navBannerType2List", navBannerType2List)
		     .addAttribute("login_equipment", login_equipment)
		     .addAttribute("print_id", print_id);
		return "front/newExam/inside";
	}
	/**
	 * 取得當前日期所在週的第一天
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
	
}
