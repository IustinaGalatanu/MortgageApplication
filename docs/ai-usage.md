# AI Usage

I used ChatGPT throughout this exercise as a research and development assistant, mainly to explore unfamiliar mortgage concepts, challenge assumptions and identify potential issues in my implementation.

Below are the main areas where AI supported my work, along with representative prompts refined for clarity.

## 1. Domain Research

I used AI to better understand buy-to-let lending, particularly LTV, ICR, stress rates and the relationship between mortgage products and institutional funders.

**Example prompt:**

> "Research how UK buy-to-let lenders assess rental affordability. Compare ICR requirements and stress-rate calculations for different fixed-rate periods, and identify which assumptions would be reasonable for a simplified mortgage eligibility engine."

## 2. Requirements & Assumptions

I used AI to challenge my interpretation of the requirements and identify missing information before implementation.

**Example prompt:**

> "Review these mortgage matching requirements from the perspective of a senior engineer. Identify ambiguities that could affect eligibility decisions, distinguish critical clarification questions from reasonable assumptions, and highlight edge cases that might otherwise be overlooked."

## 3. Financial Calculations

I used AI to verify my understanding of LTV and ICR calculations and investigate precision-related issues.

**Example prompt:**

> "Challenge my LTV and ICR calculations. Could rounding, decimal precision or boundary values incorrectly make an application eligible? Provide numerical counterexamples and suggest ways to compare monetary values safely using BigDecimal."

## 4. Testing & Edge Cases

I used AI to identify scenarios that could expose weaknesses in my rules and assessment services, particularly around eligibility boundaries and combinations of rejection reasons.

**Example prompt:**

> "Act as a critical code reviewer. Identify test scenarios that could break this mortgage assessment logic, including conflicting product and funder criteria, exact threshold boundaries, zero rental income and applications with multiple rejection reasons. Prioritise cases that are most likely to reveal incorrect behaviour."

## 5. Debugging & Review

I also used AI to investigate compilation errors, review test failures and consider whether suggested changes were necessary.

**Example prompt:**

> "Analyse these Maven compilation errors, identify any shared root cause and suggest the smallest reliable fix. Distinguish build configuration issues from problems in the application logic."

## My Approach

I treated AI suggestions as input for investigation rather than final decisions. 
For example, I kept the product and funder catalogues in memory, used precise decimal comparisons for eligibility checks and separated invalid requests from valid applications that fail lending criteria.

I remain responsible for the implementation and the decisions made throughout the exercise.