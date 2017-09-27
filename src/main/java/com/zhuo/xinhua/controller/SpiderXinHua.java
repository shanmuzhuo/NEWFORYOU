package com.zhuo.xinhua.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.zhuo.common.PropertyUtil;
import com.zhuo.pojo.Xinhua;

/**
 * @author zzx
 * @time 2017年9月27日
 * @Desc 从新华网爬取新闻内容
 *
 */

@Controller
@RequestMapping("/SpiderXinHua")
public class SpiderXinHua {
	private Gson json = new Gson();
	
	@RequestMapping("/fetch")
	@ResponseBody
	public String FetchCustom(HttpServletRequest request, Model model) {
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> tech_list = testFetchtuijian(PropertyUtil.getProperty("xinhua.tech"));
		List<Map<String, Object>> ent_list = testFetchtuijian(PropertyUtil.getProperty("xinhua.ent"));
		List<Map<String, Object>> car_list = testFetchtuijian(PropertyUtil.getProperty("xinhua.car"));
		List<Map<String, Object>> health_list = testFetchtuijian(PropertyUtil.getProperty("xinhua.health"));
		List<Xinhua> Military_list = FetchMilitary();
		map.put("tech", tech_list); // 科技
		map.put("ent", ent_list); // 娱乐
		map.put("car", car_list); // 汽车
		map.put("health", health_list); // 健康
		map.put("military", Military_list); // 军事
		list.add(map);
		System.out.println(json.toJson(map));
		return json.toJson(map);
	}
	
	/**
	 * 抓取新华网模块推荐
	 */
	@Test
	public List<Map<String, Object>> testFetchtuijian(String str) {
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			Document doc3 = Jsoup.connect(str)
					  .data("query", "Java")
					  .userAgent("Mozilla")
					  .cookie("auth", "token")
					  .timeout(3000)
					  .get();
			System.out.println(doc3.title());
			Elements h2 = doc3.select("div.swiper-wrapper").first().select("div.swiper-slide");
			System.out.println(h2.size());
			for (int i = 0; i < h2.size(); i++) {
				Element element = h2.get(i);
				if(null != element.getElementsByTag("ins") &&  !element.getElementsByTag("ins").toString().trim().equals("")) {
					continue;
				}
				String link = element.getElementsByTag("a").first().attr("href");
				String img = element.getElementsByTag("img").first().attr("abs:src");
				String title = element.getElementsByTag("p").first().text();
				System.out.println(title);
				if(null != link && null !=img && null != title) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("title", title);
					map.put("link", link);
					map.put("img", img);
					list.add(map);
				}else {
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获取军事信息
	 * @return
	 */
	public List<Xinhua> FetchMilitary() {
		List<Xinhua> list = new ArrayList<Xinhua>();
		try {
			Document doc3 = Jsoup.connect(PropertyUtil.getProperty("xinhua.Military"))
					  .data("query", "Java")
					  .userAgent("Mozilla")
					  .cookie("auth", "token")
					  .timeout(3000)
					  .get();
			System.out.println(doc3.title());
			Elements h2 = doc3.select("div.swiper-slide");
			
			for (Element element : h2) {
				Xinhua xinhua = new Xinhua();
				xinhua.title = element.getElementsByTag("h2").first().text();
				xinhua.img = element.getElementsByTag("img").attr("abs:src");
				xinhua.content = element.getElementsByTag("p").first().childNode(0).toString();
				xinhua.link = element.select("a[href]").attr("href");
				list.add(xinhua);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
