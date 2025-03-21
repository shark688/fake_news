package com.zrd.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zrd.springbootinit.model.entity.User;
import com.zrd.springbootinit.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @author <a href="https://github.com/shark688">shark688</a>
  
 */
public interface UserService extends IService<User> {
//
//    /**
//     * 用户注册
//     *
//     * @param userAccount   用户账户
//     * @param userPassword  用户密码
//     * @param checkPassword 校验密码
//     * @return 新用户 id
//     */
//    long userRegister(String userAccount, String userPassword, String checkPassword);
//
//    /**
//     * 用户登录
//     *
//     * @param userAccount  用户账户
//     * @param userPassword 用户密码
//     * @param request
//     * @return 脱敏后的用户信息
//     */
//    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);
//
//    /**
//     * 用户登录（微信开放平台）
//     *
//     * @param wxOAuth2UserInfo 从微信获取的用户信息
//     * @param request
//     * @return 脱敏后的用户信息
//     */
//    LoginUserVO userLoginByMpOpen(WxOAuth2UserInfo wxOAuth2UserInfo, HttpServletRequest request);
//
//    /**
//     * 获取当前登录用户
//     *
//     * @param request
//     * @return
//     */
//    User getLoginUser(HttpServletRequest request);
//
//    /**
//     * 获取当前登录用户（允许未登录）
//     *
//     * @param request
//     * @return
//     */
//    User getLoginUserPermitNull(HttpServletRequest request);
//
//    /**
//     * 是否为管理员
//     *
//     * @param request
//     * @return
//     */
//    boolean isAdmin(HttpServletRequest request);
//
//    /**
//     * 是否为管理员
//     *
//     * @param user
//     * @return
//     */
//    boolean isAdmin(User user);
//
//    /**
//     * 用户注销
//     *
//     * @param request
//     * @return
//     */
//    boolean userLogout(HttpServletRequest request);
//
//    /**
//     * 获取脱敏的已登录用户信息
//     *
//     * @return
//     */
//    LoginUserVO getLoginUserVO(User user);
//
//    /**
//     * 获取脱敏的用户信息
//     *
//     * @param user
//     * @return
//     */
//    UserVO getUserVO(User user);
//
//    /**
//     * 获取脱敏的用户信息
//     *
//     * @param userList
//     * @return
//     */
//    List<UserVO> getUserVO(List<User> userList);
//
//    /**
//     * 获取查询条件
//     *
//     * @param userQueryRequest
//     * @return
//     */
//    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);
//
//
//
//
//
//


    /**
     * 获取验证码
     * @param phoneNumber
     * @return
     */
    Boolean userGetCheckNumber(String phoneNumber);

    /**
     * 用户登录(验证码)
     * @param phoneNumber
     * @param request
     * @return
     */
    Boolean userLogin(String phoneNumber, String checkNumber, HttpServletRequest request);

    /**
     * 用户登出
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取当前用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取用户信息
     * @param user
     * @return
     */
    UserVO getLoginUserVO(User user);

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    User getLoginUserPermitNull(HttpServletRequest request);
}
