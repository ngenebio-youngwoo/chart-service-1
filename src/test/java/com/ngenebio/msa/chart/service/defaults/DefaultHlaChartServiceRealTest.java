package com.ngenebio.msa.chart.service.defaults;

import com.ngenebio.msa.chart.configuration.properties.ChartServiceConfiguration;
import com.ngenebio.msa.chart.exception.result.RequestResultServiceFailedException;
import com.ngenebio.msa.chart.model.ChartResult;
import com.ngenebio.msa.chart.model.chart.hla.BaseVariationPlotChartData;
import com.ngenebio.msa.chart.model.enumtype.ChartType;
import com.ngenebio.msa.chart.result.HlaResultServiceApi;
import com.ngenebio.msa.chart.selenium.ChartJavaScriptExecutorUtils;
import com.ngenebio.msa.chart.service.ChartServiceUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("dev")
public class DefaultHlaChartServiceRealTest {
    private DefaultHlaChartService hlaChartService;

    @Mock
    private HlaResultServiceApi hlaResultServiceApi;

    @Mock
    private ChartServiceConfiguration chartServiceConfiguration;

    private ChartServiceUtils chartServiceUtils;

    private final ChartJavaScriptExecutorUtils chartJavaScriptExecutorUtils = new ChartJavaScriptExecutorUtils();

    @DisplayName("통합테스트 - base coverage plot 차트 생성 성공")
    @Test
    public void baseVariationPlotTest() throws IOException, RequestResultServiceFailedException {
        // given - service
        when(chartServiceConfiguration.getSeleniumServiceUrl()).thenReturn("http://192.168.1.30:4444/wd/hub");
        chartServiceUtils = new ChartServiceUtils(chartServiceConfiguration);

        hlaChartService = new DefaultHlaChartService(
                hlaResultServiceApi,
                chartServiceUtils,
                chartJavaScriptExecutorUtils
        );

        // given
        var runId = "";
        var sampleId = "";
        var gene = "";
        var chartTypes = List.of(ChartType.BASE64);

        // given
        var dataResource = new ClassPathResource(
                "data/hla/baseVariationPlotData.json", this.getClass().getClassLoader());
        var dataJson = new String(Files.readAllBytes(dataResource.getFile().toPath()));

        var baseVariationPlotChartData = new ObjectMapper().readValue(dataJson, BaseVariationPlotChartData.class);
        when(hlaResultServiceApi.getBaseVariationPlotChartData(runId, sampleId, gene))
                .thenReturn(baseVariationPlotChartData);

        // when
        ChartResult chartResult = hlaChartService.generateBaseVariationPlot(runId, sampleId, gene, chartTypes);

        // then
        assertThat(chartResult).isNotNull();
        assertThat(chartResult.getBase64().length()).isGreaterThan(10000);
    }
}
