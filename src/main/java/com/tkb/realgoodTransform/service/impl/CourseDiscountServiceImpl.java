package com.tkb.realgoodTransform.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.tkb.realgoodTransform.dao.CourseDiscountDao;
import com.tkb.realgoodTransform.model.CourseDiscount;
import com.tkb.realgoodTransform.model.CourseDiscountCategory;
import com.tkb.realgoodTransform.model.EditLog;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.service.CourseDiscountCategoryService;
import com.tkb.realgoodTransform.service.CourseDiscountService;
import com.tkb.realgoodTransform.service.EditLogService;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.UploadUtil;

import jakarta.servlet.http.HttpServletRequest;



@Service
public class CourseDiscountServiceImpl extends BaseUtils implements CourseDiscountService {

	@Autowired
	CourseDiscountDao courseDiscountDao;
	@Autowired
	CourseDiscountCategoryService courseDiscountCategoryService;
	@Autowired
	private EditLogService editLogService;
	@Value("${upload.file.path}")
	private String uploadFilePath; // 檔案上傳位置
	
	@Override
	public List<CourseDiscount> getList(int pageCount, int pageStart, CourseDiscount courseDiscount) {
		// TODO Auto-generated method stub
		return courseDiscountDao.getList(pageCount, pageStart, courseDiscount);
	}

