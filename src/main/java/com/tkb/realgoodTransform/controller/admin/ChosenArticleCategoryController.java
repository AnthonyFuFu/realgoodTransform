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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.model.ChosenArticleCategory;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.ChosenArticleCategoryService;
import com.tkb.realgoodTransform.service.ChosenArticleService;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;


/**
 * 金榜類別Action
 * 
 * @author
 * @version 創建時間：2016-05-17
 */
@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class ChosenArticleCategoryController extends BaseUtils {

	@Autowired
	private ChosenArticleCategoryService chosenArticleCategoryService; // 精選文章類別服務
	@Autowired
	private EditLogService editLogService; // 各功能編輯的LOG服務
	@Autowired
	private ChosenArticleService chosenArticleService; // 精選文章服務

	private int pageNo; // 頁碼

	/**
	 * 清單頁面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/chosenArticleCategory/index")
	public String index(ChosenArticleCategory chosenArticleCategory, Model model, HttpServletRequest request) {
		List<ChosenArticleCategory> chosenArticleCategoryList = new ArrayList<>();

		if (pageNo != 0) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();
		pageTotalCount = chosenArticleCategoryService.getCount(chosenArticleCategory);
		pageNo = super.pageSetting(pageNo);
		chosenArticleCategoryList = chosenArticleCategoryService.getList(pageCount, pageStart, chosenArticleCategory);
		model.addAttribute("chosenArticleCategoryList", chosenArticleCategoryList);
		addModelAttribute(pageNo, model);
		return "admin/chosenArticleCategory/chosenArticleCategoryList";

	}

	/**
	 * 新增頁面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/chosenArticleCategory/add")
	public String add(ChosenArticleCategory chosenArticleCategory) {
		return "admin/chosenArticleCategory/chosenArticleCategoryForm";
	}

	/**
	 * 新增精選文章類別
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/chosenArticleCategory/addSubmit")
	public String addSubmit(ChosenArticleCategory chosenArticleCategory,
			@SessionAttribute("userAccountSession") User user, Model model) {

		try {
			chosenArticleCategory.setId(chosenArticleCategoryService.getNextId());
			chosenArticleCategory.setCreate_by(user.getAccount());
			chosenArticleCategory.setUpdate_by(user.getAccount());
			chosenArticleCategoryService.add(chosenArticleCategory);

			Gson gson = new Gson();
			String jsonString = gson.toJson(chosenArticleCategory);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(chosenArticleCategory.getId());
			editLog.setFunction("CHOSEN_ARTICLE_CATEGORY");
			editLog.setAction_type("ADD");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			model.addAttribute("message", "新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "新增失敗");
		}
		return "admin/chosenArticleCategory/toList";

	}

	
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping(value = "/chosenArticleCategory/update")
	public String update(ChosenArticleCategory chosenArticleCategory,Model model) {
		
		
		String parent_name = chosenArticleCategory.getParent_name();
		
		chosenArticleCategory = chosenArticleCategoryService.getData(chosenArticleCategory);
		
		chosenArticleCategory.setParent_name(parent_name);
		
		model.addAttribute("chosenArticleCategory",chosenArticleCategory);
		return "admin/chosenArticleCategory/chosenArticleCategoryForm";
		
	}
	
	/**
	 * 修改精選文章類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/chosenArticleCategory/updateSubmit")
	public String updateSubmit(ChosenArticleCategory chosenArticleCategory,@SessionAttribute("userAccountSession") User user, Model model){
		try {
		chosenArticleCategory.setUpdate_by(user.getAccount());
		chosenArticleCategoryService.update(chosenArticleCategory);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(chosenArticleCategory);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(chosenArticleCategory.getId());
		editLog.setFunction("CHOSEN_ARTICLE_CATEGORY");
		editLog.setAction_type("UPDATE");
		editLog.setContent(jsonString);
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		model.addAttribute("message", "更新成功");
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "更新失敗");
		}
		return "admin/chosenArticleCategory/toList";
		
	}
	@RequestMapping(value = "/chosenArticleCategory/delete")
	public String delete(ChosenArticleCategory chosenArticleCategory, Model model,
			@SessionAttribute("userAccountSession") User user, HttpServletRequest request) {
		try {
			String deleteString = request.getParameter("deleteList");
			String[] deleteArray = deleteString.split(",");
			Integer id = null;
			for (int i = 0; i < deleteArray.length; i++) {
				id = Integer.valueOf(deleteArray[i]);
				chosenArticleCategoryService.delete(id);
				Gson gson = new Gson();
				String jsonString = gson.toJson(chosenArticleCategory);
				EditLog editLog = new EditLog();
				int logId = editLogService.getNextLogId();
				editLog.setId(logId);
				editLog.setData_id(id);
				editLog.setFunction("CHOSEN_ARTICLE_CATEGORY");
				editLog.setAction_type("DELETE");
				editLog.setContent(jsonString);
				editLog.setCreate_by(user.getAccount());
				editLogService.addLog(editLog);
				model.addAttribute("message", "刪除成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "刪除失敗");
		}
		return "admin/chosenArticleCategory/toList";

	}

	@ResponseBody
	@RequestMapping(value = "/chosenArticleCategory/checkData")
	public String checkData(HttpServletRequest request) {
		String checkList = request.getParameter("checkList");
		String[] checkArray = checkList.split(",");
		String checkStr = "T";

		for (int i = 0; i < checkArray.length; i++) {

			int countSave = chosenArticleService.getCountListByCategory(Integer.valueOf(checkArray[i]));

			if (countSave > 0) {
				checkStr = "F";
				break;
			}

		}
		return checkStr;
	}

	@RequestMapping(value = "/chosenArticleCategory/checkNameRepeat")
	public ResponseEntity<?> checkNameRepeat(HttpServletRequest request) {
		String categoryName = request.getParameter("categoryName");
		int countSave = chosenArticleCategoryService.getCountByName(categoryName);
		if (countSave == 0) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	/**
	 * 重新排序資料
	 * 
	 * @return
	 */
	@PutMapping("/chosenArticleCategory/resetSort")
	public String resetSort(ChosenArticleCategory chosenArticleCategory, Model model) {
		try {
			chosenArticleCategoryService.resetSort(chosenArticleCategory);
			model.addAttribute("message", "重新排序成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "重新排序失敗");
		}
		return "admin/chosenArticleCategory/toList";

	}
	
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/chosenArticleCategory/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		getNormalBannerList = chosenArticleCategoryService.getNormalList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		
			for(Map<String, Object> map : getNormalBannerList) {
				ChosenArticleCategory chosenArticleCategory = new ChosenArticleCategory();
				
				if(map.get("ID") != null) {
					chosenArticleCategory.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("PARENT_ID") != null) {
					chosenArticleCategory.setParent_id(Integer.valueOf(map.get("PARENT_ID").toString()));
				}
				if(map.get("NAME") != null) {
					chosenArticleCategory.setName(map.get("NAME").toString());
				}
				if(map.get("LAYER") != null) {
					chosenArticleCategory.setLayer(map.get("LAYER").toString());
				}
				if(map.get("CREATE_BY") != null) {
					chosenArticleCategory.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					chosenArticleCategory.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					chosenArticleCategory.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					chosenArticleCategory.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("SORT") != null) {
					chosenArticleCategory.setSort(Integer.valueOf(map.get("SORT").toString()));
				}
				chosenArticleCategoryService.updateNormalData(chosenArticleCategory);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
}
