package com.tkb.realgoodTransform.controller.admin;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.google.gson.Gson;
import com.tkb.realgoodTransform.model.CourseDiscountCategory;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.CourseDiscountCategoryService;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class CourseDiscountCategoryController extends BaseUtils {
	
	@Autowired
	private CourseDiscountCategoryService courseDiscountCategoryService;
	
	@Autowired
	private EditLogService editLogService;
	
	private int pageNo;
	
	/**
	 * 清單頁面
	 * @return
	 */
	@RequestMapping(value = "courseDiscountCategory/index", method = {RequestMethod.POST,RequestMethod.GET})
	public String index (@ModelAttribute CourseDiscountCategory courseDiscountCategory,Model model,
			HttpServletRequest request, HttpSession session) {
		List<CourseDiscountCategory>courseDiscountCategoryList = new ArrayList<>();
		pageNo = super.setPage(pageNo, request);
		pageTotalCount = courseDiscountCategoryService.getCount(courseDiscountCategory);
		pageNo = super.pageSetting(pageNo);
		courseDiscountCategoryList = courseDiscountCategoryService.getList(pageCount, pageStart, courseDiscountCategory);
		model.addAttribute("courseDiscountCategoryList", courseDiscountCategoryList);
		addModelAttribute(pageNo, model);
		return "admin/courseDiscountCategory/courseDiscountCategoryList";
	}
	/**
	 * 新增頁面
	 * @return
	 */
	@RequestMapping(value = "/courseDiscountCategory/add", method = {RequestMethod.POST,RequestMethod.GET})
	public String add (@ModelAttribute CourseDiscountCategory courseDiscountCategory) {
		courseDiscountCategory.setName(null);
		return "admin/courseDiscountCategory/courseDiscountCategoryForm";
	}
	/**
	 * 新增類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/courseDiscountCategory/addSubmit", method = {RequestMethod.POST})
	public String addSubmit (@ModelAttribute CourseDiscountCategory courseDiscountCategory,
			@SessionAttribute("userAccountSession") User user, Model model) throws IOException{
		try {
			courseDiscountCategoryService.addSubmitFunction(courseDiscountCategory, user);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(courseDiscountCategory);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(courseDiscountCategory.getId());
			editLog.setFunction("COURSE_DISCOUNT_CATEGORY");
			editLog.setAction_type("ADD");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			model.addAttribute("message", "新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "新增失敗");
		}
		return "admin/courseDiscountCategory/toList";
	}
	/**
	 * 檢查重複類別名稱
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/courseDiscountCategory/checkCourseDiscountCategory", method = {RequestMethod.POST})
	public ResponseEntity<?>checkCourseDiscountCategory(@ModelAttribute CourseDiscountCategory courseDiscountCategory,
			HttpServletRequest request) throws IOException{
		return new ResponseEntity<String>(courseDiscountCategoryService.checkCourseDiscountCategoryFunction(courseDiscountCategory, request),HttpStatus.OK);
	}
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping(value = "courseDiscountCategory/update", method = {RequestMethod.POST,RequestMethod.GET})
	public String update (@ModelAttribute CourseDiscountCategory courseDiscountCategory,
			Model model) {
		model.addAttribute("courseDiscountCategory", courseDiscountCategoryService.updateFunction(courseDiscountCategory));
		return "admin/courseDiscountCategory/courseDiscountCategoryForm";
	}
	/**
	 * 修改類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/courseDiscountCategory/updateSubmit", method = {RequestMethod.POST})
	public String updateSubmit (@ModelAttribute CourseDiscountCategory courseDiscountCategory,
			@SessionAttribute("userAccountSession") User user, Model model) throws IOException {
		try {
			courseDiscountCategoryService.updateSubmitFunction(courseDiscountCategory, user);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(courseDiscountCategory);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(courseDiscountCategory.getId());
			editLog.setFunction("COURSE_DISCOUNT_CATEGORY");
			editLog.setAction_type("UPDATE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			model.addAttribute("message", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "修改失敗");
		}
		return "admin/courseDiscountCategory/toList";
	}
	/**
	 * 刪除類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/courseDiscountCategory/delete", method = {RequestMethod.POST})
	public String delete (@ModelAttribute CourseDiscountCategory courseDiscountCategory
			,@RequestParam("deleteList") String deleteList,
			@SessionAttribute("userAccountSession") User user,Model model) throws IOException{
		try {
			if(courseDiscountCategory.getParent_id() == null) {
				courseDiscountCategory.setParent_id(0);
			}
			String[] checkArray = deleteList.split(",");
			Integer id = null;
			Integer count;
			for(int i = 0; i<checkArray.length;i++) {
				id = Integer.valueOf(checkArray[i]);
				courseDiscountCategory.setId(id);
				
				count = courseDiscountCategoryService.deleteCheck(courseDiscountCategory);
				if(count!=0) {
					throw new IOException();
				}
				
				courseDiscountCategory = courseDiscountCategoryService.getData(courseDiscountCategory);
				
				Gson gson = new Gson();
				String jsonString = gson.toJson(courseDiscountCategory);
				EditLog editLog = new EditLog();
				int logId = editLogService.getNextLogId();
				editLog.setId(logId);
				editLog.setData_id(courseDiscountCategory.getId());
				editLog.setFunction("COURSE_DISCOUNT_CATEGORY");
				editLog.setAction_type("DELETE");
				editLog.setContent(jsonString);
				editLog.setCreate_by(user.getAccount());
				editLogService.addLog(editLog);
				courseDiscountCategoryService.delete(courseDiscountCategory, id);
			}
			
			return "redirect:index";
		} catch (IOException e) {
			model.addAttribute("message", "刪除失敗，該分類底下還有活動");
			return "admin/courseDiscountCategory/toList";
		}
		
	}
	/**
	 * 檢查是否有子類別類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/courseDiscountCategory/checkData", method = {RequestMethod.POST})
	public ResponseEntity<?>checkData(@ModelAttribute CourseDiscountCategory courseDiscountCategory,
			@RequestParam	("checkList") String checkList)throws IOException{
		return new ResponseEntity<String>(courseDiscountCategoryService.checkDataFunction(courseDiscountCategory, checkList),HttpStatus.OK);
	}
	/**
	 * 重新排序資料
	 * @return
	 */
	@RequestMapping(value ="/courseDiscountCategory/resetSort", method = {RequestMethod.POST})
	public String resetSort(@ModelAttribute CourseDiscountCategory courseDiscountCategory,
			@SessionAttribute("userAccountSession") User user) {
		if(courseDiscountCategory.getParent_id() == null) {
			courseDiscountCategory.setParent_id(0);
		}
		courseDiscountCategoryService.resetSort(courseDiscountCategory);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(0);
		editLog.setFunction("COURSE_DISCOUNT_CATEGORY");
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
	@GetMapping("/courseDiscountCategory/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		getNormalBannerList = courseDiscountCategoryService.getNormalList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		
			for(Map<String, Object> map : getNormalBannerList) {
				CourseDiscountCategory courseDiscountCategory = new CourseDiscountCategory();
				
				if(map.get("ID") != null) {
					courseDiscountCategory.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("PARENT_ID") != null) {
					courseDiscountCategory.setParent_id(Integer.valueOf(map.get("PARENT_ID").toString()));
				}
				if(map.get("NAME") != null) {
					courseDiscountCategory.setName(map.get("NAME").toString());
				}
				if(map.get("LAYER") != null) {
					courseDiscountCategory.setLayer(map.get("LAYER").toString());
				}
				if(map.get("CREATE_BY") != null) {
					courseDiscountCategory.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					courseDiscountCategory.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					courseDiscountCategory.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					courseDiscountCategory.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("SORT") != null) {
					courseDiscountCategory.setSort(Integer.valueOf(map.get("SORT").toString()));
				}
				courseDiscountCategoryService.updateNormalData(courseDiscountCategory);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
}
