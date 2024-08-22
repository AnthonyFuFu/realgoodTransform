package com.tkb.realgoodTransform.controller.admin;

import java.io.File;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.model.Admit;
import com.tkb.realgoodTransform.model.AdmitCategory;
import com.tkb.realgoodTransform.model.AdmitContent;
import com.tkb.realgoodTransform.model.AdmitContentOption;
import com.tkb.realgoodTransform.model.AdmitDetail;
import com.tkb.realgoodTransform.model.AdmitDetailLog;
import com.tkb.realgoodTransform.model.AdmitLog;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.AdmitCategoryService;
import com.tkb.realgoodTransform.service.AdmitContentOptionService;
import com.tkb.realgoodTransform.service.AdmitContentService;
import com.tkb.realgoodTransform.service.AdmitDetailService;
import com.tkb.realgoodTransform.service.AdmitService;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.CryptographyUtils;
import com.tkb.realgoodTransform.utils.UploadUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jxl.Sheet;
import jxl.Workbook;




/**
 * 金榜類別Action
 * 
 * @author
 * @version 創建時間：2016-05-17
 */
@Controller
@RequestMapping("/tkbrule")
@SessionAttributes(value = { "userAccountSession", "sideMenuList" })
public class AdmitController extends BaseUtils {
	@Value("${upload.file.path}")
	private String uploadFilePath; // 榜單檔案
	private int pageNo; // 頁碼
	
	@Autowired
	private AdmitContentService admitContentService;

	@Autowired
	private AdmitService admitService;

	@Autowired
	private AdmitContentOptionService admitContentOptionService;

	@Autowired
	private AdmitDetailService admitDetailService;

	@Autowired
	private AdmitCategoryService admitCategoryService;

	@Autowired
	private EditLogService editLogService;

	@RequestMapping(value = "/admit/index")
	public String index(Admit admit, Model model, HttpServletRequest request, HttpSession session,
			String title_index, String pageNoLog) {
		List<Admit> admitList = new ArrayList<>();
		
		if("".equals(admit.getTitle()) || admit.getTitle() == null) {
	    	admit.setTitle(title_index);
	    }
		if (pageNo != 0 && !"".equals(request.getParameter("pageNo"))) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();
		pageTotalCount = admitService.getCount(admit);
		pageNo = super.pageSetting(pageNo);
		admitList = admitService.getList(pageCount, pageStart, admit);
		
		for(Admit admits : admitList) {
			String formattedDate = admits.getUpdate_date().substring(0, 10);
		    admits.setUpdate_date(formattedDate);
		}
//        admit.setTitle(title_index);
        model.addAttribute("admit", admit);
        model.addAttribute("pageNoLog", pageNo);
		
		model.addAttribute("admitList", admitList);
		addModelAttribute(pageNo, model);
		return "admin/admit/admitList";

	}

	@RequestMapping(value = "admit/add")
	public String add(Model model, AdmitCategory admitCategory,Admit admit , String title,
			HttpSession session, String pageNoLog) {
		
		admit.setTitle("");
		
		List<AdmitCategory> admitCategoryList = new ArrayList<>();
		admitCategory.setParent_id(0);
		admitCategoryList = admitCategoryService.getLayerList("1", admitCategory);
		model.addAttribute("title", title);
		model.addAttribute("pageNoLog", pageNoLog);
		model.addAttribute("admitCategoryList", admitCategoryList);
		return "admin/admit/admitForm";

	}

