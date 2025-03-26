package com.zrd.springbootinit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zrd.springbootinit.model.entity.News;
import com.zrd.springbootinit.model.entity.User;
import com.zrd.springbootinit.model.vo.NewsListVO;
import com.zrd.springbootinit.model.vo.NewsVO;

import java.util.List;

/**
* @author 张瑞东
* @description 针对表【news(新闻)】的数据库操作Service
* @createDate 2025-03-25 21:51:26
*/
public interface NewsService extends IService<News> {

    /**
     * 获取检测新闻记录列表
     * @param page
     * @param size
     * @param loginUser
     * @return
     */
    IPage<NewsListVO> getNewsPage(Integer page, Integer size, User loginUser);

    /**
     * 检测新闻
     * @return
     */
    boolean checkNew(String newContent, User loginUser);

    /**
     * 根据id获取新闻VO
     * @param newsId
     * @param loginUser
     * @return
     */
    NewsVO getVOById(Integer newsId, User loginUser);
}
