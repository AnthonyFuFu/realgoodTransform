package com.tkb.realgoodTransform.controller.admin;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
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
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.NavBanner;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.NavBannerService;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.UploadUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class NavBannerController extends BaseUtils {
	
	@Value("${upload.file.path}")
	private String uploadFilePath; // 檔案上傳位置

	private int pageNo; // 頁碼

	@Autowired
	private NavBannerService navBannerService;
	
	@Autowired
	private EditLogService editLogService;

	/**
	 * 清單頁面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/navBanner/index", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(@ModelAttribute NavBanner navBanner, Model model, HttpServletRequest request,
			HttpSession session) {
		List<NavBanner> navBannerList = new ArrayList<>();
		
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		if(title!=null) {
			navBanner.setTitle(title);
		}

		if (pageNo != 0) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();

		pageTotalCount = navBannerService.getCount(navBanner);
		pageNo = super.pageSetting(pageNo);
		navBannerList = navBannerService.getList(pageCount, pageStart, navBanner);

		model.addAttribute("navBannerList", navBannerList).addAttribute("pageNo", pageNo)
				.addAttribute("pageTotalCount", pageTotalCount).addAttribute("pageCount", pageCount)
				.addAttribute("totalPage", totalPage).addAttribute("leftStartPage", leftStartPage)
				.addAttribute("leftPageNum", leftPageNum).addAttribute("rightPageNum", rightPageNum)
				.addAttribute("leftEndPage", leftEndPage).addAttribute("rightStartPage", rightStartPage)
				.addAttribute("rightEndPage", rightEndPage);
		return "admin/navBanner/navBannerList";
	}

	/**
	 * 新增頁面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/navBanner/add", method = { RequestMethod.GET, RequestMethod.POST })
	public String add(@ModelAttribute NavBanner navBanner, Model model,HttpServletRequest request) {
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		
		model.addAttribute("searchTitle", title)
 		.addAttribute("pageNo",pageNo);
		
		return "admin/navBanner/navBannerForm";
	}

	/**
	 * 新增資料
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "navBanner/addSubmit", method = { RequestMethod.POST })
	public String addSubmit(NavBanner navBanner, Model model, @SessionAttribute("userAccountSession") User user,
			@RequestParam("imageFile") MultipartFile image,HttpServletRequest request) throws IOException {
		UploadUtil uploadUtil = new UploadUtil();
		String checkImageWidthAndHeight = "T";
		
		Integer pageNo = 1 ;
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		
		Integer checkType = Integer.valueOf(navBanner.getType());
		if (checkType == 1) {
			if (!super.checkImageWidth(image, 300)) {
				checkImageWidthAndHeight = "F";
			}
			if (!super.checkImageHeight(image, 184)) {
				checkImageWidthAndHeight = "F";
			}
		} else if (checkType == 2) {
			if (!super.checkImageWidth(image, 300)) {
				checkImageWidthAndHeight = "F";
			}
			if (!super.checkImageHeight(image, 600)) {
				checkImageWidthAndHeight = "F";
			}
		} else {
			checkImageWidthAndHeight = "F";
		}
		if ("T".equals(checkImageWidthAndHeight)) {
			
//			//獲取show_page
//			String showGeneral = request.getParameter("showGeneral");
//			String showExaminee = request.getParameter("showExaminee");
//			String showOnJob = request.getParameter("showOnJob");
//					
//			String show_page="";
//			
//			if(showGeneral != null) {
//				show_page+=showGeneral+",";
//			}
//			if(showExaminee != null) {
//				show_page+=showExaminee+",";
//			}
//			if(showOnJob != null) {
//				show_page+=showOnJob+",";
//			}
//			show_page = show_page.substring(0, show_page.length() - 1);
			
			int id = navBannerService.getNextId();
			navBanner.setId(id);
			try {
				String imageName = uploadUtil.uploadv2(uploadFilePath + "image/navBanner/", image, navBanner.getId());
				navBanner.setImage(imageName);
			} catch (Exception e) {
				checkImageWidthAndHeight = "E";
			}
			navBanner.setCreate_by(user.getAccount());
			navBanner.setUpdate_by(user.getAccount());
//			navBanner.setShow_page(show_page);
			navBannerService.add(navBanner);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(navBanner);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(navBanner.getId());
			editLog.setFunction("NAVBANNER");
			editLog.setAction_type("ADD");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","新增成功");
		} else if ("F".equals(checkImageWidthAndHeight)) {
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","新增失敗,請檢查圖片規格,尺寸");
		} else {
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","新增失敗");
		}
		return "admin/navBanner/toList";
	}

	/**
	 * 修改頁面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/navBanner/update", method = { RequestMethod.GET, RequestMethod.POST })
	public String update(Model model, @ModelAttribute NavBanner navBanner,HttpServletRequest request) {
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		navBanner = navBannerService.getData(navBanner);
		navBanner.setCount(navBannerService.getFrontCount(navBanner));
		
		model.addAttribute(navBanner);
		model.addAttribute("searchTitle", title);
		model.addAttribute("pageNo",pageNo);
		
		return "admin/navBanner/navBannerForm";
	}

	/**
	 * 修改資料
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "navBanner/updateSubmit", method = { RequestMethod.POST })
	public String updateSubmit(NavBanner navBanner, Model model, @SessionAttribute("userAccountSession") User user,
			@RequestParam("imageFile") MultipartFile image, HttpServletRequest request) throws IOException {
		UploadUtil uploadUtil = new UploadUtil();
		String checkImageWidthAndHeight = "T";
		String deleteImage = request.getParameter("image");
		Integer checkType = Integer.valueOf(navBanner.getType());

		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		
//		if(imageFile != null) {
		if (image.getBytes().length > 0) {
			if (checkType == 1) {
				if (!super.checkImageWidth(image, 300)) {
					checkImageWidthAndHeight = "F";
				}
				if (!super.checkImageHeight(image, 184)) {
					checkImageWidthAndHeight = "F";
				}
			} else if (checkType == 2) {
				if (!super.checkImageWidth(image, 300)) {
					checkImageWidthAndHeight = "F";
				}
				if (!super.checkImageHeight(image, 600)) {
					checkImageWidthAndHeight = "F";
				}
			} else {
				checkImageWidthAndHeight = "F";
			}
		}

		if ("T".equals(checkImageWidthAndHeight)) {
			
			//獲取show_page
//			String showGeneral = request.getParameter("showGeneral");
//			String showExaminee = request.getParameter("showExaminee");
//			String showOnJob = request.getParameter("showOnJob");
//			
//			String show_page="";
//			
//			if(showGeneral != null) {
//				show_page+=showGeneral+",";
//			}
//			if(showExaminee != null) {
//				show_page+=showExaminee+",";
//			}
//			if(showOnJob != null) {
//				show_page+=showOnJob+",";
//			}
//			
//			
//			
//			
//			show_page = show_page.substring(0, show_page.length() - 1);
//			

			// if(image != null && deleteImage != null) {
			if (image.getBytes().length > 0 && deleteImage != null) {
				UploadUtil.delete(uploadFilePath + "image/navBanner/" + deleteImage);
			}
			try {
				String imageName = uploadUtil.uploadv2(uploadFilePath + "image/navBanner/", image, navBanner.getId());
				navBanner.setImage(imageName);
			} catch (Exception e) {
				checkImageWidthAndHeight = "E";
			}
			navBanner.setUpdate_by(user.getAccount());
//			navBanner.setShow_page(show_page);			
			navBannerService.update(navBanner);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(navBanner);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(navBanner.getId());
			editLog.setFunction("NAVBANNER");
			editLog.setAction_type("UPDATE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","修改成功");
		} else if ("F".equals(checkImageWidthAndHeight)) {
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","修改失敗,請檢查圖片規格,尺寸");
		} else {
			model.addAttribute("title", title);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","修改失敗");
		}
		return "admin/navBanner/toList";
	}

	/**
	 * 刪除
	 * 
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/navBanner/delete", method = {RequestMethod.POST})
	public String delete(HttpServletRequest request,NavBanner navBanner, @SessionAttribute("userAccountSession") User user,Model model) throws IOException {
		String deleteString = request.getParameter("deleteList");
		String[] deleteArray = deleteString.split(",");
		Integer id = null;
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		
		for (int i = 0; i < deleteArray.length; i++) {
			id = Integer.valueOf(deleteArray[i]);
			navBanner.setId(id);
			navBanner = navBannerService.getData(navBanner);
			
			UploadUtil.delete(uploadFilePath + "image/navBanner/" + navBanner.getImage());
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(navBanner);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(navBanner.getId());
			editLog.setFunction("NAVBANNER");
			editLog.setAction_type("DELETE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			navBannerService.delete(id);
		}
		
		model.addAttribute("message", "刪除成功")
		 .addAttribute("title", title)
	 	 .addAttribute("pageNo",pageNo);
		
		return "admin/navBanner/toList";
	}
	
//	===============
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/navBanner/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalNavBannerList = new ArrayList<>();
		getNormalNavBannerList = navBannerService.getNormalNavBannerList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			
//			for (Map<String, Object> map : getNormalNavBannerList) {
//			    for (Map.Entry<String, Object> entry : map.entrySet()) {
//			        String key = entry.getKey();
//			        Object value = entry.getValue();
//			    }
//			}
			
			for(Map<String, Object> map : getNormalNavBannerList) {
				NavBanner navBanner = new NavBanner();
				
				if(map.get("ID") != null) {
					navBanner.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("IMAGE") != null) {
					navBanner.setImage(map.get("IMAGE").toString());
				}
				if(map.get("TITLE") != null) {
					navBanner.setTitle(map.get("TITLE").toString());
				}
				if(map.get("LINK") != null) {
					navBanner.setLink(map.get("LINK").toString());
				}
				if(map.get("CLICK_RATE") != null) {
					navBanner.setClick_rate(Integer.valueOf(map.get("CLICK_RATE").toString()));
				}
				if(map.get("BEGIN_DATE") != null) {
					navBanner.setBegin_date(new Date(sdf.parse(map.get("BEGIN_DATE").toString()).getTime()));
				}
				if(map.get("END_DATE") != null) {
					navBanner.setEnd_date(new Date(sdf.parse(map.get("END_DATE").toString()).getTime()));
				}
				if(map.get("CREATE_BY") != null) {
					navBanner.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					navBanner.setCreate_date(new Timestamp(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					navBanner.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					navBanner.setUpdate_date(new Timestamp(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("SHOW") != null) {
					navBanner.setShow(Integer.valueOf(map.get("SHOW").toString()));
				}
				if(map.get("SORT") != null) {
					navBanner.setSort(Integer.valueOf(map.get("SORT").toString()));
				}
				if(map.get("TYPE") != null) {
					navBanner.setType(map.get("TYPE").toString());
				}
				if(map.get("SHOW_PAGE") != null) {
					navBanner.setShow_page(map.get("SHOW_PAGE").toString());
				}
				navBannerService.updateNormalData(navBanner);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
	
	
	
}
