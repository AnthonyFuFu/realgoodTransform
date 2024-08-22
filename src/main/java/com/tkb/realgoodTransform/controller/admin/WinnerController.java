package com.tkb.realgoodTransform.controller.admin;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.model.Winner;
import com.tkb.realgoodTransform.model.WinnerCategory;
import com.tkb.realgoodTransform.model.WinnerContent;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.service.WinnerCategoryService;
import com.tkb.realgoodTransform.service.WinnerContentService;
import com.tkb.realgoodTransform.service.WinnerService;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class WinnerController extends BaseUtils {
	
	private int pageNo;									        		//頁碼
	
	@Autowired
	private WinnerService winnerService;
	
	@Autowired
	private WinnerContentService winnerContentService;
	
	@Autowired
	private WinnerCategoryService winnerCategoryService;
	
	@Autowired
	private EditLogService editLogService;
	
	/**
	 * 清單頁面
	 * @return
	 */
	@RequestMapping(value = "/winner/index", method = {RequestMethod.POST, RequestMethod.GET})
	public String index (@ModelAttribute Winner winner,@ModelAttribute WinnerCategory winnerCategory,Model model,
			HttpServletRequest request, HttpSession session) {
		
		String searchParent_CategoryParam = request.getParameter("searchParent_category");
		String parent_category = (searchParent_CategoryParam == null || searchParent_CategoryParam.isEmpty()) ? "0" : searchParent_CategoryParam;
		
		String searchCategoryParam = request.getParameter("searchCategory");
		String searchCategory = (searchCategoryParam == null || searchCategoryParam.isEmpty()) ? "0" : searchCategoryParam;
		
		
		String searchItem = request.getParameter("searchItem") == null ? null : request.getParameter("searchItem");
		String year = request.getParameter("year") == null ? null : request.getParameter("year");
		String name = request.getParameter("name") == null ? null : request.getParameter("name");
		
		
		
		if(name!=null) {
			model.addAttribute("name", name);
		}
		if(year!=null) {
			model.addAttribute("year", year);
		}
		if(parent_category!=null && !parent_category.equals("0")) {
			winner.setParent_category(parent_category);
		}
		if(searchCategory!=null && !searchCategory.equals("0")) {
			winner.setCategory(searchCategory);
		}

		List<Winner> winnerList = new ArrayList<>();
		List<WinnerCategory>winnerCategoryList = new ArrayList<>();
		List<WinnerCategory>winnerCategoryIIList = new ArrayList<>();
		
		winnerCategory.setParent_id(0);
		winnerCategoryList = winnerCategoryService.getLayerList("1", winnerCategory);
		
		if(winner.getParent_category()!=null && !"".equals(winner.getParent_category())) {
			winnerCategory.setParent_id(Integer.valueOf(winner.getParent_category()));
			winnerCategoryIIList = winnerCategoryService.getLayerList("2", winnerCategory);
		}
		
		
		if (pageNo != 0) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();
		pageTotalCount = winnerService.getCount(winner);
		pageNo = super.pageSetting(pageNo);
		 
		winnerList = winnerService.getList(pageCount, pageStart, winner);
		model.addAttribute("winnerList", winnerList)
			 .addAttribute("winnerCategoryList", winnerCategoryList)
			 .addAttribute("winnerCategoryIIList", winnerCategoryIIList)
			 .addAttribute("searchParent_category", Integer.parseInt(parent_category))
			 .addAttribute("searchCategory", Integer.parseInt(searchCategory))
			 .addAttribute("searchItem", searchItem);
		addModelAttribute(pageNo, model);
		return "admin/winner/winnerList";
	}
	
	/**
	 * 取得第二層類別
	 */
	@RequestMapping("/winner/changeCategoryTwo")
	public ResponseEntity<?> changeCategoryTwo(@RequestParam String id, 
			WinnerCategory winnerCategory) {
		return winnerService.changeCategoryTwo(id, winnerCategory);
	}
	
	/**
	 * 取得第三層類別
	 * @throws Exception
	 */
	@RequestMapping("/winner/changeCategoryThree")
	@ResponseBody
	public String changeCategoryThree(WinnerCategory winnerCategory) throws Exception {
		List<WinnerCategory>winnerCategoryIIIList = new ArrayList<>();
		winnerCategoryIIIList = winnerCategoryService.getLayerList("3", winnerCategory);
		JSONArray tJSONArray = new JSONArray(winnerCategoryIIIList);
		return tJSONArray.toString();
	}
	
	
	/**
	 * 新增頁面
	 * @return
	 */
	@RequestMapping("/winner/add")
	public String add (@ModelAttribute WinnerCategory winnerCategory,Model model,@ModelAttribute Winner winner,HttpServletRequest request) {
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String parent_category = request.getParameter("parent_category") == null ? null : request.getParameter("parent_category");
		String category = request.getParameter("category") == null ? null : request.getParameter("category");
		String searchItem = request.getParameter("searchItem") == null ? null : request.getParameter("searchItem");
		String year = request.getParameter("year") == null ? null : request.getParameter("year");
		String name = request.getParameter("name") == null ? null : request.getParameter("name");
		if(name!=null) {
			model.addAttribute("searchName", name);
		}
		if(year!=null) {
			model.addAttribute("searchYear", year);
		}
		model.addAttribute("winnerCategoryList", winnerService.addFunction(winnerCategory))
		.addAttribute("searchItem", searchItem)
		.addAttribute("searchParent_category", parent_category)
		.addAttribute("searchCategory", category)
		.addAttribute("pageNo",pageNo);
		return "admin/winner/winnerForm";
	}
	
	/**
	 * 新增資料
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/winner/addSubmit")
	public String addSubmit (@ModelAttribute Winner winner, @SessionAttribute("userAccountSession") User user,
			HttpServletRequest request,@ModelAttribute WinnerContent winnerContent,Model model) throws IOException{
		Integer pageNo = 1 ;
		String parent_category = request.getParameter("searchParent_category") == null ? null : request.getParameter("searchParent_category");
		String category = request.getParameter("searchCategory") == null ? null : request.getParameter("searchCategory");
		String searchItem = request.getParameter("searchItem") == null ? null : request.getParameter("searchItem");
		String year = request.getParameter("searchYear") == null ? null : request.getParameter("searchYear");
		String name = request.getParameter("searchName") == null ? null : request.getParameter("searchName");
		if(name!=null) {
			model.addAttribute("name", name);
		}
		if(year!=null) {
			model.addAttribute("year", year);
		}
		try {
			winnerService.addSubmitFunction(winner, winnerContent, user, request);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(winner);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(winner.getId());
			editLog.setFunction("WINNER");
			editLog.setAction_type("ADD");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			model.addAttribute("message", "新增成功");
		} catch (NullPointerException ee) {
			ee.printStackTrace();
			model.addAttribute("message", "新增失敗,請輸入ICON,內容主題,內文");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "新增失敗");
		}
		model.addAttribute("searchItem",searchItem)
		 .addAttribute("searchParent_category",parent_category)
		 .addAttribute("searchCategory",category)
		 .addAttribute("pageNo",pageNo);
		return "admin/winner/toList";
	}
	
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping("/winner/update")
	public String update (@ModelAttribute Winner winner, @ModelAttribute WinnerCategory winnerCategory,
			@ModelAttribute WinnerContent winnerContent, Model model,HttpServletRequest request) {
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String parent_category = request.getParameter("parent_category") == null ? null : request.getParameter("parent_category");
		String category = request.getParameter("category") == null ? null : request.getParameter("category");
		String searchItem = request.getParameter("searchItem") == null ? null : request.getParameter("searchItem");
		String year = request.getParameter("year") == null ? null : request.getParameter("year");
		String name = request.getParameter("name") == null ? null : request.getParameter("name");
		if(name!=null) {
			model.addAttribute("searchName", name);
		}
		if(year!=null) {
			model.addAttribute("searchYear", year);
		}
		model.addAttribute("winner", winnerService.updateFunction(winner, winnerCategory, winnerContent).get("winner"))
			 .addAttribute("winnerCategoryList", winnerService.updateFunction(winner, winnerCategory, winnerContent).get("winnerCategoryList"))
			 .addAttribute("winnerContentList", winnerService.updateFunction(winner, winnerCategory, winnerContent).get("winnerContentList"))
			 .addAttribute("winnerCategory", winnerService.updateFunction(winner, winnerCategory, winnerContent).get("winnerCategory"))
			 .addAttribute("searchItem", searchItem)
			 .addAttribute("searchParent_category",parent_category)
			 .addAttribute("searchCategory",category)
			 .addAttribute("pageNo",pageNo);
		return "admin/winner/winnerForm";
	}
	
	/**
	 * 修改資料
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/winner/updateSubmit")
	public String updateSubmit (@ModelAttribute WinnerContent winnerContent,
			@ModelAttribute Winner winner, HttpServletRequest request, 
			@SessionAttribute("userAccountSession") User user,Model model) throws IOException{
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String parent_category = request.getParameter("searchParent_category") == null ? null : request.getParameter("searchParent_category");
		String category = request.getParameter("searchCategory") == null ? null : request.getParameter("searchCategory");
		String searchItem = request.getParameter("searchItem") == null ? null : request.getParameter("searchItem");
		String year = request.getParameter("searchYear") == null ? null : request.getParameter("searchYear");
		String name = request.getParameter("searchName") == null ? null : request.getParameter("searchName");
		if(name!=null) {
			model.addAttribute("name", name);
		}
		if(year!=null) {
			model.addAttribute("year", year);
		}
		try {
			winnerService.updateSubmitFunction(winner, winnerContent, user, request);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(winner);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(winner.getId());
			editLog.setFunction("WINNER");
			editLog.setAction_type("UPDATE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			model.addAttribute("message", "修改成功");
		} catch (NullPointerException ee) {
			ee.printStackTrace();
			model.addAttribute("message", "修改失敗,請輸入ICON,內容主題,內文");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "修改失敗");
		}
		model.addAttribute("searchItem",searchItem)
			 .addAttribute("searchParent_category",parent_category)
			 .addAttribute("searchCategory",category)
			 .addAttribute("pageNo",pageNo);
		
		return "admin/winner/toList";
	}
	
	/**
	 * 刪除贏家經驗談
	 * @return
	 */
	@RequestMapping("/winner/delete")
	public String delete (WinnerContent winnerContent, Winner winner,
			@RequestParam("deleteList") String selectList, 
			@SessionAttribute("userAccountSession") User user,HttpServletRequest request,Model model) throws IOException{
		Integer pageNo = request.getParameter("pageNo") == null ? null : Integer.parseInt(request.getParameter("pageNo"));
		String searchItem = request.getParameter("searchItem") == null ? null : request.getParameter("searchItem");
		String year = request.getParameter("year") == null ? null : request.getParameter("year");
		String name = request.getParameter("name") == null ? null : request.getParameter("name");
		String parent_category = request.getParameter("parent_category") == null ? null : request.getParameter("parent_category");
		String category = request.getParameter("category") == null ? null : request.getParameter("category");
		if(name!=null) {
			model.addAttribute("name", name);
		}
		if(year!=null) {
			model.addAttribute("year", year);
		}
		winnerService.deleteFunction(winnerContent, winner, selectList, user);
		model.addAttribute("searchItem",searchItem)
			 .addAttribute("searchParent_category",parent_category)
			 .addAttribute("searchCategory",category)
			 .addAttribute("message","刪除成功")
			 .addAttribute("pageNo",pageNo);
		return "admin/winner/toList";
	}
	
	/**
	 * 檢視贏家經驗談
	 * @return
	 */
	@RequestMapping("/winner/view")
	public String view (Winner winner , WinnerContent winnerContent, Model model) {
		model.addAttribute("winner", winnerService.viewFunction(winner, winnerContent).get("winner"))
		     .addAttribute("winnerContentList", winnerService.viewFunction(winner, winnerContent).get("winnerContentList"));
		return "admin/winner/winnerView";
	}
	
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/winner/updateNormalData")
	public String updateNormalData() throws ParseException {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		getNormalBannerList =  winnerService.getNormalList();
		List<Map<String, Object>> getNormalBannerWinnerContentList = new ArrayList<>();
		getNormalBannerWinnerContentList =  winnerContentService.getNormalList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//winner這個table
		for(Map<String, Object> map : getNormalBannerList) {
			Winner winner = new Winner();
			
			if(map.get("ID") != null) {
				winner.setId(Integer.valueOf(map.get("ID").toString()));
			}
			if(map.get("IMAGE") != null) {
				winner.setImage(map.get("IMAGE").toString());
			}
			if(map.get("CLICK_RATE") != null) {
				winner.setClick_rate(Integer.valueOf(map.get("CLICK_RATE").toString()));
			}
			if(map.get("YEAR") != null) {
				winner.setYear(map.get("YEAR").toString());
			}
			if(map.get("CATEGORY") != null) {
				winner.setCategory(map.get("CATEGORY").toString());
			}
			if(map.get("CREATE_BY") != null) {
				winner.setCreate_by(map.get("CREATE_BY").toString());
			}
			if(map.get("CREATE_DATE") != null) {
				winner.setCreate_date(map.get("CREATE_DATE").toString());
			}
			if(map.get("UPDATE_BY") != null) {
				winner.setUpdate_by(map.get("UPDATE_BY").toString());
			}
			if(map.get("UPDATE_DATE") != null) {
				winner.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
			}
			if(map.get("NAME") != null) {
				winner.setName(map.get("NAME").toString());
			}
			if(map.get("ADMITTED") != null) {
				winner.setAdmitted(map.get("ADMITTED").toString());
			}
			if(map.get("ORIGINAL") != null) {
				winner.setOriginal(map.get("ORIGINAL").toString());
			}
			if(map.get("SUMMARY") != null) {
				winner.setSummary(map.get("SUMMARY").toString());
			}
			if(map.get("SHOW") != null) {
				winner.setShow(Integer.valueOf(map.get("SHOW").toString()));
			}
			if(map.get("PHOTO") != null) {
				winner.setPhoto(map.get("PHOTO").toString());
			}
			if(map.get("VIDEO") != null) {
				winner.setVideo(map.get("VIDEO").toString());
			}
			if(map.get("PARENT_CATEGORY") != null) {
				winner.setParent_category(map.get("PARENT_CATEGORY").toString());
			}
			if(map.get("ENCRYPTURL") != null) {
				winner.setEncrypturl(map.get("ENCRYPTURL").toString());
			}
			if(map.get("STICK_TOP") != null) {
				winner.setStick_top(Integer.valueOf(map.get("STICK_TOP").toString()));
			}
			if(map.get("SHOW_INDEX") != null) {
				winner.setShow_index(Integer.valueOf(map.get("SHOW_INDEX").toString()));
			}
			if(map.get("VIDEO_IMAGE") != null) {
				winner.setVideo_image(map.get("VIDEO_IMAGE").toString());
			}
			if(map.get("CATEGORY_THREE") != null) {
				winner.setCategory_three(map.get("CATEGORY_THREE").toString());
			}
			if(map.get("PRODUCT_CATEGORY") != null) {
				winner.setProduct_category(map.get("PRODUCT_CATEGORY").toString());
			}
			
			winnerService.updateNormalData(winner);
		}
		
		
		
		//winner_content這個table
				for(Map<String, Object> map : getNormalBannerWinnerContentList) {
					WinnerContent winnerContent = new WinnerContent();
					
					if(map.get("ID") != null) {
						winnerContent.setId(Integer.valueOf(map.get("ID").toString()));
					}
					if(map.get("WINNER_ID") != null) {
						winnerContent.setWinner_id(Integer.valueOf(map.get("WINNER_ID").toString()));
					}
					if(map.get("ICON") != null) {
						winnerContent.setIcon(map.get("ICON").toString());
					}
					if(map.get("TITLE") != null) {
						winnerContent.setTitle(map.get("TITLE").toString());
					}
					if(map.get("CONTENT") != null) {
						winnerContent.setContent(map.get("CONTENT").toString());
					}
					if(map.get("CREATE_BY") != null) {
						winnerContent.setCreate_by(map.get("CREATE_BY").toString());
					}
					if(map.get("CREATE_DATE") != null) {
						winnerContent.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
					}
					if(map.get("UPDATE_BY") != null) {
						winnerContent.setUpdate_by(map.get("UPDATE_BY").toString());
					}
					if(map.get("UPDATE_DATE") != null) {
						winnerContent.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
					}
					
					winnerContentService.updateNormalData(winnerContent);
				}
		
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
}
