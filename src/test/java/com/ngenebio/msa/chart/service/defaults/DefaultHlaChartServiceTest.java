package com.ngenebio.msa.chart.service.defaults;

import com.ngenebio.msa.chart.exception.result.RequestResultServiceFailedException;
import com.ngenebio.msa.chart.model.chart.hla.BaseVariationPlotChartData;
import com.ngenebio.msa.chart.model.chart.hla.CoveragePlotChartData;
import com.ngenebio.msa.chart.model.enumtype.ChartType;
import com.ngenebio.msa.chart.result.HlaResultServiceApi;
import com.ngenebio.msa.chart.selenium.ChartJavaScriptExecutorUtils;
import com.ngenebio.msa.chart.service.ChartServiceUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.JavascriptExecutor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("local")
class DefaultHlaChartServiceTest {
    @InjectMocks
    private DefaultHlaChartService hlaChartService;

    @Spy
    private ChartServiceUtils chartServiceUtils;

    @Spy
    private ChartJavaScriptExecutorUtils chartJavaScriptExecutorUtils;

    @Spy
    private HlaResultServiceApi hlaResultServiceApi;

    @DisplayName("BaseVariationPlot 차트 생성 성공 - base64")
    @Test
    void generateBaseVariationPlotTest() throws IOException {
        // given - temp directory
        var tempDirectoryPath = Paths.get(System.getProperty("java.io.tmpdir"),
                "ngenebio_chart_temp", UUID.randomUUID().toString());

        if (!Files.exists(tempDirectoryPath))
            Files.createDirectories(tempDirectoryPath);

        when(chartServiceUtils.getTempDirectory()).thenReturn(tempDirectoryPath);

        // given - chrome driver
        when(chartServiceUtils.createChromeDriver()).thenCallRealMethod();

        // given - generateRenderedChartHtmlFile
        var dummyPath = Paths.get("");
        doReturn(dummyPath).when(chartServiceUtils).generateRenderedChartHtmlFile(any(), any(), any());

        // given - create png image
        doReturn("").when(chartJavaScriptExecutorUtils).getChartPngImageBase64(any());

        var dataFilePath = Paths.get("src", "test", "resources",
                "data", "hla", "baseVariationPlotData.json");
        var json = new String(Files.readAllBytes(dataFilePath));

        var chartTypes = List.of(ChartType.BASE64);

        // when
        var result = hlaChartService.generateBaseVariationPlot(json, chartTypes);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBase64()).isNotNull();

        verify(chartServiceUtils, times(1)).createChromeDriver();
        verify(chartServiceUtils, times(1)).getTempDirectory();
        verify(chartServiceUtils, times(1)).generateRenderedChartHtmlFile(
                tempDirectoryPath,
                "chart/hla/hla-base-variation-plot.html",
                json
        );

