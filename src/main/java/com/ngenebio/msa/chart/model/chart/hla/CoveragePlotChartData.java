package com.ngenebio.msa.chart.model.chart.hla;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ngenebio.msa.chart.model.chart.TargetMask;
import com.ngenebio.msa.chart.model.chart.TargetRegion;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoveragePlotChartData {
    @JsonProperty("targetMaskList")
    private List<TargetMask> targetMaskList;

    @JsonProperty("targetRegionList")
    private List<TargetRegion> targetRegionList;

    @JsonProperty("genotypeDepth")
    private Map<String, GenoTypeDepth> genoTypeDepthList;
}
