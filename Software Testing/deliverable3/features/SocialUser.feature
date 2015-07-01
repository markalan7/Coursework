Feature:
    As a social user
    I want access to sharing features
    So that I can share music with my friends

Scenario: Share track with others
    Given that I am in a playlist
    When I right-click and share a track
    Then I am given the option to share it with users of my choosing

Scenario: Share album with others
    Given that I am on an album page
    When I right-click and share an album
    Then I am given the option to share it with users of my choosing

Scenario: Share artist with others
    Given that I am on an artist's page
    When I right-click and share an artist
    Then I am given the option to share him/her/them with users of my choosing

Scenario: Share playlist with followers
    Given that I am in a playlist
    When I right-click and share a track
    Then I am given the option to share it globally with all my followers

Scenario: Follow a user
    Given that I am on a user's page
    When I click the Follow button
    Then I should start following them
    And my "Following" page will indicate that I followed them

Scenario: View a user's profile
    Given that I am viewing a playlist made by another user
    When I click on the user's name
    Then I should see their profile page

Scenario: Send a message
    Given that I am in the Messaging interface
    When I enter a message and a recipient and click send
    Then the recipient should recieve that message