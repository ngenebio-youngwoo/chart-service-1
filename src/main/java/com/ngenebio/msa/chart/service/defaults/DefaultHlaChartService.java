package com.ngenebio.msa.chart.service.defaults;

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
    public ChartResult generateBaseVariationPlot(String json, List<ChartType> chartTypes) throws IOException {
        Path tempDirectoryPath = null;
        WebDriver chromeDriver = null;
        final var chartHtmlFileResourcePath = "chart/hla/hla-base-variation-plot.html";

        try {
            tempDirectoryPath = chartServiceUtils.getTempDirectory();
            Path chartHtmlFilePath = chartServiceUtils.generateRenderedChartHtmlFile(tempDirectoryPath, chartHtmlFileResourcePath, json);

            chromeDriver = chartServiceUtils.createChromeDriver();

            var javaScriptExecutor = (JavascriptExecutor) chromeDriver;
            chromeDriver.get("file://" + chartHtmlFilePath.toFile().getAbsolutePath());

            var result = new ChartResult();
            for (var chartType : chartTypes) {
                switch (chartType) {
                    case BASE64 -> {
                        result.setBase64(chartJavaScriptExecutorUtils.getChartPngImageBase64(javaScriptExecutor));
                    }
                }
            }
            return result;
        } catch(Exception exception) {
            throw exception;
        } finally {
            if (chromeDriver != null) chromeDriver.close();

            chartServiceUtils.removeDirectory(tempDirectoryPath);
        }
    }

    @Override
    public ChartResult generateCoveragePlot(String json, List<ChartType> chartTypes) throws IOException {
        Path tempDirectoryPath = null;
        WebDriver chromeDriver = null;
        final var chartHtmlFileResourcePath = "chart/hla/hla-coverage-plot.html";

        try {
            tempDirectoryPath = chartServiceUtils.getTempDirectory();
            Path chartHtmlFilePath = chartServiceUtils.generateRenderedChartHtmlFile(tempDirectoryPath, chartHtmlFileResourcePath, json);

            chromeDriver = chartServiceUtils.createChromeDriver();

            var javaScriptExecutor = (JavascriptExecutor) chromeDriver;
            chromeDriver.get("file://" + chartHtmlFilePath.toFile().getAbsolutePath());

            var result = new ChartResult();
            for (var chartType : chartTypes) {
                switch (chartType) {
                    case BASE64 -> {
                        result.setBase64(chartJavaScriptExecutorUtils.getChartPngImageBase64(javaScriptExecutor));
                    }
                }
            }
            return result;
        } catch(Exception exception) {
            throw exception;
        } finally {
            if (chromeDriver != null) chromeDriver.close();

            chartServiceUtils.removeDirectory(tempDirectoryPath);
        }
    }

    @Override
    public ChartResult generateCoveragePlot(String runId, String sampleId, String gene, List<ChartType> chartTypes) throws IOException {
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
                    case BASE64 -> {
                        result.setBase64(chartJavaScriptExecutorUtils.getChartPngImageBase64(javaScriptExecutor));
                    }
                }
            }
            return result;
        } catch(Exception exception) {
            throw exception;
        } finally {
            if (chromeDriver != null) chromeDriver.close();

            chartServiceUtils.removeDirectory(tempDirectoryPath);
        }
    }
}
