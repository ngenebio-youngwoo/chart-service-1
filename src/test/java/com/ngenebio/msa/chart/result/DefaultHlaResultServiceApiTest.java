package com.ngenebio.msa.chart.result;

import com.ngenebio.msa.chart.configuration.properties.ChartServiceConfiguration;
import com.ngenebio.msa.chart.exception.result.RequestResultServiceFailedException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.Parameter.param;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("local")
class DefaultHlaResultServiceApiTest {
    private final int PORT = 29090;
    private ClientAndServer server = new ClientAndServer();

    @InjectMocks
    private DefaultHlaResultServiceApi hlaResultServiceApi;

    @Mock
    private ChartServiceConfiguration chartServiceConfiguration;

    @BeforeAll
    public void startServer() {
        server = ClientAndServer.startClientAndServer(PORT);
    }

    @AfterAll
    public void stopServer() {
        server.stop();
    }

    @DisplayName("getBaseVariationPlotChartData 성공")
    @Test
    void getBaseVariationPlotChartData() throws RequestResultServiceFailedException, IOException {
        // given
        var runId = "20220729_145710";
        var sampleId = "CRS-010";
        var gene = "A";

        when(chartServiceConfiguration.getResultServiceUrl()).thenReturn("http://localhost:" + PORT);

        var chartDataResource = new ClassPathResource(
                "data/hla/baseVariationPlotData.json",
                this.getClass().getClassLoader());

        var chartDataJson = new String(Files.readAllBytes(chartDataResource.getFile().toPath()));

        server
                .when(
                    request()
                            .withMethod("GET")
                            .withPath("/api/result/hla/chart/base-variation-plot/{runId}/{sampleId}/{gene}")
                            .withPathParameters(
                                    param("runId", runId),
                                    param("sampleId", sampleId),
                                    param("gene", gene)
                            )
                            .withHeader("Content-type", "application/json")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8")
                                )
                                .withBody(chartDataJson)
                );

        // when
        var baseVariationPlotChartData = hlaResultServiceApi.getBaseVariationPlotChartData(runId, sampleId, gene);

        // then
        assertThat(baseVariationPlotChartData).isNotNull();
    }

    @DisplayName("getCoveragePlotChartData 성공")
    @Test
    void getCoveragePlotChartDataTest() throws IOException, RequestResultServiceFailedException {
        // given
        var runId = "20220729_145710";
        var sampleId = "CRS-010";
        var gene = "A";

        var chartDataResource = new ClassPathResource(
                "data/hla/coveragePlotData.json",
                this.getClass().getClassLoader());

        var chartDataJson = new String(Files.readAllBytes(chartDataResource.getFile().toPath()));

        server
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/api/result/hla/chart/coverage-plot/{runId}/{sampleId}/{gene}")
                                .withPathParameters(
                                        param("runId", runId),
                                        param("sampleId", sampleId),
                                        param("gene", gene)
                                )
                                .withHeader("Content-type", "application/json")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8")
                                )
                                .withBody(chartDataJson)
                );

        // when
        var coveragePlotChartData = hlaResultServiceApi.getCoveragePlotChartData(runId, sampleId, gene);

        // then
        assertThat(coveragePlotChartData).isNotNull();
    }
}