package com.cj.core.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cj.core.service.ContentService;

@Controller
public class IndexController {
	
	@Resource
	private ContentService contentService;

	/**
	 * 专门用来跳转到首页
	 * 在访问http://localhost:8082/时:
	 * 经由web.xml中的欢迎页<welcome-file>index.html</welcome-file>
	 * 和mvc拦截器配置:<url-pattern>*.html</url-pattern>,
	 * 拦截了实际的url:http://localhost:8082/index.html,并由springmvc把请求交到了本handler中(index).
	 * @param model model
	 * @return jsp
	 */
	@RequestMapping(value="/index")
	public String showIndex(Model model) {
		//在跳转到首页的时候,加载大广告位轮播图的内容...
		//取大广告位内容.
		String json = contentService.getAd1List();

		//传递给jsp页面.可使用model
		//jsp中用的名字:var data = ${ad1}; 名字得对应.
		model.addAttribute("ad1",json);

		//最后再跳转到首页.
		return "/index";
	}
	
	
}
