package com.tkb.realgoodTransform.controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.tkb.realgoodTransform.model.GroupAction;
import com.tkb.realgoodTransform.model.GroupUser;
import com.tkb.realgoodTransform.model.Groups;
import com.tkb.realgoodTransform.model.Menu;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.GroupsService;
import com.tkb.realgoodTransform.service.MenuService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = {"userAccountSession", "sideMenuList"})
public class GroupsController extends BaseUtils {
	
	private int pageNo;					//頁碼
	
	@Autowired
	private GroupsService groupsService;
	
	@Autowired
	private MenuService menuService;
	
	/**
	 * 清單頁面
	 * @return
	 */
	@RequestMapping(value = "/groups/index", method = {RequestMethod.GET, RequestMethod.POST})
	public String index(@ModelAttribute Groups groups, Model model,
			HttpServletRequest request, HttpSession session) {
		List<Groups> groupsList = new ArrayList<>();
		
		pageNo = setPage(pageNo, request);
		
		//從layout選單取得目前頁面的menu.id
		Integer menu_id_hidden = request.getParameter("menu_id") == null ? 0 : Integer.valueOf(request.getParameter("menu_id"));
		
		pageTotalCount = groupsService.getCount(groups);
		pageNo = super.pageSetting(pageNo);
		groupsList = groupsService.getList(pageCount, pageStart, groups);
		model.addAttribute("groupsList", groupsList).addAttribute("menu_id_hidden", menu_id_hidden);
		addModelAttribute(pageNo, model);
		
		return "admin/groups/groupList";
	}
	
	/**
	 * 新增頁面
	 * @return
	 */
	@RequestMapping(value = "/groups/add", method = {RequestMethod.GET, RequestMethod.POST})
	public String add(Model model, HttpServletRequest request) {
		Integer menu_id_hidden = request.getParameter("menu_id_hidden") == null ? 0 : Integer.valueOf(request.getParameter("menu_id_hidden"));
		model.addAttribute("menu_id_hidden", menu_id_hidden).addAttribute("groups", new Groups());
		return "admin/groups/groupForm";
	}
	
	/**
	 * 新增群組
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "groups/addSubmit", method = RequestMethod.POST)
	public String addSubmit(Groups groups, GroupAction groupAction
			, @SessionAttribute("userAccountSession") User user
			, @RequestParam("menu_list") JSONArray menu_list, Model model) {
		groupsService.addSubmit(groups, groupAction, menu_list, user, model);
		return "admin/toList";
	}
	
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping(value = "/groups/update", method = {RequestMethod.GET, RequestMethod.POST})
	public String update(Groups groups, GroupAction groupAction, Model model, HttpServletRequest request) {
		groupsService.getUpdatePageData(groups, groupAction, model, request);
		return "admin/groups/groupForm";
	}
	
	/**
	 * 修改群組
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "groups/updateSubmit", method = RequestMethod.POST)
	public String updateSubmit(@ModelAttribute Groups groups, GroupAction groupAction,
			@SessionAttribute("userAccountSession") User user, @RequestParam("menu_list") JSONArray menu_list,Model model) throws IOException {
		groupsService.updateSubmit(groups, groupAction, user, menu_list, model);
		return "admin/toList";
	}
	
	/**
	 * 刪除群組
	 * @return
	 */
	@RequestMapping(value = "groups/delete", method = RequestMethod.POST)
	public String delete(Groups groups, HttpServletRequest request,GroupAction groupAction,
			@SessionAttribute("userAccountSession") User user) {
		groupsService.delete(groups, request, groupAction, user);
		return "redirect:index";
	}
	
	/**
	 * 刪除前檢查群組內是否還有員工還有員工
	 * @return ResponseEntity<?>
	 * @throws IOException
	 */
	@RequestMapping(value = "groups/checkData", method = RequestMethod.POST)
	public ResponseEntity<?> checkData(GroupUser groupUser, HttpServletRequest request) throws IOException {
		return groupsService.checkData(groupUser, request);
		
	}
	
	/**
	 * 取得選單清單
	 * @throws Exception
	 */
	@RequestMapping(value = "/groups/getLayer2AllList", method = RequestMethod.POST)
	public ResponseEntity<?> getLayer2AllList(@RequestParam(name = "id", defaultValue = "0") Integer group_id) throws Exception {
		return menuService.getMenuLayer2List(new Menu(), group_id);
	}
	
}
