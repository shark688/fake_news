package com.zrd.springbootinit.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 文本检测结果类
 */
// AnalysisResult.java - 主结果类
@Data
public class ApiResponse  {

    private Integer code;
    private AnalysisResultResponse data; // 对应 JSON 中的 "data"
    private String message;

    @Data
    public class AnalysisResultResponse
    {
        private Collection collection;
        private Analysis analysis;
        private Conclusion conclusion;
    }

    // Collection.java - 报告集合
    @Data
    public class Collection {
        private List<Report> reports;
    }

    // Report.java - 单个报告
    @Data
    public class Report {
        private Integer id;
        @JsonProperty("importance_score")
        private Integer importanceScore;
        private String content;
    }

    // Analysis.java - 分析部分
    @Data
    public class Analysis {
        private List<Evaluation> evaluations;
        private List<String> consistency;
    }

    // Evaluation.java - 评估项
    @Data
    public class Evaluation {
        private Integer id;
        private String content;
    }

    // Conclusion.java - 结论部分
    @Data
    public class Conclusion {
        private String evidence;
        @JsonProperty("final_judgment")
        private String finalJudgment;
        @JsonProperty("news_statement")
        private String newsStatement;
    }
}
