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
import com.tkb.realgoodTransform.model.Banner;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.BannerService;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.UploadUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class BannerController extends BaseUtils{
	
	@Value("${upload.file.path}")
	private String uploadFilePath; // 檔案上傳位置
	private int pageNo;			  //頁碼
	
	@Autowired
	private BannerService bannerService;
	
	@Autowired
	private EditLogService editLogService;
	/**
	 * 清單頁面
	 * @return
	 */
	@RequestMapping(value = "/banner/index", method = {RequestMethod.POST, RequestMethod.GET})
	public String index (@ModelAttribute Banner banner, Model model, HttpServletRequest request, HttpSession session) {
		List<Banner> bannerList = new ArrayList<>();

		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		if(title!=null) {
			banner.setTitle(title);
		}
		
		
		if (pageNo != 0) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();

		pageTotalCount = bannerService.getCount(banner);
		pageNo = super.pageSetting(pageNo);
		bannerList = bannerService.getList(pageCount, pageStart, banner);
		
		model.addAttribute("bannerList", bannerList).addAttribute("pageNo", pageNo)
		.addAttribute("pageTotalCount", pageTotalCount).addAttribute("pageCount", pageCount)
		.addAttribute("totalPage", totalPage).addAttribute("leftStartPage", leftStartPage)
		.addAttribute("leftPageNum", leftPageNum).addAttribute("rightPageNum", rightPageNum)
		.addAttribute("leftEndPage", leftEndPage).addAttribute("rightStartPage", rightStartPage)
		.addAttribute("rightEndPage", rightEndPage);
		return "admin/banner/bannerList";
	}
	/**
	 * 新增頁面
	 * @return
	 */
	@RequestMapping(value = "/banner/add", method = {RequestMethod.POST, RequestMethod.GET})
	public String add(@ModelAttribute Banner banner, Model model,HttpServletRequest request) {
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		
		model.addAttribute("searchTitle", title)
		.addAttribute("pageNo",pageNo);
		
		return "admin/banner/bannerForm";
	}
	/**
	 * 新增資料
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/banner/addSubmit", method = {RequestMethod.POST})
	public String addSubmit(Banner banner, Model model, @SessionAttribute("userAccountSession") User user,
			@RequestParam("uploadImage") MultipartFile image, @RequestParam("uploadRwdImage") MultipartFile imageRwd,HttpServletRequest request)throws IOException {
		UploadUtil uploadUtil = new UploadUtil();
		String checkStatus = "T";
		
		Integer pageNo = 1 ;
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		
		if(image != null) {
			if(super.checkImageWidth(image, 880)){

			}else{
				checkStatus = "F";
			}
			if(super.checkImageHeight(image, 250)){

			}else{
				checkStatus = "F";
			}
			if("F".equals(checkStatus)) {
				model.addAttribute("title", title);
				model.addAttribute("pageNo",pageNo);
				model.addAttribute("message","新增失敗，圖片固定880x250px");
				return "admin/banner/toList";
				}
		}
		if(imageRwd != null) {
			if(super.checkImageWidth(imageRwd, 680)){

			}else{
				checkStatus = "F";
			}
			if(super.checkImageHeight(imageRwd, 700)){

			}else{
				checkStatus = "F";
			}
			if("F".equals(checkStatus)) {
				model.addAttribute("title", title);
				model.addAttribute("pageNo",pageNo);
				model.addAttribute("message","新增失敗，圖片固定680x700px");
				return "admin/banner/toList";
				}
		}
		if("T".equals(checkStatus)) {
			int id = bannerService.getNextId();
			banner.setId(id);
			banner.setImage(uploadUtil.uploadv2(uploadFilePath + "image/banner/", image, banner.getId()));
			banner.setImage_rwd(uploadUtil.uploadv2(uploadFilePath + "image/banner/", imageRwd, banner.getId()));
			banner.setCreate_by(user.getAccount());
			banner.setUpdate_by(user.getAccount());
			bannerService.add(banner);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(banner);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(banner.getId());
			editLog.setFunction("BANNER");
			editLog.setAction_type("ADD");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","新增成功");
			return "admin/banner/toList";
		}else {
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","新增失敗");
			return "admin/banner/toList";
		}
	}
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping(value = "/banner/update", method = {RequestMethod.POST, RequestMethod.GET})
	public String update(@ModelAttribute Banner banner, Model model,HttpServletRequest request) {
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		banner = bannerService.getData(banner);
		banner.setCount(bannerService.getFrontCount(banner));
		model.addAttribute(banner)
			.addAttribute("searchTitle", title)
	 	 .addAttribute("pageNo",pageNo);
		
		return "admin/banner/bannerForm";
	}
	/**
	 * 修改資料
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/banner/updateSubmit", method = {RequestMethod.POST})
	public String updateSubmit (Banner banner, Model model,@SessionAttribute("userAccountSession") User user,
			@RequestParam("uploadImage") MultipartFile image, @RequestParam("uploadRwdImage") MultipartFile imageRwd, HttpServletRequest request) throws IOException{
		UploadUtil uploadUtil = new UploadUtil();
		String deleteImage = request.getParameter("image");
		String deleteImageRwd = request.getParameter("image_rwd");
		String checkStatus = "T";
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		if(image.getBytes().length>0) {
			if(super.checkImageWidth(image, 880)){
			}else{
				checkStatus = "F";
			}
			if(super.checkImageHeight(image, 250)){

			}else{
				checkStatus = "F";
			}
			if("F".equals(checkStatus)) {
				model.addAttribute("title", title);
				model.addAttribute("pageNo",pageNo);
				model.addAttribute("message","修改失敗，圖片固定880x250px");
				
				return "admin/banner/toList";
				}
		}
		if(imageRwd.getBytes().length>0) {
			if(super.checkImageWidth(imageRwd, 680)){
			}else{
				checkStatus = "F";
			}
			if(super.checkImageHeight(imageRwd, 700)){

			}else{
				checkStatus = "F";
			}
			if("F".equals(checkStatus)) {
				model.addAttribute("title", title);
				model.addAttribute("pageNo",pageNo);
				model.addAttribute("message","修改失敗，圖片固定680x700px");
				return "admin/banner/toList";
				}
		}
		if("T".equals(checkStatus)) {
			if(image.getBytes().length>0 && deleteImage != null) {
				UploadUtil.delete(uploadFilePath + "image/banner/" + deleteImage);
			}
			if(imageRwd.getBytes().length>0 && deleteImageRwd != null) {
				UploadUtil.delete(uploadFilePath + "image/bannerRwd/" + deleteImageRwd);
			}
			if(image.getBytes().length>0) {
				banner.setImage(uploadUtil.uploadv2(uploadFilePath + "image/banner/", image, banner.getId()));
			}
			if(imageRwd.getBytes().length>0) {
				banner.setImage_rwd(uploadUtil.uploadv2(uploadFilePath + "image/banner/", imageRwd, banner.getId()));	
			}
			banner.setUpdate_by(user.getAccount());
			bannerService.update(banner);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(banner);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(banner.getId());
			editLog.setFunction("BANNER");
			editLog.setAction_type("UPDATE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","修改成功");
			return "admin/banner/toList";
		}else {
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","新增失敗");
			return "admin/banner/toList";
		}
	}
	/**
	 * 刪除
	 * @return
	 */
	@RequestMapping(value = "/banner/delete", method = {RequestMethod.POST})
	public String delete(HttpServletRequest request,@SessionAttribute("userAccountSession") User user,Banner banner,Model model) throws IOException{
		String deleteString = request.getParameter("deleteList");
		String[] deleteArray = deleteString.split(",");
		Integer id = null;
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		
		for (int i = 0; i < deleteArray.length; i++) {
			id = Integer.valueOf(deleteArray[i]);
			
			banner.setId(id);
			banner = bannerService.getData(banner);
			UploadUtil.delete(uploadFilePath + "image/banner/" + banner.getImage());
			UploadUtil.delete(uploadFilePath + "image/banner/" + banner.getImage_rwd());
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(banner);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(banner.getId());
			editLog.setFunction("BANNER");
			editLog.setAction_type("DELETE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			bannerService.delete(id);
		}
		
		model.addAttribute("title", title);
		model.addAttribute("pageNo",pageNo);
		model.addAttribute("message", "刪除成功");
		
		
		return "admin/banner/toList";
	}
	/**
	 * 重新排序資料
	 * @return
	 */
	@RequestMapping(value = "/banner/resetSort", method = {RequestMethod.POST})
	public String resetSort(HttpServletRequest request,@SessionAttribute("userAccountSession") User user) throws IOException{
		bannerService.resetSort();
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(0);
		editLog.setFunction("BANNER");
		editLog.setAction_type("UPDATE");
		editLog.setContent("重新排序");
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		return "redirect:index";
	}
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/banner/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		getNormalBannerList = bannerService.getNormalList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			
//			for (Map<String, Object> map : getNormalBannerList) {
//			    for (Map.Entry<String, Object> entry : map.entrySet()) {
//			        String key = entry.getKey();
//			        Object value = entry.getValue();
//			    }
//			}
			
		
			for(Map<String, Object> map : getNormalBannerList) {
				Banner banner = new Banner();
				
				if(map.get("ID") != null) {
					banner.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("IMAGE") != null) {
					banner.setImage(map.get("IMAGE").toString());
				}
				if(map.get("TITLE") != null) {
					banner.setTitle(map.get("TITLE").toString());
				}
				if(map.get("LINK") != null) {
					banner.setLink(map.get("LINK").toString());
				}
				if(map.get("CLICK_RATE") != null) {
					banner.setClick_rate(Integer.valueOf(map.get("CLICK_RATE").toString()));
				}
				if(map.get("BEGIN_DATE") != null) {
					banner.setBegin_date(new Date(sdf.parse(map.get("BEGIN_DATE").toString()).getTime()));
				}
				if(map.get("END_DATE") != null) {
					banner.setEnd_date(new Date(sdf.parse(map.get("END_DATE").toString()).getTime()));
				}
				if(map.get("CREATE_BY") != null) {
					banner.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					banner.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					banner.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					banner.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("SHOW") != null) {
					banner.setShow(Integer.valueOf(map.get("SHOW").toString()));
				}
				if(map.get("SORT") != null) {
					banner.setSort(Integer.valueOf(map.get("SORT").toString()));
				}
				if(map.get("IMAGE_RWD") != null) {
					banner.setImage_rwd(map.get("IMAGE_RWD").toString());
				}
				bannerService.updateNormalData(banner);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
}
 