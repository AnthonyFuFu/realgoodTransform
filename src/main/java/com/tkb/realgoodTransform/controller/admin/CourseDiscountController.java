package com.tkb.realgoodTransform.controller.admin;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.model.CourseDiscount;
import com.tkb.realgoodTransform.model.CourseDiscountCategory;
import com.tkb.realgoodTransform.model.CourseDiscountContent;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.CourseDiscountContentService;
import com.tkb.realgoodTransform.service.CourseDiscountService;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.UploadUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class CourseDiscountController extends BaseUtils {

	private int pageNo;
	@Value("${upload.file.path}")
	private String uploadFilePath; // 檔案上傳位置
	
	@Autowired
	private CourseDiscountService courseDiscountService;
	
	@Autowired
	private CourseDiscountContentService courseDiscountContentService;
	
	@Autowired
	private EditLogService editLogService;
	
	 /**
     * 清單頁面
     * @return
     */
	@RequestMapping(value = "/courseDiscount/index", method = {RequestMethod.GET, RequestMethod.POST})
	public String index (@ModelAttribute CourseDiscount courseDiscount, Model model,
			HttpServletRequest request, HttpSession session) {
		List<CourseDiscount>courseDiscountList = new ArrayList<>();
		pageNo = super.setPage(pageNo, request);
		pageTotalCount = courseDiscountService.getCount(courseDiscount);
		pageNo = super.pageSetting(pageNo);
		courseDiscountList = courseDiscountService.getList(pageCount, pageStart, courseDiscount);
		model.addAttribute("courseDiscountList", courseDiscountList);
		addModelAttribute(pageNo, model);
		return "admin/courseDiscount/courseDiscountList";
	}
	/**
     * 新增頁面
     * @return
     */
	@RequestMapping(value = "/courseDiscount/add", method = {RequestMethod.GET, RequestMethod.POST})
	public String add (@ModelAttribute CourseDiscountCategory courseDiscountCategory, Model model,
			@ModelAttribute CourseDiscount courseDiscount) {
		model.addAttribute("courseDiscountCategoryList", courseDiscountService.addfunction(courseDiscountCategory));
		return "admin/courseDiscount/courseDiscountForm";
	}
	/**
     * 新增資料
     * @return
     * @throws IOException
     */
	@RequestMapping(value = "/courseDiscount/addSubmit", method = {RequestMethod.POST})
	public String addSubmit (@ModelAttribute CourseDiscount courseDiscount,
			@SessionAttribute("userAccountSession") User user,HttpServletRequest request,
			@RequestParam("uploadIndex_image") MultipartFile image, @RequestParam("uploadPhoto") MultipartFile photo) throws IOException{
		courseDiscountService.addSubmitFunction(courseDiscount, image, photo, user);
		return "admin/courseDiscount/toMessagePage";
	}
	/**
     * 檢查首頁置頂次數
     * @return
     * @throws IOException 
     */ 
	@RequestMapping(value = "/courseDiscount/checkTopCount", method = {RequestMethod.GET})
	public ResponseEntity<?> checkTopCount (@ModelAttribute CourseDiscount courseDiscount) throws IOException{
		return courseDiscountService.checkTopCountFunction(courseDiscount);
	}
	/**
     * 修改頁面
     * @return
     */
	@RequestMapping(value = "/courseDiscount/update", method = {RequestMethod.POST, RequestMethod.GET})
	public String update (@ModelAttribute CourseDiscount courseDiscount
			,@ModelAttribute CourseDiscountCategory courseDiscountCategory,Model model) {
		model.addAttribute("courseDiscount", courseDiscountService.updateFunction(courseDiscount, courseDiscountCategory).get("courseDiscount"));
		model.addAttribute("courseDiscountCategoryList", courseDiscountService.updateFunction(courseDiscount, courseDiscountCategory).get("courseDiscountCategoryList"));
		return "admin/courseDiscount/courseDiscountForm";
	}
	/**
     * 修改資料
     * @return
     * @throws IOException
     */
	@RequestMapping(value = "/courseDiscount/updateSubmit", method = {RequestMethod.POST})
	public String updateSubmit (@ModelAttribute CourseDiscount courseDiscount,@RequestParam("uploadIndex_image") MultipartFile image,
			@RequestParam("uploadPhoto") MultipartFile photo,@SessionAttribute("userAccountSession") User user,HttpServletRequest request ) throws IOException{
		try {
			courseDiscountService.updateSubmitFunction(courseDiscount, image, photo, user, request);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(courseDiscount);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(courseDiscount.getId());
			editLog.setFunction("COURSE_DISCOUNT");
			editLog.setAction_type("UPDATE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			courseDiscount.setShow_message("修改成功");
		
		} catch(IOException ex) {
			ex.printStackTrace();
			courseDiscount.setShow_message("修改失敗，列表頁圖片限300x184px，首頁圖片限150x150px。");
		} catch (Exception e) {
			e.printStackTrace();
			courseDiscount.setShow_message("修改失敗");
		}
		return "admin/courseDiscount/toMessagePage";
	}
	 /**
     * 刪除
     * @return
     */
	@RequestMapping(value = "/courseDiscount/delete", method = {RequestMethod.POST})
	public String delete (@ModelAttribute CourseDiscount courseDiscount,
			@RequestParam("deleteList") String deleteList,@SessionAttribute("userAccountSession") User user) throws IOException{
		
		String[] deleteArray = deleteList.split(",");
		Integer id = null;
		for(int i =0; i<deleteArray.length;i++) {
			id = Integer.valueOf(deleteArray[i]);
			courseDiscount.setId(id);
			courseDiscount = courseDiscountService.getData(courseDiscount);
			
			String osName = System.getProperty("os.name").toLowerCase();
			if (osName.contains("win")) {
				UploadUtil.delete(uploadFilePath + "image/courseDiscount/indexImage/" + courseDiscount.getIndex_image());
				UploadUtil.delete(uploadFilePath + "image/courseDiscount/photo/" + courseDiscount.getPhoto());
			} else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
				UploadUtil.delete(uploadFilePath + "image/courseDiscount/indexImage/" + courseDiscount.getIndex_image());
				UploadUtil.delete(uploadFilePath + "image/courseDiscount/photo/" + courseDiscount.getPhoto());
			} else if (osName.contains("mac")) {
				UploadUtil.delete(uploadFilePath + "image/courseDiscount/indexImage/" + courseDiscount.getIndex_image());
				UploadUtil.delete(uploadFilePath + "image/courseDiscount/photo/" + courseDiscount.getPhoto());
			} else {
				UploadUtil.delete(uploadFilePath + "image/courseDiscount/indexImage/" + courseDiscount.getIndex_image());
				UploadUtil.delete(uploadFilePath + "image/courseDiscount/photo/" + courseDiscount.getPhoto());
			}
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(courseDiscount);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(courseDiscount.getId());
			editLog.setFunction("COURSE_DISCOUNT");
			editLog.setAction_type("DELETE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			courseDiscountService.delete(courseDiscount, id);
		}
		return "redirect:index";
	}
	
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/courseDiscount/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		getNormalBannerList = courseDiscountService.getNormalList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		
			for(Map<String, Object> map : getNormalBannerList) {
				CourseDiscount courseDiscount = new CourseDiscount();
				
				if(map.get("ID") != null) {
					courseDiscount.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("TITLE") != null) {
					courseDiscount.setTitle(map.get("TITLE").toString());
				}
				if(map.get("AREA") != null) {
					courseDiscount.setArea(map.get("AREA").toString());
				}
				if(map.get("CATEGORY") != null) {
					courseDiscount.setCategory(map.get("CATEGORY").toString());
				}
				if(map.get("IMAGE") != null) {
					courseDiscount.setImage(map.get("IMAGE").toString());
				}
				if(map.get("BEGIN_DATE") != null) {
					courseDiscount.setBegin_date(new Date(sdf.parse(map.get("BEGIN_DATE").toString()).getTime()));
				}
				if(map.get("CLICK_RATE") != null) {
					courseDiscount.setClick_rate(Integer.valueOf(map.get("CLICK_RATE").toString()));
				}
				if(map.get("END_DATE") != null) {
					courseDiscount.setEnd_date(new Date(sdf.parse(map.get("END_DATE").toString()).getTime()));
				}
				if(map.get("CREATE_BY") != null) {
					courseDiscount.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					courseDiscount.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					courseDiscount.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					courseDiscount.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("CONTENT") != null) {
					courseDiscount.setContent(map.get("CONTENT").toString());
				}
				if(map.get("PHOTO") != null) {
					courseDiscount.setPhoto(map.get("PHOTO").toString());
				}
				if(map.get("TYPE_ICON") != null) {
					courseDiscount.setType_icon(map.get("TYPE_ICON").toString());
				}
				if(map.get("TYPE_ICON_TEXT") != null) {
					courseDiscount.setType_icon_text(map.get("TYPE_ICON_TEXT").toString());
				}
				if(map.get("SHOW") != null) {
					courseDiscount.setShow(Integer.valueOf(map.get("SHOW").toString()));
				}
				if(map.get("EXAM_ANALYSIS") != null) {
					courseDiscount.setExam_analysis(map.get("EXAM_ANALYSIS").toString());
				}
				if(map.get("EXAM_ANALYSIS_LINK") != null) {
					courseDiscount.setExam_analysis_link(map.get("EXAM_ANALYSIS_LINK").toString());
				}
				if(map.get("GOODIES") != null) {
					courseDiscount.setGoodies(map.get("GOODIES").toString());
				}
				if(map.get("GIFTS") != null) {
					courseDiscount.setGifts(map.get("GIFTS").toString());
				}
				if(map.get("COURSE_DISCOUNT_TOP") != null) {
					courseDiscount.setCourse_discount_top(Integer.valueOf(map.get("COURSE_DISCOUNT_TOP").toString()));
				}
				if(map.get("INDEX_IMAGE") != null) {
					courseDiscount.setIndex_image(map.get("INDEX_IMAGE").toString());
				}
				if(map.get("EDM_TYPE_ID") != null) {
					courseDiscount.setEdm_type_id(map.get("EDM_TYPE_ID").toString());
				}
				if(map.get("EDM_URL") != null) {
					courseDiscount.setEdm_url(map.get("EDM_URL").toString());
				}
				if(map.get("PRODUCT_CATEGORY") != null) {
					courseDiscount.setProduct_category(map.get("PRODUCT_CATEGORY").toString());
				}
				courseDiscountService.updateNormalData(courseDiscount);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}

	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/courseDiscount/updateNormalData2")
	public String updateNormalData2() {
		
		List<Map<String, Object>> getCcourseDiscountContentList = new ArrayList<>();
		getCcourseDiscountContentList = courseDiscountContentService.getNormalList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		
			for(Map<String, Object> map : getCcourseDiscountContentList) {
				CourseDiscountContent courseDiscountContent = new CourseDiscountContent();
				
				if(map.get("ID") != null) {
					courseDiscountContent.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("COURSE_DISCOUNT_ID") != null) {
					courseDiscountContent.setCourse_discount_id(Integer.valueOf(map.get("COURSE_DISCOUNT_ID").toString()));
				}
				if(map.get("ICON") != null) {
					courseDiscountContent.setIcon(map.get("ICON").toString());
				}
				if(map.get("TITLE") != null) {
					courseDiscountContent.setTitle(map.get("TITLE").toString());
				}
				if(map.get("CONTENT") != null) {
					courseDiscountContent.setContent(map.get("CONTENT").toString());
				}
				if(map.get("CREATE_BY") != null) {
					courseDiscountContent.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					courseDiscountContent.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					courseDiscountContent.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					courseDiscountContent.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("IMAGE") != null) {
					courseDiscountContent.setImage(map.get("IMAGE").toString());
				}
				courseDiscountContentService.updateNormalData(courseDiscountContent);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
}
