package com.zrd.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrd.springbootinit.model.entity.Evidence;
import com.zrd.springbootinit.mapper.EvidenceMapper;
import com.zrd.springbootinit.service.EvidenceService;
import org.springframework.stereotype.Service;

/**
* @author 张瑞东
* @description 针对表【evidence(证据表)】的数据库操作Service实现
* @createDate 2025-03-25 21:51:26
*/
@Service
public class EvidenceServiceImpl extends ServiceImpl<EvidenceMapper, Evidence>
    implements EvidenceService{

}




