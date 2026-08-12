package com.openplatform.open.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.openplatform.open.mapper.ApiLogMapper;
import com.openplatform.open.service.StatisticsService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final ApiLogMapper apiLogMapper;

    @Override
    public List<Map<String, Object>> statByApi() {
        return apiLogMapper.statByApi().stream().map(m -> (Map<String, Object>) m).toList();
    }

    @Override
    public List<Map<String, Object>> statByApp() {
        return apiLogMapper.statByApp().stream().map(m -> (Map<String, Object>) m).toList();
    }

    @Override
    public List<Map<String, Object>> statTrend(String startDate, String endDate) {
        return apiLogMapper.statTrend(startDate, endDate).stream().map(m -> (Map<String, Object>) m).toList();
    }
}