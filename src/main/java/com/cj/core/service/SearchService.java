package com.cj.core.service;

import com.cj.core.pojo.SearchResult;

/**
 * @description 搜索服务service
 * 通过HTTP调用service搜索服务,处理数据并返回.
 * @date 2018/5/18
 * @author cj
 */
public interface SearchService {

    /**
     * 调用服务查询商品列表
     * @param keyword 查询的条件
     * @param page 当前页码
     * @param rows 每页显示多少条
     * @return SearchResult
     * @author cj
     */
    SearchResult search(String keyword, int page, int rows);
}
