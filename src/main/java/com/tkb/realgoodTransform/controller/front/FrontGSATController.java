package com.tkb.realgoodTransform.controller.front;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tkb.realgoodTransform.model.GSAT;
import com.tkb.realgoodTransform.model.GSATAdmit;
import com.tkb.realgoodTransform.model.GSATMenu;
import com.tkb.realgoodTransform.model.GSATNews;
import com.tkb.realgoodTransform.model.GSATWinner;
import com.tkb.realgoodTransform.model.GSATWinnerContent;
import com.tkb.realgoodTransform.model.NavBanner;
import com.tkb.realgoodTransform.service.GSATAdmitService;
import com.tkb.realgoodTransform.service.GSATMenuService;
import com.tkb.realgoodTransform.service.GSATNewsService;
import com.tkb.realgoodTransform.service.GSATService;
import com.tkb.realgoodTransform.service.GSATSideBannerService;
import com.tkb.realgoodTransform.service.GSATWinnerContentService;
import com.tkb.realgoodTransform.service.GSATWinnerService;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.CryptographyUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FrontGSATController extends BaseUtils {

	private int pageNo; // 頁籤編號
	private String shareStr;
	@Autowired
	private GSATService gSATService; // 外交特考文章服務
	@Autowired
	private GSATMenuService gSATMenuService; // 選單服務
	@Autowired
	private GSATAdmitService gSATAdmitService; // 外交特考考取金榜文章服務
	@Autowired
	private GSATWinnerService gSATWinnerService; // 外交特考贏家經驗談服務
	@Autowired
	private GSATWinnerContentService gSATWinnerContentService; // 贏家經驗談內容服務
	@Autowired
	private GSATNewsService gSATNewsService; // 外特最新消息服務
	@Autowired
	private GSATSideBannerService gSATSideBannerService; // 廣告服務

	@RequestMapping("/GSAT/index")
	public String indexGSAT(GSAT gSAT, GSATMenu gSATMenu, HttpServletRequest request, Model model, NavBanner navBanner) throws UnsupportedEncodingException {
		List<GSATMenu> gSATMenuList = new ArrayList<>();
		List<GSAT> gSATArticleList = new ArrayList<>();
		List<NavBanner> navBannerList = new ArrayList<>();
		List<NavBanner> navBannerType2List = new ArrayList<>();

		String ua = request.getHeader("User-Agent");
		String login_equipment = "";
		if (ua.indexOf("Android") > -1 || ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1) {
			login_equipment = "MOBILE";
		} else {
			login_equipment = "COM";
		}

		String str = request.getParameter("str") == null ? "" : request.getParameter("str");
		if ("".equals(str)) {
			return "list";
		}

		gSAT.setId(Integer.valueOf(CryptographyUtils.staticdecryptStr(str)));

		shareStr = str;
		try {
			gSAT = gSATService.getFrontData(gSAT);
			gSATMenuList = gSATMenuService.getList(gSATMenu);
			gSATArticleList = gSATService.getCategoryList(gSAT);
//			navBannerList = gSATSideBannerService.getFrontList(navBanner);
//			navBannerType2List = gSATSideBannerService.getFrontType2List(navBanner);// 取300*600
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (gSATArticleList.size() != 0) {
			for (int i = 0; i < gSATArticleList.size(); i++) {
				// 加密ID
				gSATArticleList.get(i).setEncrypt_id(CryptographyUtils.encryptStr(String.valueOf(gSATArticleList.get(i).getId())));
				// 判斷tab再第幾個
				if (str.equals(gSATArticleList.get(i).getEncrypt_id())) {
					gSAT.setSort(i + 1);
				}
			}
		}
		model.addAttribute("gSATMenuList", gSATMenuList)
			 .addAttribute("gSATArticleList", gSATArticleList)
			 .addAttribute("navBannerList", navBannerList)
			 .addAttribute("navBannerType2List", navBannerType2List)
			 .addAttribute("gSAT", gSAT)
			 .addAttribute("login_equipment", login_equipment);
		return "front/GSAT/inside";

	}

	/*
	 * 網址加密
	 */
	@ResponseBody
	@RequestMapping("/GSAT/toEncrypt")
	public String toEncrypt(HttpServletRequest request) throws IOException {
		int id = request.getParameter("id") == null ? 0 : Integer.valueOf(request.getParameter("id"));
		return CryptographyUtils.encryptStr(String.valueOf(id));
	}

	@RequestMapping("/GSAT/list")
	public String list(GSATWinner gSATWinner, GSATMenu gSATMenu, NavBanner navBanner,
			Model model, HttpServletRequest request) {
		/*
		 * 高手經驗談清單頁面
		 */
		String replaceName = ""; // 遮掉姓名
		List<NavBanner> navBannerList = new ArrayList<>();
		List<NavBanner> navBannerType2List = new ArrayList<>();
		List<GSATWinner> gSATWinnerList = new ArrayList<>();
		List<GSATMenu> gSATMenuList = new ArrayList<>();
		String page_no = request.getParameter("pageNo") == null ? "0" : request.getParameter("pageNo");
		String pageCountStr = request.getParameter("pageCount") == null ? "" : request.getParameter("pageCount");

		String ua = request.getHeader("User-Agent");
		String login_equipment = "";
		if (ua.indexOf("Android") > -1 || ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1) {
			login_equipment = "MOBILE";
		} else {
			login_equipment = "COM";
		}

		if (!"".equals(pageCountStr)) {
			super.pageCount = Integer.valueOf(pageCountStr);
		} else {
			super.pageCount = 10;
		}
		pageTotalCount = gSATWinnerService.getFrontCount(gSATWinner);
		pageNo = super.pageSetting(Integer.valueOf(page_no));
		try {
			gSATMenuList = gSATMenuService.getList(gSATMenu);
			gSATWinnerList = gSATWinnerService.getFrontList(pageCount, pageStart, gSATWinner);
			navBannerList = gSATSideBannerService.getFrontList(navBanner);
			navBannerType2List = gSATSideBannerService.getFrontType2List(navBanner);// 取300*600
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}
		/*
		 * 姓名長度判斷 清單長度判斷 連結加密
		 */
		try {
			if (gSATWinnerList.size() != 0) {
				for (int i = 0; i < gSATWinnerList.size(); i++) {
					// 加密ID
					gSATWinnerList.get(i).setFront_url(CryptographyUtils.encryptStr(String.valueOf(gSATWinnerList.get(i).getId())));
					// 遮敝姓名長度4:XOXO 長度3:XOX
					if (gSATWinnerList.get(i).getName().length() == 3) {
						replaceName += gSATWinnerList.get(i).getName().charAt(0);
						replaceName += "O";
						replaceName += gSATWinnerList.get(i).getName().charAt(2);
						gSATWinnerList.get(i).setName(replaceName);
					} else if (gSATWinnerList.get(i).getName().length() > 3) {
						replaceName += gSATWinnerList.get(i).getName().charAt(0);
						replaceName += "O";
						replaceName += gSATWinnerList.get(i).getName().charAt(2);
						replaceName += "O";
						gSATWinnerList.get(i).setName(replaceName);
					} else if (gSATWinnerList.get(i).getName().length() < 3) {
						replaceName += gSATWinnerList.get(i).getName().charAt(0);
						replaceName += "O";
						gSATWinnerList.get(i).setName(replaceName);
					}
					replaceName = "";
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		model.addAttribute("gSATMenuList", gSATMenuList)
			 .addAttribute("gSATWinnerList", gSATWinnerList)
			 .addAttribute("navBannerList", navBannerList)
			 .addAttribute("navBannerType2List", navBannerType2List)
			 .addAttribute("login_equipment", login_equipment);
		addModelAttribute(pageNo, model);
		return "front/GSATWinner/list";

	}

	@RequestMapping("/GSAT/admit")
	public String admit(GSATAdmit gSATAdmit, GSATMenu gSATMenu, NavBanner navBanner,
			Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		List<NavBanner> navBannerList = new ArrayList<>();
		List<NavBanner> navBannerType2List = new ArrayList<>();
		List<GSATAdmit> gSATAdmitList = new ArrayList<>();
		List<GSATMenu> gSATMenuList = new ArrayList<>();

		String ua = request.getHeader("User-Agent");
		String login_equipment = "";
		if (ua.indexOf("Android") > -1 || ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1) {
			login_equipment = "MOBILE";
		} else {
			login_equipment = "COM";
		}

		try {
			gSATMenuList = gSATMenuService.getList(gSATMenu);
			gSATAdmitList = gSATAdmitService.getFrontList(gSATAdmit);
			navBannerList = gSATSideBannerService.getFrontList(navBanner);
			navBannerType2List = gSATSideBannerService.getFrontType2List(navBanner);// 取300*600
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		model.addAttribute("navBannerList", navBannerList)
			 .addAttribute("navBannerType2List", navBannerType2List)
			 .addAttribute("gSATMenuList", gSATMenuList)
			 .addAttribute("gSATAdmitList", gSATAdmitList)
			 .addAttribute("login_equipment", login_equipment);
		return "front/GSAT/admitList";

	}

	/*
	 * 外特專區贏家經驗談專區
	 */
	@RequestMapping("/GSAT/WinnerInside")
	public String WinnerInside(NavBanner navBanner, GSATWinner gSATWinner, GSATMenu gSATMenu,
			GSATWinnerContent gSATWinnerContent, HttpServletRequest request, Model model) throws UnsupportedEncodingException {
		List<NavBanner> navBannerList = new ArrayList<>();
		List<NavBanner> navBannerType2List = new ArrayList<>();
		List<GSATWinner> gSATWinnerList = new ArrayList<>();
		List<GSATMenu> gSATMenuList = new ArrayList<>();
		List<GSATWinnerContent> gSATWinnerContentList = new ArrayList<>();

		String ua = request.getHeader("User-Agent");
		String login_equipment = "";
		if (ua.indexOf("Android") > -1 || ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1) {
			login_equipment = "MOBILE";
		} else {
			login_equipment = "COM";
		}

		String replaceName = "";
		String str = request.getParameter("str") == null ? "" : request.getParameter("str");
		if ("".equals(str)) {
			return "front/GSATWinner/list";
		}
		gSATWinner.setId(Integer.valueOf(CryptographyUtils.staticdecryptStr(str)));
		gSATWinner = gSATWinnerService.getFrontData(gSATWinner);

		try {
			gSATMenuList = gSATMenuService.getList(gSATMenu);// 選單清單
			gSATWinnerList = gSATWinnerService.getFrontList(gSATWinner);// 贏家經驗談內容清單
			gSATWinnerContent.setWinner_id(gSATWinner.getId());
			gSATWinnerContentList = gSATWinnerContentService.getList(gSATWinnerContent);
			navBannerList = gSATSideBannerService.getFrontList(navBanner);// 側邊欄廣告清單
			navBannerType2List = gSATSideBannerService.getFrontType2List(navBanner);// 取300*600圖片
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// 遮敝姓名長度4:XOXO 長度3:XOX
		if (gSATWinner.getName().length() == 3) {
			replaceName += gSATWinner.getName().charAt(0);
			replaceName += "O";
			replaceName += gSATWinner.getName().charAt(2);
			gSATWinner.setName(replaceName);
		} else if (gSATWinner.getName().length() > 3) {
			replaceName += gSATWinner.getName().charAt(0);
			replaceName += "O";
			replaceName += gSATWinner.getName().charAt(2);
			replaceName += "O";
			gSATWinner.setName(replaceName);
		} else if (gSATWinner.getName().length() < 3) {
			replaceName += gSATWinner.getName().charAt(0);
			replaceName += "O";
			replaceName += "";
			gSATWinner.setName(replaceName);
		}
		replaceName = "";
		model.addAttribute("navBannerList", navBannerList)
			 .addAttribute("navBannerType2List", navBannerType2List)
			 .addAttribute("gSATMenuList", gSATMenuList)
			 .addAttribute("gSATWinnerContentList", gSATWinnerContentList)
			 .addAttribute("gSATWinnerList", gSATWinnerList)
			 .addAttribute("gSATWinner", gSATWinner)
			 .addAttribute("login_equipment", login_equipment);
		return "front/GSATWinner/inside";

	}

	@RequestMapping("/GSAT/newsList")
	public String newsList(GSATNews gSATNews, GSATMenu gSATMenu, NavBanner navBanner,
			Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		List<NavBanner> navBannerList = new ArrayList<>();
		List<NavBanner> navBannerType2List = new ArrayList<>();
		List<GSATNews> gSATNewsList = new ArrayList<>();
		List<GSATMenu> gSATMenuList = new ArrayList<>();
		String ua = request.getHeader("User-Agent");
		String login_equipment = "";
		if (ua.indexOf("Android") > -1 || ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1) {
			login_equipment = "MOBILE";
		} else {
			login_equipment = "COM";
		}

		try {
			gSATMenuList = gSATMenuService.getList(gSATMenu);// 選單清單
			gSATNewsList = gSATNewsService.getFrontList(gSATNews);// 贏家經驗談內容清單
			navBannerList = gSATSideBannerService.getFrontList(navBanner);// 側邊欄廣告清單
			navBannerType2List = gSATSideBannerService.getFrontType2List(navBanner);// 取300*600圖片
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		model.addAttribute("gSATMenuList", gSATMenuList)
			 .addAttribute("gSATNewsList", gSATNewsList)
			 .addAttribute("navBannerList", navBannerList)
			 .addAttribute("navBannerType2List", navBannerType2List)
			 .addAttribute("login_equipment", login_equipment);
		return "front/GSAT/news";

	}

	/*
	 * 側邊欄廣告點閱率
	 */
	@RequestMapping("/GSAT/navBannerUpdateClickRate")
	public void navBannerUpdateClickRate(NavBanner navBanner, HttpServletRequest request) {
		String id = request.getParameter("id");
		navBanner.setId(Integer.valueOf(id));
		gSATSideBannerService.updateClickRate(navBanner);
	}

}
