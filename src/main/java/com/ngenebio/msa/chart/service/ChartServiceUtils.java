package com.ngenebio.msa.chart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngenebio.msa.chart.configuration.properties.ChartServiceConfiguration;
import com.ngenebio.msa.chart.mustache.MustacheChartHtmlGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChartServiceUtils {
    private final ChartServiceConfiguration chartServiceConfiguration;

    private final MustacheChartHtmlGenerator mustacheChartHtmlGenerator = new MustacheChartHtmlGenerator();

    public WebDriver createChromeDriver() throws MalformedURLException {
        // Windows chromve webdriver settings
//        System.setProperty("webdriver.chrome.driver", "D:\\downloads\\chromedriver_win32\\chromedriver.exe");

        var chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--single-process");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--log-level=3");
        chromeOptions.addArguments("--window-size=1920x1080");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--disable-extensions");

        // Remove Web Driver 사용 코드
        return new RemoteWebDriver(new URL(chartServiceConfiguration.getSeleniumServiceUrl()), chromeOptions);

        // Local Web Driver 사용 코드
//        return new ChromeDriver(chromeOptions);
    }

    public Path getTempDirectory() throws IOException {
        var renderedChartHtmlFileName = UUID.randomUUID().toString();
        return createTempDirectory(renderedChartHtmlFileName);
    }

    private Path createTempDirectory(String chartFileName) throws IOException {
        var tempDirectoryPath = Paths.get(System.getProperty("java.io.tmpdir"),
                "ngenebio_chart_temp", chartFileName);
        if (!Files.exists(tempDirectoryPath))
            Files.createDirectories(tempDirectoryPath);

        return tempDirectoryPath;
    }

    public Path generateRenderedChartHtmlFile(
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
    
    public void removeDirectory(Path directoryPath) throws IOException {
        if (directoryPath == null) return;

        var directory = directoryPath.toFile();
        if (directory.canRead()) {
            FileUtils.deleteDirectory(directory);
        }
    }

    public String toJsonString(Object object) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
