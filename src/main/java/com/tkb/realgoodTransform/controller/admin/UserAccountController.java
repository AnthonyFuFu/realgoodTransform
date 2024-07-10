package com.tkb.realgoodTransform.controller.admin;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;



import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.Location;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.model.UserAccount;
import com.tkb.realgoodTransform.service.UserAccountService;
import com.tkb.realgoodTransform.tkbApiService.TkbApiService;
import com.tkb.realgoodTransform.utils.AreaLocationApi;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = {"userAccountSession", "sideMenuList"})
@RequiredArgsConstructor
public class UserAccountController extends BaseUtils {
	
	private int pageNo;					//頁碼
	
	private final String reqEncryptKey = "tkb-api-req-xxxx";
	private final String repDecryptKey = "tkb-api-rep-xxxx";
	//ERP
	private final String appName = "ERP";
	
	private final UserAccountService userAccountService;		//帳號設定服務
	private final TkbApiService tkbApiService;				//TKBAPI服務
	
	/**
	 * 帳號設定頁面
	 * @return
	 */
	@RequestMapping(value = "/userAccount/index")
	public String index(@ModelAttribute("userAccount") User user,
			Model model, HttpServletRequest request, HttpSession session) {

		AreaLocationApi areaLocationApi = new AreaLocationApi();
		List<User> userAccountList = new ArrayList<>();
		List<Location> allLocationList = areaLocationApi.jsonToLocationList(areaLocationApi.getApiLocation("0","1"));
		
		pageNo = setPage(pageNo, request);
		
		pageTotalCount = userAccountService.getCount(user);
		pageNo = super.pageSetting(pageNo);
		userAccountList = userAccountService.getList(pageCount, pageStart, user);
		
		Gson gson = new Gson();
		String userAccountListJson = gson.toJson(userAccountList);
		Type listType = new TypeToken<List<Map<String, String>>>(){}.getType();
		List<Map<String, String>> UAList = new Gson().fromJson(userAccountListJson, listType);
		List<User> convertedUserList = new ArrayList<>();
		
		SimpleDateFormat inputFormat = new SimpleDateFormat("MMM dd, yyyy");
    	
		for (Map<String, String> UAMap : UAList) {
		    String area = (String) UAMap.get("area");
		    if ("0".equals(area)) {
		    	UAMap.put("area_name", "全區");
			} else {
				for(int i=0; i<allLocationList.size(); i++) {
					if(allLocationList.get(i).getId().toString().equals(area)) {
						UAMap.put("area_name",allLocationList.get(i).getName());
					}
				}
			}
		    User tempUser = new User();
		    tempUser.setId(Integer.parseInt(UAMap.get("id")));
		    tempUser.setAccount(UAMap.get("account"));
		    tempUser.setChinese_name(UAMap.get("chinese_name"));
		    tempUser.setDepartment_no(UAMap.get("department_no"));
		    tempUser.setEmail(UAMap.get("email"));
		    tempUser.setStatus(UAMap.get("status"));
		    tempUser.setCreate_by(UAMap.get("create_by"));
		    tempUser.setUpdate_by(UAMap.get("update_by"));
		    try {
		        Date inputCreateDate = inputFormat.parse(UAMap.get("create_date"));
		        java.sql.Date createDate = new java.sql.Date(inputCreateDate.getTime());
		        
		        Date inputUpdateDate = inputFormat.parse(UAMap.get("update_date"));
		        java.sql.Date updateDate = new java.sql.Date(inputUpdateDate.getTime());
		        
		        tempUser.setCreate_date(createDate);
		        tempUser.setUpdate_date(updateDate);
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    tempUser.setArea(UAMap.get("area"));
		    tempUser.setGroups_name(UAMap.get("groups_name"));
		    tempUser.setArea_name(UAMap.get("area_name"));
		    
		    convertedUserList.add(tempUser);
		    
		}
		userAccountList = convertedUserList;
		
		model.addAttribute("userAccountList", userAccountList);
		addModelAttribute(pageNo, model);
		
		return "admin/userAccount/userAccountList";
	}
	
	/**
	 * 修改頁面
	 * @return
	 */
	@RequestMapping(value = "/userAccount/update", method = {RequestMethod.GET, RequestMethod.POST})
	public String update(Model model, User user, Area area) {
		userAccountService.getUpdatePageData(model, user, area);
		return "admin/userAccount/userAccountForm";
	}
	
	/**
	 * 跳轉頁
	 * @return String
	 */
	@RequestMapping("/userAccount/toList")
	public String toList(Model model, @ModelAttribute User userAccount) {
		model.addAttribute("userAccount", userAccount);
		return "admin/userAccount/toList";
	}
	
	/**
	 * 修改帳號設定
	 */
	@PostMapping("/userAccount/updateSubmit")
	public String updateSubmit(@SessionAttribute("userAccountSession") User userAccountSession,
			Model model, User user) {
		try {
			userAccountService.updateUserInfo(model, user, userAccountSession);
			model.addAttribute("message", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "修改失敗");
		}
		model.addAttribute("link", "index");
		return "admin/userAccount/toList";
	}
	
	/**
	 * 取得第二層地區
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@GetMapping("/userAccount/changeArea")
	public ResponseEntity<?> changeArea(HttpServletRequest request, Area area){
		
		AreaLocationApi areaLocationApi = new AreaLocationApi();
		String id = request.getParameter("id");
		String website = "D";
		List<Location> locationList = new ArrayList<>();
		List<Location> tempLocationList = new ArrayList<>();
		
		if ("0".equals(id)) {
			Location tempLocation = new Location();
			tempLocationList.add(tempLocation);
			locationList = tempLocationList;
		} else if ("1".equals(id)) {
			Location tempLocation = new Location();
			tempLocation.setId(Integer.valueOf("0"));
			tempLocation.setName("全區");
			tempLocationList.add(tempLocation);
			locationList = tempLocationList;
		} else {
			String location = areaLocationApi.getApiLocation(website,id);
			List<Location> apiLocationList = areaLocationApi.jsonToLocationList(location);
			locationList = apiLocationList;
		}
		
		if (locationList.size() == 0) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);	//204
		}else {
			return new ResponseEntity<List<Location>>(locationList, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/userAccount/getDepartment", method = RequestMethod.POST)
	public ResponseEntity<?> getDepartment(UserAccount userAccount, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		//公司代號
		String company = request.getParameter("company") == null ? "TKB" : request.getParameter("company");
		userAccount.setCompany(company);
		
		List<Map> departmentList = tkbApiService.departmentList(userAccount, appName, reqEncryptKey, repDecryptKey);
		
		return new ResponseEntity<String>(new JSONArray(departmentList).toString(), HttpStatus.OK);
	}
	
	/**
	 * 取得單位代號
	 * @throws Exception
	 */
	@RequestMapping(value = "/userAccount/getUnit", method = RequestMethod.POST)
	public ResponseEntity<?> getUnit(UserAccount userAccount,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List<Map> unitList = new ArrayList<>();
		//公司代號
		String company = request.getParameter("company") == null ? "TKB" : request.getParameter("company");
		//部門代號
		String department = request.getParameter("department") == null ? "N" : request.getParameter("department");
		userAccount.setCompany(company);
		userAccount.setDepartment_no(department);
		
		unitList = tkbApiService.unitList(userAccount, appName, reqEncryptKey, repDecryptKey);
		
		return new ResponseEntity<String>(new JSONArray(unitList).toString(), HttpStatus.OK);
	}
	
	
}
