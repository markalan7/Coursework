Feature:
    As a radio lover
    I want an extensive radio feature
    So that I can explore new artists and genres

Scenario: Create radio station
    Given that I am listening to a song
    When I right-click the title and click "create radio station"
    Then a new radio station is started seeded from that song

Scenario: Like a song from radio
    Given that I am listening to a radio station
    When I thumbs-up a track
    Then the track should be added to my "liked from radio" playlist

Scenario: Dislike a song from radio
    Given that I am listening to a radio station
    When I thumbs-down a track
    Then the current track should be skipped

Scenario: Skip a song on radio
    Given that I am listening to a radio station
    When I click the skip button
    Then the current track should be skipped
