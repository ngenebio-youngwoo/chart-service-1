package com.ngenebio.msa.chart.result;

import com.ngenebio.msa.chart.model.chart.hla.BaseVariationPlotChartData;
import com.ngenebio.msa.chart.model.chart.hla.CoveragePlotChartData;

public interface HlaResultServiceApi {
    BaseVariationPlotChartData getBaseVariationPlotChartData(String runId, String sampleId, String gene);
    CoveragePlotChartData getCoveragePlotChartData(String runId, String sampleId, String gene);
}
