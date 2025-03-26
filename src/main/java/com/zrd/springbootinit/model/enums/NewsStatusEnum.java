package com.zrd.springbootinit.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 新闻结果状态枚举
 *
 * @author <a href="https://github.com/shark688">shark688</a>
  
 */
public enum NewsStatusEnum {

    False("假", 0),
    True("真", 1),
    Wait("运行中",2),
    Error("运行失败", 3),
    REVERSE("结果翻转",4);

    private final String text;

    private final Integer value;

    NewsStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static NewsStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (NewsStatusEnum anEnum : NewsStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
