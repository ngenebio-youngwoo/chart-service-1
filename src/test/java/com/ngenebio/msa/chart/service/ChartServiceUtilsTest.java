package com.ngenebio.msa.chart.service;

import com.ngenebio.msa.chart.configuration.properties.ChartServiceConfiguration;
import com.ngenebio.msa.chart.model.chart.hla.BaseVariationPlotChartData;
import com.ngenebio.msa.chart.test.support.SeleniumTestSupportExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SeleniumTestSupportExtension.class})
@ActiveProfiles("tc")
@Testcontainers
class ChartServiceUtilsTest {
    @InjectMocks
    private ChartServiceUtils chartServiceUtils;

    @Mock
    private ChartServiceConfiguration chartServiceConfiguration;

    @DisplayName("createChromeDriver() 성공")
    @Test
    void createChromeDriverTest() throws MalformedURLException {
        var seleniumUrl = System.getProperty("dptablo.selenium.url");
        when(chartServiceConfiguration.getSeleniumServiceUrl()).thenReturn(seleniumUrl);

        var webDriver = chartServiceUtils.createChromeDriver();
        assertThat(webDriver).isNotNull();
    }

    @DisplayName("getTempDirectory 성공")
    @Test
    void getTempDirectoryTest() {
        assertThatNoException().isThrownBy(() -> {
            // given & when
            var tempDirectory = chartServiceUtils.getTempDirectory();

            // then
            assertThat(tempDirectory).isNotNull();
            assertThat(tempDirectory.toFile().canRead()).isTrue();
        });

    }

    @DisplayName("generateRenderedChartHtmlFile 성공")
    @Test
    void generateRenderedChartHtmlFileTest() throws IOException {
        // given & when
        var tempDirectory = chartServiceUtils.getTempDirectory();
        final var chartHtmlFileResourcePath = "chart/hla/hla-base-variation-plot.html";
        Path chartHtmlFilePath = chartServiceUtils.generateRenderedChartHtmlFile(tempDirectory, chartHtmlFileResourcePath, "{}");

        // then
        assertThat(chartHtmlFilePath).isNotNull();
        assertThat(chartHtmlFilePath.toFile().canRead()).isTrue();
    }

    @DisplayName("removeDirectory 성공")
    @Test
    void removeDirectoryTest() {
        assertThatNoException().isThrownBy(() -> {
            // given & when
            var tempDirectory = chartServiceUtils.getTempDirectory();
            chartServiceUtils.removeDirectory(tempDirectory);

            // then
            assertThat(tempDirectory.toFile().canRead()).isFalse();
        });
    }

    @DisplayName("toJsonString 성공")
    @Test
    void toJsonStringTest() throws IOException {
        // given
        var resourcePath = new ClassPathResource("data/hla/baseVariationPlotData.json", this.getClass().getClassLoader());
        var jsonString = new String(Files.readAllBytes(resourcePath.getFile().toPath()));

        var objectMapper = new ObjectMapper();
        var dataObject = objectMapper.readValue(jsonString, BaseVariationPlotChartData.class);

        // when
        var convertedJsonString = chartServiceUtils.toJsonString(dataObject);

        // then
        assertThat(convertedJsonString).isNotNull();
        assertThat(convertedJsonString).isEqualTo(jsonString);
    }

    @DisplayName("convertChartHtmlToDataHtmlUrlString 성공")
    @Test
    void convertChartHtmlToDataHtmlUrlStringTest() throws IOException {
        // given
        var tempDirectory = chartServiceUtils.getTempDirectory();
        final var chartHtmlFileResourcePath = "chart/hla/hla-base-variation-plot.html";
        Path chartHtmlFilePath = chartServiceUtils.generateRenderedChartHtmlFile(tempDirectory, chartHtmlFileResourcePath, "{}");

        // when
        var encodedString = chartServiceUtils.convertChartHtmlToDataHtmlUrlString(chartHtmlFilePath);

        // then
        assertThat(encodedString).isNotNull();
        assertThat(encodedString).contains("%20");
    }
}