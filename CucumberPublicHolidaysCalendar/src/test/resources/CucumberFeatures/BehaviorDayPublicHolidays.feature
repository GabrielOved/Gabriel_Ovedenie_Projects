Feature: Print next holiday

  Scenario: Get the next holiday using the current day as the starting point
    Given the current day
    When asked for the next public holiday
    Then the result should contain the next holiday

  Scenario: Get the next holiday using the current month and the 9th day as the starting point
    Given the current month and the 9 day
    When asked for the next public holiday
    Then the result should contain the next holiday

  Scenario: Get the next holiday using the month of January and the 3rd day as the starting point
    Given the month of January and the 03 day
    When asked for the next public holiday
    Then the result should contain the next holiday

  Scenario: Get the next holiday using the month of June and the 15th day as the starting point
    Given the month of June and the 15 day
    When asked for the next public holiday
    Then the result should contain the next holiday

  Scenario: Get the next holiday using the month of December and the 25th day as the starting point
    Given the month of December and the 25 day
    When asked for the next public holiday
    Then the result should contain the next holiday

  Scenario: Get the next holiday using the month of December and the 26th day as the starting point
    Given the month of December and the 26 day
    When asked for the next public holiday
    Then the result should contain the next holiday

  Scenario: Get the next holiday using the month of December and the 27th day as the starting point
    Given the month of December and the 27 day
    When asked for the next public holiday
    Then the result should contain the next holiday

  Scenario: Get the next holiday using the year of 2027 and month of April and the 30th day as the starting point
    Given the year of 2027 and month of April and the 30 day
    When asked for the next public holiday
    Then the result should contain the next holiday

  Scenario: Get the next holiday using the year of 2027 and month of December and the 26th day as the starting point
    Given the year of 2027 and month of December and the 26 day
    When asked for the next public holiday
    Then the result should contain the next holiday

  Scenario: Get the next 10 holidays using the current day as the starting point
    Given the current day
    When asked for the next 10 public holidays
    Then the result should contain the next holiday

  Scenario: Get the next 2 holidays using the current month and the 10th day as the starting point
    Given the current month and the 10 day
    When asked for the next 2 public holidays
    Then the result should contain the next holiday

  Scenario: Get the next 5 holidays using the month of December and the 1st day as the starting point
    Given the month of December and the 01 day
    When asked for the next 5 public holidays
    Then the result should contain the next holiday

  Scenario: Get the next 15 holidays using the year of 2028 and the month of January and the 1st day as the starting point
    Given the year of 2028 and month of January and the 01 day
    When asked for the next 15 public holidays
    Then the result should contain the next holiday