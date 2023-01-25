package com.ngenebio.msa.chart.controller;

import com.ngenebio.msa.chart.model.ChartResult;
import com.ngenebio.msa.chart.model.ResponseDto;
import com.ngenebio.msa.chart.model.enumtype.ChartType;
import com.ngenebio.msa.chart.service.HlaChartService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chart/hla")
@RequiredArgsConstructor
public class HlaChartController {
    @Qualifier("defaultHlaChartService")
    private final HlaChartService hlaChartService;

    @GetMapping("/base-variation-plot")
    public ResponseEntity<Resource> generateBaseVariationPlotChart() {
        // TODO: 차트 생성 서비스 호출
        throw new NotImplementedException();
    }

    @GetMapping("/coverage-plot")
    public ResponseEntity<ResponseDto<ChartResult>> generateCoveragePlotChart(
            @RequestParam String runId,
            @RequestParam String sampleId,
            @RequestParam String gene,
            @RequestParam List<ChartType> chartTypes
    ) throws IOException {
        var responseDto = new ResponseDto<ChartResult>();

        ChartResult chartResult = hlaChartService.generateCoveragePlot(runId, sampleId, gene, chartTypes);
        responseDto.setData(chartResult);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDto);
    }
}
