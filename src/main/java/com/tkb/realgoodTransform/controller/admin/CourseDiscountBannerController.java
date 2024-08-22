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
import com.tkb.realgoodTransform.model.CourseDiscountBanner;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.CourseDiscountBannerService;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.UploadUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class CourseDiscountBannerController extends BaseUtils{
	
	private int pageNo;
	@Value("${upload.file.path}")
	private String uploadFilePath; // 檔案上傳位置
	
	@Autowired
	private CourseDiscountBannerService  courseDiscountBannerService;
	
	@Autowired
	private EditLogService editLogService;
	
	/**
	 * 清單頁面
	 * @return
	 */
	@RequestMapping(value = "/courseDiscountBanner/index", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(@ModelAttribute CourseDiscountBanner courseDiscountBanner, Model model, HttpServletRequest request,
			HttpSession session) {
		List<CourseDiscountBanner> courseDiscountBannerList = new ArrayList<>();
		
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		if(title!=null) {
			courseDiscountBanner.setTitle(title);
		}
		
		if (pageNo != 0) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();

		pageTotalCount = courseDiscountBannerService.getCount(courseDiscountBanner);
		pageNo = super.pageSetting(pageNo);
		courseDiscountBannerList = courseDiscountBannerService.getList(pageCount, pageStart, courseDiscountBanner);
		
		model.addAttribute("courseDiscountBannerList", courseDiscountBannerList).addAttribute("pageNo", pageNo)
		.addAttribute("pageTotalCount", pageTotalCount).addAttribute("pageCount", pageCount)
		.addAttribute("totalPage", totalPage).addAttribute("leftStartPage", leftStartPage)
		.addAttribute("leftPageNum", leftPageNum).addAttribute("rightPageNum", rightPageNum)
		.addAttribute("leftEndPage", leftEndPage).addAttribute("rightStartPage", rightStartPage)
		.addAttribute("rightEndPage", rightEndPage);
		
		return "admin/courseDiscountBanner/courseDiscountBannerList";
	}
	/**
	 * 新增頁面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/courseDiscountBanner/add", method = {RequestMethod.GET, RequestMethod.POST})
	public String add(@ModelAttribute CourseDiscountBanner courseDiscountBanner, Model model,HttpServletRequest request) {
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		
		model.addAttribute("searchTitle", title)
		.addAttribute("pageNo",pageNo);
		
		return "admin/courseDiscountBanner/courseDiscountBannerForm";
	}
	/**
	 * 新增資料
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/courseDiscountBanner/addSubmit", method = {RequestMethod.POST})
	public String addSubmit (CourseDiscountBanner courseDiscountBanner, Model model, @SessionAttribute("userAccountSession") User user,
			@RequestParam("uploadImage") MultipartFile image,HttpServletRequest request) throws IOException{
		UploadUtil uploadUtil = new UploadUtil();
		String checkStatus = "T";
		
		Integer pageNo = 1 ;
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		
		if(image != null){
			if(super.checkImageWidth(image, 1100)){
			}else{
				checkStatus = "F";
			}
			if(super.checkImageHeight(image, 380)){
			}else{
				checkStatus = "F";
			}		
		}
		if("T".equals(checkStatus)){
			int id = courseDiscountBannerService.getNextId();
			courseDiscountBanner.setId(id);
			try {
			courseDiscountBanner.setImage(uploadUtil.uploadv2(uploadFilePath + "image/courseDiscountBanner/" , image, courseDiscountBanner.getId()));		
			}catch(Exception e) {
				checkStatus = "E";
			}
			courseDiscountBanner.setCreate_by(user.getAccount());
			courseDiscountBanner.setUpdate_by(user.getAccount());
			courseDiscountBannerService.add(courseDiscountBanner);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(courseDiscountBanner);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(courseDiscountBanner.getId());
			editLog.setFunction("COURSE_DISCOUNT_BANNER");
			editLog.setAction_type("ADD");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","新增成功");
		}else if("F".equals(checkStatus)) {
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","新增失敗,請檢查圖片規格,尺寸");
		}else {
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","新增失敗");
		}
		return "admin/courseDiscountBanner/toList";
	}
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping(value = "/courseDiscountBanner/update", method = { RequestMethod.GET, RequestMethod.POST })
	public String update(Model model, @ModelAttribute CourseDiscountBanner courseDiscountBanner,HttpServletRequest request) {
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		
		courseDiscountBanner = courseDiscountBannerService.getData(courseDiscountBanner);
		courseDiscountBanner.setCount(courseDiscountBannerService.getFrontCount(courseDiscountBanner));
		
		model.addAttribute(courseDiscountBanner)
		.addAttribute("searchTitle", title)
		.addAttribute("pageNo",pageNo);
		
		return "admin/courseDiscountBanner/courseDiscountBannerForm";
	}
	/**
	 * 修改資料
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/courseDiscountBanner/updateSubmit", method = { RequestMethod.POST })
	public String updateSubmit(CourseDiscountBanner courseDiscountBanner, Model model, @SessionAttribute("userAccountSession") User user,
			@RequestParam("uploadImage") MultipartFile image, HttpServletRequest request) throws IOException {
		UploadUtil uploadUtil = new UploadUtil();
		String checkStatus = "T";
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		
		String deleteImage = request.getParameter("image");
		if(image.getBytes().length>0){
			if(super.checkImageWidth(image, 1100)){
			}else{
				checkStatus = "F";
			}
			if(super.checkImageHeight(image, 380)){
			}else{
				checkStatus = "F";
			}		
		}
		if("T".equals(checkStatus)){
			if (image.getBytes().length > 0 && deleteImage != null) {
				// UploadUtil.delete(uploadFilePath + "image/navBanner/" + deleteImage);
				UploadUtil.delete(uploadFilePath + "image/courseDiscountBanner/" + deleteImage);
			try {
			courseDiscountBanner.setImage(uploadUtil.uploadv2(uploadFilePath + "image/courseDiscountBanner/" , image, courseDiscountBanner.getId()));		
			}catch(Exception e) {
				checkStatus = "E";
			}
			}
			courseDiscountBanner.setCreate_by(user.getAccount());
			courseDiscountBanner.setUpdate_by(user.getAccount());
			courseDiscountBannerService.update(courseDiscountBanner);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(courseDiscountBanner);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(courseDiscountBanner.getId());
			editLog.setFunction("COURSE_DISCOUNT_BANNER");
			editLog.setAction_type("UPDATE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","修改成功");
		}else if("F".equals(checkStatus)) {
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","修改失敗,請檢查圖片規格,尺寸");
		}else {
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","修改失敗");
		}
		return "admin/courseDiscountBanner/toList";
	}
	/**
	 * 刪除
	 * @return
	 */
	@RequestMapping(value = "/courseDiscountBanner/delete", method = {RequestMethod.POST})
	public String delete(HttpServletRequest request,CourseDiscountBanner courseDiscountBanner,@SessionAttribute("userAccountSession") User user,Model model) throws IOException {
		String deleteString = request.getParameter("deleteList");
		String[] deleteArray = deleteString.split(",");
		Integer id = null;
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		
		for (int i = 0; i < deleteArray.length; i++) {
			id = Integer.valueOf(deleteArray[i]);
			courseDiscountBanner.setId(id);
			courseDiscountBanner = courseDiscountBannerService.getData(courseDiscountBanner);
			UploadUtil.delete(uploadFilePath + "image/courseDiscountBanner/" + courseDiscountBanner.getImage());
			Gson gson = new Gson();
			String jsonString = gson.toJson(courseDiscountBanner);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(courseDiscountBanner.getId());
			editLog.setFunction("COURSE_DISCOUNT_BANNER");
			editLog.setAction_type("DELETE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			courseDiscountBannerService.delete(id);
		}
		
		model.addAttribute("title", title);
		model.addAttribute("pageNo",pageNo);
		model.addAttribute("message", "刪除成功");
		
		return "admin/courseDiscountBanner/toList";
	}
	/**
	 * 重新排序資料
	 * @return
	 */
	@RequestMapping(value = "/courseDiscountBanner/resetSort", method = {RequestMethod.POST})
	public String resetSort(HttpServletRequest request,@SessionAttribute("userAccountSession") User user) throws IOException{
		courseDiscountBannerService.resetSort();
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(0);
		editLog.setFunction("COURSE_DISCOUNT_BANNER");
		editLog.setAction_type("UPDATE");
		editLog.setContent("重新排序");
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		return "redirect:index";
	}
	
	
	
	
	
//	=============================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/courseDiscountBanner/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		getNormalBannerList = courseDiscountBannerService.getNormalCourseBannerList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		
			for(Map<String, Object> map : getNormalBannerList) {
				CourseDiscountBanner courseDiscountBanner = new CourseDiscountBanner();
				
				if(map.get("ID") != null) {
					courseDiscountBanner.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("IMAGE") != null) {
					courseDiscountBanner.setImage(map.get("IMAGE").toString());
				}
				if(map.get("TITLE") != null) {
					courseDiscountBanner.setTitle(map.get("TITLE").toString());
				}
				if(map.get("LINK") != null) {
					courseDiscountBanner.setLink(map.get("LINK").toString());
				}
				if(map.get("CLICK_RATE") != null) {
					courseDiscountBanner.setClick_rate(Integer.valueOf(map.get("CLICK_RATE").toString()));
				}
				if(map.get("BEGIN_DATE") != null) {
					courseDiscountBanner.setBegin_date(new Date(sdf.parse(map.get("BEGIN_DATE").toString()).getTime()));
				}
				if(map.get("END_DATE") != null) {
					courseDiscountBanner.setEnd_date(new Date(sdf.parse(map.get("END_DATE").toString()).getTime()));
				}
				if(map.get("CREATE_BY") != null) {
					courseDiscountBanner.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					courseDiscountBanner.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					courseDiscountBanner.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					courseDiscountBanner.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("SHOW") != null) {
					courseDiscountBanner.setShow(Integer.valueOf(map.get("SHOW").toString()));
				}
				if(map.get("SORT") != null) {
					courseDiscountBanner.setSort(Integer.valueOf(map.get("SORT").toString()));
				}
				courseDiscountBannerService.updateNormalData(courseDiscountBanner);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
}
