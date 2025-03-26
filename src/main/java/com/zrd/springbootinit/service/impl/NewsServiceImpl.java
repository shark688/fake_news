package com.zrd.springbootinit.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zrd.springbootinit.common.ErrorCode;
import com.zrd.springbootinit.exception.BusinessException;
import com.zrd.springbootinit.model.entity.*;
import com.zrd.springbootinit.model.vo.NewsVO;
import com.zrd.springbootinit.model.vo.ProcessResponse.Analysis;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrd.springbootinit.mapper.ResultsMapper;
import com.zrd.springbootinit.mapper.NewsMapper;
import com.zrd.springbootinit.model.enums.NewsStatusEnum;
import com.zrd.springbootinit.model.vo.NewsListVO;
import com.zrd.springbootinit.model.vo.ProcessResponse;
import com.zrd.springbootinit.service.AnalystreportsService;
import com.zrd.springbootinit.service.EvidenceService;
import com.zrd.springbootinit.service.NewsService;
import com.zrd.springbootinit.service.ResultsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 张瑞东
* @description 针对表【news(新闻)】的数据库操作Service实现
* @createDate 2025-03-25 21:51:26
*/
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News>
    implements NewsService{

    @Resource
    private ResultsService resultsService;

    @Resource
    private AnalystreportsService analystreportsService;

    @Resource
    private EvidenceService evidenceService;

    @Override
    public IPage<NewsListVO> getNewsPage(Integer page, Integer size, User loginUser) {

        Long currentUserId = loginUser.getId();

        // 创建分页对象
        Page<News> pageInfo = new Page<>(page, size);

        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", currentUserId)
                .orderByDesc("createTime");

        // 执行分页查询
        Page<News> newsPage = this.page(pageInfo, queryWrapper);

        // 转换为VO对象，并获取每个news对应的最新result的status
        return newsPage.convert(news -> {
            NewsListVO vo = new NewsListVO();
            vo.setId(news.getNewsId());
            vo.setTitle(news.getNewsTitle());

            // 转换为目标格式（2025-03-18 15:47:32）
            String formattedTime = DateUtil.format(news.getCreateTime(), "yyyy-MM-dd HH:mm:ss");

            vo.setCreateTime(formattedTime);

            // 查询该newsId对应的最新result记录的status
            QueryWrapper<Results> resultWrapper = new QueryWrapper<>();
            resultWrapper.eq("newsId", news.getNewsId())
                    .orderByDesc("createTime")
                    .last("LIMIT 1");
            Results latestResult = resultsService.getOne(resultWrapper);

            Integer statusCode = latestResult != null ? latestResult.getStatus() : null;
            vo.setStatus(NewsStatusEnum.getEnumByValue(statusCode).getText());
            return vo;
        });
    }

    /**
     * 检测新闻真伪性(单个)
     * @param newContent
     * @param loginUser
     * @return
     */
    @Override
    public boolean checkNew(String newContent, User loginUser) {

        Long userId = loginUser.getId();

        // 保存新闻信息
        News news = new News();
        news.setUserId(userId);
        news.setNewsContent(newContent);
        this.save(news);

        // 2. 保存验证结果
        Results results = new Results();
        results.setNewsId(news.getNewsId());
        results.setStatus(2);
        resultsService.save(results);


        // 1. 目标URL（本地8080端口）
//        String url = "http://localhost:5010/api/judge";
//
//        // 2. 构造JSON请求体
//        JSONObject jsonBody = new JSONObject();
//        jsonBody.set("text", "示例文本1");
//
//        // 3. 发送POST请求
//        String response = HttpRequest.post(url)
//                .header("Content-Type", "application/json") // 必须设置Content-Type
//                .body(jsonBody.toString()) // JSON字符串作为请求体
//                .execute()
//                .body();

        String response = "{\n" +
        "    \"result\": true,\n" +
                "    \"title\": \"世卫组织宣布新冠疫情全球卫生紧急状态结束\",\n" +
                "    \"summaryContent\": \"经核实，该消息与世界卫生组织官方声明一致，多方权威媒体均有报道。\",\n" +
                "    \"evidences\": [\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"evidence\": \"世界卫生组织官网2023年5月5日发布的正式声明\",\n" +
                "        \"score\": 100\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 2,\n" +
                "        \"evidence\": \"BBC、CNN等国际主流媒体的同步报道\",\n" +
                "        \"score\": 98\n" +
                "      }\n" +
                "    ],\n" +
                "    \"analyses\": [\n" +
                "      {\n" +
                "        \"analysis\": \"官方来源验证：直接匹配世卫组织官网声明\",\n" +
                "        \"relatedEvidence\": [1]\n" +
                "      },\n" +
                "      {\n" +
                "        \"analysis\": \"媒体一致性验证：多方权威媒体同步报道\",\n" +
                "        \"relatedEvidence\": [2]\n" +
                "      }\n" +
                "    ]\n" +
                "  }";

        ProcessResponse processResponse = JSONUtil.toBean(response, ProcessResponse.class);

        Boolean result = processResponse.getResult();
        String title = processResponse.getTitle();
        String summaryContent = processResponse.getSummaryContent();
        List<ProcessResponse.EvidenceVO> evidences = processResponse.getEvidences();
        List<Analysis> analyses = processResponse.getAnalyses();

        news.setNewsTitle(title);
        this.updateById(news);

        results.setStatus(result ? 1 : 0);
        results.setSummaryContent(summaryContent);
        results.setVerificationTime(new Date());
        resultsService.updateById(results);

        // 3. 保存分析报告
        for (ProcessResponse.Analysis analysis : analyses) {
            Analystreports report = new Analystreports();
            report.setResultId(results.getResultId());
            report.setReportContent(analysis.getAnalysis());
            analystreportsService.save(report);

            // 4. 保存证据
            List<Integer> relatedEvidenceIds = analysis.getRelatedEvidence();
            List<ProcessResponse.EvidenceVO> relatedEvidences = evidences.stream()
                    .filter(e -> relatedEvidenceIds.contains(e.getId()))
                    .collect(Collectors.toList());

            for (ProcessResponse.EvidenceVO evidence : relatedEvidences) {
                Evidence evidenceEntity = new Evidence();
                evidenceEntity.setReportId(report.getReportId());
                evidenceEntity.setEvidenceContent(evidence.getEvidence());
                evidenceEntity.setConfidenceLevel(evidence.getScore());
                evidenceService.save(evidenceEntity);
            }
        }

        return true;
    }

    /**
     * 根据id获取新闻VO
     * @param newsId
     * @param loginUser
     * @return
     */
    @Override
    public NewsVO getVOById(Integer newsId, User loginUser) {
        NewsVO newsVO = new NewsVO();

        // 1. 查询新闻基本信息
        News news = this.getById(newsId);
        if (news == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"找不到该新闻");
        }

        newsVO.setContent(news.getNewsContent());
        newsVO.setTitle(news.getNewsTitle());

        // 2. 查询验证结果和相关数据
        Results latestResult = resultsService.getOne(
                new LambdaQueryWrapper<Results>()
                        .eq(Results::getNewsId, newsId)
                        .orderByDesc(Results::getCreateTime)
                        .last("LIMIT 1")
        );

        if (latestResult == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"找不到最新的查询记录");
        }

        if (latestResult.getStatus() == 2) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"正在检测中,请耐心等待");
        }

        List<Analystreports> analystReports = analystreportsService.list(
                new LambdaQueryWrapper<Analystreports>()
                        .eq(Analystreports::getResultId, latestResult.getResultId())
        );

        List<Long> reportIds = analystReports.stream()
                .map(Analystreports::getReportId)
                .collect(Collectors.toList());

        List<Evidence> evidences = evidenceService.list(
                new LambdaQueryWrapper<Evidence>()
                        .in(Evidence::getReportId, reportIds)
        );

        Map<Long, List<Evidence>> evidencesByReportId = evidences.stream()
                .collect(Collectors.groupingBy(Evidence::getReportId));

        // 3. 构建Mermaid代码
        StringBuilder mermaidCode = new StringBuilder();
        mermaidCode.append("graph TD");

        // 新闻标题区块
        //mermaidCode.append("    %% 第一区块：新闻标题\n");
        mermaidCode.append("    A[\"<small>新闻标题</small><br>").append(escapeMermaidText(news.getNewsTitle())).append("\"] \n");
        mermaidCode.append("    style A fill:#FFD700,stroke:#FFA500,stroke-width:2px\n\n");

        // 证据与分析关系区块
        //mermaidCode.append("    %% 第二区块：证据与分析关系\n");
        mermaidCode.append("    B[\"证据与分析关系\"]\n");
        mermaidCode.append("    style B fill:#E8F5E9,stroke:#66BB6A,stroke-dasharray:5 5\n\n");

        // 添加证据节点(B节点)
        int evidenceCounter = 1;
        for (Evidence evidence : evidences) {
            mermaidCode.append("    B --> B").append(evidenceCounter)
                    .append("[\"").append(escapeMermaidText(evidence.getEvidenceContent()))
                    .append("\"]\n");
            evidenceCounter++;
        }
        mermaidCode.append("\n");

        // 添加分析报告节点(C节点)并建立关系
        int reportCounter = 1;
        for (Analystreports report : analystReports) {
            // 获取该报告对应的证据
            List<Evidence> relatedEvidences = evidencesByReportId.getOrDefault(report.getReportId(), Collections.emptyList());

            // 创建C节点
            mermaidCode.append("C").append(reportCounter)
                    .append("[\"").append(escapeMermaidText(report.getReportContent()))
                    .append("\"]\n");

            // 建立B到C的连接（通过相关证据）
            for (Evidence evidence : relatedEvidences) {
                int evidenceIndex = evidences.indexOf(evidence) + 1;
                mermaidCode.append("    B").append(evidenceIndex).append(" --> C").append(reportCounter).append("\n");
            }
            reportCounter++;
        }
        //mermaidCode.append("    style C fill:#E3F2FD,stroke:#42A5F5,stroke-width:1.8px");
        mermaidCode.append("\n");

        // 结论总结区块(D节点)
        //mermaidCode.append("    %% 第三区块：结论总结\n");
        mermaidCode.append("    D[\"\"\"结论总结\n");
        mermaidCode.append("    ✓ 验证结果：").append(getStatusText(latestResult.getStatus())).append("\n");
        mermaidCode.append("    📌 ").append(escapeMermaidText(latestResult.getSummaryContent())).append("\"\"\"]\n");
        mermaidCode.append("    style D fill:#FFF8E1,stroke:#FFCA28,stroke-width:2px,font-weight:600\n\n");

        // 建立C到D的连接
        for (int i = 1; i <= analystReports.size(); i++) {
            mermaidCode.append("    C").append(i).append(" --> D\n");
        }

        // 建立A到B的连接
        //mermaidCode.append("\n    %% 区块连接\n");
        mermaidCode.append("    A --> B\n");
        mermaidCode.append("   classDef title fill:#FFF3E0,stroke:#FFA726\n");
        mermaidCode.append("    classDef evidence fill:#E8F5E9,stroke:#66BB6A\n");
        mermaidCode.append("    classDef analysis fill:#F5F5F5,stroke:#9E9E9E\n");
        mermaidCode.append("    classDef conclusion fill:#FFF8E1,stroke:#FFCA28\n");
        mermaidCode.append("    class A title\n");
        mermaidCode.append("    class B,B1,B2,B3,B4,B5 evidence\n");
        mermaidCode.append("    class C1,C2,C3 analysis\n");
        mermaidCode.append("    class D conclusion\n");


        // 在返回前调用
        newsVO.setMermaidString(JSONUtil.toJsonStr(mermaidCode));
        System.out.println(mermaidCode.toString());
        return newsVO;

    }


    // 辅助方法：转义Mermaid特殊字符
    private String escapeMermaidText(String text) {
        if (text == null) return "";
        return text.replace("\"", "#quot;")
                .replace("\n", "<br>")
                .replace("\r", "");
    }

    // 辅助方法：获取状态文本
    private String getStatusText(Integer status) {
        switch (status) {
            case 0: return "假新闻";
            case 1: return "真新闻";
            default: return "未知状态";
        }
    }

    private String escapeForJson(String mermaidCode) {
        return mermaidCode.replace("\n", "<br>");
    }

}




