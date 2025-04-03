package com.zrd.springbootinit.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量删除请求
 *
 * @author <a href="https://github.com/shark688">shark688</a>
  
 */
@Data
public class DeleteBatchRequest implements Serializable {

    /**
     * id
     */
    private List<Long> ids;

    private static final long serialVersionUID = 1L;
}