package com.cj.core.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cj.core.pojo.HttpClientUtil;
import com.cj.core.pojo.TaotaoResult;
import com.cj.core.facade.search.SearchFacade;
import com.cj.core.pojo.SearchResult;
import com.cj.core.service.SearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cj
 * @description 搜索服务service
 * 通过HTTP调用service搜索服务,处理数据并返回.
 * dubbo
 * @date 2018/5/18
 */
@Service
public class SearchServiceImpl implements SearchService {

    //注入配置文件中的url.
    @Value("${SEARCH_BASE_URL}")
    private String SEARCH_BASE_URL;

    @Reference(version = "1.0.0")
    private SearchFacade searchFacade;

    /**
     * 调用服务查询商品列表
     *
     * @param keyword 查询的条件
     * @param page    当前页码
     * @param rows    每页显示多少条
     * @return SearchResult
     * @author cj
     */
    @Override
    public SearchResult search(String keyword, int page, int rows) {

        //创建doGet的参数.是个Map.
        //Map<String, String> param = new HashMap<>();
        //内含search系统发布的服务,所需要的三个参数: 搜索关键字,当前页,页大小.
        //param.put("keyword", keyword);
        // param.put("page", page + ""); //服务那边的参数,是string类型.
        //param.put("rows", rows + "");

        //用HttpClientUtil工具类,调用服务.
        //参数一,是目标服务的url.
        //老规矩,为了便于维护,把url写进spring容器可以扫描到的配置文件里: resource.properties
        //再用注解注入进来使用.
        //返回服务层提供的JSON数据.
        SearchResult searchResult = null;
        try {
            //String json = HttpClientUtil.doGet(SEARCH_BASE_URL, param);
            searchResult = searchFacade.search(keyword, page, rows);
            System.out.println("@@@@@@@@@ search返回,description: " + searchResult.getItemList().get(0).getItem_desc() + " @@@@@@@@@");
            /**
             注意:
             以后,凡是调用服务层返回的JSON,
             我都可以用TaotaoResult里的"formatToPojo(JSON,JSON里的数据类型);" 这个方法,
             来把JSON数据转成java数据.

             通过浏览器调用一下search系统提供的服务,发现返回的JSON数据,是这样的:
             {"status":200,"msg":"OK","data":{"itemList":[{"id":"1134457160","title":"E·.....

             注意到,前面的状态码,和信息,对我没用. 我要的还是后面data的值.
             而data的值,就是一个SearchResult对象.该对象里包含SearchItem对象的集合.
             为了拿到data的值,我得把JSON转成TaotaoResult,然后调用它的"getData()"方法即可.
             */
            //使用TaotaoResult, 把上面返回的JSON,转换成java对象
            //TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, SearchResult.class);
            //拿到JSON中的data数据.
            //data数据的值,就是SearchResult对象.
            //还是那个问题.调用者怎么知道,data数据的值,就是SearchResult对象??
            //searchResult = (SearchResult) taotaoResult.getData();
        } catch (Exception e) {
            searchResult = new SearchResult();
            e.printStackTrace();
        }

        return searchResult;
    }
}
