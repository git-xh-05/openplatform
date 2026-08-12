package com.openplatform.open.service;

import java.util.List;
import java.util.Map;

public interface StatisticsService {

    List<Map<String, Object>> statByApi();

    List<Map<String, Object>> statByApp();

    List<Map<String, Object>> statTrend(String startDate, String endDate);
}