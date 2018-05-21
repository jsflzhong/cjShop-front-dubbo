package com.cj.core.service.impl;

import com.cj.common.pojo.HttpClientUtil;
import com.cj.common.pojo.TaotaoResult;
import com.cj.common.utils.CookieUtils;
import com.cj.core.pojo.TbUser;
import com.cj.core.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cj
 * @description 用户相关的service
 */
@Service
public class UserServiceImpl implements UserService {

    //注入:#SSO系统的基础URL
    @Value("${SSO_BASE_URL}")
    private String SSO_BASE_URL;
    //注入:#接口三：根据token查询用户信息的接口服务
    @Value("${SSO_USER_TOKEN_SERVICE}")
    private String SSO_USER_TOKEN_SERVICE;

    /**
     * 根据request,获取cookie中的token,调用sso服务从redis中根据token获取用户信息.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return TbUser
     */
    @Override
    public TbUser getUserByToken(HttpServletRequest request, HttpServletResponse response) {
        //外层加上try/catch. 因为拦截器会在controller中调用这里的代码.
        //如果这里挂了,整个系统就瘫痪了.
        try {
            //使用cookie工具类,"从cookie中取token".
            //参数1:固定是request. 参数2:存入该cookie时使用的key.
            //存入cookie时是这样的:CookieUtils.setCookie(request, response, "TT_TOKEN", token);
            String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
            //判断token是否有值
            if (StringUtils.isBlank(token)) {
                //如果没取到token的值,则返回null.
                return null;
            }
            //如果取到了token,则调用"sso系统的服务",查询用户信息.

            /**
             强调:如果想调用远程服务,就用 HttpClientUtil的doGet()方法!
             然后把地址拆分开,写进配置文件中,用IOC容器扫描后,在上面注入,再在这里拼接!
             为的就是解耦!以后修改方便!
             */
            String json = HttpClientUtil.doGet(SSO_BASE_URL + SSO_USER_TOKEN_SERVICE + token);
            //把json转换成java对象.
            /**
             套路:固定调用 TaotaoResult.format(json)方法.
             为的就是取出JSON中,Java对象(TaotaoResult)中的:
             1.状态码;
             2."data字段"(pojo对象)的值.
             */
            //注意,这里执行TaotaoResult的第一次转换:
            //但是参数里,并没有写它里面data中的pojo的类型!
            //因为:这次转换,与之前不同! 这次需要只拿出了状态码出来判断,并没有用到里面的pojo的数据.
            //判断完状态吗后,在下面,又再次重新转换了第二次,把里面的pojo也转成对应的类型了.
            TaotaoResult result = TaotaoResult.format(json);
            //判断对象的状态如果不是200,就说明该对象在Redis中,已经过期了!
            if (result.getStatus() != 200) {
                return null;
            }
            //如果对象的状态等于200,说明上面通过调用服务,不但从Redis中取到了对应token的对象,
            //而且该对象在Redis中也没过期.
            //那么就执行TaotaoResult的第二次转换:
            //把上面返回的JSON,转换成TaotaoResult对象.
            //这次连带着把JSON里面的pojo对象,也转成对应的pojo对象.参数里也了pojo对象的类型了.
            result = TaotaoResult.formatToPojo(json, TbUser.class);
            //最终目的,取出TaotaoResult里面的pojo对象.
            TbUser user = (TbUser) result.getData();
            //返回pojo对象.
            return user;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