	@Override
	public List<CourseDiscount> getFrontList(CourseDiscount courseDiscount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CourseDiscount> getFrontList(int pageCount, int pageStart, CourseDiscount courseDiscount,
			String search_sort) {
		// TODO Auto-generated method stub
		return courseDiscountDao.getFrontList(pageCount, pageStart, courseDiscount, search_sort);
	}

	@Override
	public List<CourseDiscount> getFrontList() {
		// TODO Auto-generated method stub
		return courseDiscountDao.getFrontList();
	}

	@Override
	public Integer getCount(CourseDiscount courseDiscount) {
		// TODO Auto-generated method stub
		return courseDiscountDao.getCount(courseDiscount);
	}

	@Override
	public Integer getFrontCount(CourseDiscount courseDiscount) {
		// TODO Auto-generated method stub
		return courseDiscountDao.getFrontCount(courseDiscount);
	}

	@Override
	public CourseDiscount getData(CourseDiscount courseDiscount) {
		// TODO Auto-generated method stub
		return courseDiscountDao.getData(courseDiscount);
	}

	@Override
	public CourseDiscount getFrontData(CourseDiscount courseDiscount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNextId() {
		// TODO Auto-generated method stub
		return courseDiscountDao.getNextId();
	}

	@Override
	public void add(CourseDiscount courseDiscount) {
		// TODO Auto-generated method stub
		courseDiscountDao.add(courseDiscount);
	}

	@Override
	public void update(CourseDiscount courseDiscount) {
		// TODO Auto-generated method stub
		courseDiscountDao.update(courseDiscount);
	}

	@Override
	public void updateClickRate(CourseDiscount courseDiscount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(CourseDiscount courseDiscount,Integer id) {
		// TODO Auto-generated method stub
		courseDiscountDao.delete(courseDiscount, id);
	}

	@Override
	public Integer checkTopCount(CourseDiscount courseDiscount) {
		// TODO Auto-generated method stub
		return courseDiscountDao.checkTopCount(courseDiscount);
	}

	@Override
	public void updateIndex_Sort(CourseDiscount courseDiscount) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<CourseDiscountCategory> addfunction(CourseDiscountCategory courseDiscountCategory) {
		List<CourseDiscountCategory>courseDiscountCategoryList = new ArrayList<>();
		courseDiscountCategory.setParent_id(0);
		courseDiscountCategoryList = courseDiscountCategoryService.getLayerList("1", courseDiscountCategory);
		return courseDiscountCategoryList;
	}

	@Override
	public CourseDiscount addSubmitFunction(CourseDiscount courseDiscount, MultipartFile image, MultipartFile photo, User user) throws IOException {
		UploadUtil uploadUtil = new UploadUtil();
		String checkStatus = "T";

		if(image != null) {
			if(super.checkImageWidth(image, 150)){
			}else{
				checkStatus = "F";
			}
			if(super.checkImageHeight(image, 150)){
			}else{
				checkStatus = "F";
			}
		}
		
		if(photo != null) {
			if(super.checkImageWidth(photo, 300)){
			}else{
				checkStatus = "F";
			}
			if(super.checkImageHeight(photo, 184)){
			}else{
				checkStatus = "F";
			}
		}
		
		if("T".equals(checkStatus)) {
			int id = getNextId();
			courseDiscount.setId(id);
			try {
				courseDiscount.setIndex_image(uploadUtil.uploadv2(uploadFilePath + "image/courseDiscount/indexImage/", image, courseDiscount.getId()));
			}catch (Exception e) {
				e.printStackTrace();
			}
			try {
				courseDiscount.setPhoto(uploadUtil.uploadv2(uploadFilePath + "image/courseDiscount/photo/", photo, courseDiscount.getId()));
			}catch (Exception e) {
				e.printStackTrace();
			}
			courseDiscount.setCreate_by(user.getAccount());
			courseDiscount.setUpdate_by(user.getAccount());
			add(courseDiscount);
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(courseDiscount);
			EditLog editLog = new EditLog();
			int logId = editLogService.getNextLogId();
			editLog.setId(logId);
			editLog.setData_id(courseDiscount.getId());
			editLog.setFunction("COURSE_DISCOUNT");
			editLog.setAction_type("ADD");
			editLog.setContent(jsonString);
			editLog.setCreate_by(user.getAccount());
			editLogService.addLog(editLog);
			
			courseDiscount.setShow_message("新增成功");
		}else if("F".equals(checkStatus)) {
			courseDiscount.setShow_message("新增失敗,請檢查圖片規格,尺寸");
		}else {
			courseDiscount.setShow_message("新增失敗");
		}
		return courseDiscount;
		
	}

	@Override
	public Map<String, Object> updateFunction(CourseDiscount courseDiscount,
			CourseDiscountCategory courseDiscountCategory) {
		Map<String, Object>map = new HashMap<>();
		List<CourseDiscountCategory>courseDiscountCategoryList = new ArrayList<>();
		courseDiscountCategory.setParent_id(0);
		courseDiscountCategoryList = courseDiscountCategoryService.getLayerList("1", courseDiscountCategory);
		courseDiscount = getData(courseDiscount);
		map.put("courseDiscount", courseDiscount);
		map.put("courseDiscountCategoryList", courseDiscountCategoryList);
		return map;
	}

	@Override
	public ResponseEntity<?> checkTopCountFunction(CourseDiscount courseDiscount)throws IOException {
		String msg = "";
		Integer returnCourseDiscountTop = checkTopCount(courseDiscount);
		if(returnCourseDiscountTop >= 6) {
			msg = "true";
			return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
		}else{
			msg = "false";
			return new ResponseEntity<String>(msg, HttpStatus.OK);
		}
	}

	@Override
	public void updateSubmitFunction(CourseDiscount courseDiscount, MultipartFile image, MultipartFile photo, User user,HttpServletRequest request)
			throws IOException {
		UploadUtil uploadUtil = new UploadUtil();
		String checkStatus = "T";
		String deleteImage = request.getParameter("index_image");
		String deletePhoto = request.getParameter("photo");


		if(image.getBytes().length>0) {
			if(super.checkImageWidth(image, 150)){
			}else{
				checkStatus = "F";
			}
			if(super.checkImageHeight(image, 150)){

			}else{
				checkStatus = "F";
			}
		}
		if(photo.getBytes().length>0) {
			if(super.checkImageWidth(photo, 300)){
			}else{
				checkStatus = "F";
			}
			if(super.checkImageHeight(photo, 184)){

			}else{
				checkStatus = "F";
			}
		}
		
		
		
		if("T".equals(checkStatus)) {
			if(image.getBytes().length>0 && deleteImage != null) {
				UploadUtil.delete(uploadFilePath + "image/courseDiscount/indexImage/" + deleteImage);
			}
			if(photo.getBytes().length>0 && deletePhoto != null) {
				UploadUtil.delete(uploadFilePath + "image/courseDiscount/photo/" + deletePhoto);
			}
			if(image.getBytes().length>0) {
				courseDiscount.setIndex_image(uploadUtil.uploadv2(uploadFilePath + "image/courseDiscount/indexImage/", image, courseDiscount.getId()));
			}
			if(photo.getBytes().length>0) {
				courseDiscount.setPhoto(uploadUtil.uploadv2(uploadFilePath + "image/courseDiscount/photo/", photo, courseDiscount.getId()));
			}
			courseDiscount.setUpdate_by(user.getAccount());
			update(courseDiscount);
		}else {
			throw new IOException();
		}
	}
	
//	=========
	

	@Override
	public List<Map<String, Object>> getNormalList() {
		return courseDiscountDao.getNormalList();
	}

	@Override
	public void updateNormalData(CourseDiscount courseDiscount) {
		courseDiscountDao.updateNormalData(courseDiscount);
	}
	

}
