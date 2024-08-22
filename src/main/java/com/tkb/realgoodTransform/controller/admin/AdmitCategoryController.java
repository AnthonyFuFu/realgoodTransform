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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.model.AdmitCategory;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.AdmitCategoryService;
import com.tkb.realgoodTransform.service.AdmitService;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


/**
 * 金榜類別Action
 * 
 * @author
 * @version 創建時間：2016-05-17
 */
@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class AdmitCategoryController extends BaseUtils {

	@Autowired
	private AdmitCategoryService admitCategoryService;

	@Autowired
	private AdmitService admitService;

	@Autowired
	private EditLogService editLogService;

	private int pageNo; // 頁碼

	/**
	 * 清單頁面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/admitCategory/index")
	public String index(AdmitCategory admitCategory, Model model, HttpServletRequest request, HttpSession session) {
		List<AdmitCategory> admitCategoryList = new ArrayList<>();
		if (pageNo != 0) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();
		pageTotalCount = admitCategoryService.getCount(admitCategory);
		pageNo = super.pageSetting(pageNo);
		admitCategoryList = admitCategoryService.getList(pageCount, pageStart, admitCategory);

		
		model.addAttribute("admitCategoryList", admitCategoryList);
		addModelAttribute(pageNo, model);
		
		return "admin/admitCategory/admitCategoryList";

	}

	/**
	 * 新增頁面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/admitCategory/add")
	public String add(AdmitCategory admitCategory, Model model) {
		model.addAttribute("admitCategory", admitCategory);
		return "admin/admitCategory/admitCategoryForm";
	}

	/**
	 * 新增金榜類別
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/admitCategory/addSubmit")
	public String addSubmit(AdmitCategory admitCategory,@SessionAttribute("userAccountSession") User user, Model model) throws IOException {
		try {
			admitCategory.setId(admitCategoryService.getNextId());
			admitCategory.setCreate_by(user.getAccount());
			admitCategory.setUpdate_by(user.getAccount());
			admitCategoryService.add(admitCategory);

			Gson gson = new Gson();
			String jsonString = gson.toJson(admitCategory);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(admitCategory.getId());
			editLog.setFunction("ADMIT_CATEGORY");
			editLog.setAction_type("ADD");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			model.addAttribute("message", "新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "新增失敗");
		}
		return "admin/admitCategory/toList";

	}

	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping(value = "/admitCategory/update")
	public String update(AdmitCategory admitCategory,Model model) {
		admitCategory = admitCategoryService.getData(admitCategory);
		model.addAttribute(admitCategory);
		return "admin/admitCategory/admitCategoryForm";
	}
	
	/**
	 * 修改金榜類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/admitCategory/updateSubmit")
	public String updateSubmit(HttpServletRequest request, @SessionAttribute("userAccountSession") User user,AdmitCategory admitCategory,Model model) throws IOException {
		
		try {
		admitCategory.setUpdate_by(user.getAccount());
		admitCategoryService.update(admitCategory);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(admitCategory);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(admitCategory.getId());
		editLog.setFunction("ADMIT_CATEGORY");
		editLog.setAction_type("UPDATE");
		editLog.setContent(jsonString);
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);

		model.addAttribute("message", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "修改失敗");
		}
		return "admin/admitCategory/toList";
	}
	
	@RequestMapping(value = "/admitCategory/delete")
	public String delete(AdmitCategory admitCategory, Model model,@SessionAttribute("userAccountSession") User user, HttpServletRequest request) throws IOException {
		try {
			String deleteString = request.getParameter("deleteList");
			String[] deleteArray = deleteString.split(",");
			Integer id = null;

			for (int i = 0; i < deleteArray.length; i++) {
				id = Integer.valueOf(deleteArray[i]);
				admitCategory.setId(id);
				admitCategoryService.delete(admitCategory, id);

				Gson gson = new Gson();
				String jsonString = gson.toJson(admitCategory);
				EditLog editLog = new EditLog();
				int logId = editLogService.getNextLogId();
				editLog.setId(logId);
				editLog.setData_id(id);
				editLog.setFunction("ADMIT_CATEGORY");
				editLog.setAction_type("DELETE");
				editLog.setContent(jsonString);
				editLog.setCreate_by(user.getAccount());
				editLogService.addLog(editLog);
			}
			model.addAttribute("message", "刪除成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "刪除失敗");
		}
		return "admin/admitCategory/toList";

	}

	@ResponseBody
	@RequestMapping("/admitCategory/checkData")
	public String checkData(HttpServletRequest request) throws IOException {
		
		String checkList = request.getParameter("checkList");
		String[] checkArray = checkList.split(",");
		String checkStr = "T";
		for(int i=0; i<checkArray.length; i++) {
			int countSave=admitService.getCountListByCategory(Integer.valueOf(checkArray[i]));

			if(countSave > 0){
				checkStr = "F";
				break;
			}

		}
		return checkStr;

	}
	
	@ResponseBody
	@RequestMapping("/admitCategory/checkNameRepeat")
	public String checkNameRepeat(HttpServletRequest request) throws IOException {
		String categoryName = request.getParameter("categoryName");
		String checkStr = "T";

		int countSave = admitCategoryService.getCountByName(categoryName);

		if (countSave > 0) {
			checkStr = "F";
		}
		return checkStr;
	}
	
	/**
	 * 重新排序資料
	 * @return
	 */
	@RequestMapping("/admitCategory/resetSort")
	public String resetSort(@ModelAttribute AdmitCategory admitCategory,
			@SessionAttribute("userAccountSession") User user,Model model) {
		try {
		admitCategoryService.resetSort(admitCategory);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(0);
		editLog.setFunction("ADMIT_CATEGORY");
		editLog.setAction_type("UPDATE");
		editLog.setContent("重新排序");
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		
		model.addAttribute("message", "修改成功");
	} catch (Exception e) {
		e.printStackTrace();
		model.addAttribute("message", "修改失敗");
	}
	return "admin/admitCategory/toList";
	}
	
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/admitCategory/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		getNormalBannerList = admitCategoryService.getNormalList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
		
			for(Map<String, Object> map : getNormalBannerList) {
				AdmitCategory admitCategory = new AdmitCategory();
				
				if(map.get("ID") != null) {
					admitCategory.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("PARENT_ID") != null) {
					admitCategory.setParent_id(Integer.valueOf(map.get("PARENT_ID").toString()));
				}
				if(map.get("NAME") != null) {
					admitCategory.setName(map.get("NAME").toString());
				}
				if(map.get("LAYER") != null) {
					admitCategory.setLayer(map.get("LAYER").toString());
				}
				if(map.get("CREATE_BY") != null) {
					admitCategory.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					admitCategory.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					admitCategory.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					admitCategory.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("SORT") != null) {
					admitCategory.setSort(Integer.valueOf(map.get("SORT").toString()));
				}
				admitCategoryService.updateNormalData(admitCategory);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}

}
