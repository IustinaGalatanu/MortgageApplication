# Requirements Analysis

## Goal

Given a proposed mortgage, identify eligible products and the funders
that could support each one.

When an option is unavailable, explain why so that another system
can understand the result.

Product eligibility and funder matching are separate checks.
An eligible product does not necessarily have a matching funder.

## Explicit requirements

The exercise requires:

- A Java application using Spring Boot 4.
- An HTTP API accepting proposed mortgage details.
- A small, representative set of mortgage products.
- A small set of institutional funders with different risk appetites.
- Product eligibility results.
- Matching funders for each eligible product.
- Explanations for unavailable options.
- Local execution using Maven or Gradle.
- Documented assumptions, ambiguities and significant limitations.

The products and funders should demonstrate realistic differences,
but do not need to reproduce real commercial arrangements.

The solution should be clear, maintainable and straightforward to run.
The brief does not require a particular architecture.

The submission must include working source code, build and run
instructions, a short README, documented assumptions and limitations,
and a brief account of AI usage with the significant prompts used.

## Application inputs

The brief identifies these inputs:

| Input | Purpose |
| --- | --- |
| Case reference | Identify the mortgage case |
| Loan amount | Amount the applicant wants to borrow |
| Property value | Value used in the lending assessment |
| Expected monthly rent | Rental income used for affordability checks |
| Property type | Property classification used by lending rules |
| Applicant type | Applicant classification used by lending rules |
| Property location | Location used by geographic restrictions |
| Previous landlord experience | Whether the applicant has prior experience |

Exact field names, data types, accepted categories and validation rules
are not specified.

