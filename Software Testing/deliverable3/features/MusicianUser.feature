Feature:
    As a budding musician
    I want to discover new music
    So that I can be inspired to create new music myself

Scenario: Search by keyword
    Given that I am on the main page
    When I click the search button
    And enter a keyword
    Then I should see a list of results relevant to the entered keyword

Scenario: List of new releases
    Given that I am on the main page
    When I click the browse button
    And I select 'New Releases'
    Then a list of albums should appear

Scenario: Select music playlist by mood
    Given that I am on the main page
    When I click the browse button
    And I select 'Genres & Moods'
    Then a list of moods should appear

Scenario: Select music playlist by genre
    Given that I am on the main page
    When I click the browse button
    And I select 'Genres & Moods'
    Then a list of genres should appear

Scenario: Top songs by category
    Given that I am on the main page
    When I click the browse button
    And I select 'Top Lists'
    Then I should get a list of top hits by category
