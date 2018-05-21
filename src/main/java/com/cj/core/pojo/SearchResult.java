package com.cj.core.pojo;

import java.util.List;

/**
 * 用来封装"搜索服务"的响应数据的pojo
 *
 * @author 崔健
 * @date 2016年12月16日上午12:18:00
 */
public class SearchResult {

    private List<SearchItem> itemList; //查询的结果集-即商品列表.
    private Long recordCount; //查询结果总记录数
    private int pageCount; //查询结果的总页数
    private int curPage; //当前页码

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

}
