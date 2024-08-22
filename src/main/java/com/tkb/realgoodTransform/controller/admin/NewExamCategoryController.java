package com.tkb.realgoodTransform.controller.admin;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.tkb.realgoodTransform.model.NewExamCategory;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.NewExamCategoryService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/tkbrule")
@SessionAttributes({"userAccountSession", "sideMenuList"})
public class NewExamCategoryController extends BaseUtils {
	
	@Autowired
	private NewExamCategoryService newExamCategoryService;
	
	private int pageNo;			  //頁碼
	
	@RequestMapping("/newExamCategory/index")
	public String index(NewExamCategory newExamCategory, Model model, HttpServletRequest request,
			@RequestParam(name = "menu_id", required = false, defaultValue = "0")Integer menu_id) {
		
		if (pageNo != 0) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();

		pageNo = super.pageSetting(pageNo);
		
		newExamCategoryService.index(newExamCategory, menu_id, model, request);
		return "admin/newExamCategory/newExamCategoryList";
	}
	
	@RequestMapping("/newExamCategory/add")
	public String add(@ModelAttribute("newExamCategory") NewExamCategory newExamCategory, Model model, HttpServletRequest request) {
		newExamCategoryService.addPage(newExamCategory, model, request);
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		
		model.addAttribute("pageNo",pageNo);
		
		return "admin/newExamCategory/newExamCategoryForm";
	}
	
	@PostMapping("/newExamCategory/addSubmit")
	public String addSubmit(NewExamCategory newExamCategory, Model model,
			@SessionAttribute("userAccountSession")User user) {
		
		Integer pageNo = 1 ;
		
		model.addAttribute("pageNo",pageNo);
		model.addAttribute("message","新增成功");
		
		newExamCategoryService.add(newExamCategory, user, model);
		return "admin/newExamCategory/toList";
	}
	
	@PostMapping("/newExamCategory/checkNewExamCategory")
	public ResponseEntity<?> checkNewExamCategory(@RequestParam String newExamCategoryName,
			@RequestParam("layer")Integer layer, HttpServletRequest request){
		return newExamCategoryService.checkNewExamCategory(newExamCategoryName, layer, request);
	}
	
	@PostMapping("/newExamCategory/checkData")
	public ResponseEntity<?> checkData(@RequestParam String checkList){
		return newExamCategoryService.checkData(checkList);
	}
	@DeleteMapping("/newExamCategory/delete")
	public ResponseEntity<?> delete(@RequestParam String deleteList,
			@SessionAttribute("userAccountSession")User user,HttpServletRequest request,Model model){
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		
		model.addAttribute("pageNo",pageNo);
		model.addAttribute("message", "刪除成功");
		
		return newExamCategoryService.delete(deleteList,user);
	}
	@PutMapping("/newExamCategory/resetSort")
	public ResponseEntity<?> resetSort(@RequestParam(defaultValue = "0") Integer parent_id,@SessionAttribute("userAccountSession") User user){
		return newExamCategoryService.resetSort(parent_id,user);
	}
	@PostMapping("/newExamCategory/update")
	public String update(@RequestParam Integer id, Model model,HttpServletRequest request) {
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		
		newExamCategoryService.updatePage(id, model);
		
		model.addAttribute("pageNo",pageNo);
		
		return "admin/newExamCategory/newExamCategoryForm";
	}
	
	@PostMapping("/newExamCategory/updateSubmit")
	public String updateSubmit(NewExamCategory newExamCategory, Model model,
			@SessionAttribute("userAccountSession")User user,HttpServletRequest request) {
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		
		newExamCategoryService.updateSubmit(newExamCategory, user, model);
		
		model.addAttribute("pageNo",pageNo);
		model.addAttribute("message","修改成功");
		
		return "admin/newExamCategory/toList";
	}
	
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/newExamCategory/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		getNormalBannerList = newExamCategoryService.getNormalList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		
			for(Map<String, Object> map : getNormalBannerList) {
				NewExamCategory newExamCategory = new NewExamCategory();
				
				if(map.get("ID") != null) {
					newExamCategory.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("PARENT_ID") != null) {
					newExamCategory.setParent_id(Integer.valueOf(map.get("PARENT_ID").toString()));
				}
				if(map.get("NAME") != null) {
					newExamCategory.setName(map.get("NAME").toString());
				}
				if(map.get("LAYER") != null) {
					newExamCategory.setLayer(map.get("LAYER").toString());
				}
				if(map.get("CREATE_BY") != null) {
					newExamCategory.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					newExamCategory.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					newExamCategory.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					newExamCategory.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("SORT") != null) {
					newExamCategory.setSort(Integer.valueOf(map.get("SORT").toString()));
				}
				newExamCategoryService.updateNormalData(newExamCategory);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
	
	
}
