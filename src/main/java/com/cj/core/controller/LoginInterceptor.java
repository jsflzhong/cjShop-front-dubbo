package com.cj.core.controller;

import com.cj.core.pojo.TbUser;
import com.cj.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cj
 * @description 用户登录拦截器.
 * @date 2018/5/18
 */
@Controller
public class LoginInterceptor implements HandlerInterceptor {

    //注入service.
    @Autowired
    private UserService userService;
    /**
     * 注入#专用于跳转到登录页面的controller的请求url.
     * 注意父子容器问题.这里是子容器!而配置文件那边是spring父容器扫描的!子容器无法访问父容器的"属性配置文件",只可以使用父容器中的"对象"
     * 解决方案:
     * 使用子容器扫描一下配置文件.
     * 在子容器的配置文件中,加一个扫描配置. 可以直接把"applicationContext-service.xml"中的扫描配置,复制到"springmvc.xml"中去.
     * <context:property-placeholder location="classpath:properties/*.properties" />
     * 这样就可以了.
     */
    @Value("${SSO_LOGIN_URL}")
    private String SSO_LOGIN_URL;

    /**
     实现拦截器接口后,会自动重写接口中的三个方法:
     */

    /**
     * 重写方法一:在handler执行之前,来执行这个方法.
     * "拦截器",要在这第一个重写的方法中来写.
     * 如果返回false,就是拦截了.
     * 如果返回true,就是放行.
     * @author cj
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 1、拦截请求url. //需要在springmvc.xml中配置这个url.
        // 2、调用service,传递request过去,让service从request中取cookie,再从cookie中取token,再从redis中根据token取用户.
        TbUser user = userService.getUserByToken(request, response);
        // 3、(已在service中做完)判断如果没有取到toke,说明用户肯定没登录,则跳转到登录页面。(service返回的null,会进入下面的重定向)
        // 4、(已在service中做完)判断如果取到token，需要调用"sso系统的服务",查询该token,在Redis缓存中,对应的用户信息。
        // 5、(已在service中做完)判断如果该token在Redis中对应的用户,它的(模拟)session已经过期，则跳转到登录页面.(service返回的null,会进入下面的重定向)

        //判断service返回的TbUser对象如果为空.
        //说明在service那边,调用sso服务系统的服务,查询Redis缓存里,并没有这里拿到的token,所对应的用户数据.
        if (user == null) {
            //那么就应该通过重定向到: sso服务系统里的"专门用于跳转到登录页面用的controller",来跳转到sso系统的登录页面.
            //注意,跨系统了,应该用重定向.(因为转发不改变url)
            //注意,凡是涉及到url的,都考虑写进配置文件. 重视解耦!
            response.sendRedirect(SSO_LOGIN_URL + "?redirectURL=" + request.getRequestURL());
            //然后返回false,为了拦截用户.
            return false;
        }
        //service返回的TbUser对象如果不为空.
        //可以"放行"了. 返回 true即可.
        return true;
    }

    /**
     * 重写方法二:在handler执行之后,但在返回ModelAndView之前,来执行这个方法.
     * 完全不用管它...
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    /**
     * 在返回ModelAndView之后,最终再来执行这个方法.
     * 完全不用管它...
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
