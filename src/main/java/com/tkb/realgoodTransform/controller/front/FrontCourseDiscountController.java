package com.tkb.realgoodTransform.controller.front;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tkb.realgoodTransform.model.CourseDiscount;
import com.tkb.realgoodTransform.model.CourseDiscountBanner;
import com.tkb.realgoodTransform.model.CourseDiscountCategory;
import com.tkb.realgoodTransform.model.CourseDiscountContent;
import com.tkb.realgoodTransform.service.CourseDiscountBannerService;
import com.tkb.realgoodTransform.service.CourseDiscountCategoryService;
import com.tkb.realgoodTransform.service.CourseDiscountContentService;
import com.tkb.realgoodTransform.service.CourseDiscountService;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.CryptographyUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FrontCourseDiscountController extends BaseUtils {
	
	private int pageNo; // 頁碼
	private String shareStr;
	
	@Autowired
	private CourseDiscountService courseDiscountService;
	
	@Autowired
	private CourseDiscountCategoryService courseDiscountCategoryService;
	
	@Autowired
	private CourseDiscountBannerService courseDiscountBannerService;
	
	@Autowired
	private CourseDiscountContentService courseDiscountContentService;
	
	@RequestMapping("/courseDiscount/index")
	public String index(HttpServletRequest request, CourseDiscount courseDiscount,
			CourseDiscountCategory courseDiscountCategory, Model model, CourseDiscountBanner courseDiscountBanner) {
		List<CourseDiscountCategory> courseDiscountCategoryList = new ArrayList<>();
		List<CourseDiscount> courseDiscountList = new ArrayList<>();
		List<CourseDiscountBanner> courseDiscountBannerList = new ArrayList<>();
		String sort = request.getParameter("sort") == null ? "" : request.getParameter("sort");
		String pageCountStr = request.getParameter("page_count") == null ? "" : request.getParameter("page_count");
		courseDiscountCategory.setParent_id(0);
		courseDiscountCategoryList = courseDiscountCategoryService.getLayerList("1", courseDiscountCategory);

		pageTotalCount = courseDiscountService.getFrontCount(courseDiscount);
		if (!"".equals(pageCountStr)) {
			super.pageCount = Integer.valueOf(pageCountStr);
		} else {
			super.pageCount = 8;
		}
		pageNo = super.pageSetting(pageNo);
		courseDiscountList = courseDiscountService.getFrontList(pageCount, pageStart, courseDiscount, sort);
		courseDiscountBannerList = courseDiscountBannerService.getFrontList(courseDiscountBanner);

		model.addAttribute("courseDiscountCategoryList", courseDiscountCategoryList)
				.addAttribute("courseDiscountList", courseDiscountList)
				.addAttribute("courseDiscountBannerList", courseDiscountBannerList);
		addModelAttribute(pageNo, model);
		return "front/courseDiscount/list";
	}
	
	@RequestMapping("/courseDiscount/inside")
	public String inside(@RequestParam String str,Model model,
			CourseDiscount courseDiscount, CourseDiscountContent courseDiscountContent,HttpServletRequest request) {
		
		List<CourseDiscount> courseDiscountList = new ArrayList<>();
		List<CourseDiscount> courseDiscountRandomList = new ArrayList<>();
		List<CourseDiscountContent> courseDiscountContentList = new ArrayList<>();
		
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
			return "front/courseDiscount/list";
		}
		
		try {
			courseDiscount.setId(Integer.valueOf(CryptographyUtils.staticdecryptStr(str)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		courseDiscount = courseDiscountService.getFrontData(courseDiscount);
		courseDiscountList = courseDiscountService.getFrontList(courseDiscount);
		courseDiscountService.updateClickRate(courseDiscount);
		shareStr = str;
		
		Integer own = 0;

		for (int i = 0; i < courseDiscountList.size(); i++) {
			if (courseDiscountList.get(i).getId().equals(courseDiscount.getId())) {
				own = courseDiscountList.get(i).getRownum();
			}
		}
		
		if (own - 2 >= 0) {
			courseDiscount.setPrev_courseDiscount(courseDiscountList.get(own - 2));
		} else {
			courseDiscount.setPrev_courseDiscount(null);
		}

		if (own < courseDiscountList.size()) {
			courseDiscount.setNext_courseDiscount(courseDiscountList.get(own));
		} else {
			courseDiscount.setNext_courseDiscount(null);
		}

		courseDiscountContent.setCourse_discount_id(courseDiscount.getId());
		courseDiscountContentList = courseDiscountContentService.getList(courseDiscountContent);
		courseDiscountRandomList = courseDiscountService.getFrontList(courseDiscount);
		
		model.addAttribute("shareStr", shareStr).addAttribute("courseDiscount", courseDiscount)
				.addAttribute("courseDiscountList", courseDiscountList)
				.addAttribute("courseDiscountContentList", courseDiscountContentList)
				.addAttribute("courseDiscountRandomList", courseDiscountRandomList)
				.addAttribute("begin_date", courseDiscount.getBegin_date())
				.addAttribute("update_date", courseDiscount.getUpdate_date())
				.addAttribute("login_equipment", login_equipment)
				.addAttribute("print_id", print_id);
		
		addModelAttribute(pageNo, model);
		return "front/courseDiscount/inside";
	}

	@GetMapping("/courseDiscount/toSearch")
	public ResponseEntity<?> toSearch(HttpServletRequest request, CourseDiscount courseDiscount,
			CourseDiscountCategory courseDiscountCategory, Model model) {
		List<CourseDiscount> courseDiscountList = new ArrayList<>();
		String category_id = request.getParameter("category_id") == "" ? "" : request.getParameter("category_id");
		String searchSort = request.getParameter("searchSort") == null ? "" : request.getParameter("searchSort");
		String pageCountStr = request.getParameter("page_count") == null ? "" : request.getParameter("page_count");
		String page_no = request.getParameter("page_no") == null ? "" : request.getParameter("page_no");
		String date = request.getParameter("date") == null ? "" : request.getParameter("date");
		String week_begin_date = "", week_end_date = "";
		super.resetPage();
		if (!"".equals(category_id)) {
			courseDiscount.setCategory(category_id);
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		week_begin_date = sdf.format(getFirstDayOfWeek(cal.getTime()));
		week_end_date = sdf.format(getLastDayOfWeek(cal.getTime()));

		courseDiscount.setDate(date);
		courseDiscount.setWeek_begin_date(week_begin_date);
		courseDiscount.setWeek_end_date(week_end_date);

		if (!"".equals(pageCountStr)) {
			super.pageCount = Integer.valueOf(pageCountStr);
		} else {
			super.pageCount = 8;
		}

		pageTotalCount = courseDiscountService.getFrontCount(courseDiscount);
		pageNo = super.pageSetting(Integer.valueOf(page_no));
		courseDiscountList = courseDiscountService.getFrontList(pageCount, pageStart, courseDiscount, searchSort);

		if (courseDiscountList.size() > 0) {
			courseDiscountList.get(0).setPageCount(pageCount);
			courseDiscountList.get(0).setPageTotalCount(pageTotalCount);
			courseDiscountList.get(0).setTotalPage(totalPage);

			courseDiscountList.get(0).setLeftStartPage(leftStartPage);
			courseDiscountList.get(0).setLeftEndPage(leftEndPage);
			courseDiscountList.get(0).setRightStartPage(rightStartPage);
			courseDiscountList.get(0).setRightEndPage(rightEndPage);
			courseDiscountList.get(0).setLeftPageNum(leftPageNum);
			courseDiscountList.get(0).setRightPageNum(rightPageNum);
		}
		addModelAttribute(pageNo, model);
		return new ResponseEntity<>(courseDiscountList, HttpStatus.OK);
	}

	@RequestMapping("/courseDiscount/toEncrypt")
	@ResponseBody
	public String toEncrypt(HttpServletRequest request) throws IOException {
		
		int id = request.getParameter("id") == null ? 0 : Integer.valueOf(request.getParameter("id"));
		return CryptographyUtils.encryptStr(String.valueOf(id));
		
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
}
