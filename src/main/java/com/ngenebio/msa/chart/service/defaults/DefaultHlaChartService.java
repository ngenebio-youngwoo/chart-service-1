package com.ngenebio.msa.chart.service.defaults;

import com.ngenebio.msa.chart.model.ChartResult;
import com.ngenebio.msa.chart.model.enumtype.ChartType;
import com.ngenebio.msa.chart.selenium.ChartJavaScriptExecutorUtils;
import com.ngenebio.msa.chart.service.HlaChartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultHlaChartService implements HlaChartService {
    private ChartJavaScriptExecutorUtils chartJavaScriptExecutorUtils = new ChartJavaScriptExecutorUtils();

    @Override
    public ChartResult generateBaseVariationPlot(String json, List<ChartType> chartTypes) throws IOException {
        WebDriver chromeDriver = null;
        try {
            chromeDriver = createChromeDriver();
            var javaScriptExecutor = (JavascriptExecutor) chromeDriver;

            var htmlFile = new ClassPathResource("chart/hla-base-variation-plot.html", this.getClass().getClassLoader());
            chromeDriver.get("file://" + htmlFile.getFile().getAbsolutePath());

            var result = new ChartResult();
            for (var chartType : chartTypes) {
                switch (chartType) {
                    case BASE64 -> {
                        result.setBase64(chartJavaScriptExecutorUtils.getChartPngImageBase64(javaScriptExecutor));
                    }
                }
            }
            return result;
        } catch(Exception exception) {
            throw exception;
        } finally {
            if (chromeDriver != null) {
                chromeDriver.close();
            }
        }
    }

    private WebDriver createChromeDriver() {

        // Remove Web Driver 사용 코드
//        var chromeDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), chromeOptions);

        // Windows chromve webdriver settings
//        System.setProperty("webdriver.chrome.driver", "D:\\downloads\\chromedriver_win32\\chromedriver.exe");

        var chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("headless");
        chromeOptions.addArguments("--log-level=3");
        chromeOptions.addArguments("window-size=1920x1080");
        chromeOptions.addArguments("disable-gpu");
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("--disable-extensions");

        return new ChromeDriver(chromeOptions);
    }
}
