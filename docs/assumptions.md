# Assumptions

## Scope

Each request assesses one proposed buy-to-let purchase for one property.

The service checks product eligibility and funder compatibility.
A successful match is not a mortgage offer or final lending approval.

Remortgages, product transfers, joint-applicant assessments and property
portfolio assessments are outside this version.

Credit history, identity, legal checks and independent valuations are
not performed. The service uses the information supplied in the request.

## Application data

Required fields are:

- Case reference.
- Loan amount.
- Property value.
- Expected monthly rent.
- Property type.
- Applicant type.
- Property location.
- Previous landlord experience.
- Repayment type.

All amounts are in GBP. Loan amount and property value must be greater
than zero. Expected monthly rent can be zero or greater (zero rent is
accepted as valid input and fails the ICR affordability rule).

Monetary inputs support up to two decimal places. Additional non-zero
decimal places are rejected rather than rounded.

The case reference must contain non-whitespace text and is returned
unchanged. The same case can be assessed again with updated details.
Cases and previous results are not stored.

Landlord experience is a required boolean. A missing value is not
treated as false. A value of true indicates previous experience,
without establishing a minimum duration.

## Supported categories

| Field | Values |
| --- | --- |
| Applicant type | INDIVIDUAL, LIMITED_COMPANY |
| Property type | STANDARD, HMO, MUFB |
| Location | ENGLAND, WALES, SCOTLAND, NORTHERN_IRELAND |
| Repayment type | INTEREST_ONLY, CAPITAL_AND_INTEREST |

Limited companies are treated as one category, without distinguishing
SPVs from trading companies.

Property classifications are supplied by the caller. Bedroom counts,
unit counts, licences and postcode restrictions are not assessed.

A recognised category can still be unsupported by a product.
For example, Northern Ireland is valid input even though none of
the sample products supports it.

## Repayment types

INTEREST_ONLY means monthly payments cover interest, with the principal
remaining payable at the end unless repaid earlier.

CAPITAL_AND_INTEREST means monthly payments cover interest and gradually
repay the principal.

Each product specifies which repayment types it accepts.

The service checks repayment-type compatibility but does not calculate
monthly repayments or produce repayment schedules. Mortgage term is
therefore not required in this version.

ICR measures rental coverage of assessed interest for both repayment
types. It does not establish whether rent covers the full monthly
capital-and-interest payment.

## Loan amount and affordability

Product fees are assumed to be paid separately. The requested loan
amount is used in both LTV and ICR calculations.

The service assesses the amount requested without automatically
reducing it or suggesting an alternative loan.

Only rent from the property being assessed is included. Personal
income and rent from other properties are not considered.

Calculations:

- LTV = loan amount / property value.
- Monthly assessed interest = loan amount × annual stress rate / 12.
- ICR = monthly rent / monthly assessed interest.

Rates and ratios use decimal units: 5% = 0.05 and 125% = 1.25.

The applicant's tax band is not collected. The sample catalogue uses
these simplified minimum ICR thresholds:

| Applicant type | Minimum ICR |
| --- | ---: |
| INDIVIDUAL | 145% |
| LIMITED_COMPANY | 125% |

Using 145% for all individuals may reject cases that a more detailed
tax-band assessment would accept.

ICR is evaluated separately for each product using its stress rate.

## Sample products

Products and funders are fictional examples.

| Product ID | Property type | Fixed period | Pay rate | Max LTV | Experience required | Repayment types |
| --- | --- | --- | ---: | ---: | --- | --- |
| STANDARD_2Y_75 | STANDARD | 2 years | 5.0% | 75% | No | Both |
| STANDARD_5Y_75 | STANDARD | 5 years | 5.5% | 75% | No | Both |
| HMO_5Y_75 | HMO | 5 years | 6.0% | 75% | Yes | INTEREST_ONLY |
| MUFB_5Y_70 | MUFB | 5 years | 6.0% | 70% | Yes | INTEREST_ONLY |

The fixed period describes the initial interest-rate period,
not the full mortgage term.

Stress rates are calculated as follows:

- Two-year fixed: max(5.5%, pay rate + 2 percentage points).
- Five-year fixed: pay rate.

All products accept both applicant types and loan amounts from
£50,000 to £1,000,000 inclusive.

| Property type | Minimum property value | Supported locations |
| --- | ---: | --- |
| STANDARD | £65,000 | England, Wales, Scotland |
| HMO | £120,000 | England, Wales |
| MUFB | £120,000 | England, Wales |

The standard products overlap so that an application can qualify
for multiple products with different stress rates.

## Sample funders

| Funder ID | Supported products | Max LTV | Applicant types | Locations |
| --- | --- | ---: | --- | --- |
| FUNDER_A | Both STANDARD products | 65% | Both | England, Wales, Scotland |
| FUNDER_B | Both STANDARD products and HMO_5Y_75 | 75% | Both | England, Wales |
| FUNDER_C | HMO_5Y_75 and MUFB_5Y_70 | 70% | LIMITED_COMPANY only | England, Wales |

All applicable funder rules must pass.

Funders accept the repayment types supported by their listed products
and rely on the product's ICR assessment. They do not apply additional
repayment-type or ICR restrictions.

Each matching funder is an alternative for the whole loan.
Funding is not split, allocated or reserved, and available funding
capacity is not checked.

## Results and rejection reasons

All products are evaluated, and all failed product rules are returned.

Funders are evaluated only for eligible products. The response includes
matching funders and reasons why the others do not match.

The result distinguishes:

- Ineligible product: funding not evaluated.
- Eligible product with matching funders.
- Eligible product without matching funders.

An eligible product remains in the response even when no funder matches.

Rejection reasons include stable codes, readable messages and relevant
actual and required values. Unsupported repayment types use
REPAYMENT_TYPE_NOT_SUPPORTED.

Funder rejection details are visible to the caller in this exercise.

## Validation and calculations

Invalid or missing input prevents assessment. A valid application
that fails lending rules is a normal assessment result.

All minimum and maximum thresholds are inclusive.

Calculations use decimal arithmetic. Eligibility decisions do not use
rounded display values.

Where possible, comparisons avoid division:

- loan amount <= property value × maximum LTV.
- monthly rent × 12 >= loan amount × stress rate × minimum ICR.

## Catalogue and ordering

Products and funders come from a local, version-controlled catalogue.
Rates are not updated from external services.

The same input and catalogue produce the same result.

Results are ordered by identifier. This order does not indicate
which product is cheapest or most suitable.

## References

- [Landbay lending criteria](https://landbay.co.uk/intermediaries/lending-criteria/)
- [Landbay ICR rules](https://help.landbay.co.uk/what-is-your-minimum-icr)
- [Landbay loan calculations](https://help.landbay.co.uk/how-do-you-calculate-max-loan-amount)
- [Landbay fees](https://help.landbay.co.uk/what-fees-are-required-for-a-case)
- [Landbay HMO and MUFB criteria](https://help.landbay.co.uk/do-you-accept-mufb-hmo-enquiries)