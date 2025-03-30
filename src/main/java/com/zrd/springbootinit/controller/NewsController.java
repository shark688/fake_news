package com.zrd.springbootinit.controller;

import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zrd.springbootinit.common.BaseResponse;
import com.zrd.springbootinit.common.ErrorCode;
import com.zrd.springbootinit.common.ResultUtils;
import com.zrd.springbootinit.exception.BusinessException;
import com.zrd.springbootinit.model.dto.news.NewsCheckRequest;
import com.zrd.springbootinit.model.entity.User;
import com.zrd.springbootinit.model.vo.*;
import com.zrd.springbootinit.service.NewsService;
import com.zrd.springbootinit.service.UserService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 新闻接口
 *
 * @author <a href="https://github.com/shark688">shark688</a>
  
 */
@RestController
@RequestMapping("/news")
@Slf4j
public class NewsController {

    @Resource
    private NewsService newsService;

    @Resource
    private UserService userService;

    /**
     * 列表展示新闻
     * @param page
     * @param size
     * @param request
     * @return
     */
    @GetMapping("")
    public BaseResponse<IPage<NewsListVO>> userGetCheckNumber(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {

        User loginUser = userService.getLoginUser(request);

        // 可以在这里添加权限验证或其他业务逻辑
        IPage<NewsListVO> newsPage = newsService.getNewsPage(page, size, loginUser);
        return ResultUtils.success(newsPage);
    }


    /**
     * 检测新闻真实性
     * @param newsCheckRequest
     * @param request
     * @return
     */
    @PostMapping("/checkSingle")
    public BaseResponse<Boolean> userGetCheckNumber(@RequestBody NewsCheckRequest newsCheckRequest, HttpServletRequest request)
    {
        if (newsCheckRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String newContent = newsCheckRequest.getNewContent();
        if(StrUtil.isBlank(newContent))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请输入要检测真伪性的新闻文本!");
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = newsService.checkNew(newContent, loginUser);
        return ResultUtils.success(result,"已提交检测新闻任务");
    }


    /**
     * 根据id获取新闻
     * @param newsId
     * @param request
     * @return
     */
    @GetMapping("/{newsId}")
    public BaseResponse<NewsVO> userGetCheckNumber(@PathVariable Integer newsId, HttpServletRequest request)
    {
        if (newsId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        NewsVO result = newsService.getVOById(newsId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取翻转新闻列表
     * @param page
     * @param size
     * @param request
     * @return
     */
    @GetMapping("/listReverse")
    public BaseResponse<IPage<NewsReverseListVO>> userGetReverseNews(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {

        User loginUser = userService.getLoginUser(request);

        // 可以在这里添加权限验证或其他业务逻辑
        IPage<NewsReverseListVO> newsPage = newsService.getReverseNewsPage(page, size, loginUser);
        return ResultUtils.success(newsPage);
    }

    /**
     * 根据id获取翻转新闻时间线
     * @param newsId
     * @param request
     * @return
     */
    @GetMapping("/reverseOne/{newsId}")
    public BaseResponse<IPage<ReverseNewsVO>> userGetReverseNewsLine(
            @PathVariable Integer newsId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {

        User loginUser = userService.getLoginUser(request);

        // 可以在这里添加权限验证或其他业务逻辑
        IPage<ReverseNewsVO> newsPage = newsService.getReverseNewsLine(newsId, page, size, loginUser);
        return ResultUtils.success(newsPage);
    }
}
