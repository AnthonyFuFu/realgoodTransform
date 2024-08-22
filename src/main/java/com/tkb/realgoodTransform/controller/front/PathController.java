package com.tkb.realgoodTransform.controller.front;

import java.util.ArrayList;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tkb.realgoodTransform.model.LecturesCategory;
import com.tkb.realgoodTransform.model.NavBanner;
import com.tkb.realgoodTransform.model.WinnerCategory;
import com.tkb.realgoodTransform.service.LecturesCategoryService;
import com.tkb.realgoodTransform.service.NavBannerService;
import com.tkb.realgoodTransform.service.WinnerCategoryService;
import com.tkb.realgoodTransform.service.WinnerService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PathController {
	
	@Autowired
	private LecturesCategoryService lecturesCategoryService;
	
	@Autowired
	private NavBannerService navBannerService;
	
	@Autowired
	private WinnerCategoryService winnerCategoryService;
	
	@Autowired
	private WinnerService winnerService;
	
 @RequestMapping(value="/common/{pathName}")
 public String path(@PathVariable(value="pathName") String pathName,LecturesCategory lecturesCategory,Model model,NavBanner navBanner,HttpServletRequest request,WinnerCategory winnerCategory) {
	 
	 List<LecturesCategory>courseCategoryList = new ArrayList<>();
	 List<LecturesCategory>campCategoryList = new ArrayList<>();
//	 List<LecturesCategory>footerList = new ArrayList<>();
	 List<NavBanner>navBannerList = new ArrayList<>();
	 List<NavBanner>navBannerType2List = new ArrayList<>();
	 List<WinnerCategory>winnerCategoryList = new ArrayList<>();
//	//先列出footer所需的所有第2層課程
//		footerList = lecturesCategoryService.getFrontList(lecturesCategory);	
	 //header常態課程與營隊課程連結
		lecturesCategory.setParent_id(1);
		courseCategoryList = lecturesCategoryService.getFrontList(lecturesCategory);
		lecturesCategory.setParent_id(2);
		campCategoryList = lecturesCategoryService.getFrontList(lecturesCategory);
		//舊業面側邊廣告
		navBannerList = navBannerService.getFrontList(navBanner);
		navBannerType2List = navBannerService.getFrontType2List(navBanner);//取300*600
		//取得PO版人
				String print_id = request.getParameter("print_id");
				//判斷瀏覽器
				String ua = request.getHeader("User-Agent");
				String login_equipment = "";
				if(ua.indexOf("Android") >-1||ua.indexOf("iPhone") >-1||ua.indexOf("iPad") >-1){
					login_equipment = "MOBILE";
				}else{
					login_equipment = "COM";
				}
		winnerCategory.setParent_id(0);
		winnerCategoryList = winnerCategoryService.getLayerList("1",winnerCategory);
		//計算父類別有無文章
		for(int i=0; i<winnerCategoryList.size();i++){
		Integer id = winnerCategoryList.get(i).getId();
		Integer count = winnerService.getUseCount_parent(id);
		winnerCategoryList.get(i).setUse_count(count);
			    }
		model.addAttribute("courseCategoryList", courseCategoryList)
		     .addAttribute("campCategoryList", campCategoryList)
//		     .addAttribute("footerList", footerList)
		     .addAttribute("navBannerList", navBannerList)
		     .addAttribute("navBannerType2List", navBannerType2List)
		     .addAttribute("login_equipment", login_equipment)
		     .addAttribute("print_id", print_id)
		     .addAttribute("winnerCategoryList", winnerCategoryList);
	 return "front/common/"+pathName;
 }
 //舊業面側邊廣告更新點擊率
 public void navBannerUpdateClickRate(@RequestParam Integer id,NavBanner navBanner) {
	 navBanner.setId(id);
	 navBannerService.updateClickRate(navBanner);
 }
 @GetMapping("/search")
 public String search (@RequestParam String keyword , Model model ) {
	 if(keyword==null||"".equals(keyword)){
 		keyword = ""; 
 	}
	 model.addAttribute("keyword", keyword);
	 return "front/search";
 }
}
