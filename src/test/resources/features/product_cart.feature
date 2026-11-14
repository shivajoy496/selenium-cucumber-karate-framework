@ui @shopping-cart
Feature: Shopping cart
  As a signed-in shopper
  I want to manage products in my cart
  So that I can review items before checkout

  Background:
    Given I am signed in to the store

  Scenario: Add a product to the cart
    When I add the "Sauce Labs Backpack" to the cart
    And I open the shopping cart
    Then the shopping cart should contain "Sauce Labs Backpack"

  Scenario: Remove a product from the cart
    When I add the "Sauce Labs Bike Light" to the cart
    And I open the shopping cart
    And I remove the "Sauce Labs Bike Light" from the cart
    Then the shopping cart should be empty
