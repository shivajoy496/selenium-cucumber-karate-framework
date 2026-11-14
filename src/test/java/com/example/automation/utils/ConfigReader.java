package com.example.automation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public final class ConfigReader {
    private static final Properties PROPERTIES = loadProperties();

    private ConfigReader() {
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties was not found on the test classpath");
            }
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read config.properties", exception);
        }
    }

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        String environmentKey = key.replaceAll("([a-z0-9])([A-Z])", "$1_$2")
            .replace('.', '_')
            .toUpperCase(Locale.ROOT);
        String environmentValue = System.getenv(environmentKey);
        if ((environmentValue == null || environmentValue.isBlank()) && "app.baseUrl".equals(key)) {
            environmentValue = System.getenv("APP_BASEURL");
        }
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue;
        }
        String configuredValue = PROPERTIES.getProperty(key);
        if (configuredValue == null || configuredValue.isBlank()) {
            throw new IllegalStateException("Missing configuration value: " + key);
        }
        return configuredValue.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
