package com.cj.core.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cj.core.pojo.HttpClientUtil;
import com.cj.core.pojo.TaotaoResult;
import com.cj.core.utils.JsonUtils;
import com.cj.core.facade.service.ItemFacade;
import com.cj.core.pojo.PortalItem;
import com.cj.core.pojo.TbItem;
import com.cj.core.pojo.TbItemDesc;
import com.cj.core.pojo.TbItemParamItem;
import com.cj.core.service.ItemService;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商品Service
 * dubbo
 * @author cj
 */
@Service
public class ItemServiceImpl implements ItemService {

    //rest远程服务的基础url
    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    //rest服务系统中的: "商品基本信息服务"的URL
    @Value("${REST_ITEM_BASE_URL}")
    private String REST_ITEM_BASE_URL;
    //rest服务系统中的: "商品详情信息服务"的URL
    @Value("${REST_ITEM_DESC_URL}")
    private String REST_ITEM_DESC_URL;
    //rest服务系统中的: "商品参数服务"的URL
    @Value("${REST_ITEM_PARAM_URL}")
    private String REST_ITEM_PARAM_URL;

    //dubbo
    @Reference(version="1.0.0") //dubbo: 注解Annotation配置的方式. 如果用xml配置的方式,需要注掉本注解.
    private static ItemFacade itemFacade;

    /**
     * dubbo: xml配置的方式
     * 需要手动从IOC中取出dubbo的服务接口作为bean.而不能用注解注入.
     * 如果用xml配置的方式,需要打开本段静态代码的注解.
     */
    /*static {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/applicationContext-service.xml");
        itemFacade = (ItemFacade) ctx.getBean("itemFacade");
    }*/

    /**
     * 根据商品id,通过dubbo调用服务,查询商品基本信息.
     *
     * @author cj
     */
    @Override
    public TbItem getItemById(Long itemId) {

        //使用httpclient,调用远程服务.
        //为了维护方便,URL固定要写进配置文件中.
        //URL=rest服务系统的基础url + rest服务系统的"商品基本信息服务"的URL + 商品id.(http://localhost:8081/rest + /item/base/ + itemId)
        TbItem item = itemFacade.getItemById(itemId);
       // String json = HttpClientUtil.doGet(REST_BASE_URL + REST_ITEM_BASE_URL + itemId);
        ///转换成java对象(服务层固定是把TaotaoResult对象转成的JSON)
        //注意参数二: TaotaoResult里的data字段里封装的数据,是什么类型,这里就写什么类型.
        //一般来讲,这个类型,都是在服务系统中,查询的某个表的pojo.
        //注意了!参数2的"PortalItem"对象,是我们自己写的对象,它继承了TbItem对象.
        //这里本应该写TbItem对象的. 但是,TbItem对象里没有images这个字段,而前端jsp中又需要这个字段的值.
        //所以,我们自己写了个PortalItem对象,去继承TbItem对象,添加了一个getImages()方法. 因为TbItem对象是别的工程的,我们不能动.
        //TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, PortalItem.class);
        //取TaotaoResult里真正的数据:商品表的pojo对象
        //这里还是强转成父类,没问题的.用父类的引用,取子类的对象.

        //由于前端页面使用到了bean用的getImages()方法,但原始的TbItem中没有该方法(没有s),所以把返回的pojo转成自定义的子类PortalItem,
        //该子类中扩展出了getImages()方法.
        JSONObject jsonObject = net.sf.json.JSONObject.fromObject(item);
        PortalItem portalItem = (PortalItem) JSONObject.toBean(jsonObject, PortalItem.class);
        return portalItem;
    }

    /**
     * 根据商品id获取商品的详情数据(redis)
     *
     * @param itemId 商品id
     * @return 商品详情pojo
     * @author cj
     */
    @Override
    public String getItemDescById(Long itemId) {
        //老规矩,用httpclient,调用远程服务.
        //http://localhost:8081/rest + /item/desc/ + 144766336139977
        TbItemDesc itemDesc = itemFacade.getItemDescById(itemId);
        //String json = HttpClientUtil.doGet(REST_BASE_URL + REST_ITEM_DESC_URL + itemId);
        //TODO: 对返回值的空异常处理
        //转换成java对象,老规矩,还是TaotaoResult.
        //参数二,是TaotaoResult里data数据的类型.
       // TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbItemDesc.class);
        //取TaotaoResult对象的data字段额值. 即: 商品描述.
       // TbItemDesc itemDesc = (TbItemDesc) taotaoResult.getData();
        //取TbItemDesc对象的ItemDesc字段.表中这一列的内容,才是"商品的描述".并且是带着html标签的.正是前端需要的.
      //  return itemDesc.getItemDesc();
       // return itemDescById;
        return itemDesc.getItemDesc();
    }

    /**
     * 根据商品id获取商品的规格参数数据(redis)
     *
     * @param itemId 商品id
     * @return 商品规格参数pojo
     * @author cj
     */
    @Override
    public String getItemParamById(Long itemId) {
        //String itemParamById = itemService.getItemParamById(itemId);
        // 根据商品id获得对应的规格参数
        //String json = HttpClientUtil.doGet(REST_BASE_URL + REST_ITEM_PARAM_URL + itemId);
        TbItemParamItem itemParamItem = itemFacade.getItemParamById(itemId);
        // TODO: 对返回值的空异常处理
        // 转换成java对象TaotaoResult
        //TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbItemParamItem.class);
        // 取规格参数. 即TaotaoResult的data字段的值.
        //TbItemParamItem itemParamItem = (TbItemParamItem) taotaoResult.getData();
        // 取TbItemParamItem这各表pojo对象的paramData字段的值. 才是商品的规格参数.
        // tb_item_param_item表的`param_data`字段的值.又是一个json数据:[{"group":"主体","params":[{"k":"品牌","v":"苹果（Apple）"},{"k":"型号","v":"iP......
        String paramJson = itemParamItem.getParamData();
        // 把规格参数的json数据转换成java对象
        // 使用JsonUtils工具类,的jsonToList()方法.因为看上面,这个JSON数据是以[]开头的,是个数组.
        // 参数2,是集合[]里面的数据的类型.不知道的话,就用map.
        // 返回是个List套Map的类型.
        List<Map> mapList = JsonUtils.jsonToList(paramJson, Map.class);
        // 遍历list生成html
        // 注意,做过一次这个事了! 这次直接拷贝即可!
        // 在taotao-manager-service工程里的ItemServiceImpl里.
        // 自己归纳过:D:\Java\功能实现!\html\在java中动态拼接html.java
        StringBuffer sb = new StringBuffer();

        sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\">\n");
        sb.append("    <tbody>\n");
        for (Map map : mapList) {
            sb.append("        <tr>\n");
            sb.append("            <th class=\"tdTitle\" colspan=\"2\">" + map.get("group") + "</th>\n");
            sb.append("        </tr>\n");
            // 取规格项
            List<Map> mapList2 = (List<Map>) map.get("params");
            for (Map map2 : mapList2) {
                sb.append("        <tr>\n");
                sb.append("            <td class=\"tdTitle\">" + map2.get("k") + "</td>\n");
                sb.append("            <td>" + map2.get("v") + "</td>\n");
                sb.append("        </tr>\n");
            }
        }
        sb.append("    </tbody>\n");
        sb.append("</table>");

        return sb.toString();
    }


}
