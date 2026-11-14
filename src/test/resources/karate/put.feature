@api @api_put @posts
Feature: Update a post with PUT

  Background:
    * url karate.properties['api.posts.baseUrl'] || java.lang.System.getenv('API_POSTS_BASE_URL') || 'https://jsonplaceholder.typicode.com'

  Scenario: Replace an existing post
    Given path 'posts', 1
    And request { id: 1, userId: 1, title: 'Updated by Karate PUT', body: 'Full replacement payload' }
    When method put
    Then status 200
    And match response.id == 1
    And match response.userId == 1
    And match response.title == 'Updated by Karate PUT'
    And match response.body == 'Full replacement payload'
