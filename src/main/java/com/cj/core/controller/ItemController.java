package com.cj.core.controller;

import com.cj.core.pojo.TbItem;
import com.cj.core.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author cj
 *         商品Controller
 *         2018/5/16
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 在用户点击搜索结果后,打开一个商品详情页面.
     * 对应的jsp: 从search.jsp过来的.要打开item.jsp.
     * <p>Title:</p>
     *
     * @param itemId
     * @param model
     * @return 2016年6月29日
     * String
     */
    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable Long itemId, Model model) {
        //这个item,其实就不是TbItem的对象了.
        //而是他的子类,也就是我们写的PortalItem这个pojo对象了.
        //因为,在service层,把JSON转成java对象时,第二参数,我用的是子类PortalItem.
        //这里只不过是用父类的引用,来代表子类的对象而已.
        TbItem item = itemService.getItemById(itemId);
        model.addAttribute("item", item);
        return "item";
    }

    /**
     * 根据商品id,查询并返回商品的描述(详情)信息.
     * 对应的jsp: item.jsp.
     *
     * @param itemId 商品id
     * @return 商品的描述(详情)信息
     * String 返回html片段.
     * <p>
     * produces=MediaType.TEXT_HTML_VALUE+";charset=utf-8"的解释:
     * 如果不加这个,那么item.jsp上的确可以显示商品描述信息.但是中文部分会是乱码!
     * "TEXT_HTML_VALUE"是它内置的常量,因为我要返回的html字符串出现乱码了,所以这里选择这个常量.
     * 注意,已归纳到注解功能里!
     */
    @RequestMapping(value = "/item/desc/{itemId}", produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
    @ResponseBody
    public String getItemDesc(@PathVariable Long itemId) {
        //调用service,传递商品id.返回商品描述pojo的描述信息字段内容.
        //实际上这个已经是一个html片段了.(在rest服务中从数据库拿的商品描述原始数据,就是html片段.)
        String desc = itemService.getItemDescById(itemId);
        //直接return.
        return desc;
    }

    /**
     * 根据商品id,查询并返回商品的规格参数信息.
     *
     * @param itemId 商品id
     * @return 商品的规格参数信息. 是html片段,在service层处理的.
     * 别忘记规格参数的中文编码问题.
     * @author cj
     */
    @RequestMapping(value = "/item/param/{itemId}", produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
    @ResponseBody
    public String getItemParam(@PathVariable Long itemId) {
        String paramHtml = itemService.getItemParamById(itemId);
        return paramHtml;
    }

}
