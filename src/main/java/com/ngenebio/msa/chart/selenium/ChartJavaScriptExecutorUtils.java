package com.ngenebio.msa.chart.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.springframework.stereotype.Component;

@Component
public class ChartJavaScriptExecutorUtils {
    public String getChartPngImageBase64(JavascriptExecutor javaScriptExecutor) {
        Object response = javaScriptExecutor.executeAsyncScript(
                "var callback = arguments[arguments.length - 1];" +
                        "generateChartPngImage(callback);");

        return response instanceof String ? (String) response : response.toString();
    }
}
