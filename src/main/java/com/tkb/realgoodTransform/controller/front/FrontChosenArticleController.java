package com.tkb.realgoodTransform.controller.front;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tkb.realgoodTransform.model.ChosenArticle;
import com.tkb.realgoodTransform.model.ChosenArticleCategory;
import com.tkb.realgoodTransform.model.NavBanner;
import com.tkb.realgoodTransform.service.ChosenArticleCategoryService;
import com.tkb.realgoodTransform.service.ChosenArticleService;
import com.tkb.realgoodTransform.service.NavBannerService;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.CryptographyUtils;

import jakarta.servlet.http.HttpServletRequest;





@Controller
public class FrontChosenArticleController extends BaseUtils {
	
	private int pageNo;										//頁碼
	@Autowired
	private ChosenArticleService chosenArticleService;		//精選文章服務
	@Autowired
	private ChosenArticleCategoryService chosenArticleCategoryService;		//精選文章類別服務
	@Autowired
	private NavBannerService navBannerService;                    //廣告服務
	
	@RequestMapping("/chosenArticle/index")
	public String index(ChosenArticle chosenArticle,ChosenArticleCategory chosenArticleCategory,HttpServletRequest request,Model model) {
		List<ChosenArticleCategory> chosenArticleCategoryList = new ArrayList<>();
		List<ChosenArticleCategory> chosenArticleCategoryChildList = new ArrayList<>();
		List<ChosenArticle> chosenArticleList = new ArrayList<>();

		
		if(chosenArticle.getArticle_category()==null){
			chosenArticle.setArticle_category(0);
			chosenArticle.setCategory_layer(1);
		}
		
		String pageCountStr = request.getParameter("page_count") == null ? "" : request.getParameter("page_count");
		String sort = request.getParameter("sort") == null ? "" : request.getParameter("sort");

		pageTotalCount = chosenArticleService.getFrontCount(chosenArticle);//總筆數
		if(!"".equals(pageCountStr)) {
			super.pageCount = Integer.valueOf(pageCountStr);
		} else {
			super.pageCount = 8;
		}
		pageNo = super.pageSetting(pageNo);
		
		

		chosenArticleCategory.setParent_id(0);

		chosenArticleCategoryList = chosenArticleCategoryService.getLayerList("1", chosenArticleCategory);
		
		
		System.out.println("chosenArticleCategoryList70" + chosenArticleCategoryList);

		for(int i = 0 ; i < chosenArticleCategoryList.size() ; i++){
			
			if(chosenArticle.getArticle_category().equals(chosenArticleCategoryList.get(i).getId())){
				chosenArticleCategoryList.get(i).setIn_use(1);
			}
			
			chosenArticleCategory.setParent_id(chosenArticleCategoryList.get(i).getId());
			chosenArticleCategoryChildList = chosenArticleCategoryService.getLayerList("2", chosenArticleCategory);
			
			if(chosenArticleCategoryChildList != null){
				if(chosenArticleCategoryChildList.size() != 0){
					
					int parentCountByCategory = 0;
					
					for(int j = 0 ; j < chosenArticleCategoryChildList.size() ; j++){
						if(chosenArticle.getArticle_category().equals(chosenArticleCategoryChildList.get(j).getId())){
							chosenArticleCategoryList.get(i).setIn_use(1);
						}
						int countByCategory = chosenArticleService.getCountListByCategory(chosenArticleCategoryChildList.get(j).getId());
						chosenArticleCategoryChildList.get(j).setUse_count(countByCategory);
						parentCountByCategory = parentCountByCategory + countByCategory;

					}
					
					chosenArticleCategoryList.get(i).setChildList(chosenArticleCategoryChildList);
					chosenArticleCategoryList.get(i).setUse_count(parentCountByCategory);
					
					
				}else{
					chosenArticleCategoryList.get(i).setUse_count(0);
				}
			}else{
				chosenArticleCategoryList.get(i).setUse_count(0);
			}
		}

		chosenArticleList = chosenArticleService.getFrontList(pageCount, pageStart, chosenArticle,sort);
		
		if(chosenArticleList.size()!=0){
			for(int i = 0 ; i < chosenArticleList.size() ; i++){
				//加密ID
				chosenArticleList.get(i).setEncrypt_id(CryptographyUtils.encryptStr(String.valueOf(chosenArticleList.get(i).getId())));
			}
		}
		
		System.out.println("chosenArticleCategoryList" + chosenArticleCategoryList);
		
		
		model.addAttribute("chosenArticleCategoryList", chosenArticleCategoryList)
			.addAttribute("chosenArticleCategoryChildList", chosenArticleCategoryChildList)
			.addAttribute("chosenArticleList", chosenArticleList);
		addModelAttribute(pageNo, model);
		return "front/chosenArticle/list";

		
	}
	
