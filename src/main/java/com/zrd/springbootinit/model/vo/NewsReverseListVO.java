package com.zrd.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 翻转新闻列表结果视图
 *
 * @author <a href="https://github.com/shark688">shark688</a>
  
 */
@Data
public class NewsReverseListVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 新闻状态
     */
    private String status;

    /**
     * 新闻标题
     */
    private String title;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 是否翻转
     */
    private Integer isReverse;

    private static final long serialVersionUID = 1L;
}