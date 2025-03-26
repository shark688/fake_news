package com.zrd.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 新闻验证结果主表
 * @TableName results
 */
@TableName(value ="results")
@Data
public class Results implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long resultId;

    /**
     * 关联的新闻ID
     */
    private Long newsId;

    /**
     * 当前结果: 0-假, 1-真, 2-运行中, 3-运行失败
     */
    private Integer status;

    /**
     * 分析内容总结
     */
    private String summaryContent;

    /**
     * 验证时间
     */
    private Date verificationTime;

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