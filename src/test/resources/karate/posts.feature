@api @posts @smoke
Feature: JSONPlaceholder posts API

  Background:
    * url karate.properties['api.posts.baseUrl'] || java.lang.System.getenv('API_POSTS_BASE_URL') || 'https://jsonplaceholder.typicode.com'

  Scenario: Get a post by its identifier
    Given path 'posts', 1
    When method get
    Then status 200
    And match response.id == 1
    And match response.userId == 1
    And match response.title == '#string'
    And match response.body == '#string'

  Scenario: Return not found for an unknown post
    Given path 'posts', 999999
    When method get
    Then status 404

  Scenario: Create a post
    Given path 'posts'
    And request { title: 'Karate sample post', body: 'Created by the API automation sample', userId: 1 }
    When method post
    Then status 201
    And match response.id == '#number'
    And match response.title == 'Karate sample post'
    And match response.userId == 1
