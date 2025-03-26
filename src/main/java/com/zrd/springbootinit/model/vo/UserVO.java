package com.zrd.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图（脱敏）
 *
 * @author <a href="https://github.com/shark688">shark688</a>
  
 */
@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    private Long id;

    private String phoneNumber;

    /**
     * 用户角色：user/admin/ban
     */
    private Integer userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}