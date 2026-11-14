package com.example.automation.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {
    private final By pageTitle = By.cssSelector("[data-test='title']");
    private final By cartItems = By.cssSelector("[data-test='inventory-item']");
    private final By cartItemNames = By.cssSelector("[data-test='inventory-item-name']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return "Your Cart".equals(text(pageTitle));
    }

    public List<String> getProductNames() {
        return driver.findElements(cartItemNames).stream().map(WebElement::getText).map(String::trim).toList();
    }

    public void removeProduct(String productName) {
        WebElement row = driver.findElements(cartItems).stream()
                .filter(item -> item.findElement(cartItemNames).getText().trim().equals(productName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Product not found in cart: " + productName));
        row.findElement(By.cssSelector("button[data-test^='remove-']")).click();
    }

    public boolean isEmpty() {
        return driver.findElements(cartItems).isEmpty();
    }
}
