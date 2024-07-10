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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.LecturesCategory;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.LecturesCategoryService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class LecturesCategoryController extends BaseUtils {
	private int pageNo;												//頁碼
	@Autowired
	private LecturesCategoryService lecturesCategoryService;		//群組服務
	@Autowired
	private EditLogService editLogService;
	
	/**
	 * 清單頁面
	 * @return
	 */
	@RequestMapping("/lecturesCategory/index")
	public String index (@ModelAttribute LecturesCategory lectruCategory, Model model,
			HttpServletRequest request, HttpSession session) {
		String parent_id = request.getParameter("parent_id") == null ? null : request.getParameter("parent_id");
		String layer = request.getParameter("layer") == null ? null : request.getParameter("layer");
		if(parent_id!=null && !parent_id.equals("")) {
			lectruCategory.setParent_id(Integer.parseInt(parent_id));
		}
		if(layer!=null && !layer.equals("")) {
			lectruCategory.setLayer(layer);
		}
		
		
		List<LecturesCategory>lecturesCategoryList = new ArrayList<>();

		if (pageNo != 0) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();

		pageTotalCount = lecturesCategoryService.getCount(lectruCategory);
		pageNo = super.pageSetting(pageNo);
		
		lecturesCategoryList = lecturesCategoryService.getList(pageCount, pageStart, lectruCategory);
		model.addAttribute("lecturesCategoryList", lecturesCategoryList);
		addModelAttribute(pageNo, model);
		return "admin/lecturesCategory/lecturesCategoryList";
	}
	
	/**
	 * 新增頁面
	 * @return
	 */
	@RequestMapping("/lecturesCategory/add")
	public String add (@ModelAttribute LecturesCategory lecturesCategory,HttpServletRequest request,Model model) {
		lecturesCategory.setName(null);
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		
		model.addAttribute("pageNo",pageNo);
		
		return "admin/lecturesCategory/lecturesCategoryForm";
	}
	
	/**
	 * 新增類別
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/lecturesCategory/addSubmit")
	public String addSubmit (LecturesCategory lecturesCategory, 
			@SessionAttribute("userAccountSession") User user,Model model) throws IOException{
		try {
			
			Integer pageNo = 1 ;
			
			lecturesCategoryService.addSubmitFunction(lecturesCategory, user);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(lecturesCategory);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(lecturesCategory.getId());
			editLog.setFunction("LECTURES_CATEGORY");
			editLog.setAction_type("ADD");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","新增失敗" );
		}
		model.addAttribute("parent_id", lecturesCategory.getParent_id());
		model.addAttribute("layer", lecturesCategory.getLayer());
		return "admin/lecturesCategory/toList";
	}
	
	/**
	 * 檢查重複類別名稱
	 * @return
	 * @throws IOException 
	 */
	@GetMapping("/lecturesCategory/checkLecturesCategory")
	public ResponseEntity<?> checkLecturesCategory (LecturesCategory lecturesCategory, @RequestParam("layer") String layer,
			@RequestParam("lecturesCategoryName") String  lecturesCategoryName ) throws IOException{
		return lecturesCategoryService.checkLecturesCategoryFunction(lecturesCategory,layer,lecturesCategoryName);
	}
	
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping("/lecturesCategory/update")
	public String update (@ModelAttribute LecturesCategory lecturesCategory,Model model,HttpServletRequest request) {
		model.addAttribute("lecturesCategory", lecturesCategoryService.updateFunction(lecturesCategory));
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		
		model.addAttribute("pageNo",pageNo);
		
		return "admin/lecturesCategory/lecturesCategoryForm";
	}
	
	/**
	 * 修改類別
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/lecturesCategory/updateSubmit")
	public String updateSubmit (@ModelAttribute LecturesCategory lecturesCategory, Model model,
			@SessionAttribute("userAccountSession") User user,HttpServletRequest request) throws IOException{
		try {
			
			Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
			
			lecturesCategoryService.updateSubmitFunction(lecturesCategory, user);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(lecturesCategory);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(lecturesCategory.getId());
			editLog.setFunction("LECTURES_CATEGORY");
			editLog.setAction_type("UPDATE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","修改成功" );
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message","修改失敗" );
		}
		model.addAttribute("parent_id", lecturesCategory.getParent_id());
		model.addAttribute("layer", lecturesCategory.getLayer());
		return "admin/lecturesCategory/toList";
	}
	
	/**
	 * 刪除類別
	 * @return
	 */
	@RequestMapping("/lecturesCategory/delete")
	public String delete (LecturesCategory lecturesCategory, 
			@RequestParam("deleteList") String selectList,Model model,
			@SessionAttribute("userAccountSession") User user,HttpServletRequest request){
		try {
			
			Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
			
			lecturesCategoryService.deleteFunction(lecturesCategory, selectList, user);
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message", "刪除成功");
		} catch (IOException e) {
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message", "刪除失敗，該分類底下還有活動");
			return "admin/lecturesCategory/toList";
		}catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("pageNo",pageNo);
			model.addAttribute("message", "刪除失敗");
			return "admin/lecturesCategory/toList";
		}
		model.addAttribute("parent_id", lecturesCategory.getParent_id());
		model.addAttribute("layer", lecturesCategory.getLayer());
		return "admin/lecturesCategory/toList";
	}
	
	/**
	 * 檢查資料
	 * @return
	 */
	@GetMapping("/lecturesCategory/checkData")
	public ResponseEntity<?>checkData(LecturesCategory lecturesCategory,@RequestParam("checkList") String checkList) throws IOException{
		return lecturesCategoryService.checkData(lecturesCategory, checkList);
	}
	
	/**
	 * 重新排序資料
	 * @return
	 */
	@RequestMapping("/lecturesCategory/resetSort")
	public String resetSort (LecturesCategory lecturesCategory,
			@SessionAttribute("userAccountSession") User user,Model model) {
		lecturesCategoryService.resetSortFunction(lecturesCategory);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(0);
		editLog.setFunction("LECTURES_CATEGORY");
		editLog.setAction_type("UPDATE");
		editLog.setContent("重新排序");
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		
		model.addAttribute("message", "重新排序成功");
		model.addAttribute("parent_id", lecturesCategory.getParent_id());
		model.addAttribute("layer", lecturesCategory.getLayer());
		return "admin/lecturesCategory/toList";
	}
	
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/lecturesCategory/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		getNormalBannerList = lecturesCategoryService.getNormalList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		
			for(Map<String, Object> map : getNormalBannerList) {
				LecturesCategory lecturesCategory = new LecturesCategory();
				
				if(map.get("ID") != null) {
					lecturesCategory.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("PARENT_ID") != null) {
					lecturesCategory.setParent_id(Integer.valueOf(map.get("PARENT_ID").toString()));
				}
				if(map.get("NAME") != null) {
					lecturesCategory.setName(map.get("NAME").toString());
				}
				if(map.get("LAYER") != null) {
					lecturesCategory.setLayer(map.get("LAYER").toString());
				}
				if(map.get("CREATE_BY") != null) {
					lecturesCategory.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					lecturesCategory.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					lecturesCategory.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					lecturesCategory.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("SORT") != null) {
					lecturesCategory.setSort(Integer.valueOf(map.get("SORT").toString()));
				}
				
				lecturesCategoryService.updateNormalData(lecturesCategory);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
}
