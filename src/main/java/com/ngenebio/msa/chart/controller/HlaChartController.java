package com.ngenebio.msa.chart.controller;

import com.ngenebio.msa.chart.exception.result.RequestResultServiceFailedException;
import com.ngenebio.msa.chart.model.ChartResult;
import com.ngenebio.msa.chart.model.ResponseDto;
import com.ngenebio.msa.chart.model.enumtype.ChartType;
import com.ngenebio.msa.chart.service.HlaChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/chart/hla")
@RequiredArgsConstructor
public class HlaChartController {
    @Qualifier("defaultHlaChartService")
    private final HlaChartService hlaChartService;

    @GetMapping("/base-variation-plot")
    public ResponseEntity<ResponseDto<ChartResult>> generateBaseVariationPlotChart(
            @RequestParam("runId") String runId,
            @RequestParam("sampleId") String sampleId,
            @RequestParam("gene") String gene,
            @RequestParam("chartTypes") List<ChartType> chartTypes
    ) throws IOException, RequestResultServiceFailedException {
        var responseDto = new ResponseDto<ChartResult>();

        ChartResult chartResult = hlaChartService.generateBaseVariationPlot(runId, sampleId, gene, chartTypes);
        responseDto.setData(chartResult);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDto);
    }

    @GetMapping("/coverage-plot")
    public ResponseEntity<ResponseDto<ChartResult>> generateCoveragePlotChart(
            @RequestParam("runId") String runId,
            @RequestParam("sampleId") String sampleId,
            @RequestParam("gene") String gene,
            @RequestParam("chartTypes") List<ChartType> chartTypes
    ) throws IOException, RequestResultServiceFailedException {
        var responseDto = new ResponseDto<ChartResult>();

        ChartResult chartResult = hlaChartService.generateCoveragePlot(runId, sampleId, gene, chartTypes);
        responseDto.setData(chartResult);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDto);
    }
}