        verify(chartJavaScriptExecutorUtils, times(1))
                .getChartPngImageBase64(any(JavascriptExecutor.class));
    }
    @DisplayName("BaseVariationPlot 차트 생성 성공 - runId, sampleId, gene, base64")
    @Test
    void generateBaseVariationPlot_runId_sampleId_geneTest() throws IOException, RequestResultServiceFailedException {
        // given - data
        var runId = "run001";
        var sampleId = "sample001";
        var gene = "A";
        var chartTypes = List.of(ChartType.BASE64);
        var dataFilePath = new ClassPathResource(
                "data/hla/baseVariationPlotData.json",
                this.getClass().getClassLoader());

        var chartDataJson = new String(Files.readAllBytes(dataFilePath.getFile().toPath()));
        var objectMapper = new ObjectMapper();
        var baseVariationPlotChartData = objectMapper.readValue(chartDataJson, BaseVariationPlotChartData.class);
        when(hlaResultServiceApi.getBaseVariationPlotChartData(runId, sampleId, gene))
                .thenReturn(baseVariationPlotChartData);

        // given - temp directory
        var tempDirectoryPath = Paths.get(System.getProperty("java.io.tmpdir"),
                "ngenebio_chart_temp", UUID.randomUUID().toString());

        if (!Files.exists(tempDirectoryPath))
            Files.createDirectories(tempDirectoryPath);

        when(chartServiceUtils.getTempDirectory()).thenReturn(tempDirectoryPath);

        // given - chrome driver
        when(chartServiceUtils.createChromeDriver()).thenCallRealMethod();

        // given - generateRenderedChartHtmlFile
        var dummyPath = Paths.get("");
        doReturn(dummyPath).when(chartServiceUtils).generateRenderedChartHtmlFile(
                eq(tempDirectoryPath),
                eq("chart/hla/hla-base-variation-plot.html"),
                any(String.class));


        // given - create png image
        doReturn("").when(chartJavaScriptExecutorUtils).getChartPngImageBase64(any());

        // when
        var result = hlaChartService.generateBaseVariationPlot(runId, sampleId, gene, chartTypes);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBase64()).isNotNull();

        verify(chartServiceUtils, times(1)).createChromeDriver();
        verify(chartServiceUtils, times(1)).getTempDirectory();
        verify(chartServiceUtils, times(1)).generateRenderedChartHtmlFile(
                eq(tempDirectoryPath),
                eq("chart/hla/hla-base-variation-plot.html"),
                any(String.class)
        );

        verify(chartJavaScriptExecutorUtils, times(1))
                .getChartPngImageBase64(any(JavascriptExecutor.class));
    }

    @DisplayName("Coverage Plot 차트 생성 성공 - base64")
    @Test
    void generateCoveragePlotTest() throws IOException {
        // given - temp directory
        var tempDirectoryPath = Paths.get(System.getProperty("java.io.tmpdir"),
                "ngenebio_chart_temp", UUID.randomUUID().toString());

        if (!Files.exists(tempDirectoryPath))
            Files.createDirectories(tempDirectoryPath);

        when(chartServiceUtils.getTempDirectory()).thenReturn(tempDirectoryPath);

        // given - chrome driver
        when(chartServiceUtils.createChromeDriver()).thenCallRealMethod();

        // given - generateRenderedChartHtmlFile
        var dummyPath = Paths.get("");
        doReturn(dummyPath).when(chartServiceUtils).generateRenderedChartHtmlFile(any(), any(), any());

        // given - create png image
        doReturn("").when(chartJavaScriptExecutorUtils).getChartPngImageBase64(any());

        var json = "{ \"dummyData\": \"coverage plot\" }";

        var chartTypes = List.of(ChartType.BASE64);

        // when
        var result = hlaChartService.generateCoveragePlot(json, chartTypes);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBase64()).isNotNull();

        verify(chartServiceUtils, times(1)).createChromeDriver();
        verify(chartServiceUtils, times(1)).getTempDirectory();
        verify(chartServiceUtils, times(1)).generateRenderedChartHtmlFile(
                tempDirectoryPath,
                "chart/hla/hla-coverage-plot.html",
                json
        );

        verify(chartJavaScriptExecutorUtils, times(1))
                .getChartPngImageBase64(any(JavascriptExecutor.class));
    }

    @DisplayName("Coverage Plot 차트 생성 성공 - runId, sampleId, gene, base64")
    @Test
    void generateCoveragePlot_runId_sampleId_geneTest() throws IOException, RequestResultServiceFailedException {
        // given - data
        var runId = "run001";
        var sampleId = "sample001";
        var gene = "A";
        var chartTypes = List.of(ChartType.BASE64);
        var dataFilePath = new ClassPathResource("data/hla/coveragePlotData.json",
                this.getClass().getClassLoader());

        var chartDataJson = new String(Files.readAllBytes(dataFilePath.getFile().toPath()));
        var objectMapper = new ObjectMapper();
        var coveragePlotChartData = objectMapper.readValue(chartDataJson, CoveragePlotChartData.class);
        when(hlaResultServiceApi.getCoveragePlotChartData(runId, sampleId, gene)).thenReturn(coveragePlotChartData);

        // given - temp directory
        var tempDirectoryPath = Paths.get(System.getProperty("java.io.tmpdir"),
                "ngenebio_chart_temp", UUID.randomUUID().toString());

        if (!Files.exists(tempDirectoryPath))
            Files.createDirectories(tempDirectoryPath);

        when(chartServiceUtils.getTempDirectory()).thenReturn(tempDirectoryPath);

        // given - chrome driver
        when(chartServiceUtils.createChromeDriver()).thenCallRealMethod();

        // given - generateRenderedChartHtmlFile
        var dummyPath = Paths.get("");
        doReturn(dummyPath).when(chartServiceUtils).generateRenderedChartHtmlFile(
                eq(tempDirectoryPath),
                eq("chart/hla/hla-coverage-plot.html"),
                any(String.class));


        // given - create png image
        doReturn("").when(chartJavaScriptExecutorUtils).getChartPngImageBase64(any());

        // when
        var result = hlaChartService.generateCoveragePlot(runId, sampleId, gene, chartTypes);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBase64()).isNotNull();

        verify(chartServiceUtils, times(1)).createChromeDriver();
        verify(chartServiceUtils, times(1)).getTempDirectory();
        verify(chartServiceUtils, times(1)).generateRenderedChartHtmlFile(
                eq(tempDirectoryPath),
                eq("chart/hla/hla-coverage-plot.html"),
                any(String.class)
        );

        verify(chartJavaScriptExecutorUtils, times(1))
                .getChartPngImageBase64(any(JavascriptExecutor.class));
    }
}