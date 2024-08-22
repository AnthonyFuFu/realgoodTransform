package com.tkb.realgoodTransform.controller.admin;

import java.io.IOException;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.Menu;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.MenuService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = {"userAccountSession", "sideMenuList"})
public class MenuController extends BaseUtils {
	
	private List<Menu> menuList;		//選單清單
	private int pageNo;					//頁碼
	
	@Autowired
	private Menu menu;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private EditLogService editLogService;
	
	/**
	 * 清單頁面
	 * @return
	 */
	@RequestMapping(value = "/menu/index", method = {RequestMethod.GET, RequestMethod.POST})
	public String index(Model model, @ModelAttribute Menu menu,
			HttpServletRequest request, HttpSession session) {
		
		//2022/02/14新增:調整頁碼bug
		if(pageNo != 0) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		//2022/02/17新增，回去index方法要先reset分頁的值
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();
		
		pageTotalCount = menuService.getCount(menu);
		pageNo = super.pageSetting(pageNo);
		menuList = menuService.getList(pageCount, pageStart, menu);
		model.addAttribute("menuList", menuList).addAttribute("pageNo", pageNo).addAttribute("pageTotalCount",pageTotalCount)
			 .addAttribute("pageCount", pageCount).addAttribute("totalPage", totalPage).addAttribute("leftStartPage", leftStartPage)
//			 .addAttribute("pageStart", pageStart).addAttribute("pageMaxNum", pageMaxNum).addAttribute("leftPageNum", leftPageNum)
			 .addAttribute("leftPageNum", leftPageNum)
			 .addAttribute("rightPageNum", rightPageNum).addAttribute("leftEndPage", leftEndPage).addAttribute("rightStartPage", rightStartPage).addAttribute("rightEndPage", rightEndPage);
		
		return "admin/menu/menuList";
		
	}
	

	/**
	 * 新增頁面
	 * @return
	 */
	@RequestMapping(value = "/menu/add", method = {RequestMethod.GET, RequestMethod.POST})
	public String add(Model model, @ModelAttribute Menu menu) {
		
		menu.setName(null);
		
		return "admin/menu/menuForm";
		
	}
	
	/**
	 * 新增類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "menu/addSubmit", method = {RequestMethod.POST})
	public String addSubmit(Menu menu, Model model,
			@SessionAttribute("userAccountSession") User user) throws IOException {
		
		menu.setId(menuService.getNextId());
		if("1".equals(menu.getLayer())) {
			menu.setLink(null);
		}
		if(null == menu.getParent_id()) {
			menu.setParent_id(0);
		}
//		menu.setCreate_by(USER_DATA.getAccount());
//		menu.setUpdate_by(USER_DATA.getAccount());
//		menuService.add(menu);
		menu.setCreate_by(user.getAccount());
		menu.setUpdate_by(user.getAccount());
		menuService.add(menu);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(menu);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(menu.getId());
		editLog.setFunction("MENU");
		editLog.setAction_type("ADD");
		editLog.setContent(jsonString);
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		
		return "redirect:index";
		
	}
	
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping(value = "/menu/update", method = {RequestMethod.GET, RequestMethod.POST})
	public String update(Model model, Menu menu) {
		
		String parent_name = menu.getParent_name();
		
		menu = menuService.getData(menu);
		
		menu.setParent_name(parent_name);
		model.addAttribute(menu);
		
		return "admin/menu/menuForm";
		
	}
	
	/**
	 * 修改類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/menu/updateSubmit", method = {RequestMethod.POST})
	public String updateSubmit(Model model, Menu menu,
			@SessionAttribute("userAccountSession") User user) throws IOException {
		
		if("1".equals(menu.getLayer())) {
			menu.setLink(null);
		}
		menu.setUpdate_by(user.getAccount());
		menuService.update(menu);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(menu);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(menu.getId());
		editLog.setFunction("MENU");
		editLog.setAction_type("UPDATE");
		editLog.setContent(jsonString);
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		
		return "redirect:index";
		
	}
	
	/**
	 * 刪除
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/menu/delete", method = RequestMethod.POST)
	public String delete(HttpServletRequest request,Menu menu, @SessionAttribute("userAccountSession") User user) throws IOException {
		
		String deleteString = request.getParameter("deleteList");
		String[] deleteArray = deleteString.split(",");
		Integer id = null;
		for(int i=0; i<deleteArray.length; i++) {
			id = Integer.valueOf(deleteArray[i]);
			menu.setId(id);
			menu = menuService.getData(menu);
			Gson gson = new Gson();
			String jsonString = gson.toJson(menu);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(menu.getId());
			editLog.setFunction("MENU");
			editLog.setAction_type("DELETE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			menuService.delete(id);
		}
		
		return "redirect:index";
		
	}
	
	/**
	 * 刪除前檢查子清單是否有資料
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/menu/checkData", method = RequestMethod.POST)
	public void checkData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String checkList = request.getParameter("checkList");
		String[] checkArray = checkList.split(",");
		String checkStr = "T";
		
		if(menu == null) {
			menu = new Menu();
		}
		
		for(int i=0; i<checkArray.length; i++) {
			menu.setParent_id(Integer.valueOf(checkArray[i]));
			menuList = menuService.getSubList(menu);
			if(menuList.size() > 0) {
				checkStr = "F";
				break;
			}
		}
		
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(checkStr);
		
	}

}
