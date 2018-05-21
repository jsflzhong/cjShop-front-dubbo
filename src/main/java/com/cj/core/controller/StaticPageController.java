package com.cj.core.controller;

import com.cj.common.pojo.TaotaoResult;
import com.cj.common.utils.ExceptionUtil;
import com.cj.core.service.StaticPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description 生成静态页面Controller
 * @date 2018/5/18
 * @author cj
 */
@Controller
public class StaticPageController {

    @Autowired
    private StaticPageService staticPageService;

    @RequestMapping("/gen/item/{itemId}")
    @ResponseBody
    public TaotaoResult genItemPage(@PathVariable Long itemId) {
        try {
            TaotaoResult result = staticPageService.genItemHtml(itemId);
            //直接返回TaotaoResult对象,注解会自动把它转成JSON数据.
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //用ExceptionUtil工具类,给调用者返回异常的堆栈信息.
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }
}
