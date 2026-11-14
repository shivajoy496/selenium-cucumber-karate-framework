@ui @member
Feature: SauceDemo login
  As a shop customer
  I want to sign in
  So that I can access the product catalog

  @smoke
  Scenario: Sign in with a valid demo account
    Given I am on the login page
    When I sign in with the configured demo account
    Then the product inventory page should be displayed

  Scenario Outline: Reject invalid credentials
    Given I am on the login page
    When I sign in with username "<username>" and password "<password>"
    Then I should see the login error "<message>"

    Examples:
      | username        | password      | message                                                                   |
      | invalid_user    | secret_sauce  | Username and password do not match any user in this service               |
      | standard_user   | wrong_password| Username and password do not match any user in this service               |
