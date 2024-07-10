package com.tkb.realgoodTransform.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tkb.realgoodTransform.model.LecturesCategory;
import com.tkb.realgoodTransform.model.NavBanner;
import com.tkb.realgoodTransform.model.WinnerCategory;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/common")
public class FrontCommonController {
	
	@RequestMapping("/personLaw")
	public String getPersonLaw() {
		return "front/common/personInformationProtectionLaw";
	}

	@RequestMapping("/consultation")
	public String getConsultation_bar(LecturesCategory lecturesCategory, Model model, NavBanner navBanner, HttpServletRequest request, WinnerCategory winnerCategory) {

		// 取得PO版人
		String print_id = request.getParameter("print_id");
		// 判斷瀏覽器
		String ua = request.getHeader("User-Agent");
		String login_equipment = "";
		if (ua.indexOf("Android") > -1 || ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1) {
			login_equipment = "MOBILE";
		} else {
			login_equipment = "COM";
		}
		
		model.addAttribute("login_equipment", login_equipment)
			 .addAttribute("print_id", print_id);
		
		return "front/common/consultation_bar";
	}

}
