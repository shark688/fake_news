package com.zrd.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 分析报告表
 * @TableName analystreports
 */
@TableName(value ="analystreports")
@Data
public class Analystreports implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long reportId;

    /**
     * 关联的验证结果ID
     */
    private Long resultId;

    /**
     * 分析报告内容
     */
    private String reportContent;

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