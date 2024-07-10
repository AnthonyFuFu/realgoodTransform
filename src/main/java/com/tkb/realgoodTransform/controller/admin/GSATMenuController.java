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
import com.tkb.realgoodTransform.model.GSATMenu;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.GSATMenuService;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 類別Action
 * @author Wen
 * @version 創建時間：2016-10-11
 */
@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class GSATMenuController extends BaseUtils{
	private int pageNo;
	@Autowired
	private GSATMenuService gSATMenuService; //選單服務
	@Autowired
	private EditLogService editLogService;				//各功能編輯的LOG服務

	/**
	 * 清單頁面
	 * @return
	 */
	@RequestMapping(value = "/gSATMenu/index")
	public String index(GSATMenu gSATMenu, Model model,HttpServletRequest request) {
		
			List<GSATMenu>gSATMenuList = new ArrayList<>();
			pageNo = super.setPage(pageNo, request);
    		pageTotalCount = gSATMenuService.getCount(gSATMenu);
    		pageNo = super.pageSetting(pageNo);
    		gSATMenuList = gSATMenuService.getList(pageCount, pageStart, gSATMenu);
    		model.addAttribute("gSATMenuList", gSATMenuList)
    			 .addAttribute("gSATMenu", gSATMenu);
    		addModelAttribute(pageNo, model);
    		return "admin/GSATMenu/GSATMenuList";
	}
	
	/**
	 * 新增頁面
	 * @return
	 */
	@RequestMapping(value = "/gSATMenu/add")
	public String add(GSATMenu gSATMenu, Model model) {
		
		gSATMenu.setName(null);
		gSATMenu.setParent_id(0);
		model.addAttribute("gSATMenu", gSATMenu);
		
		return "admin/GSATMenu/GSATMenuForm";
	}
	
	/**
	 * 新增類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/gSATMenu/addSubmit")
	public String addSubmit(GSATMenu gSATMenu,@SessionAttribute("userAccountSession") User user, Model model) {
		try{
    		gSATMenu.setId(gSATMenuService.getNextId());
    		gSATMenu.setCreate_by(user.getAccount());
    		gSATMenu.setUpdate_by(user.getAccount());
    		gSATMenuService.add(gSATMenu);
    		Gson gson = new Gson();
    		String jsonString = gson.toJson(gSATMenu);
    		EditLog editLog = new EditLog();
    		int logId = editLogService.getNextLogId();
    		editLog.setId(logId);
    		editLog.setData_id(gSATMenu.getId());
    		editLog.setFunction("GSAT_MENU");
    		editLog.setAction_type("ADD");
    		editLog.setContent(jsonString);
    		editLog.setCreate_by(user.getAccount());
    		editLogService.addLog(editLog);
    		model.addAttribute("message", "新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "新增失敗");
		}
		return "admin/GSATMenu/toList";
		
	}
	
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping(value = "/gSATMenu/update")
	public String update(GSATMenu gSATMenu,Model model) {
		gSATMenu = gSATMenuService.getData(gSATMenu);
		model.addAttribute("gSATMenu", gSATMenu);
		return "admin/GSATMenu/GSATMenuForm";
		
	}
	
	/**
	 * 修改類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/gSATMenu/updateSubmit")
	public String updateSubmit(GSATMenu gSATMenu,@SessionAttribute("userAccountSession") User user, Model model) throws IOException {
		
		try{
		    gSATMenu.setUpdate_by(user.getAccount());
		    gSATMenuService.update(gSATMenu);
		    
    		Gson gson = new Gson();
    		String jsonString = gson.toJson(gSATMenu);
    		EditLog editLog = new EditLog();
    		int logId = editLogService.getNextLogId();
    		editLog.setId(logId);
    		editLog.setData_id(gSATMenu.getId());
    		editLog.setFunction("GSAT_MENU");
    		editLog.setAction_type("UPDATE");
    		editLog.setContent(jsonString);
    		editLog.setCreate_by(user.getAccount());
    		editLogService.addLog(editLog);

			model.addAttribute("message", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "修改失敗");
		}
		return "admin/GSATMenu/toList";
	}
	
	@RequestMapping(value = "/gSATMenu/delete")
	public String delete(GSATMenu gSATMenu,HttpServletRequest request,@SessionAttribute("userAccountSession") User user,Model model) throws IOException {
		
		try{
			String selectList = request.getParameter("deleteList");
			String[] selectArray = selectList.split(",");
			Integer id = null;
			for(int i=0; i<selectArray.length; i++) {
				id = Integer.valueOf(selectArray[i]);
    			gSATMenu.setId(id);
    			gSATMenuService.delete(gSATMenu ,id);
        		Gson gson = new Gson();
        		String jsonString = gson.toJson(gSATMenu);
        		EditLog editLog = new EditLog();
        		int logId = editLogService.getNextLogId();
        		editLog.setId(logId);
        		editLog.setData_id(id);
        		editLog.setFunction("GSAT_MENU");
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
		return "admin/GSATMenu/toList";
		
	}
	
	@RequestMapping(value = "/gSATMenu/checkData")
	@ResponseBody
	public String checkData(GSATMenu gSATMenu,HttpServletRequest request) throws IOException {
		List<GSATMenu>gSATMenuList = new ArrayList<>();

		String checkList = request.getParameter("checkList");
		String[] checkArray = checkList.split(",");
		String checkStr = "T";
		for(int i=0; i<checkArray.length; i++) {
			gSATMenu.setParent_id(Integer.valueOf(checkArray[i]));
			gSATMenuList = gSATMenuService.getSubList(gSATMenu);

			if(gSATMenuList.size() > 0) {
				
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
	@RequestMapping(value = "/gSATMenu/checkGSATMenu")
	@ResponseBody
	public String checkGSATMenu(HttpServletRequest request){
		String gSATMenuName = request.getParameter("gSATMenuName");
		String layer = request.getParameter("layer");
		String msg = "";
		
		GSATMenu gSATMenu = new GSATMenu();

		gSATMenu.setName(gSATMenuName);
		gSATMenu.setLayer(layer);

		String returnGSATMenuName = gSATMenuService.checkGSATMenu(gSATMenu);
		
		if(returnGSATMenuName == null) {
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
	@RequestMapping(value = "/gSATMenu/resetSort")
	public String resetSort(GSATMenu gSATMenu,Model model) {
	try {
		gSATMenuService.resetSort(gSATMenu);
		model.addAttribute("message", "重新排序成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "重新排序失敗");
		}
		return "admin/GSATMenu/toList";
	}
	
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/gSATMenu/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		getNormalBannerList = gSATMenuService.getNormalList();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			
			
		
			for(Map<String, Object> map : getNormalBannerList) {
				GSATMenu gSATMenu = new GSATMenu();
				
				if(map.get("ID") != null) {
					gSATMenu.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("PARENT_ID") != null) {
					gSATMenu.setParent_id(Integer.valueOf(map.get("PARENT_ID").toString()));
				}
				if(map.get("NAME") != null) {
					gSATMenu.setName(map.get("NAME").toString());
				}
				if(map.get("LAYER") != null) {
					gSATMenu.setLayer(map.get("LAYER").toString());
				}
				
				if(map.get("CREATE_BY") != null) {
					gSATMenu.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					gSATMenu.setCreate_date(new Timestamp(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					gSATMenu.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					gSATMenu.setUpdate_date(new Timestamp(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("SHOW") != null) {
					gSATMenu.setShow(Integer.valueOf(map.get("SHOW").toString()));
				}
				if(map.get("SORT") != null) {
					gSATMenu.setSort(Integer.valueOf(map.get("SORT").toString()));
				}
				if(map.get("LINK") != null) {
					gSATMenu.setLink(map.get("LINK").toString());
				}
				gSATMenuService.updateNormalData(gSATMenu);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
	
}
