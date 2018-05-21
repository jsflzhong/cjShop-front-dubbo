package com.cj.core.controller;

import com.cj.core.pojo.SearchResult;
import com.cj.core.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

/**
 * @author cj
 * @description 搜索功能的Controller
 * @date 2018/5/18
 */
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 调用service执行查询,给前端返回search.jsp逻辑视图,用model给前端携带4个数据.
     *
     * @param keyword 查询关键字 请求参数名与形参名不一致,需要用注解关联上.
     * @param page    当前页码 分页相关参数给默认值.
     * @param rows    页大小
     * @param model   封装给前端的响应数据.
     * @return 逻辑视图:search.jsp
     * 2016年6月26日
     * @author cj
     */
    @RequestMapping("/search")
    public String search(@RequestParam("q") String keyword,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "60") Integer rows, Model model) {
        /*try {
            //get请求方式的,查询条件的中文乱码处理.
            //keyword = new String(keyword.getBytes("iso8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            keyword = "";
            e.printStackTrace();
        }*/
        //调用service执行查询. 返回SearchResult.
        SearchResult searchResult = searchService.search(keyword, page, rows);

        //从上面返回的SearchResult中,拿响应参数.
        //通过model对象,把响应参数传递 给页面.
        model.addAttribute("query", keyword);
        model.addAttribute("totalPages", searchResult.getPageCount());
        model.addAttribute("itemList", searchResult.getItemList());
        model.addAttribute("page", searchResult.getCurPage());

        //返回逻辑视图
        return "search";
    }
}
