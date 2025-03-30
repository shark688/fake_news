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