package com.example.automation.api;

import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.Assumptions;

public class KarateApiTest {
    @Karate.Test
    Karate postsApi() {
        Assumptions.assumeTrue(Boolean.parseBoolean(System.getProperty("karate.enabled", "true")),
            "Karate API tests disabled with -Dkarate.enabled=false");
        String tagExpression = System.getProperty("karate.tags", "@api");
        return Karate.run("classpath:karate/posts.feature", "classpath:karate/put.feature").tags(tagExpression);
    }
}
