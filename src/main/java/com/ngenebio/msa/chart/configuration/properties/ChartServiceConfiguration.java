package com.ngenebio.msa.chart.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "chart-service")
@Data
public class ChartServiceConfiguration {
    private String resultServiceUrl;
    private String seleniumServiceUrl;
}