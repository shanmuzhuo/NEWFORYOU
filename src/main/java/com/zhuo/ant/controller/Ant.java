package com.zhuo.ant.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 爬虫测试类
 * @author zzx
 * 2017年9月25日10:38:07
 *
 */

@Controller
@RequestMapping("/Ant")
public class Ant {
	
	@RequestMapping("/weight")
    public String getWeight(HttpServletRequest request, Model model) {
    	System.out.println("fasdf");
		return null;
    }
	
}
