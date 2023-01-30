package com.ngenebio.msa.chart.service.defaults;

import com.ngenebio.msa.chart.exception.result.RequestResultServiceFailedException;
import com.ngenebio.msa.chart.model.ChartResult;
import com.ngenebio.msa.chart.model.enumtype.ChartType;
import com.ngenebio.msa.chart.result.HlaResultServiceApi;
import com.ngenebio.msa.chart.selenium.ChartJavaScriptExecutorUtils;
import com.ngenebio.msa.chart.service.ChartServiceUtils;
import com.ngenebio.msa.chart.service.HlaChartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultHlaChartService implements HlaChartService {
    @Qualifier("defaultHlaResultServiceApi")
    private final HlaResultServiceApi hlaResultServiceApi;

    private final ChartServiceUtils chartServiceUtils;
    private final ChartJavaScriptExecutorUtils chartJavaScriptExecutorUtils;

    @Override
    public ChartResult generateBaseVariationPlot(
            String runId,
            String sampleId,
            String gene,
            List<ChartType> chartTypes
    ) throws IOException, RequestResultServiceFailedException {
        Path tempDirectoryPath = null;
        WebDriver chromeDriver = null;
        final var chartHtmlFileResourcePath = "chart/hla/hla-base-variation-plot.html";

        try {
            var baseVariationPlotChartData = hlaResultServiceApi.getBaseVariationPlotChartData(runId, sampleId, gene);

            tempDirectoryPath = chartServiceUtils.getTempDirectory();
            Path chartHtmlFilePath = chartServiceUtils.generateRenderedChartHtmlFile(
                    tempDirectoryPath,
                    chartHtmlFileResourcePath,
                    chartServiceUtils.toJsonString(baseVariationPlotChartData)
            );

            chromeDriver = chartServiceUtils.createChromeDriver();

            var javaScriptExecutor = (JavascriptExecutor) chromeDriver;

            // local chrome driver에 html 물리경로를 지정하여 호출하는 코드
//            chromeDriver.get("file://" + chartHtmlFilePath.toFile().getAbsolutePath());

            // remote selenium에 html문서내용을 url로 전달하여 로드하는 코드
            var htmlDataUrlString = chartServiceUtils.convertChartHtmlToDataHtmlUrlString(chartHtmlFilePath);
            chromeDriver.get("data:text/html;charset=UTF-8," + htmlDataUrlString);

            var result = new ChartResult();
            for (var chartType : chartTypes) {
                switch (chartType) {
                    case BASE64 -> result.setBase64(chartJavaScriptExecutorUtils.getChartPngImageBase64(javaScriptExecutor));
                }
            }
            return result;
        } finally {
            if (chromeDriver != null) {
                chromeDriver.close();
                chromeDriver.quit();
            }

            chartServiceUtils.removeDirectory(tempDirectoryPath);
        }
    }

    @Override
    public ChartResult generateCoveragePlot(String runId, String sampleId, String gene, List<ChartType> chartTypes) throws IOException, RequestResultServiceFailedException {
        Path tempDirectoryPath = null;
        WebDriver chromeDriver = null;
        final var chartHtmlFileResourcePath = "chart/hla/hla-coverage-plot.html";

        try {
            var coveragePlotChartData = hlaResultServiceApi.getCoveragePlotChartData(runId, sampleId, gene);

            tempDirectoryPath = chartServiceUtils.getTempDirectory();
            Path chartHtmlFilePath = chartServiceUtils.generateRenderedChartHtmlFile(
                    tempDirectoryPath,
                    chartHtmlFileResourcePath,
                    chartServiceUtils.toJsonString(coveragePlotChartData)
            );

            chromeDriver = chartServiceUtils.createChromeDriver();

            var javaScriptExecutor = (JavascriptExecutor) chromeDriver;
            chromeDriver.get("file://" + chartHtmlFilePath.toFile().getAbsolutePath());

            var result = new ChartResult();
            for (var chartType : chartTypes) {
                switch (chartType) {
                    case BASE64 -> result.setBase64(chartJavaScriptExecutorUtils.getChartPngImageBase64(javaScriptExecutor));
                }
            }
            return result;
        } finally {
            if (chromeDriver != null) chromeDriver.close();

            chartServiceUtils.removeDirectory(tempDirectoryPath);
        }
    }
}
