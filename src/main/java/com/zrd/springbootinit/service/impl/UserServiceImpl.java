package com.zrd.springbootinit.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrd.springbootinit.common.BaseResponse;
import com.zrd.springbootinit.common.ErrorCode;
import com.zrd.springbootinit.constant.RedisConstant;
import com.zrd.springbootinit.exception.BusinessException;
import com.zrd.springbootinit.mapper.UserMapper;
import com.zrd.springbootinit.model.entity.User;
import com.zrd.springbootinit.model.vo.UserVO;
import com.zrd.springbootinit.service.UserService;
import com.zrd.springbootinit.utils.CommentUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.concurrent.TimeUnit;

import static com.zrd.springbootinit.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现
 *
 * @author <a href="https://github.com/shark688">shark688</a>
  
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "zrd";

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     *
     * @param phoneNumber
     * @return
     */
    @Override
    public Boolean userGetCheckNumber(String phoneNumber) {
        String checkNumber = "init";
        try {
            //checkNumber = CommentUtils.sendMessage(phoneNumber);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"短信服务异常");
        }
        String redisKey = RedisConstant.PHONE_NUMBER_KEY + phoneNumber;
        redisTemplate.opsForValue().set(redisKey, checkNumber);
        redisTemplate.expire(redisKey,5, TimeUnit.MINUTES);
        log.info("验证码{}已发送到{}", checkNumber, phoneNumber);
        return true;
    }

    /**
     * 用户登录(验证码)
     * @param phoneNumber
     * @param request
     * @return
     */
    @Override
    public Boolean userLogin(String phoneNumber, String checkNumber, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isBlank(phoneNumber)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号不能为空");
        }

        String redisKey = RedisConstant.PHONE_NUMBER_KEY + phoneNumber;
        String result = redisTemplate.opsForValue().get(redisKey).toString();

        // 如果 Redis 中没有获取到验证码，输出错误信息
        if (result == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"验证码不存在或已过期");
        }

        // 如果用户输入的验证码与 Redis 中存储的验证码不一致，输出错误信息
        if (!StrUtil.equals(result,checkNumber)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"验证码不匹配");
        }

        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phoneNumber", phoneNumber);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            User newUser = new User();
            newUser.setPhoneNumber(phoneNumber);
            this.save(newUser);
            request.getSession().setAttribute(USER_LOGIN_STATE, newUser);
        }
        // 3. 记录用户的登录态
        else
        {
            request.getSession().setAttribute(USER_LOGIN_STATE, user);
        }
        log.info("电话号{}已登录", phoneNumber);
        return true;
    }


    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }

    @Override
    public UserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

