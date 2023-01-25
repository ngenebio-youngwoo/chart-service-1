package com.ngenebio.msa.chart.result;

import com.ngenebio.msa.chart.model.chart.hla.BaseVariationPlotChartData;
import com.ngenebio.msa.chart.model.chart.hla.CoveragePlotChartData;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class DefaultHlaResultServiceApi implements HlaResultServiceApi {
    @Override
    public BaseVariationPlotChartData getBaseVariationPlotChartData(String runId, String sampleId, String gene) {
        // TODO: Not implement
        throw new NotImplementedException();
    }

    @Override
    public CoveragePlotChartData getCoveragePlotChartData(String runId, String sampleId, String gene) {
        // TODO: Not implement
        throw new NotImplementedException();
    }
}