See [Application data](assumptions.md#application-data) and
[Supported categories](assumptions.md#supported-categories).

I have included repayment type as an additional input. It is not
explicitly required by the brief.

See [Repayment types](assumptions.md#repayment-types).

## Expected results

The brief requires eligibility results, funding matches and explanations.
I have chosen the following response behaviour:

| Outcome | Information returned |
| --- | --- |
| Invalid request | Validation errors; no eligibility assessment |
| Ineligible product | Product rejection reasons; funding not evaluated |
| Eligible product with matching funders | Matching funders and rejection reasons for other funders |
| Eligible product without matching funders | Product remains eligible, with reasons why no funder matches |

All configured products are evaluated.

An assessment with no matching products or funders is a normal result,
not a system error.

Rejection reasons include stable codes and readable messages.
Where useful, they also include the actual value and the required limit.

For example, an LTV rejection can include the application's LTV
and the product's maximum LTV.

See [Results and rejection reasons](assumptions.md#results-and-rejection-reasons).

## Design decisions left to me

The brief deliberately leaves these decisions to me:

- API routes, HTTP methods and request/response formats.
- How products, funders and rules are represented.
- How catalogue data is stored.
- How eligibility and funding checks are organised.
- How errors and rejection reasons are returned.
- How results are ordered.

These choices do not automatically require clarification.

A database is neither required nor prohibited. I have chosen a local
catalogue without storing cases or assessment history.

Maven is the selected build tool; the brief also permits Gradle.

I will document the API usage and main code structure in the README.

## Scope boundaries

The brief explicitly says that the following are not required:

- A user interface.
- Authentication.
- Cloud infrastructure.
- Integrations with other systems.

I have also chosen not to include:

- Remortgages or product transfers.
- Joint-applicant or property portfolio assessments.
- Credit, identity, legal or independent valuation checks.
- Monthly repayment calculations or repayment schedules.
- Alternative loan recommendations.
- Funding allocation, reservation or live capacity checks.
- Stored cases or assessment history.

These are project scope decisions, not additional exclusions imposed
by the brief.

See [Scope](assumptions.md#scope),
[Repayment types](assumptions.md#repayment-types) and
[Sample funders](assumptions.md#sample-funders).

## Ambiguities and questions

The following points are not fully specified in the brief.
For each one, I explain the interpretation I have chosen and link
to the relevant assumption.

### 1. Which currency and monetary precision should be used?

The brief does not specify a currency or decimal precision.

I use GBP and accept monetary amounts to penny precision.
Additional non-zero decimal places are rejected rather than rounded.

See [Application data](assumptions.md#application-data).

### 2. Does a match mean eligibility or mortgage approval?

The brief asks which products an application could qualify for.
It does not describe a complete lending approval process.

I treat a match as confirmation that the configured rules have passed.
It is not a mortgage offer and does not imply that external checks
have passed.

See [Scope](assumptions.md#scope).

### 3. Which transaction and repayment types are supported?

The brief does not specify purchases, remortgages, product transfers
or repayment methods.

I cover purchases only and include INTEREST_ONLY and
CAPITAL_AND_INTEREST as repayment options.

The service checks whether each product accepts the requested
repayment type. It does not calculate the full monthly repayment.

Passing ICR does not confirm that rent covers both interest and
capital repayments.

See [Scope](assumptions.md#scope) and
[Repayment types](assumptions.md#repayment-types).

### 4. How detailed should location and classifications be?

Location could mean a country, region or postcode. Applicant and
property types could also contain more detailed categories.

I use UK countries, three property types and two applicant types.
The service does not infer company structure, property size or
licensing status from these classifications.

Recognised but unsupported categories produce business rejections.
Unknown categories produce validation errors.

See [Supported categories](assumptions.md#supported-categories).

### 5. What counts as previous landlord experience?

The brief asks whether the applicant has experience, without
specifying a minimum duration.

I use a required boolean. True indicates prior experience,
not a particular number of months or years.

See [Application data](assumptions.md#application-data).

### 6. Which product and funder rules should be used?

The brief permits a representative model and leaves the exact
criteria unspecified.

I use fictional products with explicit eligibility rules.
Funder risk appetite is represented through supported products,
maximum LTV, applicant types and locations.

The sample catalogue defines the rules used by the application.

See [Sample products](assumptions.md#sample-products) and
[Sample funders](assumptions.md#sample-funders).

### 7. How should affordability be assessed without tax band or other income?

The request does not include the applicant's tax band, salary
or income and liabilities from other properties.

I use only the expected rent from the property being assessed,
with simplified ICR thresholds by applicant type.

This may produce different results from a fuller affordability
assessment.

See [Loan amount and affordability](assumptions.md#loan-amount-and-affordability).

### 8. Are product fees paid separately or added to the loan?

The brief does not explain whether the requested amount includes
financed product fees.

I assume fees are paid separately and use the requested loan amount
in both LTV and ICR calculations.

The service assesses that amount without changing it or suggesting
a smaller loan.

See [Loan amount and affordability](assumptions.md#loan-amount-and-affordability).

### 9. What happens when a product qualifies but no funder matches?

The brief separates eligibility from funding but leaves the response
representation open.

I retain the eligible product and explain why no funder matches.
This is different from an ineligible product whose funders have
not been evaluated.

See [Results and rejection reasons](assumptions.md#results-and-rejection-reasons).

### 10. Does funder support mean compatibility or available money?

The brief describes funder risk appetite but does not require checks
against available funding budgets.

I treat a funder match as compatibility with its configured rules.
Each matching funder is an alternative for the whole loan.

The service does not combine funders, check remaining budgets
or reserve money.

See [Sample funders](assumptions.md#sample-funders).

### 11. How much rejection detail should be returned?

The brief requires enough information for another system to understand
why an option is unavailable.

I return all failed applicable rules with stable codes and readable
explanations. Funder rejection details are visible for the fictional
catalogue.

Before using real funder rules, I would clarify which commercial
details may be disclosed.

See [Results and rejection reasons](assumptions.md#results-and-rejection-reasons).

### 12. Can the same case be assessed more than once?

Persistence, reassessment and case-reference uniqueness are unspecified.

The reference identifies the case in the calling system.
Each assessment uses the current request, allowing the same case
to be assessed again after its details change.

Previous results are not stored, and case-reference uniqueness
is not enforced.

See [Application data](assumptions.md#application-data).

### 13. How should thresholds and result ordering work?

The brief does not specify numerical boundary behaviour or ordering.

I use inclusive thresholds and decimal comparisons without rounding
values before eligibility checks.

Repeated assessments with unchanged input and catalogue data return
the same results, ordered by product and funder identifier.
This order does not imply a recommendation.

See [Validation and calculations](assumptions.md#validation-and-calculations)
and [Catalogue and ordering](assumptions.md#catalogue-and-ordering).

## When I would ask for clarification

The assumptions above are sufficient for this exercise.

Before using the service for real lending decisions, I would clarify
whether the result is preliminary eligibility or lending approval,
whether capital-and-interest mortgages require full repayment
affordability checks, and whether funder matching must account
for available funds.

I would also confirm who maintains the lending rules and which
funder rejection details can be shared with the caller.

## References

- Supplied exercise: Mortgage Product and Funder Matching.
- [Project assumptions](assumptions.md)
- [Domain research](domain-research.md)
- [Landbay lending criteria](https://landbay.co.uk/intermediaries/lending-criteria/)
- [Landbay ICR rules](https://help.landbay.co.uk/what-is-your-minimum-icr)
- [Landbay loan calculations](https://help.landbay.co.uk/how-do-you-calculate-max-loan-amount)