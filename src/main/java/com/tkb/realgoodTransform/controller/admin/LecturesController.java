package com.tkb.realgoodTransform.controller.admin;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.tkb.realgoodTransform.model.Lectures;
import com.tkb.realgoodTransform.model.LecturesCategory;
import com.tkb.realgoodTransform.model.LecturesContent;
import com.tkb.realgoodTransform.model.LecturesPlace;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.LecturesCategoryService;
import com.tkb.realgoodTransform.service.LecturesContentService;
import com.tkb.realgoodTransform.service.LecturesPlaceService;
import com.tkb.realgoodTransform.service.LecturesService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class LecturesController extends BaseUtils {

	private int pageNo;
	@Autowired
	private LecturesService lecturesService;
	@Autowired
	private LecturesCategoryService lecturesCategoryService;
	@Autowired
	private LecturesContentService lecturesContentService;
	@Autowired
	private LecturesPlaceService lecturesPlaceService;
	
	/**
	 * 清單頁面
	 * @return
	 */
	@RequestMapping("/lectures/index")
	public String index (@ModelAttribute Lectures lectures, LecturesCategory lecturesCategory,
			Model model, HttpServletRequest request, HttpSession session) {
		List<Lectures>lecturesList = new ArrayList<>();
		List<LecturesCategory>lecturesCategoryList = new ArrayList<>();
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		String searchtype = request.getParameter("searchtype") == null ? null : request.getParameter("searchtype");
		String searchCategoryParam = request.getParameter("searchCategory");
		String searchCategory = (searchCategoryParam == null || searchCategoryParam.isEmpty()) ? "0" : searchCategoryParam;

		if(title!=null) {
			lectures.setTitle(title);
		}
		if(searchCategory!=null && !searchCategory.equals("0")) {
			lectures.setCategory(searchCategory);
		}
		lecturesCategory.setParent_id(0);
		lecturesCategoryList = lecturesCategoryService.getLayerList("1", lecturesCategory);
		
		if (pageNo != 0) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();
		pageTotalCount = lecturesService.getCount(lectures);
		pageNo = super.pageSetting(pageNo);
		
		lecturesList = lecturesService.getList(pageCount, pageStart, lectures);
		model.addAttribute("lecturesCategoryList", lecturesCategoryList)
			 .addAttribute("lecturesList", lecturesList)
			 .addAttribute("searchtype", searchtype)
			 .addAttribute("searchCategory", Integer.parseInt(searchCategory));
		addModelAttribute(pageNo, model);
		return "admin/lectures/lecturesList";
	}
	

	
	/**
	 * 新增頁面
	 * @return
	 */
	@RequestMapping("/lectures/add")
	public String add (LecturesCategory lecturesCategory,LecturesPlace lecturesPlace, Model model,HttpServletRequest request) {
		Lectures lectures = new Lectures();
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		String searchtype = request.getParameter("searchtype") == null ? null : request.getParameter("searchtype");
		String searchCategoryParam = request.getParameter("searchCategory");
		String searchCategory = (searchCategoryParam == null || searchCategoryParam.isEmpty()) ? "0" : searchCategoryParam;
		
		model.addAttribute("lecturesCategoryList", lecturesService.add(lecturesCategory, lecturesPlace, lectures))
			 .addAttribute("lectures", lectures)
			 .addAttribute("searchTitle", title)
		     .addAttribute("searchtype", searchtype)
			 .addAttribute("searchCategory",searchCategory)
			 .addAttribute("pageNo",pageNo);
		return "admin/lectures/lecturesForm";
	}
	
	/**
	 * 新增資料
	 * @return
	 * @throws IOException
	 * @throws ParseException 
	 */
	@PostMapping("/lectures/addSubmit")
	public String addSubmit (Lectures lectures, LecturesPlace lecturesPlace,LecturesContent lecturesContent, @SessionAttribute("userAccountSession") User user,Model model,HttpServletRequest request) throws IOException,ParseException{
		Integer pageNo = 1 ;
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		String searchtype = request.getParameter("searchtype") == null ? null : request.getParameter("searchtype");
		String searchCategoryParam = request.getParameter("searchCategory");
		String searchCategory = (searchCategoryParam == null || searchCategoryParam.isEmpty()) ? "0" : searchCategoryParam;
		lecturesService.addSubmit(lectures, lecturesContent, lecturesPlace, user, request,model);
		model.addAttribute("searchtype", lectures.getCategory())
	    	 .addAttribute("title", title)
	    	 .addAttribute("searchtype", searchtype)
	    	 .addAttribute("searchCategory", searchCategory)
	    	 .addAttribute("pageNo",pageNo);
		return "admin/lectures/toList";
	}
	
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping("/lectures/update")
	public String update(Lectures lectures, LecturesCategory lecturesCategory,LecturesContent lecturesContent,
			LecturesPlace lecturesPlace, Model model,HttpServletRequest request) {
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		String searchtype = request.getParameter("searchtype") == null ? null : request.getParameter("searchtype");
		String searchCategoryParam = request.getParameter("searchCategory");
		String searchCategory = (searchCategoryParam == null || searchCategoryParam.isEmpty()) ? "0" : searchCategoryParam;
		
		Map<String, Object> map = lecturesService.update(lectures, lecturesCategory, lecturesContent, lecturesPlace);
		model.addAttribute("lectures", map.get("lectures"))
		     .addAttribute("lecturesCategoryList", map.get("lecturesCategoryList"))
		     .addAttribute("lecturesContentList", map.get("lecturesContentList"))
		     .addAttribute("lecturesPlaceList", map.get("lecturesPlaceList"))
		     .addAttribute("lecturesCategory", map.get("lecturesCategory"))
		     .addAttribute("searchTitle", title)
		     .addAttribute("searchtype", searchtype)
			 .addAttribute("searchCategory",searchCategory)
			 .addAttribute("pageNo",pageNo);
		return "admin/lectures/lecturesForm";
	}
	
	
	/**
	 * 修改資料
	 * @return
	 * @throws IOException
	 * @throws ParseException 
	 */
	@PostMapping("/lectures/updateSubmit")
	public String updateSubmit(Lectures lectures, LecturesContent lecturesContent,LecturesPlace lecturesPlace, User user, Model model, HttpServletRequest request) throws IOException, ParseException{
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
			String searchtype = request.getParameter("searchtype") == null ? null : request.getParameter("searchtype");
			String searchCategoryParam = request.getParameter("searchCategory");
			String searchCategory = (searchCategoryParam == null || searchCategoryParam.isEmpty()) ? "0" : searchCategoryParam;
			lecturesService.updateSubmit(lectures, lecturesContent, lecturesPlace, user, request,model);
			model.addAttribute("searchtype", lectures.getCategory())
		    	 .addAttribute("title", title)
		    	 .addAttribute("searchtype", searchtype)
		    	 .addAttribute("searchCategory", searchCategory)
		    	 .addAttribute("pageNo",pageNo);
		return "admin/lectures/toList";
	}
	
	/**
	 * 刪除百官公告
	 * @return
	 */
	@RequestMapping("/lectures/delete")
	public String delete (Lectures lectures, LecturesContent lecturesContent, LecturesPlace lecturesPlace,
			HttpServletRequest request, @SessionAttribute("userAccountSession") User user,Model model) throws IOException{
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		String searchtype = request.getParameter("searchtype") == null ? null : request.getParameter("searchtype");
		String searchCategoryParam = request.getParameter("searchCategory");
		String searchCategory = (searchCategoryParam == null || searchCategoryParam.isEmpty()) ? "0" : searchCategoryParam;
		lecturesService.delete(lectures, lecturesContent, lecturesPlace, request, user);
		model.addAttribute("message", "刪除成功")
		     .addAttribute("title", title)
		     .addAttribute("searchtype", searchtype)
			 .addAttribute("searchCategory",searchCategory)
			 .addAttribute("pageNo",pageNo);
		return "admin/lectures/toList";
	}
	
	/**
	 * 檢查置頂次數
	 * @return
	 * @throws IOException 
	 */	
	@GetMapping("/lectures/checkTopCount")
	public ResponseEntity<?>checkTopCount(Lectures lectures)throws IOException{
		return lecturesService.checkTopCountFunction(lectures);
	}
	
	
//	========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/lectures/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalLecturesList = new ArrayList<>();
		getNormalLecturesList = lecturesService.getNormalList();
		
		List<Map<String, Object>> getNormalLectureContentList = new ArrayList<>();
		getNormalLectureContentList = lecturesContentService.getNormalList();
		
		List<Map<String, Object>> getNormalLectruePlaceList = new ArrayList<>();
		getNormalLectruePlaceList = lecturesPlaceService.getNormalList();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		
			//Lectures這張table資料更新
			for(Map<String, Object> map : getNormalLecturesList) {
				Lectures lectures = new Lectures();
				
				if(map.get("ID") != null) {
					lectures.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("TITLE") != null) {
					lectures.setTitle(map.get("TITLE").toString());
				}
				if(map.get("AREA") != null) {
					lectures.setArea(map.get("AREA").toString());
				}
				if(map.get("CATEGORY") != null) {
					lectures.setCategory(map.get("CATEGORY").toString());
				}
				if(map.get("IMAGE") != null) {
					lectures.setImage(map.get("IMAGE").toString());
				}
				if(map.get("BEGIN_DATE") != null) {
					lectures.setBegin_date(new Date(sdf.parse(map.get("BEGIN_DATE").toString()).getTime()));
				}
				if(map.get("END_DATE") != null) {
					lectures.setEnd_date(new Date(sdf.parse(map.get("END_DATE").toString()).getTime()));
				}
				if(map.get("CLICK_RATE") != null) {
					lectures.setClick_rate(Integer.valueOf(map.get("CLICK_RATE").toString()));
				}
				if(map.get("CREATE_BY") != null) {
					lectures.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					lectures.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					lectures.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					lectures.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("CONTENT") != null) {
					lectures.setContent(map.get("CONTENT").toString());
				}
				if(map.get("FARE") != null) {
					lectures.setFare(map.get("FARE").toString());
				}
				if(map.get("PLACE") != null) {
					lectures.setPlace(map.get("PLACE").toString());
				}
				if(map.get("PHONE") != null) {
					lectures.setPhone(map.get("PHONE").toString());
				}
				if(map.get("FARE_MONEY") != null) {
					lectures.setFare_money(map.get("FARE_MONEY").toString());
				}
				if(map.get("LECTURES_TOP") != null) {
					lectures.setLectures_top(map.get("LECTURES_TOP").toString());
				}
				if(map.get("SEO_CONTENT") != null) {
					lectures.setSeo_content(map.get("SEO_CONTENT").toString());
				}
				if(map.get("LECTURE_TYPE_ID") != null) {
					lectures.setLecture_type_id(Integer.valueOf(map.get("LECTURE_TYPE_ID").toString()));
				}
				if(map.get("ENCRYPTURL") != null) {
					lectures.setEncrypturl(map.get("ENCRYPTURL").toString());
				}
				if(map.get("PRODUCT_CATEGORY") != null) {
					lectures.setProduct_category(map.get("PRODUCT_CATEGORY").toString());
				}
				lecturesService.updateNormalData(lectures);
				
			}
			
			
			//LecturesContent這張table資料更新
			for(Map<String, Object> map : getNormalLectureContentList) {
				LecturesContent lecturesContent = new LecturesContent();
				
				if(map.get("ID") != null) {
					lecturesContent.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("LECTURES_ID") != null) {
					lecturesContent.setLectures_id(Integer.valueOf(map.get("LECTURES_ID").toString()));
				}
				if(map.get("ICON") != null) {
					lecturesContent.setIcon(map.get("ICON").toString());
				}
				if(map.get("TITLE") != null) {
					lecturesContent.setTitle(map.get("TITLE").toString());
				}
				if(map.get("CONTENT") != null) {
					lecturesContent.setContent(map.get("CONTENT").toString());
				}
				if(map.get("CREATE_BY") != null) {
					lecturesContent.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					lecturesContent.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					lecturesContent.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					lecturesContent.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				lecturesContentService.updateNormalData(lecturesContent);
				
			}
			
			
			//LecturesPlace這張table資料更新
			for(Map<String, Object> map : getNormalLectruePlaceList) {
				LecturesPlace lecturesPlace = new LecturesPlace();
				
				if(map.get("ID") != null) {
					lecturesPlace.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("LECTURES_ID") != null) {
					lecturesPlace.setLectures_id(Integer.valueOf(map.get("LECTURES_ID").toString()));
				}
				if(map.get("PLACENAME") != null) {
					lecturesPlace.setPlaceName(map.get("PLACENAME").toString());
				}
				if(map.get("PLACEDAY") != null) {
					lecturesPlace.setPlaceDay(map.get("PLACEDAY").toString());
				}
				if(map.get("CREATE_BY") != null) {
					lecturesPlace.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					lecturesPlace.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					lecturesPlace.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					lecturesPlace.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("PLACEEVENT") != null) {
					lecturesPlace.setPlaceEvent(map.get("PLACEEVENT").toString());
				}
				if(map.get("PLACE_ADDRESS") != null) {
					lecturesPlace.setPlace_address(map.get("PLACE_ADDRESS").toString());
				}
				lecturesPlaceService.updateNormalData(lecturesPlace);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
	
	
	
}
