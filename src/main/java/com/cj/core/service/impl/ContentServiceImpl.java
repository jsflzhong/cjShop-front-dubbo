package com.cj.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cj.core.facade.service.ContentFacade;
import com.cj.core.facade.service.ItemFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.cj.core.pojo.HttpClientUtil;
import com.cj.core.pojo.TaotaoResult;
import com.cj.core.utils.JsonUtils;
import com.cj.core.pojo.AdNode;
import com.cj.core.pojo.TbContent;
import com.cj.core.service.ContentService;

/**
 * 内容管理service
 * dubbo
 * @author 崔健
 * @date 2016年10月7日下午10:48:27
 */
@Service
public class ContentServiceImpl implements ContentService {

	//用注解,注入配置文件中的几个属性值.
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${REST_CONTENT_URL}")
	private String REST_CONTENT_URL;
	@Value("${REST_CONTENT_AD1_CID}")
	private String REST_CONTENT_AD1_CID;

	@Reference(version="1.0.0") //dubbo: 注解Annotation配置的方式. 如果用xml配置的方式,需要注掉本注解.
	private static ContentFacade contentFacade;

	/**
	 * dubbo: xml配置的方式
	 * 需要手动从IOC中取出dubbo的服务接口作为bean.而不能用注解注入.
	 * 如果用xml配置的方式,需要打开本段静态代码的注解.
	 */
	/*static {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/applicationContext-service.xml");
		contentFacade = (ContentFacade) ctx.getBean("contentFacade");
	}*/
	
	/**
	 * 获得大广告位的内容
	 */
	@Override
	public String getAd1List() {


		//用HttpClient,远程调用服务,来获得数据. (已经把HttpClientUtil工具类,放进common里了.用Get请求的方法.)
		//参数是远程服务层的url.不要写死. 要写进配置文件. 
		//spring容器已经加载那个配置文件了,在上面用@Value从配置文件中取值.
		//把url分三段,原来是这样的:http://localhost:8081/rest/content/89
		//第一段:http://localhost:8081/rest  第二段:/content 第三段:/89(大广告位的cid)
		//返回一段JSON数据.
		//String json = HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_URL + REST_CONTENT_AD1_CID);
		//把json转换成java对象,为了取JSON里面的data属性的值.
		//可以使用jsonutils工具类,也可以使用taotaoResult这个pojo自己的方法来转.
		//上面的JSON串中的data属性里,是个list. 所以这里用formatToList()方法.
		//参数1,把这个JSON数据转成pojo. 就是上面返回的json.
		//参数2,指定List中每个元素的数据类型.
		String resultJson = null;
		try {
			//TaotaoResult taotaoResult = TaotaoResult.formatToList(json, TbContent.class);
			//resultJson = "";
			//取data属性, 其实就是: "内容"list列表 .
			//List<TbContent> contentList = (List<TbContent>) taotaoResult.getData();
			List<TbContent> contentList = contentFacade.getContentList(Long.parseLong(REST_CONTENT_AD1_CID));
			//把上面拿到的"内容"list列表,转换成AdNode这个pojo的list列表.
			//因为AdNode这个pojo的字段格式,符合前端轮播图需要的JSON格式.
			//先建个载体集合.为AdNode的集合列表.
			List<AdNode> resultList = new ArrayList<>();
			for (TbContent tbContent : contentList) {
                //new一个用来响应轮播图的pojo.
                AdNode node = new AdNode();
                //开始补全字段.
                node.setHeight(240);//可以从前端的注释中的JSON数据中取值.
                node.setWidth(670);
                node.setSrc(tbContent.getPic());//图片的地址源.从数据库的pojo中拿.

                node.setHeightB(240);
                node.setWidthB(550);
                node.setSrcB(tbContent.getPic2());//图片的地址源.从数据库的pojo中拿.

                node.setAlt(tbContent.getSubTitle()); //库表中有俩title. 这个具体得问项目经理,用哪个.
                node.setHref(tbContent.getUrl());

                //把pojo,添加到list集合.
                resultList.add(node);
                //到此,这个resultList集合中的数据结构,就符合前端轮播图所需要的格式了.
                //但是,现在还是个Java的list对象,前端需要的是JSON!
                //所以,下面得把这个list,给转成JSON字符串,才能响应给前端.
            }
			//需要把resultList转换成json数据.因为前端需要的是个JSON数据.
			//调用工具类.
			resultJson = JsonUtils.objectToJson(resultList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//返回这个最终的JSON字符串.
		return resultJson;
	}

}
