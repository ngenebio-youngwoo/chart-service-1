package com.ngenebio.msa.chart.result;

import com.ngenebio.msa.chart.configuration.properties.ChartServiceConfiguration;
import com.ngenebio.msa.chart.exception.result.RequestResultServiceFailedException;
import com.ngenebio.msa.chart.model.chart.hla.BaseVariationPlotChartData;
import com.ngenebio.msa.chart.model.chart.hla.CoveragePlotChartData;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class DefaultHlaResultServiceApi implements HlaResultServiceApi {
    private final ChartServiceConfiguration chartServiceConfiguration;

    @Override
    public BaseVariationPlotChartData getBaseVariationPlotChartData(
            String runId,
            String sampleId,
            String gene
    ) throws RequestResultServiceFailedException {
        var webClient = createWebClient();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/result/hla/chart/base-variation-plot/{runId}/{sampleId}/{gene}")
                        .build(runId, sampleId, gene))
                .retrieve()
                .bodyToMono(BaseVariationPlotChartData.class)
                .blockOptional().orElseThrow(RequestResultServiceFailedException::new);
    }

    @Override
    public CoveragePlotChartData getCoveragePlotChartData(
            String runId,
            String sampleId,
            String gene
    ) throws RequestResultServiceFailedException {
        var webClient = createWebClient();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/result/hla/chart/coverage-plot/{runId}/{sampleId}/{gene}")
                        .build(runId, sampleId, gene))
                .retrieve()
                .bodyToMono(CoveragePlotChartData.class)
                .blockOptional().orElseThrow(RequestResultServiceFailedException::new);
    }

    private WebClient createWebClient() {
        var httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

        var exchangeStrategies = ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        return WebClient.builder()
                .baseUrl(chartServiceConfiguration.getResultServiceUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", chartServiceConfiguration.getResultServiceUrl()))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(exchangeStrategies)
                .build();
    }
}
