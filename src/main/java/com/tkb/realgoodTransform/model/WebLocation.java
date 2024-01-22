package com.tkb.realgoodTransform.model;

import lombok.Data;

@Data
public class WebLocation {
	
	private Integer id;			//流水號
	private String location;	//地區
	private String web;			//單位-單位網站代碼學堂：T / 大碩：D / 百官：A / 龍門：L / 甄戰：R / 洋碩：Y / 千碩：C / TV：V / GO：B
	private Integer sort;		//排序
	
}
