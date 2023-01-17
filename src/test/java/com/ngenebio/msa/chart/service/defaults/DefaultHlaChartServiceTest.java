package com.ngenebio.msa.chart.service.defaults;

import com.ngenebio.msa.chart.model.enumtype.ChartType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("local")
class DefaultHlaChartServiceTest {
    @InjectMocks
    private DefaultHlaChartService hlaChartService;

    @DisplayName("BaseVariationPlot 차트 생성 성공")
    @Test
    void generateBaseVariationPlotTest() throws IOException {
        // given
        var dataFilePath = Paths.get("src", "test", "resources",
                "data", "hla", "baseVariationPlotData.json");
        var json = new String(Files.readAllBytes(dataFilePath));

        var chartTypes = Arrays.asList(ChartType.BASE64);

        // when
        var result = hlaChartService.generateBaseVariationPlot(json, chartTypes);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBase64()).isNotNull();
    }
}