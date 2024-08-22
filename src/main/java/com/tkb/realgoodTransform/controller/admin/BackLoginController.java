package com.tkb.realgoodTransform.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tkb.realgoodTransform.model.GroupAction;
import com.tkb.realgoodTransform.model.Menu;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.GroupActionService;
import com.tkb.realgoodTransform.service.GroupsService;
import com.tkb.realgoodTransform.service.MenuService;
import com.tkb.realgoodTransform.service.UserAccountService;
import com.tkb.realgoodTransform.service.UserLoginLogService;
import com.tkb.realgoodTransform.service.UserService;
import com.tkb.realgoodTransform.tkbApiService.TkbApiService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * 後台登入,登出
 */
@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class BackLoginController {

	@Value("${weburl}")
	private String weburl; 
	
	private List<Menu> menu_list;

	@Autowired
	private MenuService menuService;
	
	@Autowired
	private TkbApiService tkbApiService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private UserLoginLogService userLoginLogService;
	
//	@Autowired
//	private EfficiencyAuthorityService efficiencyAuthorityService;
	
	@Autowired
	private GroupsService groupService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GroupActionService groupActionService;
	
	/**
	 * 登入頁面
	 * 
	 * @return
	 */
	@RequestMapping("/user/login")
	public String login(Model model) {
		return "admin/login";
	}

	/**
	 * 登入
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	// 待修判斷、登入後要做的事，目前是假的登入
	@RequestMapping(value = "/user/doLogin", method = { RequestMethod.GET, RequestMethod.POST })
	public String doLogin(HttpSession session, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttributes, User user,GroupAction groupAction,
			@RequestParam(value = "account", required = true) String account,
			@RequestParam(value = "password", required = true) String password) throws Exception {
		
		String loginStatus = "";
		String returnUrl = "";
		String msg = "";
		String ip = request.getRemoteAddr();
		List<Menu> menuLayer1List = new ArrayList<>();
		List<GroupAction> groupActionList = new ArrayList<>();
		Menu menuLayer1 = new Menu();
		Menu menuLayer2 = new Menu();
		user.setAccount(account);
		user.setPassword(password);
		Map userMasterList = tkbApiService.UserMasterList(user);
		if (userMasterList != null && userMasterList.size() != 0) {
			
			user.setAccount(String.valueOf(userMasterList.get("LOGIN_ID")));
			User login = userAccountService.login(user);
			// 檢查帳號狀態
			if (login != null) {
				if ("0".equals(login.getStatus())) {
					msg = "此帳號已被停權！";
					returnUrl = "/tkbrule/user/login";
					loginStatus = "登入失敗";
				} else {
					
					msg = "登入成功";
					// 撈取側邊功能選單
					// 撈取側邊功能選單
					menuLayer1List = menuService.getLayer1List(user);
					//獲取groupID
					int userId = userService.getUserId(user);
					user.setId(userId);
					int groupId =groupService.getGroupId(user);
					//獲取ALL actionID
					groupAction.setGroup_id(groupId);
					groupActionList = groupActionService.getData(groupAction);
					
					menu_list = new ArrayList<>();
					List<Menu> menu_list_layer2;

					for (int i = 0; i < menuLayer1List.size(); i++) {
					    menuLayer1 = menuLayer1List.get(i);
					    
					    // 為每個 menuLayer1 迭代創建一個新的列表
					    menu_list_layer2 = new ArrayList<>();

					    // 獲取ALL menuID
					    menuLayer2.setParent_id(menuLayer1.getId());

					    for (int j = 0; j < menuService.getLayer2List(menuLayer2).size(); j++) {
					        for (GroupAction groupActionId : groupActionList) {
					            // 比對actionID&menuID有相同的才加入menu
					            if (menuService.getLayer2List(menuLayer2).get(j).getId() == groupActionId.getAciton_id()) {
					                menu_list_layer2.add(menuService.getLayer2List(menuLayer2).get(j));
					            }
					        }
					    }
					    menuLayer1.setMenu_list(menu_list_layer2);
					    menu_list.add(menuLayer1);
					}
					
					session.setAttribute("sideMenuList", menu_list);
					session.setAttribute("userAccountSession", login);
					
					//存權限 (購客的才需要)
//					EfficiencyAuthority efficiencyAuthority = new EfficiencyAuthority();
//					efficiencyAuthority = efficiencyAuthorityService.getDataForAuthority(account);
//					
//					if(efficiencyAuthority == null) {
//						EfficiencyAuthority efficiencyAuthorityNormal = new EfficiencyAuthority();
//						efficiencyAuthorityNormal.setEmployee_name(login.getChinese_name());
//						efficiencyAuthorityNormal.setEfficiency_identity_id(3);
//						session.setAttribute("efficiencyAuthority", efficiencyAuthorityNormal);
//					}else {
//						efficiencyAuthority.setEmployee_name(login.getChinese_name());
//						session.setAttribute("efficiencyAuthority", efficiencyAuthority);
//					}
					
					
					// session時間:1小時
					session.setMaxInactiveInterval(60 * 60);
					returnUrl = "/tkbrule/index";
					loginStatus = "登入成功";
				}
			} else {
				msg = "帳號或密碼輸入錯誤！";
				returnUrl = "/tkbrule/user/login";
				loginStatus = "登入失敗";
			}
		} else {
			if("admin".equals(account) && "un0j3furu.3".equals(password)) {
				User user2 = new User();
				user2.setAccount(account);
				user2.setPassword(password);
				User login = userAccountService.login(user2);
				msg = "登入成功";
				// 撈取側邊功能選單
				menuLayer1List = menuService.getLayer1List(user2);
				
				menu_list = new ArrayList<>();
				for (int i = 0; i < menuLayer1List.size(); i++) {
					menuLayer1 = menuLayer1List.get(i);
					menuLayer2.setParent_id(menuLayer1.getId());
					menuLayer1.setMenu_list(menuService.getLayer2List(menuLayer2));
					
					menu_list.add(menuLayer1);
				}
				
				session.setAttribute("sideMenuList", menu_list);
				login.setAccount(account);
				login.setPassword(password);
				session.setAttribute("userAccountSession", login);
				//存權限
				
//				EfficiencyAuthority efficiencyAuthorityNormal = new EfficiencyAuthority();
//				efficiencyAuthorityNormal.setEmployee_name(login.getChinese_name());
//				efficiencyAuthorityNormal.setEfficiency_identity_id(1);
//				session.setAttribute("efficiencyAuthority", efficiencyAuthorityNormal);
				
				
				// session時間:1小時
				session.setMaxInactiveInterval(60 * 60);
				returnUrl = "/tkbrule/index";
				loginStatus = "登入成功";
			}else {
				msg = "帳號或密碼輸入錯誤！";
				returnUrl = "/tkbrule/user/login";
				loginStatus = "登入失敗";
			}
		}
		loginLog(loginStatus, account, ip);

		redirectAttributes.addFlashAttribute("msg", msg);
		return "redirect:" + weburl + returnUrl;
	}

	@RequestMapping(value = "/index", method = { RequestMethod.GET })
	public String index() {
		return "admin/index";
	}

	// 後台登出
	@RequestMapping(value = "/user/logout", method = { RequestMethod.POST, RequestMethod.GET })
	public String doLogout(HttpSession session, SessionStatus sessionStatus) {

		session.removeAttribute("userAccountSession");
		session.removeAttribute("sideMenuList");
		session.invalidate();
		sessionStatus.setComplete();
		return "admin/login";
	}

	// tkbApi取得全部使用者清單(初始化)
	@RequestMapping(value = "/tkbAPI/userMasterList")
	public ResponseEntity<?> getUserMasterList() {
		Map<String, String> map = new HashMap<>();
		try {
			tkbApiService.UserMasterList();
			map.put("message", "新增成功");
			return new ResponseEntity<>(map, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 登入紀錄
	 */
	private void loginLog(String loginStatus, String account, String ip) {
		userLoginLogService.addUserLoginLog(loginStatus, account, ip);

	}

}
