package com.example.automation.runner;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import com.example.automation.api.KarateApiTest;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines({"cucumber", "junit-jupiter"})
@SelectClasspathResource("features")
@SelectClasses(KarateApiTest.class)
@ConfigurationParameter(key = GLUE_PROPERTY_NAME,
        value = "com.example.automation.steps,com.example.automation.hooks")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
        value = "pretty, summary, html:target/cucumber-report.html, json:target/cucumber.json, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
@ConfigurationParameter(key = "cucumber.ansi-colors.disabled", value = "true")
public class RunCucumberTest {
}
