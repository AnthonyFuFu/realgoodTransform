package com.tkb.realgoodTransform.controller.admin;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.tkb.realgoodTransform.model.GSAT;
import com.tkb.realgoodTransform.model.GSATCategory;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.GSATCategoryService;
import com.tkb.realgoodTransform.service.GSATService;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.CryptographyUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class GSATController extends BaseUtils {

	private int pageNo;
	@Autowired
	private GSATService gSATService; // 外交特考文章服務
	@Autowired
	private GSATCategoryService gSATCategoryService; // 類別服務
	@Autowired
	private EditLogService editLogService; // 各功能編輯的LOG服務

	/**
	 * 清單頁面
	 * @return
	 */
	@RequestMapping(value = "/gSAT/index")
	public String index(GSAT gSAT, GSATCategory gSATCategory, Model model,
			HttpServletRequest request, HttpSession session) {
		List<GSATCategory> gSATCategoryList = new ArrayList<>();
		List<GSAT> gSATList = new ArrayList<>();
		
		gSATCategory.setParent_id(0);
		try {
			gSATCategoryList = gSATCategoryService.getLayerList("1", gSATCategory);
			pageNo = super.setPage(pageNo, request);
			pageTotalCount = gSATService.getCount(gSAT);
			pageNo = super.pageSetting(pageNo);
			gSATList = gSATService.getList(pageCount, pageStart, gSAT);
			if (gSATList.size() != 0) {
				for (int i = 0; i < gSATList.size(); i++) {
					// 加密ID
					gSATList.get(i).setEncrypt_id(CryptographyUtils.encryptStr(String.valueOf(String.valueOf(gSATList.get(i).getId()))));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("gSATCategoryList", gSATCategoryList)
			 .addAttribute("gSATList",gSATList)
			 .addAttribute("gSATCategory",gSATCategory)
			 .addAttribute("gSAT",gSAT);
		addModelAttribute(pageNo, model);
		return "admin/GSAT/GSATList";
	}

	/**
	 * 新增頁面
	 * @return
	 */
	@RequestMapping(value = "/gSAT/add")
	public String add(GSAT gSAT,GSATCategory gSATCategory,Model model) {
		List<GSATCategory> gSATCategoryList = new ArrayList<>();
		gSATCategory.setParent_id(0);
		gSATCategoryList = gSATCategoryService.getLayerList("1", gSATCategory);		
		model.addAttribute("gSATCategoryList",gSATCategoryList)
			 .addAttribute("gSAT",gSAT);
		return "admin/GSAT/GSATForm";

	}
	
	/**
	 * 新增資料
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/gSAT/addSubmit")
	public String addSubmit(GSAT gSAT,@SessionAttribute("userAccountSession") User user, Model model){
		
		try{
    		int id = gSATService.getNextId();
    		gSAT.setId(id);
    		gSAT.setCreate_by(user.getAccount());
    		gSAT.setUpdate_by(user.getAccount());
    		gSATService.add(gSAT);
    		
    		Gson gson = new Gson();
    		String jsonString = gson.toJson(gSAT);
    		EditLog editLog = new EditLog();
    		int logId = editLogService.getNextLogId();
    		editLog.setId(logId);
    		editLog.setData_id(id);
    		editLog.setFunction("GSAT");
    		editLog.setAction_type("ADD");
    		editLog.setContent(jsonString);
    		editLog.setCreate_by(user.getAccount());
    		editLogService.addLog(editLog);
    		model.addAttribute("message", "新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "新增失敗");
		}
		return "admin/GSAT/toList";

	}
	
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping(value = "/gSAT/update")
	public String update(GSAT gSAT,GSATCategory gSATCategory,Model model) {
		List<GSATCategory> gSATCategoryList = new ArrayList<>();
		gSATCategory.setParent_id(0);
		gSATCategoryList = gSATCategoryService.getLayerList("1", gSATCategory);		
		gSAT = gSATService.getData(gSAT);
		model.addAttribute("gSATCategoryList",gSATCategoryList)
			 .addAttribute("gSAT",gSAT);
		return "admin/GSAT/GSATForm";
		
	}
	
	/**
	 * 修改資料
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/gSAT/updateSubmit")
	public String updateSubmit(GSAT gSAT,@SessionAttribute("userAccountSession") User user, Model model){
		  try{
		      gSAT.setUpdate_by(user.getAccount());
		      gSATService.update(gSAT);
		      
	    		Gson gson = new Gson();
	    		String jsonString = gson.toJson(gSAT);
	    		EditLog editLog = new EditLog();
	    		int logId = editLogService.getNextLogId();
	    		editLog.setId(logId);
	    		editLog.setData_id(gSAT.getId());
	    		editLog.setFunction("GSAT");
	    		editLog.setAction_type("UPDATE");
	    		editLog.setContent(jsonString);
	    		editLog.setCreate_by(user.getAccount());
	    		editLogService.addLog(editLog);
	    		model.addAttribute("message", "修改成功");
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("message", "修改失敗");
			}
			return "admin/GSAT/toList";
		}
		
	
	/**
	 * 瀏覽外交特考--連結
	 * @return
	 */
	@RequestMapping(value = "/gSAT/view")
	public String view(GSAT gSAT,GSATCategory gSATCategory,Model model){
		List<GSATCategory> gSATCategoryList = new ArrayList<>();
    	gSATCategory.setParent_id(0);
    	gSATCategoryList = gSATCategoryService.getLayerList("1", gSATCategory);		
    	gSAT = gSATService.getData(gSAT);
    	gSAT.setFront_url(CryptographyUtils.encryptStr(String.valueOf(String.valueOf(gSAT.getId()))));
    	model.addAttribute("gSATCategoryList", gSATCategoryList)
    		 .addAttribute("gSAT",gSAT);
		return "admin/gSAT/view";
	}
	
	/**
	 * 刪除外交特考文章
	 * @return
	 */
	@RequestMapping(value = "/gSAT/delete")
	public String delete(GSAT gSAT,HttpServletRequest request,@SessionAttribute("userAccountSession") User user,Model model){
		
		try{
			String selectList = request.getParameter("deleteList");
			String[] selectArray = selectList.split(",");
			Integer id = null;
			for(int i=0; i<selectArray.length; i++) {
				id = Integer.valueOf(selectArray[i]);
    			gSAT.setId(id);
    			gSAT = gSATService.getData(gSAT);
    			gSATService.delete(gSAT,id);
	    		Gson gson = new Gson();
	    		String jsonString = gson.toJson(gSAT);
	    		EditLog editLog = new EditLog();
	    		int logId = editLogService.getNextLogId();
	    		editLog.setId(logId);
	    		editLog.setData_id(id);
	    		editLog.setFunction("GSAT");
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
		return "admin/GSAT/toList";
		
	}
	
	/**
	 * 抓取類別ID
	 * @return
	 * @throws IOException 
	 */	
	@ResponseBody
	@RequestMapping(value = "/gSAT/getCategoryId")
	public String getCategoryId(HttpServletRequest request) throws IOException {
		  String msg="";
	      String gSATCategory_name = request.getParameter("gSATCategory_name");
	      Integer id = gSATCategoryService.getCategoryId(gSATCategory_name);
	      msg = String.valueOf(id);
	      return msg;
	}	
	
	
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/gSAT/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalList = new ArrayList<>();
		getNormalList = gSATService.getNormalList();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			for(Map<String, Object> map : getNormalList) {
				GSAT gSAT = new GSAT();
				
				if(map.get("ID") != null) {
					gSAT.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("CATEGORY") != null) {
					gSAT.setCategory(map.get("CATEGORY").toString());
				}
				if(map.get("TITLE") != null) {
					gSAT.setTitle(map.get("TITLE").toString());
				}
				if(map.get("SEO") != null) {
					gSAT.setSeo(map.get("SEO").toString());
				}
				if(map.get("ARTICLE_NAME") != null) {
					gSAT.setArticle_name(map.get("ARTICLE_NAME").toString());
				}
				if(map.get("ARTICLE_URL") != null) {
					gSAT.setArticle_url(map.get("ARTICLE_URL").toString());
				}
				
				
				if(map.get("CREATE_BY") != null) {
					gSAT.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					gSAT.setCreate_date(new Timestamp(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					gSAT.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					gSAT.setUpdate_date(new Timestamp(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("CONTENT") != null) {
					gSAT.setContent(map.get("CONTENT").toString());
				}
				if(map.get("SORT") != null) {
					gSAT.setSort(Integer.valueOf(map.get("SORT").toString()));
				}
				if(map.get("SHOW") != null) {
					gSAT.setShow(Integer.valueOf(map.get("SHOW").toString()));
				}
				if(map.get("PRODUCT_CATEGORY") != null) {
					gSAT.setProduct_category(map.get("PRODUCT_CATEGORY").toString());
				}
				
				gSATService.updateNormalData(gSAT);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}

}
