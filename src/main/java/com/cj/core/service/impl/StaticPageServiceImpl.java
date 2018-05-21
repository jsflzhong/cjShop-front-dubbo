package com.cj.core.service.impl;

import com.cj.common.pojo.TaotaoResult;
import com.cj.core.pojo.TbItem;
import com.cj.core.service.ItemService;
import com.cj.core.service.StaticPageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cj
 * @description 用于生成freemaker静态页的服务的service
 * @date 2018/5/18
 */
@Service
public class StaticPageServiceImpl implements StaticPageService {

    //注入ItemService,这个service里,有"获取商品详情页面"的三个业务方法.
    //我们不用再写,直接注入这个service.在下面调用它的方法即可.
    @Autowired
    private ItemService itemService;
    //注入在IOC容器配置的FreeMarkerConfigurer对象. freemarker必须使用这个对象.
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    //注入配置文件中的路径.
    @Value("${STATIC_PAGE_PATH}")
    private String STATIC_PAGE_PATH;

    /**
     * 根据商品id,调用rest服务查询商品的基本信息,详情信息,参数信息,然后调用freemaker生成html静态页.
     * @param itemId 商品id
     * @return TaotaoResult
     * @throws Exception Exception
     * @author cj
     */
    @Override
    public TaotaoResult genItemHtml(Long itemId) throws Exception {
        //调用上面注入的service.获取商品"基本信息".
        TbItem tbItem = itemService.getItemById(itemId);
        //调用上面注入的service.获取"商品描述".
        String itemDesc = itemService.getItemDescById(itemId);
        //调用上面注入的service.获取商品的"规格参数".
        String itemParam = itemService.getItemParamById(itemId);

        //生成静态页面...
        //用注入的FreeMarkerConfigurer对象,获得之前测试时用的Configuration对象. 得用这个.
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //获得指定的模板. 得去创建这个模板.
        Template template = configuration.getTemplate("item.ftl");
        template.setOutputEncoding("UTF-8");
        //创建一个数据集.
        Map root = new HashMap<>();
        //向数据集中添加属性
        //这里添加什么? 不是随意的,要分析模板中需要什么,再来这里添加.
        root.put("item", tbItem);
        root.put("itemDesc", itemDesc);
        root.put("itemParam", itemParam);
        //创建一个Writer对象,写出静态文件,到指定的路径. 一提路径,肯定是写进配置文件中的.
        //参数: 路径 + 文件名 + 后缀名. 文件名可以是商品的id.
        //Writer out = new FileWriter(new File(STATIC_PAGE_PATH + itemId + ".html")); //这种方式导致生成的html页中的中文乱码
        //解决生成的html页中的中文乱码的问题
        File htmlFile = new File(STATIC_PAGE_PATH + itemId + ".html");
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile),"UTF-8"));
        //生成静态文件
        //参数: 数据集 和 输出流.
        template.process(root, out);
        //别忘记刷出和关闭流.
        out.flush();
        out.close();

        //返回TaotaoResult的ok方法即可.
        return TaotaoResult.ok();
    }
}
