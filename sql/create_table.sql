# 数据库初始化
# @author <a href="https://github.com/shark688">shark688</a>


-- 创建库
create database if not exists fake_news;

-- 切换库
use fake_news;

-- 用户表
create table if not exists fake_news.user
(
    id          bigint auto_increment comment 'id'
        primary key,
    phoneNumber varchar(20)                        not null comment '手机号',
    userRole    tinyint  default 0                 not null comment '用户角色：0-user/1-admin/2-ban',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '用户' collate = utf8mb4_unicode_ci;


-- 新闻表
create table if not exists fake_news.news
(
    newsId      bigint auto_increment comment 'id'
        primary key,
    userId      bigint                             not null comment '创建新闻的用户id',
    newsTitle   varchar(20)                        null comment '新闻标题',
    newsContent TEXT                               not null comment '新闻完整内容',
    newsUrl    VARCHAR(500)                       null comment '新闻来源URL',
    isReverse   tinyint                            not null comment '是否为翻转新闻',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '新闻' collate = utf8mb4_unicode_ci;

-- 结果表
create table if not exists fake_news.results
(
    resultId      bigint auto_increment comment 'id'
        primary key,
    newsId        bigint                           not null comment '关联的新闻ID',
    status        tinyint                          not null comment '当前结果: 0-假, 1-真, 2-运行中, 3-运行失败',
    summaryContent text                            null comment '分析内容总结',
    verificationTime datetime default CURRENT_TIMESTAMP comment '验证时间',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '新闻验证结果主表' collate = utf8mb4_unicode_ci;

-- 分析报告表
create table if not exists fake_news.analystReports
(
    reportId      bigint auto_increment comment 'id'
        primary key,
    resultId      bigint                           not null comment '关联的验证结果ID',
    reportContent text                             not null comment '分析报告内容',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '分析报告表' collate = utf8mb4_unicode_ci;

-- 证据表
create table if not exists fake_news.evidence
(
    evidenceId      bigint auto_increment comment 'id'
        primary key,
    reportId      bigint                           not null comment '关联的分析ID',
    evidenceContent text                           not null comment '证据内容',
    confidenceLevel tinyint                        not null comment '置信度(0-100)',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '证据表' collate = utf8mb4_unicode_ci;


-- 新闻1: COVID-19疫苗有效性研究（真实）
INSERT INTO fake_news.news (newsId, userId, newsTitle, newsContent, newsUrl, isReverse, createTime, updateTime, isDelete) VALUES
    (1, 1, 'COVID-19疫苗有效性研究', '最新研究表明，COVID-19疫苗在预防重症方面有效性超过90%。该研究基于全球10万人的数据样本。', 'https://www.who.int/news-room/detail/2023-03-15-covid-vaccine-effectiveness', 0, '2023-03-15 09:00:00', '2023-03-15 09:00:00', 0);

INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (1, 1, 1, '经核实，该新闻内容与世界卫生组织公布数据一致，为真实信息。', '2023-03-15 14:30:00', '2023-03-15 14:30:00', '2023-03-15 14:30:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (1, 1, '通过比对WHO官方数据和多家权威医学期刊，确认疫苗有效性数据准确。', '2023-03-15 14:35:00', '2023-03-15 14:35:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
    (1, 1, 'WHO官网2023年3月报告', 95, '2023-03-15 14:40:00', '2023-03-15 14:40:00', 0);

-- 新闻2: 气候变化是骗局（虚假）
INSERT INTO fake_news.news (newsId, userId, newsTitle, newsContent, newsUrl, isReverse, createTime, updateTime, isDelete) VALUES
    (2, 3, '科学家承认气候变化是骗局', '一篇声称科学家承认气候变化是骗局的虚假文章在社交媒体流传。', 'https://fake-news-site.com/climate-hoax', 1, '2023-04-01 12:00:00', '2023-04-01 12:00:00', 0);

INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (2, 2, 0, '该新闻与科学共识相悖，NASA等权威机构已多次驳斥类似说法。', '2023-04-01 15:20:00', '2023-04-01 15:20:00', '2023-04-01 15:20:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (2, 2, '核查发现该网站以传播虚假信息闻名，引用来源不可靠。', '2023-04-01 15:25:00', '2023-04-01 15:25:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
    (2, 2, 'NASA气候变化数据页面', 98, '2023-04-01 15:30:00', '2023-04-01 15:30:00', 0);

-- 新闻3: 美国通过新基础设施法案（真实）
INSERT INTO fake_news.news (newsId, userId, newsTitle, newsContent, newsUrl, isReverse, createTime, updateTime, isDelete) VALUES
    (3, 2, '美国通过1.2万亿美元基础设施法案', '美国国会通过了历史性的1.2万亿美元基础设施投资法案，将用于道路、桥梁和宽带建设。', 'https://www.nytimes.com/2023/04/15/us/politics/infrastructure-bill-passed.html', 0, '2023-04-15 08:45:00', '2023-04-15 08:45:00', 0);

INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (3, 3, 1, '纽约时报等主流媒体均有报道，白宫官网发布正式声明。', '2023-04-15 12:10:00', '2023-04-15 12:10:00', '2023-04-15 12:10:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (3, 3, '核查国会投票记录和白宫官方声明，确认信息准确。', '2023-04-15 12:15:00', '2023-04-15 12:15:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
    (3, 3, '白宫官网声明文件', 97, '2023-04-15 12:20:00', '2023-04-15 12:20:00', 0);

-- 新闻4: 喝漂白剂治疗新冠（虚假危险信息）
INSERT INTO fake_news.news (newsId, userId, newsTitle, newsContent, newsUrl, isReverse, createTime, updateTime, isDelete) VALUES
    (4, 3, '医生推荐喝漂白剂预防新冠', '网络流传某医生推荐饮用稀释漂白剂预防新冠病毒的文章。', 'https://health-scam.com/bleach-covid', 1, '2023-05-10 18:30:00', '2023-05-10 18:30:00', 0);

INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (4, 4, 0, '医学专家警告这是危险错误信息，饮用漂白剂可能致命。', '2023-05-10 20:15:00', '2023-05-10 20:15:00', '2023-05-10 20:15:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (4, 4, '美国FDA已发布警告，该网站多次传播危险医疗建议。', '2023-05-10 20:20:00', '2023-05-10 20:20:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
    (4, 4, 'FDA警告声明编号2023-045', 99, '2023-05-10 20:25:00', '2023-05-10 20:25:00', 0);

-- 新闻5: AI突破蛋白质折叠难题（真实科技）
INSERT INTO fake_news.news (newsId, userId, newsTitle, newsContent, newsUrl, isReverse, createTime, updateTime, isDelete) VALUES
    (5, 1, 'DeepMind解决蛋白质折叠世纪难题', 'DeepMind的AlphaFold成功预测蛋白质3D结构，解决困扰生物学界50年的难题。', 'https://www.nature.com/articles/d41586-023-00872-w', 0, '2023-06-01 07:00:00', '2023-06-01 07:00:00', 0);

INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (5, 5, 1, '《自然》杂志发表同行评议论文确认该突破。', '2023-06-01 11:30:00', '2023-06-01 11:30:00', '2023-06-01 11:30:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (5, 5, '核查Nature论文和独立实验室验证结果，突破属实。', '2023-06-01 11:35:00', '2023-06-01 11:35:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
    (5, 5, 'Nature期刊2023年6月论文', 96, '2023-06-01 11:40:00', '2023-06-01 11:40:00', 0);

-- 新闻6: 某明星死亡谣言（虚假）
INSERT INTO fake_news.news (newsId, userId, newsTitle, newsContent, newsUrl, isReverse, createTime, updateTime, isDelete) VALUES
    (6, 2, '著名演员汤姆·克鲁斯意外去世', '网络疯传汤姆·克鲁斯在新西兰拍摄时发生意外身亡。', 'https://celebrity-gossip.net/tom-crash', 1, '2023-07-15 22:00:00', '2023-07-15 22:00:00', 0);

INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (6, 6, 0, '汤姆·克鲁斯本人社交媒体发布视频证明健在，经纪公司已发声明。', '2023-07-16 08:45:00', '2023-07-16 08:45:00', '2023-07-16 08:45:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (6, 6, '该谣言网站有多次编造假新闻的历史，IP追踪显示与某竞争对手有关。', '2023-07-16 08:50:00', '2023-07-16 08:50:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
    (6, 6, '汤姆·克鲁斯Instagram更新视频', 100, '2023-07-16 08:55:00', '2023-07-16 08:55:00', 0);

-- 新闻7: 中国经济季度增长数据（真实）
INSERT INTO fake_news.news (newsId, userId, newsTitle, newsContent, newsUrl, isReverse, createTime, updateTime, isDelete) VALUES
    (7, 1, '中国第二季度GDP增长5.5%', '国家统计局数据显示，中国第二季度GDP同比增长5.5%，超出市场预期。', 'https://www.stats.gov.cn/english/PressRelease/202307/t20230717_1907322.html', 0, '2023-07-17 10:00:00', '2023-07-17 10:00:00', 0);

INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (7, 7, 1, '数据与国家统计局官网发布一致，国际媒体均有报道。', '2023-07-17 14:20:00', '2023-07-17 14:20:00', '2023-07-17 14:20:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (7, 7, '比对统计局原始数据和多国媒体报道，确认数据准确性。', '2023-07-17 14:25:00', '2023-07-17 14:25:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
    (7, 7, '国家统计局2023年7月17日新闻稿', 97, '2023-07-17 14:30:00', '2023-07-17 14:30:00', 0);

-- 新闻8: 早期疫情报道后被反转（isReverse=1）
INSERT INTO fake_news.news (newsId, userId, newsTitle, newsContent, newsUrl, isReverse, createTime, updateTime, isDelete) VALUES
    (8, 2, '新冠病毒不会人传人', '早期卫生部门称新型冠状病毒不会人传人。', 'https://www.earlynews.com/2020-01/ncov-no-human-transmission', 1, '2023-08-05 09:00:00', '2023-08-05 09:00:00', 0);

INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (8, 8, 0, '该早期判断已被后续科学研究推翻，WHO已确认新冠病毒人传人。', '2023-08-05 13:40:00', '2023-08-05 13:40:00', '2023-08-05 13:40:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (8, 8, '这是早期认知局限导致的错误判断，该新闻应标记为已反转。', '2023-08-05 13:45:00', '2023-08-05 13:45:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
    (8, 8, 'WHO 2020年1月22日紧急委员会会议记录', 90, '2023-08-05 13:50:00', '2023-08-05 13:50:00', 0);

-- 新闻9: 土耳其地震灾情（真实）
INSERT INTO fake_news.news (newsId, userId, newsTitle, newsContent, newsUrl, isReverse, createTime, updateTime, isDelete) VALUES
    (9, 3, '土耳其7.8级地震致万人遇难', '土耳其东南部发生7.8级强震，造成超过1.7万人死亡，多国派遣救援队。', 'https://www.bbc.com/news/world-europe-64533851', 0, '2023-02-06 05:00:00', '2023-02-06 05:00:00', 0);

INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (9, 9, 1, '联合国灾害评估报告确认地震规模和伤亡数据基本准确。', '2023-02-06 12:30:00', '2023-02-06 12:30:00', '2023-02-06 12:30:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (9, 9, '比对多国地震监测数据和土耳其官方通报，灾情报道属实。', '2023-02-06 12:35:00', '2023-02-06 12:35:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
    (9, 9, '美国地质调查局地震数据', 98, '2023-02-06 12:40:00', '2023-02-06 12:40:00', 0);

-- 新闻10: 神奇减肥产品广告（虚假）
INSERT INTO fake_news.news (newsId, userId, newsTitle, newsContent, newsUrl, isReverse, createTime, updateTime, isDelete) VALUES
    (10, 1, '一个月瘦30斤的神奇减肥茶', '某品牌减肥茶广告声称无需运动饮食，一个月可减重30斤，多位明星代言。', 'https://miracle-tea.com/lose-weight-fast', 1, '2023-09-20 14:00:00', '2023-09-20 14:00:00', 0);

INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (10, 10, 0, '经查证，所谓明星代言图片系PS合成，产品无任何科学依据。', '2023-09-20 18:15:00', '2023-09-20 18:15:00', '2023-09-20 18:15:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (10, 10, '该网站使用虚假前后对比图，多名被冒用肖像的明星已发律师函。', '2023-09-20 18:20:00', '2023-09-20 18:20:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
    (10, 10, 'FDA未批准该产品作为减肥药物的文件', 97, '2023-09-20 18:25:00', '2023-09-20 18:25:00', 0);



-- 新闻11: 早期关于MH370航班发现残骸的误报（后被反转）
INSERT INTO fake_news.news (newsId, userId, newsTitle, newsContent, newsUrl, isReverse, createTime, updateTime, isDelete) VALUES
    (11, 2, 'MH370航班残骸在柬埔寨丛林发现', '卫星图像专家称在柬埔寨丛林发现疑似MH370航班残骸，多国媒体转载报道。', 'https://aviation-news.net/mh370-found-cambodia', 1, '2023-08-10 07:30:00', '2023-08-10 07:30:00', 0);

-- 第一次验证（初始状态：运行中）
INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (11, 11, 2, '正在协调卫星图像分析专家进行核实', '2023-08-10 10:00:00', '2023-08-10 10:00:00', '2023-08-10 10:00:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (11, 11, '初步分析卫星图像存在疑似飞机形状物体，需实地核查', '2023-08-10 10:05:00', '2023-08-10 10:05:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
    (11, 11, '商业卫星公司提供的原始图像', 70, '2023-08-10 10:10:00', '2023-08-10 10:10:00', 0);

-- 第二次验证（误判为真）
INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (12, 11, 1, '初步确认图像特征与波音777机型匹配', '2023-08-11 09:15:00', '2023-08-11 09:15:00', '2023-08-11 09:15:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (12, 12, '三位航空专家独立分析认为图像可信度较高', '2023-08-11 09:20:00', '2023-08-11 09:20:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
    (12, 12, '航空器识别专家签字确认文件', 80, '2023-08-11 09:25:00', '2023-08-11 09:25:00', 0);

-- 第三次验证（最终反转结论）
INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (13, 11, 0, '实地考察确认是地形错觉，马来西亚民航局正式辟谣', '2023-08-15 14:00:00', '2023-08-15 14:00:00', '2023-08-15 14:00:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (13, 13, '联合考察队现场拍摄照片显示所谓"残骸"实为岩石地貌', '2023-08-15 14:05:00', '2023-08-15 14:05:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
                                                                                                                              (13, 13, '马来西亚民航局2023年8月15日官方声明', 95, '2023-08-15 14:10:00', '2023-08-15 14:10:00', 0),
                                                                                                                              (14, 13, '实地考察4K视频证据', 98, '2023-08-15 14:15:00', '2023-08-15 14:15:00', 0);

-- 新闻12: 某科技公司新产品功能误报（后被纠正）
INSERT INTO fake_news.news (newsId, userId, newsTitle, newsContent, newsUrl, isReverse, createTime, updateTime, isDelete) VALUES
    (12, 1, '苹果iPhone15将搭载屏下指纹识别', '多个科技媒体报道苹果将在iPhone15系列重新引入Touch ID屏下指纹技术。', 'https://tech-rumors.com/apple-touchid-return', 1, '2023-09-05 08:00:00', '2023-09-05 08:00:00', 0);

-- 第一次验证（供应链消息）
INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (14, 12, 2, '正在核实供应链消息来源可靠性', '2023-09-05 11:30:00', '2023-09-05 11:30:00', '2023-09-05 11:30:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (14, 14, '两家亚洲供应商透露收到指纹模组样品需求', '2023-09-05 11:35:00', '2023-09-05 11:35:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
    (15, 14, '供应链分析师内部备忘录', 65, '2023-09-05 11:40:00', '2023-09-05 11:40:00', 0);

-- 第二次验证（误判为真）
INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (15, 12, 1, '多个独立消息源确认测试样机存在该功能', '2023-09-08 15:20:00', '2023-09-08 15:20:00', '2023-09-08 15:20:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (15, 15, '彭博社报道与早期供应链消息相互印证', '2023-09-08 15:25:00', '2023-09-08 15:25:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
    (16, 15, '彭博社2023年9月8日报道截图', 75, '2023-09-08 15:30:00', '2023-09-08 15:30:00', 0);

-- 第三次验证（最终反转）
INSERT INTO fake_news.results (resultId, newsId, status, summaryContent, verificationTime, createTime, updateTime, isDelete) VALUES
    (16, 12, 0, '苹果发布会确认未采用该技术，早期消息系供应链测试误导', '2023-09-12 20:00:00', '2023-09-12 20:00:00', '2023-09-12 20:00:00', 0);

INSERT INTO fake_news.analystReports (reportId, resultId, reportContent, createTime, updateTime, isDelete) VALUES
    (16, 16, '苹果高级副总裁澄清该技术仅为供应商自行测试', '2023-09-12 20:05:00', '2023-09-12 20:05:00', 0);

INSERT INTO fake_news.evidence (evidenceId, reportId, evidenceContent, confidenceLevel, createTime, updateTime, isDelete) VALUES
                                                                                                                              (17, 16, '苹果秋季发布会完整视频(2:15:30处)', 99, '2023-09-12 20:10:00', '2023-09-12 20:10:00', 0),
                                                                                                                              (18, 16, '苹果官方技术规格页面', 100, '2023-09-12 20:15:00', '2023-09-12 20:15:00', 0);