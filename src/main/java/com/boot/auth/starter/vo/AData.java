package com.boot.auth.starter.vo;

import java.util.Map;

public interface AData {

    Map<String, Object> getData();

    <T> T getData(String dataName);

    <T> T addData(String dataName, T dataValue);

    <T> Map<String, T> addAllData(Map<String, T> data);
}
