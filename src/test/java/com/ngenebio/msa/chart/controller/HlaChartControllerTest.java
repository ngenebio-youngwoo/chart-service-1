package com.ngenebio.msa.chart.controller;

import com.ngenebio.msa.chart.model.ChartResult;
import com.ngenebio.msa.chart.model.enumtype.ChartType;
import com.ngenebio.msa.chart.service.HlaChartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = HlaChartController.class)
@AutoConfigureMockMvc(addFilters = false)
class HlaChartControllerTest {

    private MockMvc mockMvc;

    @Qualifier("defaultHlaChartService")
    @MockBean
    private HlaChartService hlaChartService;

    @BeforeEach
    public void setup(
            WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint())
                )
                .build();
    }

    @Test
    void generateBaseVariationPlotChart() {
    }

    @DisplayName("HLA - coverage plot - 성공")
    @Test
    void generateCoveragePlotChart() throws Exception {
        // given
        var runId = "20220729_145710";
        var sampleId = "crs010";
        var gene = "A";

        var chartResult = ChartResult.builder()
                .base64("base64~~~")
                .build();

        doReturn(chartResult).when(hlaChartService)
                .generateCoveragePlot(runId, sampleId, gene, Arrays.asList(ChartType.BASE64));


        // when
        MvcResult mvcResult = mockMvc.perform(
                        get("/chart/hla/coverage-plot")
                                .queryParam("runId", runId)
                                .queryParam("sampleId", sampleId)
                                .queryParam("gene", gene)
                                .queryParam("chartTypes", "base64")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("hla-{methodName}",
                        queryParameters(
                                parameterWithName("runId").description("Run Id"),
                                parameterWithName("sampleId").description("Sample Id"),
                                parameterWithName("gene").description("Gene (ex: A | B | )"),
                                parameterWithName("chartTypes").description(
                                        String.format("생성된 차트의 형태 (%s)", ChartType.getAllValueString()))
                        )
                ))
                .andReturn();

        // then
        var resultResponse = mvcResult.getResponse();
        assertThat(resultResponse.getStatus()).isEqualTo(200);
        assertThat(resultResponse.getHeader(HttpHeaders.CONTENT_TYPE)).contains("application/json");
        assertThat(resultResponse.getContentAsByteArray()).isNotNull();

        verify(hlaChartService, times(1))
                .generateCoveragePlot(eq(runId), eq(sampleId), eq(gene), anyList());
    }
}