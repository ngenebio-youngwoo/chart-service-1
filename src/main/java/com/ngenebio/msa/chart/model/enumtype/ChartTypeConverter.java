package com.ngenebio.msa.chart.model.enumtype;

import org.springframework.core.convert.converter.Converter;

public class ChartTypeConverter implements Converter<String, ChartType> {
    @Override
    public ChartType convert(String value) {
        return ChartType.valueOf(value.toUpperCase());
    }
}
