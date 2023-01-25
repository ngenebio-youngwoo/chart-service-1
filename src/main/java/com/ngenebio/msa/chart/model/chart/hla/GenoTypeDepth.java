package com.ngenebio.msa.chart.model.chart.hla;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenoTypeDepth {
    @JsonProperty("Region")
    private String region;

    @JsonProperty("IsHeteroPosition")
    private Boolean heteroPosition;

    @JsonProperty("IsMaskingRegion")
    private Boolean maskingRegion;

    @JsonProperty("CountA")
    private Long countA;

    @JsonProperty("CountT")
    private Long countT;

    @JsonProperty("CountG")
    private Long countG;

    @JsonProperty("CountC")
    private Long countC;

    @JsonProperty("CountDeletion")
    private Long countDeletion;
}
