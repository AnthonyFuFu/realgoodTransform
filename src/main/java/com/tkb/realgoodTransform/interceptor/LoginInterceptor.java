package com.tkb.realgoodTransform.interceptor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.UserAccountService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private UserAccountService userAccountService;
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
	private static final List<String> EXCLUDE_PATH = List.of("/tkbrule/index", "/tkbrule/user/logout", "/tkbrule/tkbAPI/userMasterList");
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		User userAccountSession = (User) request.getSession().getAttribute("userAccountSession");
		
		//已登入
		if(userAccountSession != null) {
			if( "/tkbrule/user/login".equals(request.getServletPath()) || "/tkbrule/".equals(request.getServletPath()) || "/tkbrule".equals(request.getServletPath()) ) {
				response.sendRedirect(request.getContextPath() + "/tkbrule/index"); //暫定導回後台首頁
				return false;
			} else {
				/*權限判斷*/
				String servletPath = request.getServletPath();
				String[] splitPath = servletPath.substring(1).split("/");
				if(!EXCLUDE_PATH.contains(servletPath)) {
					List<String> userMenuList = userAccountService.getUserMenuList(userAccountSession, splitPath[1]);
					if(userMenuList.size() != 0) {
						long startTime = System.currentTimeMillis();
						request.setAttribute("startTime", startTime);
						return true;
					}else {
						response.sendRedirect(request.getContextPath() + "/tkbrule/index");
						return false;
					}
				}
				
				long startTime = System.currentTimeMillis();
				request.setAttribute("startTime", startTime);
				return true;
			}
		//未登入
		} else {
			//請求至登入頁或執行登入動作 則導至請求的目的端
			if( "/tkbrule/user/doLogin".equals(request.getServletPath()) && "GET".equals(request.getMethod()) ) {
				response.sendRedirect(request.getContextPath() + "/tkbrule/user/login");
				return false;
			//請求至非登入頁 或執行非登入動作 導至登入頁
			} else if ( "/tkbrule/user/doLogin".equals(request.getServletPath()) && "POST".equals(request.getMethod()) ) {
				long startTime = System.currentTimeMillis();
				request.setAttribute("startTime", startTime);
				return true;
			} else if ( "/tkbrule/user/login".equals(request.getServletPath()) || "/tkbrule/user/logOut".equals(request.getServletPath()) ) {
				long startTime = System.currentTimeMillis();
				request.setAttribute("startTime", startTime);
				return true;
			} else {
				//自動導至網站首頁
				if( request.getServletPath().indexOf("/admin") >= 0 ) {
					long startTime = System.currentTimeMillis();
					request.setAttribute("startTime", startTime);
					return true;
				} else {
					response.sendRedirect(request.getContextPath() + "/tkbrule/user/login");
					return false;
				}
			}
		}
	
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		long startTime = (Long)request.getAttribute("startTime");
		long endTime = System.currentTimeMillis();
		long executeTime = endTime - startTime;
		
//		modelAndView.addObject("executeTime", executeTime);
		
		logger.info("[" + handler + "] executeTime : " + executeTime + "ms" );
		response.setHeader("Cache-Control", "private");
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

}
