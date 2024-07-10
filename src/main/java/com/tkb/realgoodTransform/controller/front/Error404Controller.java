package com.tkb.realgoodTransform.controller.front;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Error404Controller implements ErrorController {
	@RequestMapping("/error")
	public ModelAndView handleError() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error");
		return modelAndView;
	}
	public String getErrorPath() {
		return "/error";
	}
}
