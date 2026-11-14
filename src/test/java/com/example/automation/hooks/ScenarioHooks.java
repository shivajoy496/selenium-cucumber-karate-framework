package com.example.automation.hooks;

import com.example.automation.context.ScenarioContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScenarioHooks {
    private final ScenarioContext scenarioContext;

    public ScenarioHooks(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Before
    public void startBrowser() {
        scenarioContext.startBrowser();
    }

    @After
    public void stopBrowser(Scenario scenario) {
        try {
            WebDriver driver = scenarioContext.getDriver();
            if (scenario.isFailed() && driver instanceof TakesScreenshot screenshotDriver) {
                scenario.attach(screenshotDriver.getScreenshotAs(OutputType.BYTES), "image/png", "failure-screenshot");
            }
        } catch (RuntimeException screenshotFailure) {
            scenario.log("Could not capture failure screenshot: " + screenshotFailure.getMessage());
        } finally {
            scenarioContext.stopBrowser();
        }
    }
}