	@RequestMapping("/chosenArticle/inside")
	public String inside(ChosenArticle chosenArticle,HttpServletRequest request,NavBanner navBanner,Model model) throws UnsupportedEncodingException{
		List<ChosenArticle> chosenArticleList = new ArrayList<>();
		List<NavBanner>navBannerList = new ArrayList<>();
		List<NavBanner>navBannerType2List = new ArrayList<>();
		List<ChosenArticle> prevList = new ArrayList<>();
		List<ChosenArticle> nextList = new ArrayList<>();
		
		String str = request.getParameter("str") == null ? "" : request.getParameter("str");
		String shareStr="";
		if("".equals(str)){
			return "list";
		}
		int id = Integer.valueOf(CryptographyUtils.staticdecryptStr(str));
		chosenArticle.setId(id);
		
		chosenArticleService.updateClickRate(chosenArticle);//增加點閱率
		chosenArticle = chosenArticleService.getFrontData(chosenArticle);
		chosenArticle.setEncrypt_id(str);
		chosenArticle.setCategory_layer(2);
		chosenArticle.setContent_string(chosenArticle.getContent());
		chosenArticle.setQuote_content_string(chosenArticle.getQuote_content());

		chosenArticleList = chosenArticleService.getFrontList(chosenArticle);
		prevList = chosenArticleService.getPrevList(chosenArticle);
		if(prevList.size()!=0) {
			chosenArticle.setPrev_article(prevList.get(0));
		}
		
		nextList = chosenArticleService.getNextList(chosenArticle);
		if(nextList.size()!=0) {
			chosenArticle.setNext_article(nextList.get(0));
		}
		navBannerList = navBannerService.getFrontList(navBanner);
		navBannerType2List = navBannerService.getFrontType2List(navBanner);//取300*600	
		shareStr = str;
		model.addAttribute("shareStr", shareStr)
			.addAttribute("prevList", prevList)
			.addAttribute("nextList", nextList)
			.addAttribute("chosenArticle", chosenArticle)
			.addAttribute("navBannerList", navBannerList)
			.addAttribute("navBannerType2List", navBannerType2List)
			.addAttribute("chosenArticleList", chosenArticleList);
		return "front/chosenArticle/inside";

		
	}
	
	/*
     * 側邊欄廣告點閱率
     * 
     */
	@RequestMapping("/chosenArticle/navBannerUpdateClickRate")
    public void navBannerUpdateClickRate(HttpServletRequest request,NavBanner navBanner) {
        String id = request.getParameter("id");
        navBanner.setId(Integer.valueOf(id));
        navBannerService.updateClickRate(navBanner);
        
    }	
	
	@PostMapping("/chosenArticle/toSearch")
	public ResponseEntity<?> toSearch (ChosenArticle chosenArticle,ChosenArticleCategory chosenArticleCategory,HttpServletRequest request,Model model) throws IOException{
		String category_id = request.getParameter("category_id") == "" ? "" : request.getParameter("category_id");
		String searchSort = request.getParameter("searchSort") == null ? "" : request.getParameter("searchSort");
		String pageCountStr = request.getParameter("page_count") == null ? "" : request.getParameter("page_count");
		String page_no = request.getParameter("page_no") == null ? "" : request.getParameter("page_no");
		super.resetPage();
		List<ChosenArticle>chosenArticleList = new ArrayList<>();
		if(!"".equals(category_id)) {
			chosenArticle.setArticle_category(Integer.parseInt(category_id));
		}else {
			chosenArticle.setArticle_category(0);
			chosenArticle.setCategory_layer(1);
		}
		
		if(!"".equals(pageCountStr)) {
			super.pageCount = Integer.valueOf(pageCountStr);
		}else {
			super.pageCount = 8;
		}
		pageTotalCount = chosenArticleService.getFrontCount(chosenArticle);
		pageNo = super.pageSetting(Integer.valueOf(page_no));
		chosenArticleList = chosenArticleService.getFrontList(pageCount, pageStart, chosenArticle, searchSort);
		if(chosenArticleList.size()>0){
			chosenArticleList.get(0).setPageCount(pageCount);
			chosenArticleList.get(0).setPageTotalCount(pageTotalCount);
			chosenArticleList.get(0).setTotalPage(totalPage);
			
			chosenArticleList.get(0).setLeftStartPage(leftStartPage);
			chosenArticleList.get(0).setLeftEndPage(leftEndPage);
			chosenArticleList.get(0).setRightStartPage(rightStartPage);
			chosenArticleList.get(0).setRightEndPage(rightEndPage);
			chosenArticleList.get(0).setLeftPageNum(leftPageNum);
			chosenArticleList.get(0).setRightPageNum(rightPageNum);
		}
		addModelAttribute(pageNo, model);
		return new ResponseEntity<>(chosenArticleList, HttpStatus.OK);
	}
	
	
}
