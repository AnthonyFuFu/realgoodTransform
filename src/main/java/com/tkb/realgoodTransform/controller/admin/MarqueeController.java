package com.tkb.realgoodTransform.controller.admin;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.tkb.realgoodTransform.model.Marquee;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.MarqueeService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class MarqueeController extends BaseUtils {
	
	@Value("${upload.file.path}")
	private String uploadFilePath; // 檔案上傳位置
	private int pageNo;
	
	@Autowired
	private MarqueeService  marqueeService;
	
	@Autowired
	private EditLogService editLogService;
	
	/**
	 * 清單頁面
	 * @return
	 */
	@RequestMapping(value = "/marquee/index", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(@ModelAttribute Marquee marquee, Model model, HttpServletRequest request,
			HttpSession session) {
		List<Marquee> marqueeList = new ArrayList<>();
		
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		if(title!=null) {
			marquee.setTitle(title);
		}

		if (pageNo != 0) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();

		pageTotalCount = marqueeService.getCount(marquee);
		pageNo = super.pageSetting(pageNo);
		marqueeList = marqueeService.getList(pageCount, pageStart, marquee);
		
		model.addAttribute("marqueeList", marqueeList).addAttribute("pageNo", pageNo)
		.addAttribute("pageTotalCount", pageTotalCount).addAttribute("pageCount", pageCount)
		.addAttribute("totalPage", totalPage).addAttribute("leftStartPage", leftStartPage)
		.addAttribute("leftPageNum", leftPageNum).addAttribute("rightPageNum", rightPageNum)
		.addAttribute("leftEndPage", leftEndPage).addAttribute("rightStartPage", rightStartPage)
		.addAttribute("rightEndPage", rightEndPage);
		return "admin/marquee/marqueeList";
	}
	/**
	 * 新增頁面
	 * @return
	 */
	@RequestMapping(value = "/marquee/add", method = {RequestMethod.GET, RequestMethod.POST})
	public String add(@ModelAttribute Marquee marquee, Model model,HttpServletRequest request) {
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		
		model.addAttribute("searchTitle", title)
		.addAttribute("pageNo",pageNo);
		
		return "admin/marquee/marqueeForm";
	}
	/**
	 * 新增資料
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/marquee/addSubmit", method = {RequestMethod.POST})
	public String addSubmit (HttpServletRequest request,@ModelAttribute Marquee marquee, Model model, @SessionAttribute("userAccountSession") User user) throws IOException{
		
		Integer pageNo = 1 ;
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		
		int id = marqueeService.getNextId();
		marquee.setId(id);
		marquee.setCreate_by(user.getAccount());
		marquee.setUpdate_by(user.getAccount());
		marqueeService.add(marquee);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(marquee);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(marquee.getId());
		editLog.setFunction("MARQUEE");
		editLog.setAction_type("ADD");
		editLog.setContent(jsonString);
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		
		model.addAttribute("title", title);
		model.addAttribute("pageNo",pageNo);
		model.addAttribute("message","新增跑馬燈成功");
		return "admin/marquee/toList";
	}
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping(value = "/marquee/update", method = {RequestMethod.GET, RequestMethod.POST})
	public String update(HttpServletRequest request,@ModelAttribute Marquee marquee, Model model) {
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		
		marquee = marqueeService.getData(marquee);
		model.addAttribute(marquee)
		.addAttribute("searchTitle", title)
	 	 .addAttribute("pageNo",pageNo);
		
		return "admin/marquee/marqueeForm";
	}
	/**
	 * 修改資料
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value ="/marquee/updateSubmit", method= {RequestMethod.GET, RequestMethod.POST})
	public String updateSubmit (HttpServletRequest request,Marquee marquee, Model model, @SessionAttribute("userAccountSession") User user) {
		
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("searchTitle") == null ? null : request.getParameter("searchTitle");
		
		marquee.setUpdate_by(user.getAccount());
		marqueeService.update(marquee);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(marquee);
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(marquee.getId());
		editLog.setFunction("MARQUEE");
		editLog.setAction_type("UPDATE");
		editLog.setContent(jsonString);
		editLog.setCreate_by(user.getAccount());
		editLogService.addLog(editLog);
		
		model.addAttribute("title", title);
		model.addAttribute("pageNo",pageNo);
		model.addAttribute("message","修改跑馬燈成功");
		return "admin/marquee/toList";
	}
	/**
	 * 刪除
	 * @return
	 */
	@RequestMapping(value ="/marquee/delete", method = {RequestMethod.POST})
	public String delete(Model model,HttpServletRequest request,Marquee marquee,@SessionAttribute("userAccountSession") User user) throws IOException {
		String deleteString = request.getParameter("deleteList");
		String[] deleteArray = deleteString.split(",");
		Integer id = null;
		
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String title = request.getParameter("title") == null ? null : request.getParameter("title");
		
		for (int i = 0; i < deleteArray.length; i++) {
			id = Integer.valueOf(deleteArray[i]);
			marquee.setId(id);
			marquee = marqueeService.getData(marquee);
			Gson gson = new Gson();
			String jsonString = gson.toJson(marquee);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(marquee.getId());
			editLog.setFunction("MARQUEE");
			editLog.setAction_type("DELETE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			marqueeService.delete(id);
		}
		
		model.addAttribute("title", title);
		model.addAttribute("pageNo",pageNo);
		model.addAttribute("message", "刪除成功");
		
		return "admin/marquee/toList";
	}
	/**
	 * 重新排序資料
	 * @return
	 */
	@RequestMapping(value = "/marquee/resetSort", method= {RequestMethod.POST})
	public String resetSort(HttpServletRequest request,@SessionAttribute("userAccountSession") User user) throws IOException{
		marqueeService.resetSort();
		EditLog editLog = new EditLog();
		int logId = editLogService.getNextLogId();
		editLog.setId(logId);
		editLog.setData_id(0);
		editLog.setFunction("MARQUEE");
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
	@GetMapping("/marquee/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		getNormalBannerList = marqueeService.getNormalBannerList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		
			for(Map<String, Object> map : getNormalBannerList) {
				Marquee marquee = new Marquee();
				
				if(map.get("ID") != null) {
					marquee.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("TITLE") != null) {
					marquee.setTitle(map.get("TITLE").toString());
				}
				if(map.get("CONTENT") != null) {
					marquee.setContent(map.get("CONTENT").toString());
				}
				if(map.get("LINK") != null) {
					marquee.setLink(map.get("LINK").toString());
				}
				if(map.get("CLICK_RATE") != null) {
					marquee.setClick_rate(Integer.valueOf(map.get("CLICK_RATE").toString()));
				}
				if(map.get("BEGIN_DATE") != null) {
					marquee.setBegin_date(new Date(sdf.parse(map.get("BEGIN_DATE").toString()).getTime()));
				}
				if(map.get("END_DATE") != null) {
					marquee.setEnd_date(new Date(sdf.parse(map.get("END_DATE").toString()).getTime()));
				}
				if(map.get("CREATE_BY") != null) {
					marquee.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					marquee.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					marquee.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					marquee.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
				}
				if(map.get("SORT") != null) {
					marquee.setSort(Integer.valueOf(map.get("SORT").toString()));
				}
				marqueeService.updateNormalData(marquee);
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
}
