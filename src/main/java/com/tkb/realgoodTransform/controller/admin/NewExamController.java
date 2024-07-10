package com.tkb.realgoodTransform.controller.admin;


import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.tkb.realgoodTransform.model.NewExam;
import com.tkb.realgoodTransform.model.NewExamCategory;
import com.tkb.realgoodTransform.model.NewExamContent;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.NewExamContentService;
import com.tkb.realgoodTransform.service.NewExamService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class NewExamController extends BaseUtils {
	private int pageNo;
	@Autowired
	NewExamService newExamService;
	@Autowired
	NewExamContentService newExamContentService;
	
	/**
	 * 清單頁面
	 * @return
	 */
	@RequestMapping("/newExam/index")
	public String index (NewExam newExam, HttpServletRequest request,
			Model model, HttpSession session) {
		List<NewExam>newExamList = new ArrayList<>();
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		if(title!=null) {
			newExam.setTitle(title);
		}
		
		if (pageNo != 0) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();
		pageTotalCount = newExamService.getCount(newExam);
		pageNo = super.pageSetting(pageNo);
		
		newExamList = newExamService.getList(pageCount, pageStart, newExam);
		model.addAttribute("newExamList", newExamList);
		addModelAttribute(pageNo, model);
		return "admin/newExam/newExamList";
	}
	
	/**
	 * 新增頁面
	 * @return
	 */
	@RequestMapping("/newExam/add")
	public String add (NewExam newExam,NewExamCategory newExamCategory,Model model,HttpServletRequest request) {
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		model.addAttribute("newExamCategoryList", newExamService.add(newExamCategory))
		 	 .addAttribute("searchTitle", title)
			 .addAttribute("pageNo",pageNo);
		return "admin/newExam/newExamForm";
	}
	
	/**
	 * 新增資料
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/newExam/addSubmit")
	public String addSubmit (NewExam newExam,NewExamContent newExamContent,HttpServletRequest request,
			@SessionAttribute("userAccountSession") User user,Model model) throws IOException{
		Integer pageNo = 1 ;
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		newExamService.addSubmit(newExam, newExamContent, user, request, model);
		model.addAttribute("title", title)
			 .addAttribute("pageNo",pageNo);
		return "admin/newExam/toList";
	}
	
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping("/newExam/update")
	public String update (NewExam newExam, NewExamCategory newExamCategory ,NewExamContent newExamContent,
			Model model,HttpServletRequest request) {
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		newExamService.update(newExam, newExamCategory, newExamContent, model);
		model.addAttribute("searchTitle", title)
		 	 .addAttribute("pageNo",pageNo);
		return "admin/newExam/newExamForm";
	}
	
	/**
	 * 修改資料
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/newExam/updateSubmit")
	public String updateSubmit (NewExam newExam,NewExamContent newExamContent,HttpServletRequest request,
			@SessionAttribute("userAccountSession") User user,Model model) throws IOException{
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		
		newExamService.updateSubmit(newExam, newExamContent, user, request, model);
		model.addAttribute("title", title)
	 	 	 .addAttribute("pageNo",pageNo);
		return "admin/newExam/toList";
	}
	
	/**
	 * 刪除百官公告
	 * @return
	 */
	@RequestMapping("/newExam/delete")
	public String delete (NewExam newExam, NewExamContent newExamContent,@RequestParam("deleteList")String deleteList,
			@SessionAttribute("userAccountSession") User user,Model model,HttpServletRequest request) throws IOException{
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		
		newExamService.delete(newExam, newExamContent, deleteList, user);
		model.addAttribute("message", "刪除成功")
			 .addAttribute("title", title)
	 	 	 .addAttribute("pageNo",pageNo);
		return "admin/newExam/toList";
	}
	
	
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/newExam/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		getNormalBannerList = newExamService.getNormalList();
		
		List<Map<String, Object>> getNormalBannerList2 = new ArrayList<>();
		getNormalBannerList2 = newExamContentService.getNormalList();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		
			//newexam表格的
			for(Map<String, Object> map : getNormalBannerList) {
				NewExam newExam = new NewExam();
				
				if(map.get("ID") != null) {
					newExam.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("TITLE") != null) {
					newExam.setTitle(map.get("TITLE").toString());
				}
				if(map.get("AREA") != null) {
					newExam.setArea(map.get("AREA").toString());
				}
				if(map.get("CATEGORY") != null) {
					newExam.setCategory(map.get("CATEGORY").toString());
				}
				if(map.get("IMAGE") != null) {
					newExam.setImage(map.get("IMAGE").toString());
				}
				if(map.get("BEGIN_DATE") != null) {
					newExam.setBegin_date(new Date(sdf.parse(map.get("BEGIN_DATE").toString()).getTime()));
				}
				if(map.get("END_DATE") != null) {
					newExam.setEnd_date(new Date(sdf.parse(map.get("END_DATE").toString()).getTime()));
				}
				if(map.get("CLICK_RATE") != null) {
					newExam.setClick_rate(Integer.valueOf(map.get("CLICK_RATE").toString()));
				}
				if(map.get("CREATE_BY") != null) {
					newExam.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					newExam.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					newExam.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					newExam.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("CONTENT") != null) {
					newExam.setContent(map.get("CONTENT").toString());
				}
				if(map.get("PHOTO") != null) {
					newExam.setPhoto(map.get("PHOTO").toString());
				}
				if(map.get("ENCRYPTURL") != null) {
					newExam.setEncrypturl(map.get("ENCRYPTURL").toString());
				}
				if(map.get("PRODUCT_CATEGORY") != null) {
					newExam.setProduct_category(map.get("PRODUCT_CATEGORY").toString());
				}
				newExamService.updateNormalData(newExam);
				
			}
			
			
			
			//newexam_content表格的
			for(Map<String, Object> map : getNormalBannerList2) {
				NewExamContent newExamContent = new NewExamContent();
				
				if(map.get("ID") != null) {
					newExamContent.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("NEWEXAM_ID") != null) {
					newExamContent.setNewExam_id(Integer.valueOf(map.get("NEWEXAM_ID").toString()));
				}
				if(map.get("ICON") != null) {
					newExamContent.setIcon(map.get("ICON").toString());
				}
				if(map.get("TITLE") != null) {
					newExamContent.setTitle(map.get("TITLE").toString());
				}
				if(map.get("CONTENT") != null) {
					newExamContent.setContent(map.get("CONTENT").toString());
				}
				if(map.get("CREATE_BY") != null) {
					newExamContent.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					newExamContent.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					newExamContent.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					newExamContent.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				newExamContentService.updateNormalData(newExamContent);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
	
}
