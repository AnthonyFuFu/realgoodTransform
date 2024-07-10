package com.tkb.realgoodTransform.controller.admin;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.model.GSATCategory;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.GSATCategoryService;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * 類別Action
 */
@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class GSATCategoryController extends BaseUtils{

	@Autowired
	private GSATCategoryService gSATCategoryService;
	@Autowired
	private EditLogService editLogService;				//各功能編輯的LOG服務
	private int pageNo;

	/**
	 * 清單頁面
	 * @return
	 */
	@RequestMapping(value = "/gSATCategory/index")
	public String index(GSATCategory gSATCategory, Model model,HttpServletRequest request, HttpSession session) {
		List<GSATCategory>gSATCategoryList = new ArrayList<>();
		pageNo = super.setPage(pageNo, request);
		pageTotalCount = gSATCategoryService.getCount(gSATCategory);
		pageNo = super.pageSetting(pageNo);
		gSATCategoryList = gSATCategoryService.getList(pageCount, pageStart, gSATCategory);
		model.addAttribute("gSATCategoryList", gSATCategoryList)
			 .addAttribute("gSATCategory", gSATCategory);
		addModelAttribute(pageNo, model);
		return "admin/GSATCategory/GSATCategoryList";
	}
	
	/**
	 * 新增頁面
	 * @return
	 */
	@RequestMapping(value = "/gSATCategory/add")
	public String add(GSATCategory gSATCategory, Model model) {
		
		gSATCategory.setName(null);
		gSATCategory.setParent_id(0);
		model.addAttribute("gSATCategory", gSATCategory);
		
		return "admin/GSATCategory/GSATCategoryForm";
		
	}
	
	/**
	 * 新增類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/gSATCategory/addSubmit")
	public String addSubmit(GSATCategory gSATCategory,@SessionAttribute("userAccountSession") User user, Model model) {
		try{
		gSATCategory.setId(gSATCategoryService.getNextId());
		gSATCategory.setCreate_by(user.getAccount());
		gSATCategory.setUpdate_by(user.getAccount());
		gSATCategoryService.add(gSATCategory);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(gSATCategory);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(gSATCategory.getId());
		editLog.setFunction("GSAT_CATEGORY");
		editLog.setAction_type("ADD");
		editLog.setContent(jsonString);
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		model.addAttribute("message", "新增成功");

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "新增失敗");
		}
		return "admin/GSATCategory/toList";
		
	}
	
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping(value = "/gSATCategory/update")
	public String update(GSATCategory gSATCategory,Model model) {
		gSATCategory = gSATCategoryService.getData(gSATCategory);
		model.addAttribute("gSATCategory", gSATCategory);
		return "admin/GSATCategory/GSATCategoryForm";
	}
	
	/**
	 * 修改類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/gSATCategory/updateSubmit")
	public String updateSubmit(GSATCategory gSATCategory,@SessionAttribute("userAccountSession") User user, Model model){
		try{
		
				gSATCategory.setUpdate_by(user.getAccount());
				gSATCategoryService.update(gSATCategory);
				
				Gson gson = new Gson();
				String jsonString = gson.toJson(gSATCategory);
				EditLog editLog = new EditLog();
				int logId = editLogService.getNextLogId();
				editLog.setId(logId);
				editLog.setData_id(gSATCategory.getId());
				editLog.setFunction("GSAT_CATEGORY");
				editLog.setAction_type("UPDATE");
				editLog.setContent(jsonString);
				editLog.setCreate_by(user.getAccount());
				editLogService.addLog(editLog);
		
				model.addAttribute("message", "修改成功");
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("message", "修改失敗");
			}
			return "admin/GSATCategory/toList";
}
	@RequestMapping(value = "/gSATCategory/delete")
	public String delete(GSATCategory gSATCategory,HttpServletRequest request,@SessionAttribute("userAccountSession") User user,Model model){
		
		try{
			String selectList = request.getParameter("deleteList");
			String[] selectArray = selectList.split(",");
			Integer id = null;
			for(int i=0; i<selectArray.length; i++) {
			id = Integer.valueOf(selectArray[i]);
			gSATCategory.setId(id);
			gSATCategoryService.delete(gSATCategory ,id);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(gSATCategory);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(id);
			editLog.setFunction("GSAT_CATEGORY");
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
			return "admin/GSATCategory/toList";
	}
	
	
	@RequestMapping(value = "/gSATCategory/checkData")
	@ResponseBody
	public String checkData(GSATCategory gSATCategory,HttpServletRequest request) {
		List<GSATCategory> gSATCategoryList = new ArrayList<>();

		String checkList = request.getParameter("checkList");
		String[] checkArray = checkList.split(",");
		String checkStr = "T";
		
		for(int i=0; i<checkArray.length; i++) {

			gSATCategory.setParent_id(Integer.valueOf(checkArray[i]));
			gSATCategoryList = gSATCategoryService.getSubList(gSATCategory);

			if(gSATCategoryList.size() > 0) {
				
				checkStr = "F";
				break;
			}
		}
		return checkStr;
	}	
	
	/**
	 * 檢查重複類別名稱
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/gSATCategory/checkGSATCategory")
	@ResponseBody
	public String checkGSATCategory(HttpServletRequest request){
		String gSATCategoryName = request.getParameter("gSATCategoryName");
		String layer = request.getParameter("layer");
		String msg = "";
		
		GSATCategory gSATCategory = new GSATCategory();

		gSATCategory.setName(gSATCategoryName);
		gSATCategory.setLayer(layer);

		String returngSATCategoryName = gSATCategoryService.checkGSATCategory(gSATCategory);
		
		if(returngSATCategoryName == null) {
			msg = "true";
		} else {
			msg = "false";
		}
		return msg;
	}		
	
	/**
	 * 重新排序資料
	 * @return
	 */
	@RequestMapping(value = "/gSATCategory/resetSort")
	public String resetSort(GSATCategory gSATCategory,Model model) {
		try {
			gSATCategoryService.resetSort(gSATCategory);
			model.addAttribute("message", "重新排序成功");
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("message", "重新排序失敗");
			}
			return "admin/GSATCategory/toList";
		}
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/gSATCategory/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalList = new ArrayList<>();
		getNormalList = gSATCategoryService.getNormalList();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			for(Map<String, Object> map : getNormalList) {
				GSATCategory gSATCategory = new GSATCategory();
				
				if(map.get("ID") != null) {
					gSATCategory.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("PARENT_ID") != null) {
					gSATCategory.setParent_id(Integer.valueOf(map.get("PARENT_ID").toString()));
				}
				if(map.get("NAME") != null) {
					gSATCategory.setName(map.get("NAME").toString());
				}
				if(map.get("LAYER") != null) {
					gSATCategory.setLayer(map.get("LAYER").toString());
				}
				
				if(map.get("CREATE_BY") != null) {
					gSATCategory.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					gSATCategory.setCreate_date(new Timestamp(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					gSATCategory.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					gSATCategory.setUpdate_date(new Timestamp(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("SORT") != null) {
					gSATCategory.setSort(Integer.valueOf(map.get("SORT").toString()));
				}
				
				gSATCategoryService.updateNormalData(gSATCategory);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
	
}
