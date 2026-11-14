package com.example.automation.context;

import com.example.automation.utils.DriverFactory;
import org.openqa.selenium.WebDriver;

/**
 * Mutable state shared only within one Cucumber scenario by PicoContainer.
 */
public class ScenarioContext {
    private WebDriver driver;

    public void startBrowser() {
        driver = DriverFactory.createDriver();
    }

    public WebDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("The scenario WebDriver has not been started");
        }
        return driver;
    }

    public void stopBrowser() {
        try {
            DriverFactory.quitDriver();
        } finally {
            driver = null;
        }
    }
}
