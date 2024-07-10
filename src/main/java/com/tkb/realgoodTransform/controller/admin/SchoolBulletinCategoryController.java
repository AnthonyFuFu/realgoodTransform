package com.tkb.realgoodTransform.controller.admin;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.SchoolBulletin;
import com.tkb.realgoodTransform.model.SchoolBulletinCategory;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.SchoolBulletinCategoryService;
import com.tkb.realgoodTransform.service.SchoolBulletinService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class SchoolBulletinCategoryController extends BaseUtils{
	private int pageNo;							//頁碼
	@Autowired
	SchoolBulletinCategoryService schoolBulletinCategoryService;
	@Autowired
	SchoolBulletinService schoolBulletinService;
	@Autowired
	private EditLogService editLogService;
	/**
	 * 清單頁面
	 * @return
	 */
	@RequestMapping(value = "schoolBulletinCategory/index", method = {RequestMethod.POST, RequestMethod.GET})
	public String index(@ModelAttribute SchoolBulletinCategory schoolBulletinCategory, Model model, 
			HttpServletRequest request, HttpSession session) {
		List<SchoolBulletinCategory> schoolBulletinCategoryList = new ArrayList<>();
		if (pageNo != 0) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();
		pageTotalCount = schoolBulletinCategoryService.getCount(schoolBulletinCategory);
		pageNo = super.pageSetting(pageNo);
		schoolBulletinCategoryList = schoolBulletinCategoryService.getList(pageCount, pageStart, schoolBulletinCategory);
		
		model.addAttribute("schoolBulletinCategoryList", schoolBulletinCategoryList).addAttribute("pageNo", pageNo)
		.addAttribute("pageTotalCount", pageTotalCount).addAttribute("pageCount", pageCount)
		.addAttribute("totalPage", totalPage).addAttribute("leftStartPage", leftStartPage)
		.addAttribute("leftPageNum", leftPageNum).addAttribute("rightPageNum", rightPageNum)
		.addAttribute("leftEndPage", leftEndPage).addAttribute("rightStartPage", rightStartPage)
		.addAttribute("rightEndPage", rightEndPage);
		return "admin/schoolBulletinCategory/schoolBulletinCategoryList";
	}
	/**
	 * 新增頁面
	 * @return
	 */
	@RequestMapping(value = "schoolBulletinCategory/add", method = {RequestMethod.POST})
	public String add(Model model, @ModelAttribute SchoolBulletinCategory schoolBulletinCategory) {
		schoolBulletinCategory.setName(null);
		return "admin/schoolBulletinCategory/schoolBulletinCategoryForm";
	}
	/**
	 * 新增類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "schoolBulletinCategory/addSubmit", method = {RequestMethod.POST})
	public String addSubmit (Model model, @ModelAttribute SchoolBulletinCategory schoolBulletinCategory, 
			@SessionAttribute("userAccountSession") User user)throws IOException {
		schoolBulletinCategory.setId(schoolBulletinCategoryService.getNextId());
		if(schoolBulletinCategory.getParent_id() == null) {
			schoolBulletinCategory.setParent_id(0);
		}
		schoolBulletinCategory.setCreate_by(user.getAccount());
		schoolBulletinCategory.setUpdate_by(user.getAccount());
		schoolBulletinCategoryService.add(schoolBulletinCategory);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(schoolBulletinCategory);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(schoolBulletinCategory.getId());
		editLog.setFunction("SCHOOL_BULLETIN_CATEGORY");
		editLog.setAction_type("ADD");
		editLog.setContent(jsonString);
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		
		schoolBulletinCategory.setShow_message("新增成功");
		return "admin/schoolBulletinCategory/toMessagePage";
	}
	
	@RequestMapping(value = "schoolBulletinCategory/checkRepeat", method = {RequestMethod.POST})
	public ResponseEntity<?> checkRepeat(@ModelAttribute SchoolBulletinCategory schoolBulletinCategory,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		String name = request.getParameter("name");
		String layer = request.getParameter("layer");
		String checkStr = "T";
		
		schoolBulletinCategory.setName(name);
		schoolBulletinCategory.setLayer(layer);
		schoolBulletinCategory = schoolBulletinCategoryService.checkRepeat(schoolBulletinCategory);
		if(schoolBulletinCategory != null) {
			checkStr = "F";
		}else {
			checkStr = "T";
			
		}
		return new ResponseEntity<String>(checkStr, HttpStatus.OK);
	}
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping(value = "schoolBulletinCategory/update", method = {RequestMethod.POST, RequestMethod.GET})
	public String update(Model model, @ModelAttribute SchoolBulletinCategory schoolBulletinCategory) {
		String parent_name = schoolBulletinCategory.getParent_name();
		schoolBulletinCategory = schoolBulletinCategoryService.getData(schoolBulletinCategory);
		schoolBulletinCategory.setParent_name(parent_name);
		model.addAttribute(schoolBulletinCategory);
		return "admin/schoolBulletinCategory/schoolBulletinCategoryForm";
	}
	/**
	 * 修改類別
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "schoolBulletinCategory/updateSubmit", method = {RequestMethod.POST})
	public String updateSubmit(Model model, @ModelAttribute SchoolBulletinCategory schoolBulletinCategory,
			@SessionAttribute("userAccountSession") User user) throws IOException {
		schoolBulletinCategory.setUpdate_by(user.getAccount());
		schoolBulletinCategoryService.update(schoolBulletinCategory);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(schoolBulletinCategory);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(schoolBulletinCategory.getId());
		editLog.setFunction("SCHOOL_BULLETIN_CATEGORY");
		editLog.setAction_type("UPDATE");
		editLog.setContent(jsonString);
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		
		schoolBulletinCategory.setShow_message("修改成功");
		return "admin/schoolBulletinCategory/toMessagePage";
	}
	/**
	 * 刪除
	 * @return
	 */
	@RequestMapping(value = "schoolBulletinCategory/delete", method = {RequestMethod.POST})
	public ResponseEntity<?> delete(HttpServletRequest request, @ModelAttribute SchoolBulletinCategory schoolBulletinCategory,
			@SessionAttribute("userAccountSession") User user) throws IOException {
		String selectList = request.getParameter("deleteList");
		String[] selectArray = selectList.split(",");
		Integer id = null;
		Integer count;
		for(int i = 0;i<selectArray.length;i++) {
			id = Integer.valueOf(selectArray[i]);
			schoolBulletinCategory.setId(id);
			
			count = schoolBulletinCategoryService.deleteCheck(schoolBulletinCategory);
			if(count!=0) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			
			schoolBulletinCategory = schoolBulletinCategoryService.getData(schoolBulletinCategory);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(schoolBulletinCategory);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(schoolBulletinCategory.getId());
			editLog.setFunction("SCHOOL_BULLETIN_CATEGORY");
			editLog.setAction_type("DELETE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			schoolBulletinCategoryService.delete(schoolBulletinCategory ,id);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@RequestMapping(value = "schoolBulletinCategory/checkData", method = {RequestMethod.POST})
	public ResponseEntity<?> checkData(@ModelAttribute SchoolBulletinCategory schoolBulletinCategory,@ModelAttribute SchoolBulletin schoolBulletin,
			HttpServletRequest request, HttpServletResponse response)throws IOException {
		List<SchoolBulletinCategory> schoolBulletinCategoryList = new ArrayList<>();
		String checkList = request.getParameter("checkList");
		String[] checkArray = checkList.split(",");
		String checkStr = "T";
		for(int i=0; i<checkArray.length; i++) {
			schoolBulletinCategory.setParent_id(Integer.valueOf(checkArray[i]));
			schoolBulletinCategoryList = schoolBulletinCategoryService.getSubList(schoolBulletinCategory);
			if(schoolBulletinCategoryList.size() > 0) {
				checkStr = "F";
				break;
			}
		}
		return new ResponseEntity<String>(checkStr, HttpStatus.OK);
	}
	/**
	 * 重新排序資料
	 * @return
	 */
	@RequestMapping(value = "schoolBulletinCategory/resetSort", method = {RequestMethod.POST})
	public String resetSort (HttpServletRequest request, @ModelAttribute SchoolBulletinCategory schoolBulletinCategory,
			@SessionAttribute("userAccountSession") User user) throws IOException{
		if(schoolBulletinCategory.getParent_id() == null) {
			schoolBulletinCategory.setParent_id(0);
		}
		schoolBulletinCategoryService.resetSort(schoolBulletinCategory);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(0);
		editLog.setFunction("SCHOOL_BULLETIN_CATEGORY");
		editLog.setAction_type("UPDATE");
		editLog.setContent("重新排序");
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		return "redirect:index";
	}
	
	
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/schoolBulletinCategory/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		getNormalBannerList = schoolBulletinCategoryService.getNormalList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		
			for(Map<String, Object> map : getNormalBannerList) {
				SchoolBulletinCategory schoolBulletinCategory = new SchoolBulletinCategory();
				
				if(map.get("ID") != null) {
					schoolBulletinCategory.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("PARENT_ID") != null) {
					schoolBulletinCategory.setParent_id(Integer.valueOf(map.get("PARENT_ID").toString()));
				}
				if(map.get("NAME") != null) {
					schoolBulletinCategory.setName(map.get("NAME").toString());
				}
				if(map.get("LAYER") != null) {
					schoolBulletinCategory.setLayer(map.get("LAYER").toString());
				}
				if(map.get("CREATE_BY") != null) {
					schoolBulletinCategory.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					schoolBulletinCategory.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					schoolBulletinCategory.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					schoolBulletinCategory.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("SORT") != null) {
					schoolBulletinCategory.setSort(Integer.valueOf(map.get("SORT").toString()));
				}
				schoolBulletinCategoryService.updateNormalData(schoolBulletinCategory);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
	
}
