package com.openplatform.open.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.openplatform.open.service.StatisticsService;

import java.util.List;
import java.util.Map;

@Tag(name = "调用统计")
@RestController
@RequiredArgsConstructor
@RequestMapping("/open-platform/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "按 API 统计", description = "按 API 维度统计调用次数、成功率、平均耗时")
    @SaCheckPermission("open-platform:statistics:view")
    @GetMapping("/api")
    public List<Map<String, Object>> statByApi() {
        return statisticsService.statByApi();
    }

    @Operation(summary = "按应用统计", description = "按应用维度统计调用次数、成功率、平均耗时")
    @SaCheckPermission("open-platform:statistics:view")
    @GetMapping("/app")
    public List<Map<String, Object>> statByApp() {
        return statisticsService.statByApp();
    }

    @Operation(summary = "趋势统计", description = "按时间维度统计调用趋势")
    @SaCheckPermission("open-platform:statistics:view")
    @GetMapping("/trend")
    public List<Map<String, Object>> statTrend(@RequestParam(required = false) String startDate,
                                                @RequestParam(required = false) String endDate) {
        return statisticsService.statTrend(startDate, endDate);
    }
}