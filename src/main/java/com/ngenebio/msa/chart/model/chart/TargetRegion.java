package com.ngenebio.msa.chart.model.chart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TargetRegion {
    @JsonProperty("Gene")
    private String gene;

    @JsonProperty("Region")
    private String region;

    @JsonProperty("StartPosition")
    private Long startPosition;

    @JsonProperty("EndPosition")
    private Long endPosition;
}
