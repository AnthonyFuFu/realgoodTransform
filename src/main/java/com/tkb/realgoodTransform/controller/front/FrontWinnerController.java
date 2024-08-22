package com.tkb.realgoodTransform.controller.front;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tkb.realgoodTransform.model.NavBanner;
import com.tkb.realgoodTransform.model.Winner;
import com.tkb.realgoodTransform.model.WinnerCategory;
import com.tkb.realgoodTransform.model.WinnerContent;
import com.tkb.realgoodTransform.service.NavBannerService;
import com.tkb.realgoodTransform.service.WinnerCategoryService;
import com.tkb.realgoodTransform.service.WinnerContentService;
import com.tkb.realgoodTransform.service.WinnerService;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.CryptographyUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FrontWinnerController extends BaseUtils {
	
	private int pageNo; // 頁碼
	
	@Autowired
	private WinnerService winnerService;
	
	@Autowired
	private WinnerCategoryService winnerCategoryService;
	
	@Autowired
	private WinnerContentService winnerContentService;
	
	@Autowired
	private NavBannerService navBannerService;

	@GetMapping("/winner/index")
	public String index(Winner winner, WinnerCategory winnerCategory, HttpServletRequest request, Model model,
			@RequestParam(required = false) String parentCategory) {
		List<WinnerCategory> winnerCategoryList = new ArrayList<>();
		List<WinnerCategory> winnerCategoryInnerList = new ArrayList<>();
		List<Winner> winnerList = new ArrayList<>();
		List<Winner> winnerYearList = new ArrayList<>();
		String sort = request.getParameter("sort") == null ? "" : request.getParameter("sort");
		String pageCountStr = request.getParameter("page_count") == null ? "" : request.getParameter("page_count");
		winnerCategory.setParent_id(0);
		winnerCategoryList = winnerCategoryService.getLayerList("1", winnerCategory);

		// 計算父類別有無文章
		for (int i = 0; i < winnerCategoryList.size(); i++) {
			Integer id = winnerCategoryList.get(i).getId();
			Integer count = winnerService.getUseCount_parent(id);
			winnerCategoryList.get(i).setUse_count(count);
		}
		// 類別第二層串接
		for (int i = 0; i < winnerCategoryList.size(); i++) {
			WinnerCategory winnerCategoryTemp = new WinnerCategory();
			winnerCategory = winnerCategoryList.get(i);
			winnerCategoryTemp.setParent_id(winnerCategoryList.get(i).getId());
			winnerCategory.setWinnerCategoryIIList(winnerCategoryService.getLayerList("2", winnerCategoryTemp));
			// 計算子類別有無文章
			for (int j = 0; j < winnerCategory.getWinnerCategoryIIList().size(); j++) {
				Integer id = winnerCategory.getWinnerCategoryIIList().get(j).getId();
				Integer count = winnerService.getUseCount_child(id);
				winnerCategory.getWinnerCategoryIIList().get(j).setUse_count(count);
			}
			winnerCategoryInnerList.add(i, winnerCategory);

		}
		if (parentCategory != null || !"".equals(parentCategory)) {
			winner.setParent_category(parentCategory);
		}
		pageTotalCount = winnerService.getFrontCount(winner);
		if (!"".equals(pageCountStr)) {
			super.pageCount = Integer.valueOf(pageCountStr);
		} else {
			super.pageCount = 8;
		}
		pageNo = super.pageSetting(pageNo);
		winnerList = winnerService.getFrontList(pageCount, pageStart, winner, sort);
		addModelAttribute(pageNo, model);
		winnerYearList = winnerService.getList(winner);
		model.addAttribute("winnerCategoryList", winnerCategoryList)
				.addAttribute("winnerCategoryInnerList", winnerCategoryInnerList).addAttribute("winnerList", winnerList)
				.addAttribute("winnerYearList", winnerYearList).addAttribute("parentCategory", parentCategory);
		
		return "front/winner/list";
	}

	@PostMapping("/winner/toSearch")
	public ResponseEntity<?> toSearch(Winner winner, WinnerCategory winnerCategory, HttpServletRequest request,
			Model model) {
		List<Winner> winnerList = new ArrayList<>();
		List<WinnerCategory> winnerCategoryList = new ArrayList<>();
		String layer = request.getParameter("layer") == null ? "" : request.getParameter("layer");
		String category_id = request.getParameter("category_id") == "" ? "" : request.getParameter("category_id");
		String sort = request.getParameter("sort") == null ? "" : request.getParameter("sort");
		String pageCountStr = request.getParameter("page_count") == null ? "" : request.getParameter("page_count");
		String page_no = request.getParameter("page_no") == null ? "" : request.getParameter("page_no");
		String parent_category_id = request.getParameter("parent_category_id") == "" ? ""
				: request.getParameter("parent_category_id");
		String year = request.getParameter("year") == "" ? "" : request.getParameter("year");
		super.resetPage();
		
		if (!"".equals(category_id)) {
			winner.setCategory(category_id);
		}

		if (!"".equals(year)) {
			winner.setYear(year);
		}

		if (!"".equals(parent_category_id)) {
			winner.setParent_category(parent_category_id);
		}
		if (!"".equals(pageCountStr)) {
			super.pageCount = Integer.valueOf(pageCountStr);
		} else {
			super.pageCount = 8;
		}
		if ("1".equals(layer)) {
			if (!"".equals(category_id)) {
				winnerCategory.setParent_id(Integer.valueOf(category_id));
				winnerCategoryList = winnerCategoryService.getLayerList("2", winnerCategory);
			}

			pageTotalCount = winnerService.getFrontCount(winner, winnerCategoryList);
			pageNo = super.pageSetting(Integer.valueOf(page_no));
			winnerList = winnerService.getFrontList(pageCount, pageStart, winner, winnerCategoryList, sort);
		} else {
			if (!"".equals(category_id)) {
				winner.setCategory(category_id);
			}

			pageTotalCount = winnerService.getFrontCount(winner);
			pageNo = super.pageSetting(Integer.valueOf(page_no));
			winnerList = winnerService.getFrontList(pageCount, pageStart, winner, sort);

		}
		if (winnerList.size() > 0) {
			winnerList.get(0).setPageCount(pageCount);
			winnerList.get(0).setPageTotalCount(pageTotalCount);
			winnerList.get(0).setTotalPage(totalPage);

			winnerList.get(0).setLeftStartPage(leftStartPage);
			winnerList.get(0).setLeftEndPage(leftEndPage);
			winnerList.get(0).setRightStartPage(rightStartPage);
			winnerList.get(0).setRightEndPage(rightEndPage);
			winnerList.get(0).setLeftPageNum(leftPageNum);
			winnerList.get(0).setRightPageNum(rightPageNum);

		}
		addModelAttribute(pageNo, model);
		return new ResponseEntity<>(winnerList, HttpStatus.OK);
	}

	@GetMapping("/winner/categoryTwo")
	public ResponseEntity<?> categoryTwo(HttpServletRequest request, WinnerCategory winnerCategory) {
		List<WinnerCategory> winnerCategoryList = new ArrayList<>();
		String id = request.getParameter("id");
		winnerCategory.setParent_id(Integer.valueOf(id));
		winnerCategoryList = winnerCategoryService.getLayerList("2", winnerCategory);
		return new ResponseEntity<>(winnerCategoryList, HttpStatus.OK);
	}

	@GetMapping("/winner/inside")
	public String inside(@RequestParam String str, Winner winner, WinnerCategory winnerCategory,
			WinnerContent winnerContent, NavBanner navBanner, Model model,HttpServletRequest request) {
		List<Winner> winnerList = new ArrayList<>();
		List<Winner> prevList = new ArrayList<>();
		List<Winner> nextList = new ArrayList<>();
		List<WinnerContent> winnerContentList = new ArrayList<>();
		List<NavBanner> navBannerList = new ArrayList<>();
		List<NavBanner> navBannerType2List = new ArrayList<>();

		String ua = request.getHeader("User-Agent");
		String login_equipment = "";
		if(ua.indexOf("Android")>-1||ua.indexOf("iPhone")>-1||ua.indexOf("iPad")>-1) {
			login_equipment = "MOBILE";
		}else{
			login_equipment = "COM";
		}
		String shareStr = "";
		if ("".equals(str)) {
			return "front/winner/list";
		}
		try {
			winner.setId(Integer.valueOf(CryptographyUtils.staticdecryptStr(str)));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		winner = winnerService.getFrontData(winner);
		winnerList = winnerService.getFrontList(winner);
		winnerService.updateClickRate(winner);
		
		prevList = winnerService.getPrevList(winner);
		if(prevList.size()!=0) {
			winnerCategory.setId(Integer.parseInt(prevList.get(0).getCategory()));
			winnerCategory = winnerCategoryService.getData(winnerCategory);
			prevList.get(0).setCate_name(winnerCategory.getName());
		}
		
		nextList = winnerService.getNextList(winner);
		if(nextList.size()!=0) {
			winnerCategory.setId(Integer.parseInt(nextList.get(0).getCategory()));
			winnerCategory = winnerCategoryService.getData(winnerCategory);
			nextList.get(0).setCate_name(winnerCategory.getName());
		}
		

		winnerContent.setWinner_id(winner.getId());
		winnerContentList = winnerContentService.getList(winnerContent);

		shareStr = str;
		navBannerList = navBannerService.getFrontList(navBanner);
		navBannerType2List = navBannerService.getFrontType2List(navBanner);// 取300*600
		
		
		model.addAttribute("winnerList", winnerList).addAttribute("prevList", prevList)
				.addAttribute("nextList", nextList).addAttribute("winnerContentList", winnerContentList)
				.addAttribute("navBannerList", navBannerList).addAttribute("navBannerType2List", navBannerType2List)
				.addAttribute("login_equipment", login_equipment)
				.addAttribute("winner", winner).addAttribute("begin_date", winner.getCreate_date())
				.addAttribute("update_date", winner.getUpdate_date()).addAttribute("shareStr", shareStr);
		return "front/winner/inside";
	}

	@RequestMapping("/winner/videoIndex")
	public String videoIndex(Winner winner, WinnerCategory winnerCategory, HttpServletRequest request, Model model) {
		List<Winner> winnerYearList = new ArrayList<>();
		List<Winner> winnerVideoIndexList = new ArrayList<>();
		List<Winner> winnerVideoListII = new ArrayList<>();
		List<WinnerCategory> winnerCategoryList = new ArrayList<>();
		winnerYearList = winnerService.getVideoYearList(winner);
		winnerCategory.setParent_id(0);
		winnerCategoryList = winnerCategoryService.getLayerList("1", winnerCategory);
		// 計算父類別有無文章
		for (int i = 0; i < winnerCategoryList.size(); i++) {
			Integer id = winnerCategoryList.get(i).getId();
			Integer count = winnerService.getUseCount_parent(id);
			winnerCategoryList.get(i).setUse_count(count);
			if (winnerCategoryList.get(i).getUse_count() > 0) {
				Winner winnerVideo = new Winner();
				winnerVideo.setCategoryName(winnerCategoryList.get(i).getName());
				winnerVideo.setParent_category(String.valueOf(winnerCategoryList.get(i).getId()));
				winnerVideo.setCategory("0");

				winnerVideo.setVideoList(winnerService.getVideoList(winnerVideo));
				
				if(winnerVideo.getVideoList().size() > 0) {
					winnerVideoListII.add(winnerVideo);
				}else {
					
				}
				

			}
		}
		
		winnerVideoIndexList = winnerService.getVideoHotList(winner);
		model.addAttribute("winnerYearList", winnerYearList).addAttribute("winnerVideoIndexList", winnerVideoIndexList)
				.addAttribute("winnerCategoryList", winnerCategoryList)
				.addAttribute("winnerVideoListII", winnerVideoListII);
		return "front/winner/videoList";
	}

	@GetMapping("/winner/search")
	public ResponseEntity<?> search(HttpServletRequest request, Winner winner, WinnerCategory winnerCategory) {
		String year = request.getParameter("year");
		String category = request.getParameter("category");
		List<Winner> winnerVideoListII = new ArrayList<>();
		List<WinnerCategory> winnerCategoryList = new ArrayList<>();
		winnerCategory.setParent_id(0);
		winnerCategoryList = winnerCategoryService.getLayerList("1", winnerCategory);
		if (category == null || "".equals(category)) {
			// 計算父類別有無文章
			for (int i = 0; i < winnerCategoryList.size(); i++) {
				Integer id = winnerCategoryList.get(i).getId();
				Integer count = winnerService.getUseCount_parent(id);
				winnerCategoryList.get(i).setUse_count(count);
				if (winnerCategoryList.get(i).getUse_count() > 0) {
					Winner winnerVideo = new Winner();
					winnerVideo.setYear(year);
					winnerVideo.setCategoryName(winnerCategoryList.get(i).getName());
					winnerVideo.setParent_category(String.valueOf(winnerCategoryList.get(i).getId()));
					winnerVideo.setCategory("0");
					winnerVideo.setVideoList(winnerService.getVideoList(winnerVideo));
					
					if(winnerVideo.getVideoList().size() > 0) {
						winnerVideoListII.add(winnerVideo);
					}else {
						
					}
				}
			}
		} else {
			Winner winnerVideo = new Winner();
			winnerVideo.setYear(year);
			winnerVideo.setCategoryName(winnerCategoryService.getCategoryName(category, winnerCategory));
			winnerVideo.setParent_category(category);
			winnerVideo.setVideoList(winnerService.getVideoList(winnerVideo));
			winnerVideoListII.add(winnerVideo);
		}

		return new ResponseEntity<>(winnerVideoListII, HttpStatus.OK);
	}

	@PostMapping("/schoolBulletin/winnerUpdateClickRate")
	public void winnerUpdateClickRate(@RequestParam String id, Winner winner) {
		winner.setId(Integer.valueOf(id));
		winnerService.updateClickRate(winner);
	}

}
