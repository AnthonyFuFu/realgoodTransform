package com.tkb.realgoodTransform.controller.admin;

import java.io.IOException;
import java.sql.Date;
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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.model.GSATNews;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.GSATNewsService;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class GSATNewsController extends BaseUtils {

	private int pageNo;
	@Autowired
	private GSATNewsService gSATNewsService;
	@Autowired
	private EditLogService editLogService; // 各功能編輯的LOG服務

	/**
	 * 清單頁面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/gSATNews/index")
	public String index(GSATNews gSATNews, HttpServletRequest request, Model model) {
		List<GSATNews> gSATNewsList = new ArrayList<>();
		pageNo = super.setPage(pageNo, request);
		pageTotalCount = gSATNewsService.getCount(gSATNews);
		pageNo = super.pageSetting(pageNo);
		gSATNewsList = gSATNewsService.getList(pageCount, pageStart, gSATNews);
		model.addAttribute("gSATNewsList", gSATNewsList)
			 .addAttribute("gSATNews", gSATNews);
		addModelAttribute(pageNo, model);
		return "admin/GSATNews/GSATNewsList";

	}

	/**
	 * 新增頁面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/gSATNews/add")
	public String add(GSATNews gSATNews, Model model) {
		model.addAttribute("gSATNews", gSATNews);
		return "admin/GSATNews/GSATNewsForm";
	}

	/**
	 * 新增資料
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/gSATNews/addSubmit")
	public String addSubmit(GSATNews gSATNews, @SessionAttribute("userAccountSession") User user,
			Model model) {
		try {
			int id = gSATNewsService.getNextId();
			gSATNews.setId(id);
			gSATNews.setCreate_by(user.getAccount());
			gSATNews.setUpdate_by(user.getAccount());
			gSATNewsService.add(gSATNews);

			Gson gson = new Gson();
			String jsonString = gson.toJson(gSATNews);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(gSATNews.getId());
			editLog.setFunction("GSAT_NEWS");
			editLog.setAction_type("ADD");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			model.addAttribute("message", "新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "新增失敗");
		}
		return "admin/GSATNews/toList";

	}

	/**
	 * 修改頁面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/gSATNews/update")
	public String update(GSATNews gSATNews, Model model) {
		gSATNews = gSATNewsService.getData(gSATNews);
		model.addAttribute("gSATNews", gSATNews);
		return "admin/GSATNews/GSATNewsForm";

	}

	/**
	 * 修改資料
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/gSATNews/updateSubmit")
	public String updateSubmit(GSATNews gSATNews, @SessionAttribute("userAccountSession") User user,
			Model model) {
		try {
			gSATNews.setUpdate_by(user.getAccount());
			gSATNewsService.update(gSATNews);
			Gson gson = new Gson();
			String jsonString = gson.toJson(gSATNews);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(gSATNews.getId());
			editLog.setFunction("GSAT_NEWS");
			editLog.setAction_type("UPDATE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			model.addAttribute("message", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "修改失敗");
		}
		return "admin/GSATNews/toList";

	}

	/**
	 * 刪除外交特考文章
	 * 
	 * @return
	 */
	@RequestMapping(value = "/gSATNews/delete")
	public String delete(GSATNews gSATNews, HttpServletRequest request,
			@SessionAttribute("userAccountSession") User user, Model model) {

		try {
			String selectList = request.getParameter("deleteList");
			String[] selectArray = selectList.split(",");
			Integer id = null;
			for (int i = 0; i < selectArray.length; i++) {
				id = Integer.valueOf(selectArray[i]);
				gSATNews.setId(id);
				gSATNewsService.delete(gSATNews, id);
				Gson gson = new Gson();
				String jsonString = gson.toJson(gSATNews);
				EditLog editLog = new EditLog();
				int logId = editLogService.getNextLogId();
				editLog.setId(logId);
				editLog.setData_id(id);
				editLog.setFunction("GSAT_NEWS");
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
		return "admin/GSATNews/toList";

	}
	
	
	
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/gSATNews/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalList = new ArrayList<>();
		getNormalList = gSATNewsService.getNormalList();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			for(Map<String, Object> map : getNormalList) {
				GSATNews gSATNews = new GSATNews();
				
				if(map.get("ID") != null) {
					gSATNews.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("TITLE") != null) {
					gSATNews.setTitle(map.get("TITLE").toString());
				}
				if(map.get("CATEGORY") != null) {
					gSATNews.setCategory(map.get("CATEGORY").toString());
				}
				if(map.get("SHOW") != null) {
					gSATNews.setShow(Integer.valueOf(map.get("SHOW").toString()));
				}
				
				if(map.get("CREATE_BY") != null) {
					gSATNews.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					gSATNews.setCreate_date(new Timestamp(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					gSATNews.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					gSATNews.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("BEGIN_DATE") != null) {
					gSATNews.setBegin_date(new Date(sdf.parse(map.get("BEGIN_DATE").toString()).getTime()));
				}
				if(map.get("END_DATE") != null) {
					gSATNews.setEnd_date(new Date(sdf.parse(map.get("END_DATE").toString()).getTime()));
				}
				if(map.get("CONTENT") != null) {
					gSATNews.setContent(map.get("CONTENT").toString());
				}
				gSATNewsService.updateNormalData(gSATNews);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
}
