package com.tkb.realgoodTransform.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.UserAccountService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = {"userAccountSession", "sideMenuList"})
@RequiredArgsConstructor
public class UserAccountController extends BaseUtils {

	private int pageNo;					//頁碼
	
	private final String reqEncryptKey = "tkb-api-req-xxxx";
	private final String repDecryptKey = "tkb-api-rep-xxxx";
	//ERP
	private final String appName = "ERP";
	
	private final UserAccountService userAccountService;		//帳號設定服務

	@RequestMapping(value = "/userAccount/index")
	public String index(@ModelAttribute("userAccount") User user, Model model, HttpServletRequest request, HttpSession session) {
		
		return "admin/userAccount/userAccountList";
	}
}
