package com.tkb.realgoodTransform.controller.front;

import java.util.ArrayList;
import java.util.List;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.Location;
import com.tkb.realgoodTransform.utils.AreaLocationApi;
import com.tkb.realgoodTransform.utils.BaseUtils;

import jakarta.servlet.http.HttpServletRequest;



@Controller
@RequestMapping(value = "/location")
public class FrontLocationController extends BaseUtils {
	
	@RequestMapping("/index")
	public String index(Location location,Area area,Model model,HttpServletRequest request) {
		List<Area> areaList = new ArrayList<>();
		List<Area> areaTempList = new ArrayList<>();
		List<Location> locationList = new ArrayList<>();
	
		String website = "R";
		AreaLocationApi areaLocationApi = new AreaLocationApi();
		String apiArea = areaLocationApi.getApiArea();
		List<Area> apiAreaList = areaLocationApi.jsonToAreaList(apiArea);
		areaList = apiAreaList;

		for (int i = 0; i < areaList.size(); i++) {
			Area areaTemp = areaList.get(i);
			if (areaTemp != null) {
				String apiLocation = areaLocationApi.getApiLocation(website, areaTemp.getId().toString());
				if (apiLocation != null && !"".equals(apiLocation)) {
					areaTempList.add(areaTemp);
				}
			}
		}
		areaList = areaTempList;

		String apiLocation = areaLocationApi.getApiLocation(website, "2");
		List<Location> apiLocationList = areaLocationApi.jsonToLocationList(apiLocation);
		locationList = apiLocationList;
		
		
		System.out.println("areaList" + areaList);
		System.out.println("apiLocation" + apiLocation);
		System.out.println("locationList" + locationList);
		
		

		model.addAttribute("areaList", areaList).addAttribute("locationList", locationList);
		return "front/location/list";
		
	}
	
	@PostMapping("/toSearch")
	public ResponseEntity<?>toSearch(HttpServletRequest request,Location location,Area area){
		
		List<Location> locationList = new ArrayList<>();

		String layer = request.getParameter("layer") == null ? "" : request.getParameter("layer");
		String area_id = request.getParameter("area_id") == "" ? "" : request.getParameter("area_id");

		AreaLocationApi areaLocationApi = new AreaLocationApi();
		String website = "R";

		if ("1".equals(layer)) {
			if (!"".equals(area_id)) {
				location.setArea(area_id);
				String apiLocation = areaLocationApi.getApiLocation(website, location.getArea());
				List<Location> apiLocationList = areaLocationApi.jsonToLocationList(apiLocation);
				locationList = apiLocationList;
			}
		}
		
		System.out.println("locationList" + locationList);
		
		
		locationList.get(0).setTotalPage(super.totalPage);
		return new ResponseEntity<>(locationList,HttpStatus.OK);
	}
}
