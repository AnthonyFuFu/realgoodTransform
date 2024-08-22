package com.tkb.realgoodTransform.controller.admin;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.model.ChosenArticle;
import com.tkb.realgoodTransform.model.ChosenArticleCategory;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.ARTICLEAPIService;
import com.tkb.realgoodTransform.service.ChosenArticleCategoryService;
import com.tkb.realgoodTransform.service.ChosenArticleService;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.CryptographyUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class ChosenArticleController extends BaseUtils {

	@Autowired
	private ChosenArticleService chosenArticleService; // 精選文章服務
	
	@Autowired
	private ChosenArticleCategoryService chosenArticleCategoryService; // 精選文章類別服務
	
	@Autowired
	private ARTICLEAPIService articleapiService;
	
	@Autowired
	private EditLogService editLogService; // 各功能編輯的LOG服務

	private int pageNo; // 頁碼

	@RequestMapping(value = "/chosenArticle/index")
	public String index(ChosenArticle chosenArticle, Model model, HttpServletRequest request, HttpSession session, String titleIndex) {
		List<ChosenArticle> chosenArticleList = new ArrayList<>();
		
		if (pageNo != 0 && !"".equals(request.getParameter("pageNo"))) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		
		if("".equals(chosenArticle.getTitle()) || chosenArticle.getTitle() == null) {
			chosenArticle.setTitle(titleIndex);
	    }
		
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();
		pageTotalCount = chosenArticleService.getCount(chosenArticle);
		pageNo = super.pageSetting(pageNo);
		chosenArticleList = chosenArticleService.getList(pageCount, pageStart, chosenArticle);
		model.addAttribute("chosenArticleList", chosenArticleList);
		addModelAttribute(pageNo, model).addAttribute("pageNoLog", pageNo);
		return "admin/chosenArticle/chosenArticleList";

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/chosenArticle/add")
	public String add(ChosenArticle chosenArticle, HttpServletRequest request,
			ChosenArticleCategory chosenArticleCategory, Model model, String pageNoLog, String title) throws Exception {
		List<ChosenArticleCategory> chosenArticleCategoryList = new ArrayList<>();
		String article_num = request.getParameter("article_num");
		Map articleData = articleapiService.quoteArticleDate(article_num, articleAppName, articleReqEncryptKey,
				articleRepDecryptKey);
		
		chosenArticle.setTitle("");
		
		if (articleData != null) {

			chosenArticle.setArticle_num(articleData.get("ARTICLE_NUM").toString());
			chosenArticle.setArticle_author(articleData.get("CREATE_BY").toString());
			chosenArticle.setQuote_content_string(articleData.get("CONTENT").toString());
			chosenArticleCategory.setParent_id(0);
			chosenArticleCategoryList = chosenArticleCategoryService.getLayerList("1", chosenArticleCategory);
			model.addAttribute("chosenArticleCategoryList", chosenArticleCategoryList).addAttribute("pageNoLog", pageNoLog)
			.addAttribute("titleIndex", title);
			return "admin/chosenArticle/chosenArticleForm";
		} else {
			model.addAttribute("message", "無此文章資料");
			return "admin/chosenArticle/toList";
		}

	}

	/**
	 * 新增資料
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/chosenArticle/addSubmit")
	public String addSubmit(ChosenArticle chosenArticle,@SessionAttribute("userAccountSession") User user, Model model
			, String pageNoLog, String titleIndex) {

		try {
			String create_user = user.getAccount();
			int id = chosenArticleService.getNextId();
			chosenArticle.setId(id);
			chosenArticle.setDelete_status("0");
			chosenArticle.setCreate_by(create_user);
			chosenArticle.setUpdate_by(create_user);
			chosenArticle.setEncrypturl(CryptographyUtils.encryptStr(String.valueOf(chosenArticle.getId())));
			chosenArticleService.add(chosenArticle);

			Gson gson = new Gson();
			String jsonString = gson.toJson(chosenArticle);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(id);
			editLog.setFunction("CHOSEN_ARTICLE");
			editLog.setAction_type("ADD");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			model.addAttribute("message", "新增精選文章成功").addAttribute("pageNo", pageNoLog).addAttribute("titleIndex", titleIndex);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "新增精選文章失敗");
		}
		return "admin/chosenArticle/toList";
	}

	
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping(value = "/chosenArticle/update")
	public String update(ChosenArticle chosenArticle,ChosenArticleCategory chosenArticleCategory,Model model
			, String pageNoLog,  String title) {
		List<ChosenArticleCategory> chosenArticleCategoryList = new ArrayList<>();
		
		chosenArticleCategory.setParent_id(0);
		chosenArticleCategoryList = chosenArticleCategoryService.getLayerList("1", chosenArticleCategory);

		chosenArticle = chosenArticleService.getData(chosenArticle);
		
		chosenArticleCategory.setId(chosenArticle.getArticle_category());
		
		chosenArticleCategory = chosenArticleCategoryService.getData(chosenArticleCategory);
		
		chosenArticle.setCategory_parent(String.valueOf(chosenArticleCategory.getParent_id()));
		
		chosenArticle.setContent_string(chosenArticle.getContent());
		chosenArticle.setQuote_content_string(chosenArticle.getQuote_content());
		model.addAttribute("chosenArticleCategoryList", chosenArticleCategoryList)
			 .addAttribute("chosenArticleCategory", chosenArticleCategory)
			 .addAttribute("chosenArticle", chosenArticle).addAttribute("pageNoLog", pageNoLog).addAttribute("titleIndex", title);


		return "admin/chosenArticle/chosenArticleForm";
		
	}
	

	/**
	 * 修改資料
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/chosenArticle/updateSubmit")
	public String updateSubmit(ChosenArticle chosenArticle,@SessionAttribute("userAccountSession") User user, Model model
			, String pageNoLog, String titleIndex){
		try {
		chosenArticle.setUpdate_by(user.getAccount());
		chosenArticleService.update(chosenArticle);
				
		Gson gson = new Gson();
		String jsonString = gson.toJson(chosenArticle);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(chosenArticle.getId());
		editLog.setFunction("CHOSEN_ARTICLE");
		editLog.setAction_type("UPDATE");
		editLog.setContent(jsonString);
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);

		model.addAttribute("message", "更新成功").addAttribute("pageNo", pageNoLog).addAttribute("titleIndex", titleIndex);
		
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "更新失敗");
		}
		return "admin/chosenArticle/toList";
	}

	/**
	 * 刪除
	 * 
	 * @return
	 */
	@RequestMapping(value = "/chosenArticle/delete")
	public String delete(ChosenArticle chosenArticle, Model model,
			@SessionAttribute("userAccountSession") User user, HttpServletRequest request) {
		try {
			String deleteString = request.getParameter("deleteList");
			String[] deleteArray = deleteString.split(",");
			Integer id = null;
			for (int i = 0; i < deleteArray.length; i++) {
				id = Integer.valueOf(deleteArray[i]);
				chosenArticleService.delete(id);
				Gson gson = new Gson();
				String jsonString = gson.toJson(chosenArticle);
				EditLog editLog = new EditLog();
				int logId = editLogService.getNextLogId();
				editLog.setId(logId);
				editLog.setData_id(id);
				editLog.setFunction("CHOSEN_ARTICLE");
				editLog.setAction_type("DELETE");
				editLog.setContent(jsonString);
				editLog.setCreate_by(user.getAccount());
				editLogService.addLog(editLog);
				model.addAttribute("message", "刪除成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "刪除失敗");
		}
		return "admin/chosenArticle/toList";

	}

	@RequestMapping(value = "/chosenArticle/displayOff")
	public String displayOff(HttpServletRequest request, Model model, ChosenArticle chosenArticle,
			@SessionAttribute("userAccountSession") User user) {
		try {
			String deleteString = request.getParameter("deleteList");
			String[] deleteArray = deleteString.split(",");
			Integer id = null;

			for (int i = 0; i < deleteArray.length; i++) {
				id = Integer.valueOf(deleteArray[i]);
				chosenArticleService.updateDisplayHide(id);

				Gson gson = new Gson();
				String jsonString = gson.toJson(chosenArticle);
				EditLog editLog = new EditLog();
				int logId = editLogService.getNextLogId();
				editLog.setId(logId);
				editLog.setData_id(id);
				editLog.setFunction("CHOSEN_ARTICLE");
				editLog.setAction_type("UPDATE");
				editLog.setContent(jsonString);
				editLog.setCreate_by(user.getAccount());
				editLogService.addLog(editLog);
			}

			model.addAttribute("message", "下架成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "下架失敗");
		}
		return "admin/chosenArticle/toList";

	}

	@RequestMapping(value = "/chosenArticle/displayOn")
	public String displayOn(HttpServletRequest request, Model model, ChosenArticle chosenArticle,@SessionAttribute("userAccountSession") User user) {
		try {
			String deleteString = request.getParameter("deleteList");
			String[] deleteArray = deleteString.split(",");
			Integer id = null;

			for (int i = 0; i < deleteArray.length; i++) {
				id = Integer.valueOf(deleteArray[i]);
				chosenArticleService.updateDisplayShow(id);

				Gson gson = new Gson();
				String jsonString = gson.toJson(chosenArticle);
				EditLog editLog = new EditLog();
				int logId = editLogService.getNextLogId();
				editLog.setId(logId);
				editLog.setData_id(id);
				editLog.setFunction("CHOSEN_ARTICLE");
				editLog.setAction_type("UPDATE");
				editLog.setContent(jsonString);
				editLog.setCreate_by(user.getAccount());
				editLogService.addLog(editLog);
			}

			model.addAttribute("message", "上架成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "上架失敗");
		}
		return "admin/chosenArticle/toList";

	}

	/**
	 * 取得第二層分類
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/chosenArticle/changeCategory")
	public List<ChosenArticleCategory> changeCategory(ChosenArticleCategory chosenArticleCategory,
			HttpServletRequest request) throws Exception {
		List<ChosenArticleCategory> chosenArticleCategoryList = new ArrayList<>();
		String category_id = request.getParameter("category_id");
		chosenArticleCategory.setParent_id(Integer.valueOf(category_id));
		chosenArticleCategoryList = chosenArticleCategoryService.getLayerList("2", chosenArticleCategory);
		return chosenArticleCategoryList;
	}
//	
//	/**
//	 * 確認置頂筆數
//	 * @throws Exception
//	 */
//	public void checkTopCount() throws Exception {
//		
//		String article_id = request.getParameter("article_id");
//		String resultStatus = "F";
//		
//		int countArticle = chosenArticleService.getCountListByTop(Integer.valueOf(article_id));
//		
//		if(countArticle >= 2){
//			resultStatus = "T";
//		}
//
//		
//		response.setCharacterEncoding("utf-8");
//		PrintWriter out = response.getWriter();
//		out.write(resultStatus);
//		
//	}
//
//	
//	public static Clob stringToClob(String str) {
//	    if (null == str)
//	        return null;
//	    else {
//	        try {
//	            java.sql.Clob c = new javax.sql.rowset.serial.SerialClob(str
//	                    .toCharArray());
//	            return c;
//	        } catch (Exception e) {
//	            return null;
//	        }
//	    }
//	}
//	
//	 public String getClobType(Clob cb){
//			Reader reader = null;
//			StringBuffer sb = new StringBuffer();
//			try {
//				reader = cb.getCharacterStream();
//				BufferedReader br = new BufferedReader(reader);
//				String temp = null;
//				while ((temp=br.readLine()) != null) {
//					sb.append(temp);
//				}
//			} catch (Exception e) {
//				
//			}finally{
//				if (reader!=null) {
//					try {
//						reader.close();
//					} catch (IOException e) {
//					}
//				}
//			} 	
//			return sb.toString();
//	} 
//	
//	     /**
//	     *寫入加密ID進入資料庫
//	     * @throws Exception
//	     */
//	    public void setEncryptId() throws Exception {
//	        String msg = "寫入成功";
//	        if(chosenArticle == null) {
//	            chosenArticle = new ChosenArticle();
//	                }
//	        chosenArticleList = chosenArticleService.getList(chosenArticle);
//	        try
//	                    {
//    	        if(chosenArticleList.size()!=0){
//    	            for(int i = 0 ; i < chosenArticleList.size() ; i++){
//    	                //加密ID
//    	                chosenArticleList.get(i).setEncrypt_id(super.encryptStr(String.valueOf(chosenArticleList.get(i).getId())));
//    	                chosenArticleService.updateId(chosenArticleList.get(i));
//    	                        }
//    	                    }
//	                    }
//	        catch(Exception ex)
//	                {
//	                    ex.printStackTrace();
//	                }
//	        response.setCharacterEncoding("utf-8");
//	        PrintWriter out = response.getWriter();
//	        out.write(msg);
//	        
//	    }
	
		@SuppressWarnings({ "rawtypes", "unchecked" })
	    @ResponseBody
		@RequestMapping(value = "/chosenArticle/updateAllArticle")
	     public String updateAllArticle(ChosenArticle chosenArticle,HttpServletRequest request,@SessionAttribute("userAccountSession") User user
	    		 ) throws Exception{
	    		
	   		  		
	   		List<ChosenArticle> list = chosenArticleService.getArticleListForUpdate();

	   		String article_num  = request.getParameter("article_num") == null ? "" : request.getParameter("article_num");
	   		String msg = "批次更新失敗";
	   		String create_user = user.getAccount();
	   		String id_text = "";

	   		for(int i = 0 ;i < list.size();i++){
	   			article_num = list.get(i).getArticle_num();
	   			
	   			Map articleData = articleapiService.quoteArticleDate(article_num, articleAppName, articleReqEncryptKey, articleRepDecryptKey);
	   			
	   			if(articleData != null){
	   				//通過文章才更新
	   				
	   				if("C".equals(articleData.get("VERIFY_STATUS").toString())){
	   	 				chosenArticle.setId(list.get(i).getId());
	   	 				chosenArticle.setUpdate_by(create_user);
	   	 				if(list.get(i).getId() != null){
	   	 					
	   	 					if("".equals(id_text)){
	   	 						id_text = id_text + list.get(i).getId();
	   	 					}else{
	   	 						id_text = id_text+ "," + list.get(i).getId();
	   	 					}
	   	 					
	   	 				}
	   	 				
	   	 				chosenArticle.setQuote_content_string(articleData.get("CONTENT").toString());
	   	 				chosenArticleService.updateQuoteArticle(chosenArticle);

	   				}

	   			}
	   	
	   		}
	   		
	   		if(!"".equals(id_text)){
	   			Map map1 = new HashMap();
	   			map1.put("ALL_ARTICLE_UPDATE_ID", id_text);
	   			
	   			Gson gson = new Gson();
	  			String jsonString = gson.toJson(map1);
	   			
	  			EditLog editLog = new EditLog();
	   			int logId = editLogService.getNextLogId();
	   			editLog.setId(logId);
	   			editLog.setData_id(0);
	   			editLog.setFunction("CHOSEN_ARTICLE");
	   			editLog.setAction_type("UPDATE");
//	   			editLog.setContent(id_text);
	   			editLog.setContent(jsonString);
	   			editLog.setCreate_by(user.getAccount());
	   			editLogService.addLog(editLog);
	   		}
	   		msg = "批次更新成功";
	          return msg;
	   	}    
	    
	    
	    
	    
//		=========================
		
		
		/**
		 * 更新正是機資料到本地方法
		 */
		@GetMapping("/chosenArticle/updateNormalData")
		public String updateNormalData() {
			
			List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
			getNormalBannerList = chosenArticleService.getNormalList();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
			
				for(Map<String, Object> map : getNormalBannerList) {
					ChosenArticle chosenArticle = new ChosenArticle();
					
					if(map.get("ID") != null) {
						chosenArticle.setId(Integer.valueOf(map.get("ID").toString()));
					}
					if(map.get("ARTICLE_NUM") != null) {
						chosenArticle.setArticle_num(map.get("ARTICLE_NUM").toString());
					}
					if(map.get("ARTICLE_CATEGORY") != null) {
						chosenArticle.setArticle_category(Integer.valueOf(map.get("ARTICLE_CATEGORY").toString()));
					}
					if(map.get("ARTICLE_AUTHOR") != null) {
						chosenArticle.setArticle_author(map.get("ARTICLE_AUTHOR").toString());
					}
					if(map.get("TITLE") != null) {
						chosenArticle.setTitle(map.get("TITLE").toString());
					}
					if(map.get("SUMMARY") != null) {
						chosenArticle.setSummary(map.get("SUMMARY").toString());
					}
					if(map.get("IMAGE_URL") != null) {
						chosenArticle.setImage_url(map.get("IMAGE_URL").toString());
					}
					if(map.get("CLICK_RATE") != null) {
						chosenArticle.setClick_rate(Integer.valueOf(map.get("CLICK_RATE").toString()));
					}
					if(map.get("TOP_STATUS") != null) {
						chosenArticle.setTop_status(map.get("TOP_STATUS").toString());
					}
					if(map.get("DISPLAY") != null) {
						chosenArticle.setDisplay(map.get("DISPLAY").toString());
					}
					if(map.get("DELETE_STATUS") != null) {
						chosenArticle.setDelete_status(map.get("DELETE_STATUS").toString());
					}
					if(map.get("CREATE_BY") != null) {
						chosenArticle.setCreate_by(map.get("CREATE_BY").toString());
					}
					if(map.get("CREATE_DATE") != null) {
						chosenArticle.setCreate_date(map.get("CREATE_DATE").toString());
					}
					if(map.get("UPDATE_BY") != null) {
						chosenArticle.setUpdate_by(map.get("UPDATE_BY").toString());
					}
					if(map.get("UPDATE_DATE") != null) {
						chosenArticle.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
					}
					if(map.get("LAST_UPDATE") != null) {
						chosenArticle.setLast_update(new Date(sdf.parse(map.get("LAST_UPDATE").toString()).getTime()));
					}
					if(map.get("BANNER_URL") != null) {
						chosenArticle.setBanner_url(map.get("BANNER_URL").toString());
					}
					if(map.get("EDIT_FROM") != null) {
						chosenArticle.setEdit_from(map.get("EDIT_FROM").toString());
					}
					if(map.get("QUOTE_CONTENT") != null) {
						chosenArticle.setQuote_content(map.get("QUOTE_CONTENT").toString());
					}
					if(map.get("CONTENT") != null) {
						chosenArticle.setContent(map.get("CONTENT").toString());
					}
					if(map.get("ENCRYPTURL") != null) {
						chosenArticle.setEncrypturl(map.get("ENCRYPTURL").toString());
					}
					if(map.get("PRODUCT_CATEGORY") != null) {
						chosenArticle.setProduct_category(map.get("PRODUCT_CATEGORY").toString());
					}
					chosenArticleService.updateNormalData(chosenArticle);
					
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//轉發 到首頁更新
			return "redirect:index";
		}
	    
	    

}
