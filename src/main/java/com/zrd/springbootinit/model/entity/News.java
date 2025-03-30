package com.zrd.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 新闻
 * @TableName news
 */
@TableName(value ="news")
@Data
public class News implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long newsId;

    /**
     * 创建新闻的用户id
     */
    private Long userId;

    /**
     * 新闻标题
     */
    private String newsTitle;

    /**
     * 新闻完整内容
     */
    private String newsContent;

    /**
     * 新闻来源URL
     */
    private String newsUrl;

    /**
     * 是否为翻转新闻
     */
    private Integer isReverse;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}