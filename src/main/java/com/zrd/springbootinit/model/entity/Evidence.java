package com.zrd.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 证据表
 * @TableName evidence
 */
@TableName(value ="evidence")
@Data
public class Evidence implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long evidenceId;

    /**
     * 关联的分析ID
     */
    private Long reportId;

    /**
     * 证据内容
     */
    private String evidenceContent;

    /**
     * 置信度(0-100)
     */
    private Integer confidenceLevel;

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