package com.zrd.springbootinit.controller;

import com.zrd.springbootinit.common.BaseResponse;
import com.zrd.springbootinit.model.vo.NewsListVO;
import com.zrd.springbootinit.service.ResultsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 结果接口
 *
 * @author <a href="https://github.com/shark688">shark688</a>
  
 */
@RestController
@RequestMapping("/results")
@Slf4j
public class ResultsController {

    @Resource
    private ResultsService resultsService;


}
