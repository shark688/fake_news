package com.zrd.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 新闻视图
 *
 * @author <a href="https://github.com/shark688">shark688</a>

 */
@Data
public class NewsVO implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     *  Mermaid Live图字符串
     */
    private String mermaidString;


    private static final long serialVersionUID = 1L;
}
