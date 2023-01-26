package com.ngenebio.msa.chart.model.enumtype;

public enum ChartType {
    BASE64("base64");


    private final String value;

    ChartType(String value) {
        this.value = value;
    }

    public static String getAllValueString() {
        return ChartType.BASE64.value;
    }

    public String getValue() {
        return this.value;
    }
}