	/**
	 * 新增資料
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/admit/addSubmit")
	public String addSubmit(Admit admit, AdmitContent admitContent, AdmitContentOption admitContentOption,
			AdmitDetail admitDetail, @SessionAttribute("userAccountSession") User user,
			@RequestParam("uploadImage_banner") MultipartFile uploadImage_banner,
			@RequestParam("uploadImage_list") MultipartFile uploadImage_list,
			@RequestParam("uploadFile") MultipartFile uploadFile,
			 Model model,HttpServletRequest request, String title_index, String pageNoLog) throws IOException {
		
		model.addAttribute("title_index", title_index);
		model.addAttribute("pageNoLog", pageNoLog);

		if (super.checkImageWidth(uploadImage_banner, 1180) && super.checkImageHeight(uploadImage_banner, 581)
				&& super.checkImageWidth(uploadImage_list, 300) && super.checkImageHeight(uploadImage_list, 184)) {
			UploadUtil uploadUtil = new UploadUtil();
			String create_user = user.getAccount();
			admit.setImage_url(uploadUtil.uploadv2(uploadFilePath + "image/admit/", uploadImage_list, admit.getId()));
			admit.setBanner_url(uploadUtil.uploadv2(uploadFilePath + "image/admit/banner/", uploadImage_banner, admit.getId()));
			admit.setFile_url(uploadUtil.uploadv2(uploadFilePath + "excel/admit/", uploadFile, admit.getId()));
			
			admit.setCreate_by(create_user);
			admit.setUpdate_by(create_user);
			
			int id = admitService.getNextId();// admit_id 主榜ID
			admit.setId(id);
			
			admit.setEncrypturl(CryptographyUtils.encryptStr(String.valueOf(admit.getId())));
			
			admitService.add(admit);
			
			int contentId = 0;
			int contentOptionId = 0;
			int detailId = 0;
			
			admitContent.setAdmit_id(admit.getId());
			admitContent.setCreate_by(create_user);
		
			String contentTitle = request.getParameter("admit.content_title") == null ? "" : request.getParameter("admit.content_title");
			String content_summary = request.getParameter("admit.content_summary") == null ? "" : request.getParameter("admit.content_summary");
			String content_name = request.getParameter("admit.content_name") == null ? "" : request.getParameter("admit.content_name");
			String content_achievement = request.getParameter("admit.content_achievement") == null ? "" : request.getParameter("admit.content_achievement");

			admit.setContent_title(contentTitle);
			admit.setContent_summary(content_summary);
			admit.setContent_name(content_name);
			admit.setContent_achievement(content_achievement);
			
			
			String[] contentTitleList = admit.getContent_title().split(",_,");
			String[] contentSummaryList = admit.getContent_summary().split(",_,");
			String[] contentNameList = admit.getContent_name().split(",_,");
			String[] contentAchievementList = admit.getContent_achievement().split(",_,");

			for (int i = 0; i < contentTitleList.length; i++) {
				if (contentTitleList[i] != null && !"".equals(contentTitleList[i])) {
					contentId = admitContentService.getNextContentId();// 金榜內容ID
					admitContent.setId(contentId);
					admitContent.setTitle(contentTitleList[i]);
					admitContent.setSummary(contentSummaryList[i]);

					admitService.addContent(admitContent);

					String[] contentNameListOption = contentNameList[i].split("_a_");
					String[] contentAchievementListOption = contentAchievementList[i].split("_a_");

					admitContentOption.setContent_id(contentId);
					admitContentOption.setCreate_by(create_user);
					for (int j = 0; j < contentNameListOption.length; j++) {
						contentOptionId = admitContentOptionService.getNextContentOptionId();
						admitContentOption.setId(contentOptionId);
						admitContentOption.setName(contentNameListOption[j]);
						admitContentOption.setAchievement(contentAchievementListOption[j]);

						admitService.addContentOption(admitContentOption);
					}
				}

			}

			admitDetail.setAdmit_id(id);
			admitDetail.setCreate_by(create_user);
			admitDetail.setUpdate_by(create_user);
			Workbook workbook;
			try {
				
				workbook = Workbook.getWorkbook(new File(uploadFilePath+ "excel/admit/"+admit.getFile_url()));
//				workbook = Workbook.getWorkbook(new File(stringFile));
				Sheet sheet = workbook.getSheet(0);
				for(int i = 0 ; i < sheet.getRows() ; i++){
					if(sheet.getCell(0,i).getContents() == null || "".equals(sheet.getCell(0,i).getContents())){
						break;
					}else{
						detailId = admitDetailService.getNextDetailId();
						admitDetail.setId(detailId);
						admitDetail.setName(sheet.getCell(0,i).getContents());
						admitDetail.setType(sheet.getCell(1,i).getContents());
						admitDetail.setAchievement(sheet.getCell(2,i).getContents());
						admitService.addDetail(admitDetail);
					}
					
				}
				Gson gson = new Gson();
				String jsonString = gson.toJson(admit);
				EditLog editLog = new EditLog();
				int logId1 = editLogService.getNextLogId();
				editLog.setId(logId1);
				editLog.setData_id(admit.getId());
				editLog.setFunction("ADMIT");
				editLog.setAction_type("ADD");
				editLog.setContent(jsonString);
				editLog.setCreate_by(user.getAccount());
				editLogService.addLog(editLog);
				
				model.addAttribute("message", "新增成功");

			} catch (Exception e) {
				e.printStackTrace();
				admit.setShow_message("新增失敗\n列表頁圖片尺寸必須為300px*184px\n內頁Banner圖片尺寸必須為1180px*581px");
				model.addAttribute("message", "新增失敗");
			}

		}else {
			model.addAttribute("message", "新增失敗\n列表頁圖片尺寸必須為300px*184px\n內頁Banner圖片尺寸必須為1180px*581px");
		}
		return "admin/admit/toList";
	}



	/**
	 * 修改頁面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/admit/update")
	public String update(Admit admit,AdmitContent admitContent,AdmitContentOption admitContentOption,AdmitDetail admitDetail,
			AdmitCategory admitCategory,Model model,HttpServletRequest request, String pageNoLog, String title) {

		List<AdmitCategory> admitCategoryList = new ArrayList<>();
		List<AdmitContent> admitContentList = new ArrayList<>();
		List<AdmitContent> contentOptionList = new ArrayList<>();
		List<AdmitContentOption> admitContentOptionList = new ArrayList<>();
		List<AdmitDetail> admitDetailList = new ArrayList<>();
		
		int admitId = request.getParameter("admitDetail.admit_id") == null ? 0 : Integer.valueOf(request.getParameter("admitDetail.admit_id"));

		admit.setId(admitId);

		admitCategory.setParent_id(0);
		admitCategoryList = admitCategoryService.getLayerList("1", admitCategory);

		admit = admitService.getData(admit);

		admitContent.setAdmit_id(admit.getId());

		admitContentList = admitContentService.getFrontContentList(admitContent);

		for (int i = 0; i < admitContentList.size(); i++) {

			admitContent = admitContentList.get(i);

			admitContentOption.setContent_id(admitContentList.get(i).getId());

			// 取得選項清單
			admitContentOptionList = admitContentOptionService.getFrontContentOptionList(admitContentOption);
			admitContent.setAdmitOptionList(admitContentOptionList);
			contentOptionList.add(i, admitContent);

		}
		admitDetail.setAdmit_id(admit.getId());
		admitDetailList = admitDetailService.getDetailList(admitDetail);
		model.addAttribute("admitDetailList", admitDetailList)
 			 .addAttribute("admitCategoryList", admitCategoryList)
			 .addAttribute("admitContentList", admitContentList)
			 .addAttribute("contentOptionList", contentOptionList)
			 .addAttribute("admitContentOptionList", admitContentOptionList)
			 .addAttribute("admit", admit).addAttribute("pageNoLog", pageNoLog).addAttribute("title", title);
		
		
		return "admin/admit/admitForm";

	}

	/**
	 * 修改資料
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/admit/updateSubmit")
	public String updateSubmit(Admit admit,AdmitContent admitContent,AdmitContentOption admitContentOption
			,AdmitLog admitLog,@SessionAttribute("userAccountSession") User user,@RequestParam(value="uploadImage_banner" ,required = false) MultipartFile uploadImage_banner,
			@RequestParam(value="uploadImage_list",required = false) MultipartFile uploadImage_list,Model model,HttpServletRequest request, String pageNoLog, String title_index) throws IOException {
		List<AdmitContent> admitContentList = new ArrayList<>();
		
		UploadUtil uploadUtil = new UploadUtil();
		String deleteImage_list = request.getParameter("image_url");
		String deleteImage_banner = request.getParameter("banner_url");
		String checkStatus = "T";
		if (uploadImage_list.getBytes().length>0) {
			if (super.checkImageWidth(uploadImage_list, 300) && super.checkImageHeight(uploadImage_list, 184)) {

			} else {
				checkStatus = "F";
			}
		}
		if (uploadImage_banner.getBytes().length>0) {
			if (super.checkImageWidth(uploadImage_banner, 1180) && super.checkImageHeight(uploadImage_banner, 581)) {
			} else {
				checkStatus = "F";
			}
		}

		if ("T".equals(checkStatus)) {

			String update_user = user.getAccount();


			if (uploadImage_list.getBytes().length>0 && deleteImage_list != null) {
				UploadUtil.delete(uploadFilePath + "image/admit/" + deleteImage_list);
			}
			if (uploadImage_banner.getBytes().length> 0&& deleteImage_banner != null) {
				UploadUtil.delete(uploadFilePath + "image/admit/banner/" + deleteImage_banner);
			}
			if (uploadImage_list.getBytes().length> 0) {
				admit.setImage_url(uploadUtil.uploadv2(uploadFilePath + "image/admit/", uploadImage_list, admit.getId()));
			}
			if (uploadImage_banner.getBytes().length> 0) {
				admit.setBanner_url(uploadUtil.uploadv2(uploadFilePath + "image/admit/banner/", uploadImage_banner, admit.getId()));	
				}
			
			
			admit.setUpdate_by(update_user);
			admitService.update(admit);

			admitContentList = admitContentService.getContentListByAdmitId(admit.getId());

			for (int j = 0; j < admitContentList.size(); j++) {
				admitContentOptionService.delete(admitContentList.get(j).getId());
			}
			admitContentService.delete(admit.getId());

			Admit oldAdmit = new Admit();
			oldAdmit = admitService.getData(admit);
			int logId = admitService.getNextAdmitLogId();
			admitLog.setId(logId);
			admitLog.setAdmit_id(admit.getId());
			admitLog.setAction_type("1");
			admitLog.setTitle(oldAdmit.getTitle());
			admitLog.setCreate_by(user.getAccount());
			admitService.addAdmitLog(admitLog);

			int contentId = 0;
			int contentOptionId = 0;
			
			String contentTitle = request.getParameter("admit.content_title") == null ? "" : request.getParameter("admit.content_title");
			String content_summary = request.getParameter("admit.content_summary") == null ? "" : request.getParameter("admit.content_summary");
			String content_name = request.getParameter("admit.content_name") == null ? "" : request.getParameter("admit.content_name");
			String content_achievement = request.getParameter("admit.content_achievement") == null ? "" : request.getParameter("admit.content_achievement");

			admit.setContent_title(contentTitle);
			admit.setContent_summary(content_summary);
			admit.setContent_name(content_name);
			admit.setContent_achievement(content_achievement);
			
			String[] contentTitleList = admit.getContent_title().split(",_,");
			String[] contentSummaryList = admit.getContent_summary().split(",_,");
			String[] contentNameList = admit.getContent_name().split(",_,");
			String[] contentAchievementList = admit.getContent_achievement().split(",_,");

			admitContent.setAdmit_id(admit.getId());
			admitContent.setCreate_by(update_user);

			for (int i = 0; i < contentTitleList.length; i++) {

				if (contentTitleList[i] != null && !"".equals(contentTitleList[i])) {
					contentId = admitContentService.getNextContentId();// 金榜內容ID
					admitContent.setId(contentId);
					admitContent.setTitle(contentTitleList[i]);
					admitContent.setSummary(contentSummaryList[i]);

					admitService.addContent(admitContent);

					String[] contentNameListOption = contentNameList[i].split("_a_");
					String[] contentAchievementListOption = contentAchievementList[i].split("_a_");

					admitContentOption.setContent_id(contentId);
					admitContentOption.setCreate_by(update_user);
					for (int j = 0; j < contentNameListOption.length; j++) {
						contentOptionId = admitContentOptionService.getNextContentOptionId();
						admitContentOption.setId(contentOptionId);
						admitContentOption.setName(contentNameListOption[j]);
						admitContentOption.setAchievement(contentAchievementListOption[j]);

						admitService.addContentOption(admitContentOption);
					}
				}

			}

			Gson gson = new Gson();
			String jsonString = gson.toJson(admit);
			EditLog editLog = new EditLog();
			int logId1 = editLogService.getNextLogId();
			editLog.setId(logId1);
			editLog.setData_id(admit.getId());
			editLog.setFunction("ADMIT");
			editLog.setAction_type("UPDATE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			model.addAttribute("message", "修改成功").addAttribute("pageNoLog", pageNoLog).addAttribute("title_index", title_index);
			return "admin/admit/toList";

		} else {
			model.addAttribute("message", "修改失敗\n列表頁圖片尺寸必須為300px*184px\n內頁Banner圖片尺寸必須為1180px*581px");
			return "admin/admit/toList";
		}

	}

	@RequestMapping(value = "/admit/toDetail")
	public String toDetail(AdmitDetail admitDetail,Model model,HttpServletRequest request) {
		List<AdmitDetail> admitDetailList = new ArrayList<>();
		String id = request.getParameter("admitDetail.admit_id") == null ? "" : request.getParameter("admitDetail.admit_id");
		admitDetail.setAdmit_id(Integer.parseInt(id));
		admitDetailList = admitDetailService.getDetailList(admitDetail);
		model.addAttribute("admitDetailList", admitDetailList);
		return "admin/admit/admitDetailForm";


	}
	@RequestMapping(value = "/admit/updateDetail")
	@ResponseBody
	public String updateDetail(AdmitDetail admitDetail,AdmitDetailLog admitDetailLog,HttpServletRequest request, @SessionAttribute("userAccountSession") User user) throws IOException {

		int detailId = request.getParameter("detailId") == null ? 0 : Integer.valueOf(request.getParameter("detailId"));
		String detailName = request.getParameter("detailName") == null ? "" : request.getParameter("detailName");
		String detailType = request.getParameter("detailType") == null ? "" : request.getParameter("detailType");
		String detailAchievement = request.getParameter("detailAchievement") == null ? ""
				: request.getParameter("detailAchievement");
		String returnStatus = "F";

		if (detailId == 0 || "".equals(detailName) || "".equals(detailType) || "".equals(detailAchievement)) {
			returnStatus = "F";
		} else {

			admitDetail.setId(detailId);

			int logId = admitDetailService.getNextDetailLogId();

			admitDetail = admitDetailService.getData(admitDetail);

			admitDetailLog.setId(logId);
			admitDetailLog.setAdmit_id(admitDetail.getAdmit_id());
			admitDetailLog.setAdmit_detail_id(detailId);
			admitDetailLog.setAction_type("1");// (1:修改,2:刪除)
			String preStr = admitDetail.getName() + "," + admitDetail.getType() + "," + admitDetail.getAchievement();
			admitDetailLog.setPre_detail(preStr);
			String newStr = detailName + "," + detailType + "," + detailAchievement;
			admitDetailLog.setNew_detail(newStr);
			admitDetailLog.setCreate_by(user.getAccount());

			admitDetailService.updateDetail(detailId, detailName, detailType, detailAchievement,
					user.getAccount());

			admitDetailService.addDetailLog(admitDetailLog);
			returnStatus = "T";
		}
		return returnStatus;

	}

	@RequestMapping(value = "/admit/deleteDetail")
	public String deleteDetail(AdmitDetail admitDetail,AdmitDetailLog admitDetailLog,HttpServletRequest request, @SessionAttribute("userAccountSession") User user,Model model) {
		List<AdmitDetail> admitDetailList = new ArrayList<>();
		try {
		int logId = admitDetailService.getNextDetailLogId();

		int detailId = request.getParameter("detailId") == null ? 0 : Integer.valueOf(request.getParameter("detailId"));
		int admitId = request.getParameter("admitId") == null ? 0 : Integer.valueOf(request.getParameter("admitId"));

		admitDetail.setId(detailId);

		admitDetail = admitDetailService.getData(admitDetail);

		admitDetailLog.setId(logId);
		admitDetailLog.setAdmit_id(admitDetail.getAdmit_id());
		admitDetailLog.setAdmit_detail_id(detailId);
		admitDetailLog.setAction_type("2");// (1:修改,2:刪除)
		String preStr = admitDetail.getName() + "," + admitDetail.getType() + "," + admitDetail.getAchievement();
		admitDetailLog.setPre_detail(preStr);
		admitDetailLog.setNew_detail("");
		admitDetailLog.setCreate_by(user.getAccount());

		admitDetailService.deleteDetail(detailId);
		admitDetailService.addDetailLog(admitDetailLog);

		admitDetail.setAdmit_id(admitId);

		admitDetailList = admitDetailService.getDetailList(admitDetail);
		model.addAttribute("message", "刪除成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "刪除失敗");
		}
		model.addAttribute("admitDetailList",admitDetailList);

			return "admin/admit/admitDetailForm";
		}

	/**
	 * 刪除
	 * 
	 * @return
	 */
	@RequestMapping(value = "/admit/delete", method = {RequestMethod.POST})
	public String delete(Admit admit,AdmitContent admitContent,AdmitContentOption admitContentOption,
			AdmitDetailLog admitDetailLog,AdmitLog admitLog,HttpServletRequest request,
			@SessionAttribute("userAccountSession") User user,Model model) throws IOException {

		
		List<AdmitContent> admitContentList = new ArrayList<>();

		
		String deleteString = request.getParameter("deleteList");
		String[] deleteArray = deleteString.split(",");
		Integer id = null;
		
		try {
		for (int i = 0; i < deleteArray.length; i++) {
			id = Integer.valueOf(deleteArray[i]);

			admit.setId(id);
			admit = admitService.getData(admit);
			admitContentList = admitContentService.getContentListByAdmitId(id);

			for (int j = 0; j < admitContentList.size(); j++) {
				admitContentOptionService.delete(admitContentList.get(j).getId());
			}
			admitContentService.delete(id);
			UploadUtil.delete(uploadFilePath + "image/images/admit/" + admit.getImage_url());
			UploadUtil.delete(uploadFilePath + "image/images/admit/banner/" + admit.getBanner_url());
			UploadUtil.delete(uploadFilePath + "excel/admit/" + admit.getFile_url());

			
			admitService.delete(id);
			int logId = admitService.getNextAdmitLogId();
			admitLog.setId(logId);
			admitLog.setAdmit_id(Integer.valueOf(deleteArray[i]));
			admitLog.setAction_type("2");
			admitLog.setTitle(admit.getTitle());
			admitLog.setCreate_by(user.getAccount());
			admitService.addAdmitLog(admitLog);

			Gson gson = new Gson();
			String jsonString = gson.toJson(admit);
			EditLog editLog = new EditLog();
			int logId1 = editLogService.getNextLogId();
			editLog.setId(logId1);
			editLog.setData_id(admit.getId());
			editLog.setFunction("ADMIT");
			editLog.setAction_type("DELETE");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
		}
			model.addAttribute("message", "刪除成功");
    	} catch (Exception e) {
    		e.printStackTrace();
    		model.addAttribute("message", "刪除失敗");
    	}

    	return "admin/admit/toList";

	}
	@RequestMapping(value = "/admit/displayOff")
	public String displayOff(HttpServletRequest request,Model model) throws IOException {
		try{
			String deleteString = request.getParameter("deleteList");
			String[] deleteArray = deleteString.split(",");
			Integer id = null;
			for (int i = 0; i < deleteArray.length; i++) {
				id = Integer.valueOf(deleteArray[i]);
                admitService.updateDisplayHide(id);
            }
			model.addAttribute("message", "下架成功");
    	} catch (Exception e) {
    		e.printStackTrace();
    		model.addAttribute("message", "下架失敗");
    	}
    	return "admin/admit/toList";
    	

	}
	@RequestMapping(value = "/admit/displayOn")
	public String displayOn(HttpServletRequest request,Model model) throws IOException {
		try{
			String deleteString = request.getParameter("deleteList");
			String[] deleteArray = deleteString.split(",");
			Integer id = null;
			
			for (int i = 0; i < deleteArray.length; i++) {
				id = Integer.valueOf(deleteArray[i]);
                admitService.updateDisplayShow(id);
            }
			model.addAttribute("message", "上架成功");
    	} catch (Exception e) {
    		e.printStackTrace();
    		model.addAttribute("message", "上架失敗");
    	}
    	return "admin/admit/toList";
    		
    	}
	
	
	
	
//	=========================
	
	
	/**
	 * 更新正是機資料到本地方法
	 */
	@GetMapping("/admit/updateNormalData")
	public String updateNormalData() {
		
		List<Map<String, Object>> getNormalBannerList = new ArrayList<>();
		getNormalBannerList = admitService.getNormalList();
		List<Map<String, Object>> getNormalAdmitContentList = new ArrayList<>();
		getNormalAdmitContentList = admitContentService.getNormalList();
		List<Map<String, Object>> getNormalAdmitContentOptionList = new ArrayList<>();
		getNormalAdmitContentOptionList = admitContentOptionService.getNormalList();
		List<Map<String, Object>> getNormalAdmitDetailList = new ArrayList<>();
		getNormalAdmitDetailList = admitDetailService.getNormalDetailList();
		List<Map<String, Object>> getNormalAdmitDetailLogList = new ArrayList<>();
		getNormalAdmitDetailLogList = admitDetailService.getNormalDetailLogList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		
			//admit這張table
			for(Map<String, Object> map : getNormalBannerList) {
				Admit admit = new Admit();
				
				if(map.get("ID") != null) {
					admit.setId(Integer.valueOf(map.get("ID").toString()));
				}
				if(map.get("TITLE") != null) {
					admit.setTitle(map.get("TITLE").toString());
				}
				if(map.get("BANNER_CONTENT_BLACK") != null) {
					admit.setBanner_content_black(map.get("BANNER_CONTENT_BLACK").toString());
				}
				if(map.get("BANNER_CONTENT_RED") != null) {
					admit.setBanner_content_red(map.get("BANNER_CONTENT_RED").toString());
				}
				if(map.get("TOTAL_LIST") != null) {
					admit.setTotal_list(map.get("TOTAL_LIST").toString());
				}
				if(map.get("TOTAL_CONTENT") != null) {
					admit.setTotal_content(map.get("TOTAL_CONTENT").toString());
				}
				if(map.get("FILE_URL") != null) {
					admit.setFile_url(map.get("FILE_URL").toString());
				}
				if(map.get("CREATE_BY") != null) {
					admit.setCreate_by(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null) {
					admit.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
				}
				if(map.get("UPDATE_BY") != null) {
					admit.setUpdate_by(map.get("UPDATE_BY").toString());
				}
				if(map.get("UPDATE_DATE") != null) {
					admit.setUpdate_date(map.get("UPDATE_DATE").toString());
				}
				if(map.get("CLICK_RATE") != null) {
					admit.setClick_rate(Integer.valueOf(map.get("CLICK_RATE").toString()));
				}
				if(map.get("ADMIT_YEAR") != null) {
					admit.setAdmit_year(Integer.valueOf(map.get("ADMIT_YEAR").toString()));
				}
				if(map.get("ADMIT_CATEGORY") != null) {
					admit.setAdmit_category(Integer.valueOf(map.get("ADMIT_CATEGORY").toString()));
				}
				if(map.get("IMAGE_URL") != null) {
					admit.setImage_url(map.get("IMAGE_URL").toString());
				}
				if(map.get("BANNER_URL") != null) {
					admit.setBanner_url(map.get("BANNER_URL").toString());
				}
				if(map.get("DISPLAY") != null) {
					admit.setDisplay(map.get("DISPLAY").toString());
				}
				if(map.get("SEO_CONTENT") != null) {
					admit.setSeo_content(map.get("SEO_CONTENT").toString());
				}
				if(map.get("ENCRYPTURL") != null) {
					admit.setEncrypturl(map.get("ENCRYPTURL").toString());
				}
				admitService.updateNormalData(admit);
				
			}
			
			
			//admit_content這張table
			if(getNormalAdmitContentList != null && getNormalAdmitContentList.size() != 0) {
				for(Map<String, Object> map : getNormalAdmitContentList) {
					AdmitContent admitContent = new AdmitContent();
					
					if(map.get("ID") != null) {
						admitContent.setId(Integer.valueOf(map.get("ID").toString()));
					}
					if(map.get("ADMIT_ID") != null) {
						admitContent.setAdmit_id(Integer.valueOf(map.get("ADMIT_ID").toString()));
					}
					if(map.get("TITLE") != null) {
						admitContent.setTitle(map.get("TITLE").toString());
					}
					if(map.get("SUMMARY") != null) {
						admitContent.setSummary(map.get("SUMMARY").toString());
					}
					if(map.get("CREATE_BY") != null) {
						admitContent.setCreate_by(map.get("CREATE_BY").toString());
					}
					if(map.get("CREATE_DATE") != null) {
						admitContent.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
					}
					admitContentService.updateNormalData(admitContent);
					
				}
			}
			
			//admit_content_option這張table
			if(getNormalAdmitContentOptionList != null && getNormalAdmitContentOptionList.size() != 0) {
				for(Map<String, Object> map : getNormalAdmitContentOptionList) {
					AdmitContentOption admitContentOption = new AdmitContentOption();
					
					if(map.get("ID") != null) {
						admitContentOption.setId(Integer.valueOf(map.get("ID").toString()));
					}
					if(map.get("CONTENT_ID") != null) {
						admitContentOption.setContent_id(Integer.valueOf(map.get("CONTENT_ID").toString()));
					}
					if(map.get("ACHIEVEMENT") != null) {
						admitContentOption.setAchievement(map.get("ACHIEVEMENT").toString());
					}
					if(map.get("NAME") != null) {
						admitContentOption.setName(map.get("NAME").toString());
					}
					if(map.get("CREATE_BY") != null) {
						admitContentOption.setCreate_by(map.get("CREATE_BY").toString());
					}
					if(map.get("CREATE_DATE") != null) {
						admitContentOption.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
					}
					admitContentOptionService.updateNormalData(admitContentOption);
					
				}
			}
			
			//admit_detail這張table
				for(Map<String, Object> map : getNormalAdmitDetailList) {
					AdmitDetail admitDetail = new AdmitDetail();
					
					if(map.get("ID") != null) {
						admitDetail.setId(Integer.valueOf(map.get("ID").toString()));
					}
					if(map.get("ADMIT_ID") != null) {
						admitDetail.setAdmit_id(Integer.valueOf(map.get("ADMIT_ID").toString()));
					}
					if(map.get("NAME") != null) {
						admitDetail.setName(map.get("NAME").toString());
					}
					if(map.get("TYPE") != null) {
//						admitDetail.setName(map.get("TYPE").toString());
						admitDetail.setType(map.get("TYPE").toString());
					}
					if(map.get("ACHIEVEMENT") != null) {
						admitDetail.setAchievement(map.get("ACHIEVEMENT").toString());
					}
					if(map.get("CREATE_BY") != null) {
						admitDetail.setCreate_by(map.get("CREATE_BY").toString());
					}
					if(map.get("CREATE_DATE") != null) {
						admitDetail.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
					}
					if(map.get("UPDATE_BY") != null) {
						admitDetail.setUpdate_by(map.get("UPDATE_BY").toString());
					}
					if(map.get("UPDATE_DATE") != null) {
						admitDetail.setUpdate_date(new Date(sdf.parse(map.get("UPDATE_DATE").toString()).getTime()));
					}
					admitDetailService.updateNormalDetailData(admitDetail);
					
			}
			
			//admit_detail_log這張table
				for(Map<String, Object> map : getNormalAdmitDetailLogList) {
					AdmitDetailLog admitDetailLog = new AdmitDetailLog();
					
					if(map.get("ID") != null) {
						admitDetailLog.setId(Integer.valueOf(map.get("ID").toString()));
					}
					if(map.get("ADMIT_ID") != null) {
						admitDetailLog.setAdmit_id(Integer.valueOf(map.get("ADMIT_ID").toString()));
					}
					if(map.get("ADMIT_DETAIL_ID") != null) {
						admitDetailLog.setAdmit_detail_id(Integer.valueOf(map.get("ADMIT_DETAIL_ID").toString()));
					}
					if(map.get("ACTION_TYPE") != null) {
						admitDetailLog.setAction_type(map.get("ACTION_TYPE").toString());
					}
					if(map.get("PRE_DETAIL") != null) {
						admitDetailLog.setPre_detail(map.get("PRE_DETAIL").toString());
					}
					if(map.get("NEW_DETAIL") != null) {
						admitDetailLog.setNew_detail(map.get("NEW_DETAIL").toString());
					}
					if(map.get("CREATE_BY") != null) {
						admitDetailLog.setCreate_by(map.get("CREATE_BY").toString());
					}
					if(map.get("CREATE_DATE") != null) {
						admitDetailLog.setCreate_date(new Date(sdf.parse(map.get("CREATE_DATE").toString()).getTime()));
					}
					admitDetailService.updateNormalDetailLogData(admitDetailLog);
					
				}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//轉發 到首頁更新
		return "redirect:index";
	}
	
	
	
}