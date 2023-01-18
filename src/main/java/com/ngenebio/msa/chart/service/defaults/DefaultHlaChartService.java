package com.ngenebio.msa.chart.service.defaults;

import com.ngenebio.msa.chart.model.ChartResult;
import com.ngenebio.msa.chart.model.enumtype.ChartType;
import com.ngenebio.msa.chart.mustache.MustacheChartHtmlGenerator;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultHlaChartService implements HlaChartService {
    private final ChartJavaScriptExecutorUtils chartJavaScriptExecutorUtils = new ChartJavaScriptExecutorUtils();
    private final MustacheChartHtmlGenerator mustacheChartHtmlGenerator = new MustacheChartHtmlGenerator();

    @Override
    public ChartResult generateBaseVariationPlot(String json, List<ChartType> chartTypes) throws IOException {
        Path tempDirectoryPath = null;
        WebDriver chromeDriver = null;
        try {
            var renderedChartHtmlFileName = UUID.randomUUID().toString();

            tempDirectoryPath = createTempDirectory(renderedChartHtmlFileName);

            var chartHtmlFileResourcePath = "chart/hla-base-variation-plot.html";
            Path chartHtmlFilePath = generateRenderedChartHtmlFile(tempDirectoryPath, chartHtmlFileResourcePath, json);

            chromeDriver = createChromeDriver();
            var javaScriptExecutor = (JavascriptExecutor) chromeDriver;
            chromeDriver.get("file://" + chartHtmlFilePath.toFile().getAbsolutePath());

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

            // TODO: 테스트를 위해 임시 비활성화
//            if (tempDirectoryPath != null)
//                FileUtils.deleteDirectory(tempDirectoryPath.toFile());
        }
    }

    private static Path createTempDirectory(String chartFileName) throws IOException {
        var tempDirectoryPath = Paths.get(System.getProperty("java.io.tmpdir"),
                "ngenebio_chart_temp", chartFileName);
        if (!Files.exists(tempDirectoryPath))
            Files.createDirectories(tempDirectoryPath);

        return tempDirectoryPath;
    }

    private Path generateRenderedChartHtmlFile(
            Path tempDirectoryPath,
            String htmlFileResourcePath,
            String json
    ) throws IOException {
        var chartHtmlFile = new ClassPathResource(htmlFileResourcePath, this.getClass().getClassLoader());
        var chartHtmlString = new String(Files.readAllBytes(chartHtmlFile.getFile().toPath()));
        var renderedChartHtmlString = mustacheChartHtmlGenerator.execute(chartHtmlString, json);

        return Files.write(
                Paths.get(tempDirectoryPath.toFile().getAbsolutePath(), UUID.randomUUID().toString() + ".html"),
                renderedChartHtmlString.getBytes());
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