//    @Override
//    public long userRegister(String userAccount, String userPassword, String checkPassword) {
//        // 1. 校验
//        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
//        }
//        if (userAccount.length() < 4) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
//        }
//        if (userPassword.length() < 8 || checkPassword.length() < 8) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
//        }
//        // 密码和校验密码相同
//        if (!userPassword.equals(checkPassword)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
//        }
//        synchronized (userAccount.intern()) {
//            // 账户不能重复
//            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("userAccount", userAccount);
//            long count = this.baseMapper.selectCount(queryWrapper);
//            if (count > 0) {
//                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
//            }
//            // 2. 加密
//            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
//            // 3. 插入数据
//            User user = new User();
//            user.setUserAccount(userAccount);
//            user.setUserPassword(encryptPassword);
//            boolean saveResult = this.save(user);
//            if (!saveResult) {
//                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
//            }
//            return user.getId();
//        }
//    }
//
//    @Override
//    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
//        // 1. 校验
//        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
//        }
//        if (userAccount.length() < 4) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
//        }
//        if (userPassword.length() < 8) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
//        }
//        // 2. 加密
//        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
//        // 查询用户是否存在
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("userAccount", userAccount);
//        queryWrapper.eq("userPassword", encryptPassword);
//        User user = this.baseMapper.selectOne(queryWrapper);
//        // 用户不存在
//        if (user == null) {
//            log.info("user login failed, userAccount cannot match userPassword");
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
//        }
//        // 3. 记录用户的登录态
//        request.getSession().setAttribute(USER_LOGIN_STATE, user);
//        return this.getLoginUserVO(user);
//    }
//
//    @Override
//    public LoginUserVO userLoginByMpOpen(WxOAuth2UserInfo wxOAuth2UserInfo, HttpServletRequest request) {
//        String unionId = wxOAuth2UserInfo.getUnionId();
//        String mpOpenId = wxOAuth2UserInfo.getOpenid();
//        // 单机锁
//        synchronized (unionId.intern()) {
//            // 查询用户是否已存在
//            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("unionId", unionId);
//            User user = this.getOne(queryWrapper);
//            // 被封号，禁止登录
//            if (user != null && UserRoleEnum.BAN.getValue().equals(user.getUserRole())) {
//                throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "该用户已被封，禁止登录");
//            }
//            // 用户不存在则创建
//            if (user == null) {
//                user = new User();
//                user.setUnionId(unionId);
//                user.setMpOpenId(mpOpenId);
//                user.setUserAvatar(wxOAuth2UserInfo.getHeadImgUrl());
//                user.setUserName(wxOAuth2UserInfo.getNickname());
//                boolean result = this.save(user);
//                if (!result) {
//                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "登录失败");
//                }
//            }
//            // 记录用户的登录态
//            request.getSession().setAttribute(USER_LOGIN_STATE, user);
//            return getLoginUserVO(user);
//        }
//    }
//
//    /**
//     * 获取当前登录用户
//     *
//     * @param request
//     * @return
//     */
//    @Override
//    public User getLoginUser(HttpServletRequest request) {
//        // 先判断是否已登录
//        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
//        User currentUser = (User) userObj;
//        if (currentUser == null || currentUser.getId() == null) {
//            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
//        }
//        // 从数据库查询（追求性能的话可以注释，直接走缓存）
//        long userId = currentUser.getId();
//        currentUser = this.getById(userId);
//        if (currentUser == null) {
//            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
//        }
//        return currentUser;
//    }
//
//    /**
//     * 获取当前登录用户（允许未登录）
//     *
//     * @param request
//     * @return
//     */
//    @Override
//    public User getLoginUserPermitNull(HttpServletRequest request) {
//        // 先判断是否已登录
//        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
//        User currentUser = (User) userObj;
//        if (currentUser == null || currentUser.getId() == null) {
//            return null;
//        }
//        // 从数据库查询（追求性能的话可以注释，直接走缓存）
//        long userId = currentUser.getId();
//        return this.getById(userId);
//    }
//
//    /**
//     * 是否为管理员
//     *
//     * @param request
//     * @return
//     */
//    @Override
//    public boolean isAdmin(HttpServletRequest request) {
//        // 仅管理员可查询
//        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
//        User user = (User) userObj;
//        return isAdmin(user);
//    }
//
//    @Override
//    public boolean isAdmin(User user) {
//        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
//    }
//
//    /**
//     * 用户注销
//     *
//     * @param request
//     */
//    @Override
//    public boolean userLogout(HttpServletRequest request) {
//        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
//            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
//        }
//        // 移除登录态
//        request.getSession().removeAttribute(USER_LOGIN_STATE);
//        return true;
//    }
//
//    @Override
//    public LoginUserVO getLoginUserVO(User user) {
//        if (user == null) {
//            return null;
//        }
//        LoginUserVO loginUserVO = new LoginUserVO();
//        BeanUtils.copyProperties(user, loginUserVO);
//        return loginUserVO;
//    }
//
//    @Override
//    public UserVO getUserVO(User user) {
//        if (user == null) {
//            return null;
//        }
//        UserVO userVO = new UserVO();
//        BeanUtils.copyProperties(user, userVO);
//        return userVO;
//    }
//
//    @Override
//    public List<UserVO> getUserVO(List<User> userList) {
//        if (CollUtil.isEmpty(userList)) {
//            return new ArrayList<>();
//        }
//        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
//    }
//
//    @Override
//    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
//        if (userQueryRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
//        }
//        Long id = userQueryRequest.getId();
//        String unionId = userQueryRequest.getUnionId();
//        String mpOpenId = userQueryRequest.getMpOpenId();
//        String userName = userQueryRequest.getUserName();
//        String userProfile = userQueryRequest.getUserProfile();
//        String userRole = userQueryRequest.getUserRole();
//        String sortField = userQueryRequest.getSortField();
//        String sortOrder = userQueryRequest.getSortOrder();
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq(id != null, "id", id);
//        queryWrapper.eq(StringUtils.isNotBlank(unionId), "unionId", unionId);
//        queryWrapper.eq(StringUtils.isNotBlank(mpOpenId), "mpOpenId", mpOpenId);
//        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
//        queryWrapper.like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile);
//        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
//        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
//                sortField);
//        return queryWrapper;
//    }

}
