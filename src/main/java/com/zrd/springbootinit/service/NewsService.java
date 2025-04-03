package com.zrd.springbootinit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zrd.springbootinit.model.entity.News;
import com.zrd.springbootinit.model.entity.User;
import com.zrd.springbootinit.model.vo.NewsListVO;
import com.zrd.springbootinit.model.vo.NewsReverseListVO;
import com.zrd.springbootinit.model.vo.NewsVO;
import com.zrd.springbootinit.model.vo.ReverseNewsVO;

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

    /**
     * 获取翻转新闻记录列表
     * @param page
     * @param size
     * @param loginUser
     * @return
     */
    IPage<NewsReverseListVO> getReverseNewsPage(Integer page, Integer size, User loginUser);

    /**
     * 获取翻转新闻时间线
     * @param page
     * @param size
     * @param loginUser
     * @return
     */
    IPage<ReverseNewsVO> getReverseNewsLine(Integer newsId, Integer page, Integer size, User loginUser);

    /**
     * 逻辑删除新闻
     * @param newsId 新闻ID
     * @return 是否删除成功
     */
    boolean deleteNewsById(Long newsId);

    /**
     * 批量逻辑删除新闻
     * @param newsIds 新闻ID列表
     * @return 是否删除成功
     */
    boolean batchDeleteNews(List<Long> newsIds);
}
