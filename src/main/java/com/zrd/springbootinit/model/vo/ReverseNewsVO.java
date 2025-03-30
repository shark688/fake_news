package com.zrd.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 翻转新闻时间线视图
 *
 * @author <a href="https://github.com/shark688">shark688</a>

 */
@Data
public class ReverseNewsVO implements Serializable {

    /**
     * 新闻状态
     */
    private String status;

    /**
     * 内容
     */
    private String summaryContent;

    /**
     * 证实时间
     */
    private String verificationTime;


    private static final long serialVersionUID = 1L;
}
