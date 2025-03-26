package com.zrd.springbootinit.model.dto.news;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 判断新闻请求
 *
 * @author <a href="https://github.com/shark688">shark688</a>
  
 */
@Data
public class NewsCheckRequest implements Serializable {

    /**
     * 检测新闻
     */
    private String newContent;


    private static final long serialVersionUID = 1L;
}