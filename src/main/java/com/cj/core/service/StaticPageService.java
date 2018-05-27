package com.cj.core.service;

import com.cj.core.pojo.TaotaoResult;

/**
 * @author cj
 * @description 用于生成freemaker静态页的服务的service
 * @date 2018/5/18
 */
public interface StaticPageService {

    /**
     * 根据商品id,调用rest服务查询商品的基本信息,详情信息,参数信息,然后调用freemaker生成html静态页.
     * @param itemId 商品id
     * @return TaotaoResult
     * @throws Exception Exception
     * @author cj
     */
    TaotaoResult genItemHtml(Long itemId) throws Exception;
}
