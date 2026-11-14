package com.example.automation.pages;

import com.example.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage extends BasePage {
    private final By pageTitle = By.cssSelector("[data-test='title']");
    private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            return "Products".equals(text(pageTitle));
        } catch (org.openqa.selenium.TimeoutException exception) {
            return false;
        }
    }

    public void addProduct(String productName) {
        String productCard = "//div[contains(@class,'inventory_item')][.//div[contains(@class,'inventory_item_name') and normalize-space()="
                + xpathLiteral(productName) + "]]";
        By addButton = By.xpath(productCard + "//button[starts-with(@data-test,'add-to-cart-')]");
        By removeButton = By.xpath(productCard + "//button[starts-with(@data-test,'remove-')]");
        click(addButton);
        visible(removeButton);
    }

    public void openCart() {
        String cartUrl = clickable(cartLink).getAttribute("href");
        if (cartUrl == null || cartUrl.isBlank()) {
            click(cartLink);
        } else {
            driver.navigate().to(cartUrl);
        }
        WaitUtils.urlContains(driver, "/cart.html");
    }

    private String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        return "concat('" + value.replace("'", "', \"'\", '") + "')";
    }
}
