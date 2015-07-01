Feature:
    As a general user
    I want free access to Spotify's music library
    So that I can try out their service and listen to music I like

Scenario: Create an account with an email that is already in use
    Given I am on the Spotify website
    and I am not currently signed in to an account
    and I select the 'Sign up with your email address' option
    When I enter the following valid credentials: username, password, date of birth, and gender
    But the email address I entered is associated with another account
    And I select 'Sign up'
    Then a new account is not created
    And I am informed that the email address is already in use

Scenario: Successful sign-in given correct credentials
    Given a correct username
    And a correct password
    When I try to log in with those credentials
    Then I should see the home page of http://play.spotify.com

Scenario: Unsuccessful sign-in given incorrect password
    Given a correct username
    And an incorrect password
    When I try to log in with those credentials
    Then I should see an error message indicating incorrect credentials

Scenario: Language can be selected
    Given that I am on the settings page
    When I click the language dropbox
    And I select a new language
    And I reload the page
    Then the page should reappear in the selected language

Scenario: Successful log-out
    Given I am signed in
    When I click the setting button
    And I click the "Log Out" button
    Then I should be logged out of my account