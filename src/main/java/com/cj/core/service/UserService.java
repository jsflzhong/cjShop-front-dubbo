package com.cj.core.service;

import com.cj.core.pojo.TbUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description 用户相关的service
 * @author cj
 */
public interface UserService {

    /**
     * 根据request,获取cookie中的token,调用sso服务从redis中根据token获取用户信息.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return TbUser
     */
    TbUser getUserByToken(HttpServletRequest request,HttpServletResponse response);
}
