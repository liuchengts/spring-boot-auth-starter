package com.boot.auth.starter.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 打包信息返回客户端。
 */
public interface PackVo<T extends Serializable> {

    T getVo();

    void setVo(T vo);

    Map<String, Object> getAttribute();

    void setAttribute(Map<String, Object> attribute);

    Map<String, Object> addAttribute(String attributeName, Object attributeValue);

    List<T> getVoList();

    void setVoList(List<T> voList);

}
