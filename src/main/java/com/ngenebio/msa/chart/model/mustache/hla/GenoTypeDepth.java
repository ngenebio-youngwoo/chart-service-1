package com.ngenebio.msa.chart.model.mustache.hla;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenoTypeDepth {
    @JsonProperty("Region")
    private String region;

    @JsonProperty("isHeteroPosition")
    private Boolean heteroPosition;

    @JsonProperty("isMaskingRegion")
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
