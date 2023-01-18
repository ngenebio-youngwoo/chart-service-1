package com.ngenebio.msa.chart.mustache;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.util.HashMap;

public class MustacheChartHtmlGenerator {
    public String execute(String chartTemplate, String jsonData) {
        var data = new HashMap<>();
        data.put("jsonData", jsonData);

        Template template = Mustache.compiler().compile(chartTemplate);
        return template.execute(data);
    }
}
