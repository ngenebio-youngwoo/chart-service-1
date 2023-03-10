package com.ngenebio.msa.chart.service;

import com.ngenebio.msa.chart.model.ChartResult;
import com.ngenebio.msa.chart.model.enumtype.ChartType;

import java.io.IOException;
import java.util.List;

public interface HlaChartService {
    ChartResult generateBaseVariationPlot(String json, List<ChartType> chartTypes) throws IOException;
}
