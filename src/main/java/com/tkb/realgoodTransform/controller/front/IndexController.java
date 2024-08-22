package com.tkb.realgoodTransform.controller.front;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tkb.realgoodTransform.model.Admit;
import com.tkb.realgoodTransform.model.Banner;
import com.tkb.realgoodTransform.model.ChosenArticle;
import com.tkb.realgoodTransform.model.CourseDiscount;
import com.tkb.realgoodTransform.model.Lectures;
import com.tkb.realgoodTransform.model.Location;
import com.tkb.realgoodTransform.model.Marquee;
import com.tkb.realgoodTransform.model.NewExam;
import com.tkb.realgoodTransform.model.SchoolBulletin;
import com.tkb.realgoodTransform.model.Winner;
import com.tkb.realgoodTransform.service.AdmitService;
import com.tkb.realgoodTransform.service.BannerService;
import com.tkb.realgoodTransform.service.ChosenArticleService;
import com.tkb.realgoodTransform.service.CourseDiscountService;
import com.tkb.realgoodTransform.service.LecturesService;
import com.tkb.realgoodTransform.service.MarqueeService;
import com.tkb.realgoodTransform.service.NewExamService;
import com.tkb.realgoodTransform.service.SchoolBulletinService;
import com.tkb.realgoodTransform.service.WinnerService;
import com.tkb.realgoodTransform.utils.AreaLocationApi;
import com.tkb.realgoodTransform.utils.BaseUtils;
import com.tkb.realgoodTransform.utils.CryptographyUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseUtils {
	
	@Autowired
	private AdmitService admitService;
	
	@Autowired
	private BannerService bannerService;
	
	@Autowired
	private MarqueeService marqueeService;
	
	@Autowired
	private NewExamService newExamService;
	
	@Autowired
	private SchoolBulletinService schoolBulletinService;
	
	@Autowired
	private WinnerService winnerService;
	
	@Autowired
	private LecturesService lecturesService;
	
	@Autowired
	private CourseDiscountService courseDiscountService;
	
	@Autowired
	private ChosenArticleService chosenArticleService;
	
	@RequestMapping(value={"","/","/index"})
	public String index (HttpServletRequest request,Model model,Banner banner,Marquee marquee,Lectures lectures,SchoolBulletin schoolBulletin,Winner winner) {
		
		
		//圖片輪播banner
		List<Banner>bannerList = new ArrayList<>();
		bannerList = bannerService.getFrontList(banner);
		//跑馬燈
		List <Marquee>marqueeList = new ArrayList<>();
		marqueeList = marqueeService.getFrontList(marquee);
		//最新考情區
		List<NewExam> newExamList = new ArrayList<>();
		newExamList = newExamService.getFrontList();
		//最新課表區
		List<Lectures>lecturesList = new ArrayList<>();
		lecturesList = lecturesService.getFrontList(lectures);
		//甄戰公告區
		List<SchoolBulletin>schoolBulletinList = new ArrayList<>();
		schoolBulletinList = schoolBulletinService.getFrontList(schoolBulletin);
		
		AreaLocationApi areaLocationApi = new AreaLocationApi();
		List<Location> allLocationList = areaLocationApi.jsonToLocationList(areaLocationApi.getApiLocation("0", "1"));
		
		Gson gson = new Gson();
		
		String schoolBulletinJson = gson.toJson(schoolBulletinList);
		Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
		List<Map<String, String>> bulletinList = new Gson().fromJson(schoolBulletinJson, listType);
		List<SchoolBulletin> convertedSBList = new ArrayList<>();

		SimpleDateFormat inputFormat = new SimpleDateFormat("MMM d, yyyy");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

		for (Map<String, String> bulletinMap : bulletinList) {
			String area = (String) bulletinMap.get("area");
			if ("0".equals(area)) {
				bulletinMap.put("area_name", "全區");
			} else {
				for (int i = 0; i < allLocationList.size(); i++) {
					if (allLocationList.get(i).getId().toString().equals(area)) {
						if ("2".equals(allLocationList.get(i).getArea_id().toString())) {
							bulletinMap.put("area_name", "北區");
						} else if ("3".equals(allLocationList.get(i).getArea_id().toString())) {
							bulletinMap.put("area_name", "桃竹區");
						} else if ("4".equals(allLocationList.get(i).getArea_id().toString())) {
							bulletinMap.put("area_name", "中區");
						} else if ("5".equals(allLocationList.get(i).getArea_id().toString())) {
							bulletinMap.put("area_name", "嘉南區");
						} else if ("6".equals(allLocationList.get(i).getArea_id().toString())) {
							bulletinMap.put("area_name", "高屏區");
						}
					}
				}
			}
			SchoolBulletin schoolBulletinTemp = new SchoolBulletin();
			schoolBulletinTemp.setId(Integer.parseInt(bulletinMap.get("id")));
			schoolBulletinTemp.setTitle(bulletinMap.get("title"));
			schoolBulletinTemp.setArea(bulletinMap.get("area"));
			schoolBulletinTemp.setCategory(bulletinMap.get("category"));
			schoolBulletinTemp.setImage(bulletinMap.get("image"));
			try {
				Date inputBeginDate = inputFormat.parse(bulletinMap.get("begin_date").toString());
				Date outputBeginDate = outputFormat.parse(outputFormat.format(inputBeginDate));
				java.sql.Date beginSqlDate = new java.sql.Date(outputBeginDate.getTime());

				Date inputEndDate = inputFormat.parse(bulletinMap.get("end_date"));
				Date outputEndDate = outputFormat.parse(outputFormat.format(inputEndDate));
				java.sql.Date endDate = new java.sql.Date(outputEndDate.getTime());

				Date inputCreateDate = inputFormat.parse(bulletinMap.get("create_date"));
				Date outputCreateDate = outputFormat.parse(outputFormat.format(inputCreateDate));
				java.sql.Date createDate = new java.sql.Date (outputCreateDate.getTime());
				
				Date inputUpdateDate = inputFormat.parse(bulletinMap.get("update_date"));
				Date outputUpdateDate = outputFormat.parse(outputFormat.format(inputUpdateDate));
				java.sql.Date updateDate = new java.sql.Date (outputUpdateDate.getTime());

				schoolBulletinTemp.setBegin_date(beginSqlDate);
				schoolBulletinTemp.setEnd_date(endDate);
				schoolBulletinTemp.setCreate_date(createDate);
				schoolBulletinTemp.setUpdate_date(updateDate);

			} catch (Exception e) {
				e.printStackTrace();
			}
			schoolBulletinTemp.setClick_rate(Integer.parseInt(bulletinMap.get("click_rate")));
			schoolBulletinTemp.setCreate_by(bulletinMap.get("create_by"));
			schoolBulletinTemp.setUpdate_by(bulletinMap.get("update_by"));
			schoolBulletinTemp.setContent(bulletinMap.get("content"));
			schoolBulletinTemp.setEncrypturl(bulletinMap.get("encrypturl"));
			schoolBulletinTemp.setCategory_name(bulletinMap.get("category_name"));
			schoolBulletinTemp.setArea_name(bulletinMap.get("area_name"));
			convertedSBList.add(schoolBulletinTemp);
		}
		schoolBulletinList = convertedSBList;
		//考取金榜專區
		List<Admit>admitList = new ArrayList<>();
		admitList = admitService.getFrontList();
		if(admitList.size()!=0){
		       for(int i = 0 ; i < admitList.size() ; i++){                
		           admitList.get(i).setStr(CryptographyUtils.encryptStr(String.valueOf(admitList.get(i).getId())));
		                    }
	    }
		//贏家經驗談專區
		List<Winner>winnerList = new ArrayList<>();
		winnerList=winnerService.getRandomList(winner);
		
		//影音專區
		List<Winner>videoBigList = new ArrayList<>();
		List<Winner>videoList = new ArrayList<>();
		videoBigList = winnerService.getVideoIndexList(winner);
		winner.setId(videoBigList.get(0).getId());
		videoList = winnerService.getFrontVideoList(winner);
		List<Winner>recommendList = new ArrayList<>();
		recommendList = winnerService.getRecommendVideoList(winner);
		
		//課程優惠專區
		List<CourseDiscount>courseDiscountList = new ArrayList<>();
		courseDiscountList = courseDiscountService.getFrontList();
		
		//精選文章專區
		List<ChosenArticle> chosenArticlesList = new ArrayList<>();
		chosenArticlesList = chosenArticleService.getFrontList();
		
		model.addAttribute("bannerList",bannerList);
		model.addAttribute("marqueeList",marqueeList);
		model.addAttribute("newExamList", newExamList);
		model.addAttribute("lecturesList",lecturesList);
		model.addAttribute("schoolBulletinList", schoolBulletinList);
		model.addAttribute("admitList",admitList);
		model.addAttribute("winnerList",winnerList);
		model.addAttribute("videoBigList",videoBigList);
		model.addAttribute("videoList",videoList);
		model.addAttribute("recommendList",recommendList);
		model.addAttribute("courseDiscountList",courseDiscountList);
		model.addAttribute("chosenArticlesList",chosenArticlesList);
		
		return "front/index";
	}
	
	
	@RequestMapping("/changeIndexVideoData")
	@ResponseBody
	public List<Winner> changeIndexVideoData(HttpServletRequest request,Winner winner,Model model) throws IOException{
		
		String id = request.getParameter("id") == "" ? null : request.getParameter("id");
		winner.setId(Integer.valueOf(id));
		return  winnerService.getVideoMainList(winner);

		
	}	

	@RequestMapping("/changeIndexVideoList")
	@ResponseBody
	public List<Winner> changeIndexVideoList(HttpServletRequest request,Winner winner) throws IOException {
		
		String id = request.getParameter("id") == "" ? null : request.getParameter("id");
		String category_id = request.getParameter("category_id") == "" ? "" : request.getParameter("category_id");
		
		winner.setId(Integer.valueOf(id));
		winner.setCategory(category_id);
		return winnerService.getVideoCategoryList(winner);
		
	}

	@RequestMapping("/banner_updateClickRate")
	@ResponseBody
	public void banner_updateClickRate(HttpServletRequest request) {
		
		String id = request.getParameter("id");
		Banner banner = new Banner();
		
		banner.setId(Integer.valueOf(id));
		bannerService.updateClickRate(banner);
		
	}

	@RequestMapping("/marquee_updateClickRate")
	@ResponseBody
	public void marquee_updateClickRate(HttpServletRequest request) {

        String id = request.getParameter("id");
    	Marquee marquee = new Marquee();
        
        marquee.setId(Integer.valueOf(id));
        marqueeService.updateClickRate(marquee);
        
	}
	
}
