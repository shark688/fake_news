package com.zrd.springbootinit.model.vo;

import lombok.Data;
import java.util.List;

/**
 * 文本检测结果类
 */
@Data
public class ProcessResponse {
    private Boolean result;
    private String title;
    private String summaryContent;
    private List<EvidenceVO> evidences;
    private List<Analysis> analyses;

    // 内部类：Evidence
    @Data
    public static class EvidenceVO {
        private Integer id;
        private String evidence;
        private Integer score;
    }

    // 内部类：Analysis
    @Data
    public static class Analysis {
        private String analysis;
        private List<Integer> relatedEvidence;
    }
}