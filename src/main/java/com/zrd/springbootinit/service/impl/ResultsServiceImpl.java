package com.zrd.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrd.springbootinit.model.entity.Results;
import com.zrd.springbootinit.mapper.ResultsMapper;
import com.zrd.springbootinit.service.ResultsService;
import org.springframework.stereotype.Service;

/**
* @author 张瑞东
* @description 针对表【results(新闻验证结果主表)】的数据库操作Service实现
* @createDate 2025-03-25 21:51:26
*/
@Service
public class ResultsServiceImpl extends ServiceImpl<ResultsMapper, Results>
    implements ResultsService{

}




