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
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.model.Winner;
import com.tkb.realgoodTransform.model.WinnerCategory;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.WinnerCategoryService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class WinnerCategoryController extends BaseUtils {
	
	@Autowired
	private WinnerCategoryService winnerCategoryService;
	
	@Autowired
	private EditLogService editLogService;
	
	private int pageNo;							//頁碼
	
	/**
	 * 清單頁面
	 * @return
	 */
	@RequestMapping(value = "/winnerCategory/index", method = {RequestMethod.POST,RequestMethod.GET})
	public String index (@ModelAttribute WinnerCategory winnerCategory, Model model, 
			HttpServletRequest request, HttpSession session) {
		String parent_id = request.getParameter("parent_id") == null ? null : request.getParameter("parent_id");
		String layer = request.getParameter("layer") == null ? null : request.getParameter("layer");
		if(parent_id!=null) {
			winnerCategory.setParent_id(Integer.parseInt(parent_id));
		}
		if(layer!=null) {
			winnerCategory.setLayer(layer);
		}
		List<WinnerCategory>winnerCategoryList = new ArrayList<>();
		pageNo = super.setPage(pageNo, request);
		pageTotalCount = winnerCategoryService.getCount(winnerCategory);
		pageNo = super.pageSetting(pageNo);
		
		winnerCategoryList = winnerCategoryService.getList(pageCount, pageStart, winnerCategory);
		
		model.addAttribute("winnerCategoryList", winnerCategoryList)
		.addAttribute("winnerCategory", winnerCategory);
		addModelAttribute(pageNo, model);
		return "admin/winnerCategory/winnerCategoryList";
	}
	/**
	 * 新增頁面
	 * @return
	 */
	@RequestMapping(value = "/winnerCategory/add", method = {RequestMethod.POST, RequestMethod.GET})
	public String add (@ModelAttribute WinnerCategory winnerCategory) {
		winnerCategory.setName(null);
		return "admin/winnerCategory/winnerCategoryForm";
	}
	/**
	 * 新增類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/winnerCategory/addSubmit", method = {RequestMethod.POST})
	public String addSubmit(@ModelAttribute WinnerCategory winnerCategory, 
			@SessionAttribute("userAccountSession") User user, Model model) throws IOException{
		try {
			winnerCategoryService.addSubmitFunction(winnerCategory, user);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(winnerCategory);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(winnerCategory.getId());
			editLog.setFunction("WINNER_CATEGORY");
			editLog.setAction_type("ADD");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			model.addAttribute("message", "新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "新增失敗");
		}
		model.addAttribute("parent_id", winnerCategory.getParent_id());
		model.addAttribute("layer", winnerCategory.getLayer());
		return "admin/winnerCategory/toList";
	}
	/**
	 * 檢查重複類別名稱
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/winnerCategory/checkRepeat", method = {RequestMethod.POST})
	public ResponseEntity<?>checkRepeat(@ModelAttribute WinnerCategory winnerCategory, @RequestParam("name") String name,
			@RequestParam("layer") String layer)throws IOException{
		return new ResponseEntity<String>(winnerCategoryService.checkRepeatFunction(name, layer, winnerCategory),HttpStatus.OK);
	}
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping(value = "/winnerCategory/update", method = {RequestMethod.POST, RequestMethod.GET})
	public String update (@ModelAttribute WinnerCategory winnerCategory, Model model) {
		model.addAttribute("winnerCategory", winnerCategoryService.updateFunction(winnerCategory));
		return "admin/winnerCategory/winnerCategoryForm";
	}
	/**
	 * 修改類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/winnerCategory/updateSubmit", method = {RequestMethod.POST})
	public String updateSubmit (@ModelAttribute WinnerCategory winnerCategory,
			@SessionAttribute("userAccountSession") User user, Model model) throws IOException{
		try {
			winnerCategoryService.updateSubmitFunction(winnerCategory, user);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(winnerCategory);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(winnerCategory.getId());
			editLog.setFunction("WINNER_CATEGORY");
			editLog.setAction_type("UPDATE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			model.addAttribute("message", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "修改失敗");
		}
		model.addAttribute("parent_id", winnerCategory.getParent_id());
		model.addAttribute("layer", winnerCategory.getLayer());
		return "admin/winnerCategory/toList";
	}
	/**
	 * 刪除類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/winnerCategory/delete", method = {RequestMethod.POST})
	public String delete (@ModelAttribute WinnerCategory winnerCategory, 
			@RequestParam("deleteList") String deleteList,
			@SessionAttribute("userAccountSession") User user, Model model) throws IOException {
		try {
			winnerCategoryService.deleteFunction(winnerCategory, deleteList, user);
			model.addAttribute("message", "刪除成功");
		} catch (IOException e) {
			model.addAttribute("message", "刪除失敗，該分類底下還有活動");
			return "admin/winnerCategory/toList";
		}
		model.addAttribute("parent_id", winnerCategory.getParent_id());
		model.addAttribute("layer", winnerCategory.getLayer());
		return "admin/winnerCategory/toList";

	}
	/**
	 * 檢查類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/winnerCategory/checkData", method = {RequestMethod.POST})
	public ResponseEntity<?> checkData (@ModelAttribute WinnerCategory winnerCategory, 
			@RequestParam("checkList") String checkList, @ModelAttribute Winner winner) throws IOException{
		return new ResponseEntity<String>(winnerCategoryService.checkDataFunction(winnerCategory, winner, checkList),HttpStatus.OK);
	}
	/**
	 * 重新排序資料
	 * @return
	 */
	@RequestMapping(value = "/winnerCategory/resetSort", method = {RequestMethod.POST})
	public String resetSort (@ModelAttribute WinnerCategory winnerCategory,
			@SessionAttribute("userAccountSession") User user,Model model) {
		if(winnerCategory.getParent_id() == null) {
			winnerCategory.setParent_id(0);
		}
		winnerCategoryService.resetSortFunction(winnerCategory);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(0);
		editLog.setFunction("WINNER_CATEGORY");
		editLog.setAction_type("UPDATE");
		editLog.setContent("重新排序");
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		model.addAttribute("message", "重新排序成功");
		model.addAttribute("parent_id", winnerCategory.getParent_id());
		model.addAttribute("layer", winnerCategory.getLayer());
		return "admin/winnerCategory/toList";
	}
	
	
	
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/winnerCategory/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		getNormalBannerList = winnerCategoryService.getNormalList();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		
			for(Map<String, Object> map : getNormalBannerList) {
				WinnerCategory winnerCategory = new WinnerCategory();
				
				if(map.get("ID") != null) {
					winnerCategory.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("PARENT_ID") != null) {
					winnerCategory.setParent_id(Integer.valueOf(map.get("PARENT_ID").toString()));
				}
				if(map.get("NAME") != null) {
					winnerCategory.setName(map.get("NAME").toString());
				}
				if(map.get("LAYER") != null) {
					winnerCategory.setLayer(map.get("LAYER").toString());
				}
				if(map.get("CREATE_BY") != null) {
					winnerCategory.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					winnerCategory.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					winnerCategory.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					winnerCategory.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("SORT") != null) {
					winnerCategory.setSort(Integer.valueOf(map.get("SORT").toString()));
				}
				if(map.get("CODE") != null) {
					winnerCategory.setCategory_code(Integer.valueOf(map.get("CODE").toString()));
				}
				winnerCategoryService.updateNormalData(winnerCategory);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
}
