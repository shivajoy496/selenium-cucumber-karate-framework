package com.example.automation.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.automation.context.ScenarioContext;
import com.example.automation.pages.CartPage;
import com.example.automation.pages.InventoryPage;
import com.example.automation.pages.LoginPage;
import com.example.automation.utils.ConfigReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StoreSteps {
    private final ScenarioContext scenarioContext;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;

    public StoreSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        loginPage = new LoginPage(scenarioContext.getDriver());
        loginPage.open(ConfigReader.get("app.baseUrl"));
    }

    @When("I sign in with the configured demo account")
    public void signInWithConfiguredDemoAccount() {
        loginPage.login(ConfigReader.get("app.username"), ConfigReader.get("app.password"));
    }

    @When("I sign in with username {string} and password {string}")
    public void signInWithCredentials(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("the product inventory page should be displayed")
    public void inventoryPageShouldBeDisplayed() {
        inventoryPage = new InventoryPage(scenarioContext.getDriver());
        assertThat(inventoryPage.isDisplayed()).as("inventory page is displayed").isTrue();
    }

    @Then("I should see the login error {string}")
    public void shouldSeeLoginError(String expectedMessage) {
        assertThat(loginPage.getErrorMessage()).contains(expectedMessage);
    }

    @Given("I am signed in to the store")
    public void iAmSignedInToTheStore() {
        loginPage = new LoginPage(scenarioContext.getDriver());
        loginPage.open(ConfigReader.get("app.baseUrl"));
        loginPage.login(ConfigReader.get("app.username"), ConfigReader.get("app.password"));
        inventoryPage = new InventoryPage(scenarioContext.getDriver());
        assertThat(inventoryPage.isDisplayed()).as("inventory page is displayed after sign-in").isTrue();
    }

    @When("I add the {string} to the cart")
    public void iAddProductToCart(String productName) {
        inventoryPage = new InventoryPage(scenarioContext.getDriver());
        inventoryPage.addProduct(productName);
    }

    @And("I open the shopping cart")
    public void iOpenTheShoppingCart() {
        inventoryPage = new InventoryPage(scenarioContext.getDriver());
        inventoryPage.openCart();
        cartPage = new CartPage(scenarioContext.getDriver());
        assertThat(cartPage.isDisplayed()).as("shopping cart page is displayed").isTrue();
    }

    @Then("the shopping cart should contain {string}")
    public void cartShouldContainProduct(String productName) {
        assertThat(cartPage.getProductNames()).contains(productName);
    }

    @And("I remove the {string} from the cart")
    public void iRemoveProductFromCart(String productName) {
        cartPage.removeProduct(productName);
    }

    @Then("the shopping cart should be empty")
    public void shoppingCartShouldBeEmpty() {
        assertThat(cartPage.isEmpty()).as("shopping cart is empty").isTrue();
    }
}
