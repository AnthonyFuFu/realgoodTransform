package com.tkb.realgoodTransform.controller.front;

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

import com.tkb.realgoodTransform.model.Admit;
import com.tkb.realgoodTransform.model.AdmitCategory;
import com.tkb.realgoodTransform.model.AdmitContent;
import com.tkb.realgoodTransform.model.AdmitContentOption;
import com.tkb.realgoodTransform.model.AdmitDetail;
import com.tkb.realgoodTransform.service.AdmitCategoryService;
import com.tkb.realgoodTransform.service.AdmitContentOptionService;
import com.tkb.realgoodTransform.service.AdmitContentService;
import com.tkb.realgoodTransform.service.AdmitDetailService;
import com.tkb.realgoodTransform.service.AdmitService;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.CryptographyUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FrontAdmitController extends BaseUtils {
	
	@Autowired
	private AdmitService admitService;
	
	@Autowired
	private AdmitContentService admitContentService;
	
	@Autowired
	private AdmitContentOptionService admitContentOptionService;
	
	@Autowired
	private AdmitDetailService admitDetailService;

	@Autowired
	private AdmitCategoryService admitCategoryService;		//金榜類別服務
	
//	private List<Admit> admitList;							//金榜清單
	
	private int pageNo;										//頁碼
	
	@RequestMapping("/admit/index")
	public String index(Admit admit,AdmitCategory admitCategory,HttpServletRequest request,Model model) {
		List<Admit> yearList = new ArrayList<>(); 
		List<Admit> admitList = new ArrayList<>();
		List<AdmitCategory> admitCategoryList = new ArrayList<>();
		
		
		String pageCountStr = request.getParameter("page_count") == null ? "" : request.getParameter("page_count");

		pageTotalCount = admitService.getFrontCount(admit);//總筆數
		if(!"".equals(pageCountStr)) {
			super.pageCount = Integer.valueOf(pageCountStr);
		} else {
			super.pageCount = 8;
		}
		pageNo = super.pageSetting(pageNo);

		yearList = admitService.getAllAdmitYear();
		admitCategory.setParent_id(0);
		admitCategoryList = admitCategoryService.getLayerList("1", admitCategory);
		for(int i = 0 ; i < admitCategoryList.size() ; i++){
			int countByCategory = admitService.getCountListByCategory(admitCategoryList.get(i).getId());
			admitCategoryList.get(i).setUse_count(countByCategory);
		}

		admitList = admitService.getFrontList(pageCount, pageStart, admit);
		if(admitList.size()!=0){
		       for(int i = 0 ; i < admitList.size() ; i++){                
		           admitList.get(i).setStr(CryptographyUtils.encryptStr(String.valueOf(admitList.get(i).getId())));
		                    }
	     }    		
		
		model.addAttribute("yearList",yearList)
			 .addAttribute("admitList",admitList)
			 .addAttribute("admitCategoryList",admitCategoryList);
		addModelAttribute(pageNo, model);
		return "front/admit/list";
		
	}
	
	@RequestMapping("/admit/inside")
	public String inside(HttpServletRequest request, Model model, Admit admit, AdmitDetail admitDetail) throws NumberFormatException, UnsupportedEncodingException {
			
			List<AdmitContentOption> admitContentOptionList = new ArrayList<>(); 
		
			AdmitContent admitContent = new AdmitContent();
	
			String str = request.getParameter("str") == null ? "" : request.getParameter("str");
			
			if("".equals(str)){
				return "list";
			}
			
	//		int admit_id = request.getParameter("admit_id") == null ? 0 : Integer.valueOf(request.getParameter("admit_id"));
			String shareStr = str;
			int admit_id = Integer.valueOf(CryptographyUtils.staticdecryptStr(str));
			
			admit.setId(admit_id);
			admitService.updateClickRate(admit);//增加點閱率
			admit = admitService.getFrontData(admit);
			
	
			if(admit != null) {
				
				admitContent.setAdmit_id(admit.getId());
				
				List<AdmitContent> admitContentList = admitContentService.getFrontContentList(admitContent);
	
				ArrayList<AdmitContent> contentOptionList = new ArrayList<AdmitContent>();
	
				for(int i=0; i<admitContentList.size(); i++) {
	
					admitContent = admitContentList.get(i);
	
					AdmitContentOption admitContentOption = new AdmitContentOption();
					admitContentOption.setContent_id(admitContentList.get(i).getId());;
					
					//取得選項清單
					admitContentOptionList = admitContentOptionService.getFrontContentOptionList(admitContentOption);
					admitContent.setAdmitOptionList(admitContentOptionList);;
					contentOptionList.add(i, admitContent);
					
				}
				
				admitDetail.setAdmit_id(admit.getId());
				List<AdmitDetail> admitDetailList = admitDetailService.getDetailList(admitDetail);
				String date = admit.getUpdate_date().substring(0, 10);

				String formattedDate = date.replace("-", "/");

				admit.setUpdate_date(formattedDate);
		        
//		        model.addAttribute("dateTime", dateTime);
				model.addAttribute("admit",admit);
				model.addAttribute("shareStr",shareStr);
				model.addAttribute("admitContentList",admitContentList);
				model.addAttribute("admitDetailList",admitDetailList);
				model.addAttribute("contentOptionList",contentOptionList);
				model.addAttribute("admitContentOptionList",admitContentOptionList);
			}
			
			return "front/admit/inside";
			
		}
	
//	public void toEncrypt() throws IOException {
//		
//		int admit_id = request.getParameter("admit_id") == null ? 0 : Integer.valueOf(request.getParameter("admit_id"));
//		
//		response.setCharacterEncoding("utf-8");
//		PrintWriter out = response.getWriter();
//		out.write(super.encryptStr(String.valueOf(admit_id)));
//	}
	
	@PostMapping("/admit/toSearch")
	public ResponseEntity<?> toSearch(Admit admit, AdmitCategory admitCategory, HttpServletRequest request,
			Model model) {
		List<Admit> admitList = new ArrayList<>();
		String category_id = request.getParameter("category_id") == "" ? "" : request.getParameter("category_id");
		String pageCountStr = request.getParameter("page_count") == null ? "" : request.getParameter("page_count");
		String page_no = request.getParameter("page_no") == null ? "" : request.getParameter("page_no");
		String year = request.getParameter("year") == "" ? "" : request.getParameter("year");
		super.resetPage();
		if (!"".equals(category_id)) {
			admit.setAdmit_category(Integer.parseInt(category_id));
		}

		if (!"".equals(year)) {
			admit.setAdmit_year(Integer.parseInt(year));
		}

		if (!"".equals(pageCountStr)) {
			super.pageCount = Integer.valueOf(pageCountStr);
		} else {
			super.pageCount = 8;
		}
			pageTotalCount = admitService.getFrontCount(admit);
			pageNo = super.pageSetting(Integer.valueOf(page_no));
			admitList = admitService.getFrontList(pageCount, pageStart, admit);
			if(admitList.size()!=0){
			       for(int i = 0 ; i < admitList.size() ; i++){                
			           admitList.get(i).setStr(CryptographyUtils.encryptStr(String.valueOf(admitList.get(i).getId())));
			                    }
		     }  

		if (admitList.size() > 0) {
			admitList.get(0).setPageCount(pageCount);
			admitList.get(0).setPageTotalCount(pageTotalCount);
			admitList.get(0).setTotalPage(totalPage);

			admitList.get(0).setLeftStartPage(leftStartPage);
			admitList.get(0).setLeftEndPage(leftEndPage);
			admitList.get(0).setRightStartPage(rightStartPage);
			admitList.get(0).setRightEndPage(rightEndPage);
			admitList.get(0).setLeftPageNum(leftPageNum);
			admitList.get(0).setRightPageNum(rightPageNum);

		}

		addModelAttribute(pageNo, model);
		return new ResponseEntity<>(admitList, HttpStatus.OK);
	}
	
}
