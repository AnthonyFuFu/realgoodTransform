package com.tkb.realgoodTransform.controller.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.tkb.realgoodTransform.model.Groups;
import com.tkb.realgoodTransform.model.Menu;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.model.UserAccount;
import com.tkb.realgoodTransform.service.GroupsService;
import com.tkb.realgoodTransform.service.MenuService;
import com.tkb.realgoodTransform.service.UserAccountService;
import com.tkb.realgoodTransform.service.UserLoginLogService;
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

	private List<Menu> menu_list;

	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private MenuService menuService;
	
	
	@Autowired
	private TkbApiService tkbApiService;
	@Autowired
	private UserLoginLogService userLoginLogService;

	/**
	 * 登入頁面
	 * 
	 * @return
	 */
	@RequestMapping("/user/login")
	public String login(Model model) {
		
//		List<Map<String, Object>> oracleList = menuService.getListForInsertData();
//		List<Map<String, Object>> postgreList = menuService.getListForChecktData();
//		oracleList.stream().forEach(oracleItem -> {
//			Menu menu = new Menu();
//			menu.setId(oracleItem.get("ID") != null ? Integer.valueOf(oracleItem.get("ID").toString()) : null);
//			menu.setParent_id(oracleItem.get("PARENT_ID") != null ? Integer.valueOf(oracleItem.get("PARENT_ID").toString()) : null);
//			menu.setName(oracleItem.get("NAME") != null ? oracleItem.get("NAME").toString() : null);
//			menu.setLayer(oracleItem.get("LAYER") != null ? oracleItem.get("LAYER").toString() : null);
//			menu.setLink(oracleItem.get("LINK") != null ? oracleItem.get("LINK").toString() : null);
//			menu.setCreate_by(oracleItem.get("CREATE_BY") != null ? oracleItem.get("CREATE_BY").toString() : null);
//			menu.setCreate_date(oracleItem.get("CREATE_DATE") != null ? parseDateString(oracleItem.get("CREATE_DATE").toString()) : null);
//			menu.setUpdate_by(oracleItem.get("UPDATE_BY") != null ? oracleItem.get("UPDATE_BY").toString() : null);
//			menu.setUpdate_date(oracleItem.get("UPDATE_DATE") != null ? parseDateString(oracleItem.get("UPDATE_DATE").toString()) : null);
//			if (postgreList.isEmpty()) {
//			    // 新增
//				menuService.insertForRemake(menu);
//			} else if (postgreList.size() == oracleList.size()) {
//			    // 更新
//				menuService.updateForRemake(menu);
//			} else if (oracleList.size() > postgreList.size()) {
//		        // 新增多出來的數據
//			    List<Map<String, Object>> addDataList = oracleList.subList(postgreList.size(), oracleList.size());
//			    addDataList.forEach(additionalItem -> {
//					Menu add = new Menu();
//					add.setId(additionalItem.get("ID") != null ? Integer.valueOf(additionalItem.get("ID").toString()) : null);
//					add.setParent_id(additionalItem.get("PARENT_ID") != null ? Integer.valueOf(additionalItem.get("PARENT_ID").toString()) : null);
//					add.setName(additionalItem.get("NAME") != null ? additionalItem.get("NAME").toString() : null);
//					add.setLayer(additionalItem.get("LAYER") != null ? additionalItem.get("LAYER").toString() : null);
//					add.setLink(additionalItem.get("LINK") != null ? additionalItem.get("LINK").toString() : null);
//					add.setCreate_by(additionalItem.get("CREATE_BY") != null ? additionalItem.get("CREATE_BY").toString() : null);
//					add.setCreate_date(additionalItem.get("CREATE_DATE") != null ? parseDateString(additionalItem.get("CREATE_DATE").toString()) : null);
//					add.setUpdate_by(additionalItem.get("UPDATE_BY") != null ? additionalItem.get("UPDATE_BY").toString() : null);
//					add.setUpdate_date(additionalItem.get("UPDATE_DATE") != null ? parseDateString(additionalItem.get("UPDATE_DATE").toString()) : null);
//					menuService.insertForRemake(add);
//			    });
//			}
//		});
		
		
//		List<Map<String, Object>> oracleList = userAccountService.getListForInsertData();
//		List<Map<String, Object>> postgreList = userAccountService.getListForChecktData();
//		oracleList.stream().forEach(oracleItem -> {
//			UserAccount userAccount = new UserAccount();
//			userAccount.setId(oracleItem.get("ID") != null ? Integer.valueOf(oracleItem.get("ID").toString()) : null);
//			userAccount.setAccount(oracleItem.get("ACCOUNT") != null ? oracleItem.get("ACCOUNT").toString() : "");
//			userAccount.setChinese_name(oracleItem.get("CHINESE_NAME") != null ? oracleItem.get("CHINESE_NAME").toString() : null);
//			userAccount.setDepartment_no(oracleItem.get("DEPARTMENT_NO") != null ? oracleItem.get("DEPARTMENT_NO").toString() : null);
//			userAccount.setUnit_no(oracleItem.get("UNIT_NO") != null ? oracleItem.get("UNIT_NO").toString() : null);
//			userAccount.setEmail(oracleItem.get("EMAIL") != null ? oracleItem.get("EMAIL").toString() : null);
//			userAccount.setStatus(oracleItem.get("STATUS") != null ? oracleItem.get("STATUS").toString() : null);
//			userAccount.setCreate_by(oracleItem.get("CREATE_BY") != null ? oracleItem.get("CREATE_BY").toString() : null);
//			userAccount.setCreate_date(oracleItem.get("CREATE_DATE") != null ? parseDateString(oracleItem.get("CREATE_DATE").toString()) : null);
//			userAccount.setUpdate_by(oracleItem.get("UPDATE_BY") != null ? oracleItem.get("UPDATE_BY").toString() : null);
//			userAccount.setUpdate_date(oracleItem.get("UPDATE_DATE") != null ? parseDateString(oracleItem.get("UPDATE_DATE").toString()) : null);
//			userAccount.setArea(oracleItem.get("AREA") != null ? oracleItem.get("AREA").toString() : null);
//			if (postgreList.isEmpty()) {
//			    // 新增
//			    userAccountService.insertForRemake(userAccount);
//			} else if (postgreList.size() == oracleList.size()) {
//			    // 更新
//			    userAccountService.updateForRemake(userAccount);
//			} else if (oracleList.size() > postgreList.size()) {
//		        // 新增多出來的數據
//			    List<Map<String, Object>> addDataList = oracleList.subList(postgreList.size(), oracleList.size());
//			    addDataList.forEach(additionalItem -> {
//			        UserAccount add = new UserAccount();
//			        add.setId(additionalItem.get("ID") != null ? Integer.valueOf(additionalItem.get("ID").toString()) : null);
//			        add.setAccount(additionalItem.get("ACCOUNT") != null ? additionalItem.get("ACCOUNT").toString() : "");
//			        add.setChinese_name(additionalItem.get("CHINESE_NAME") != null ? additionalItem.get("CHINESE_NAME").toString() : null);
//			        add.setDepartment_no(additionalItem.get("DEPARTMENT_NO") != null ? additionalItem.get("DEPARTMENT_NO").toString() : null);
//			        add.setUnit_no(additionalItem.get("UNIT_NO") != null ? additionalItem.get("UNIT_NO").toString() : null);
//			        add.setEmail(additionalItem.get("EMAIL") != null ? additionalItem.get("EMAIL").toString() : null);
//			        add.setStatus(additionalItem.get("STATUS") != null ? additionalItem.get("STATUS").toString() : null);
//			        add.setCreate_by(additionalItem.get("CREATE_BY") != null ? additionalItem.get("CREATE_BY").toString() : null);
//			        add.setCreate_date(additionalItem.get("CREATE_DATE") != null ? parseDateString(additionalItem.get("CREATE_DATE").toString()) : null);
//			        add.setUpdate_by(additionalItem.get("UPDATE_BY") != null ? additionalItem.get("UPDATE_BY").toString() : null);
//			        add.setUpdate_date(additionalItem.get("UPDATE_DATE") != null ? parseDateString(additionalItem.get("UPDATE_DATE").toString()) : null);
//			        add.setArea(additionalItem.get("AREA") != null ? additionalItem.get("AREA").toString() : null);
//			        userAccountService.insertForRemake(add);
//			    });
//			}
//		});
		
//		model.addAttribute("userAccountList", userAccountList);
		
		return "admin/login";
	}
	
	
	
	private Date parseDateString(String date) {
	    try {
	        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date parsedDate = inputFormat.parse(date);
	        // 格式化成 yyyy-MM-dd 格式
	        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
	        String formattedDate = outputFormat.format(parsedDate);

	        return outputFormat.parse(formattedDate);
//	        return formattedDate;
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
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
			RedirectAttributes redirectAttributes, User user,
			@RequestParam(value = "account", required = true) String account,
			@RequestParam(value = "password", required = true) String password) throws Exception {
		String loginStatus = "";
		String returnUrl = "";
		String msg = "";
		String ip = request.getRemoteAddr();
		List<Menu> menuLayer1List = new ArrayList<>();
		Menu menuLayer1 = new Menu();
		Menu menuLayer2 = new Menu();
		System.out.println(account);
		System.out.println(password);
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
					returnUrl = "login";
					loginStatus = "登入失敗";
				} else {
					msg = "登入成功";
					// 撈取側邊功能選單
					menuLayer1List = menuService.getLayer1List(user);
					menu_list = new ArrayList<>();
					for (int i = 0; i < menuLayer1List.size(); i++) {
						menuLayer1 = menuLayer1List.get(i);
						menuLayer2.setParent_id(menuLayer1.getId());
						menuLayer1.setMenu_list(menuService.getLayer2List(menuLayer2));
						menu_list.add(menuLayer1);
					}
					session.setAttribute("sideMenuList", menu_list);
					session.setAttribute("userAccountSession", login);
					// session時間:1小時
					session.setMaxInactiveInterval(60 * 60);
					returnUrl = "/tkbrule/index";
					loginStatus = "登入成功";
				}
			} else {
				msg = "帳號或密碼輸入錯誤！";
				returnUrl = "login";
				loginStatus = "登入失敗";
			}
		} else {
			msg = "帳號或密碼輸入錯誤！";
			returnUrl = "login";
			loginStatus = "登入失敗";
		}
		loginLog(loginStatus, account, ip);

		redirectAttributes.addFlashAttribute("msg", msg);
		return "redirect:" + returnUrl;
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
