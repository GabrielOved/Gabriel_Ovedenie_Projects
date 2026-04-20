Feature: Print holidays for a given year
  Scenario: Get all holidays for the current year
    Given the current year
    When asked for all public holidays
    Then the result should contain all public holidays

  Scenario: Get all holidays for the year of 2024
    Given the year 2024
    When asked for all public holidays
    Then the result should contain all public holidays

  Scenario: Get all holidays for the year of 2025
    Given the year 2025
    When asked for all public holidays
    Then the result should contain all public holidays

  Scenario: Get all holidays for the year of 2027
    Given the year 2027
    When asked for all public holidays
    Then the result should contain all public holidays

  Scenario: Get all holidays for the year of 2028
    Given the year 2028
    When asked for all public holidays
    Then the result should contain all public holidays

