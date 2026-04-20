Feature: Print holidays for given months
  Scenario: Get the next holidays for the current month
    Given the current month
    When asked for public holidays
    Then the result should contain the public holidays

  Scenario: Get the next holidays for the month of February
    Given the month of February
    When asked for public holidays
    Then the result should contain the public holidays

  Scenario: Get the next holidays for the month of November
    Given the month of November
    When asked for public holidays
    Then the result should contain the public holidays

  Scenario: Get the next holidays sorted correctly for the months of January, April, June
    Given the months of January, June, April
    When asked for public holidays
    Then the result should contain the public holidays

  Scenario: Get the next holidays for the months starting at February and ending at September
    Given the months from February to September
    When asked for public holidays
    Then the result should contain the public holidays