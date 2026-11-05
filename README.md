
# Mortgage Product and Funder Matching

A Java Spring Boot API for assessing buy-to-let mortgage applications against a catalogue of mortgage products and institutional funders.

The service identifies eligible products, matches them with suitable funders and explains why other options are unavailable.

This is an eligibility assessment, not a final mortgage offer or lending approval.

## Tech Stack

Java 21 · Spring Boot 4 · Maven · Spring Web MVC · Bean Validation · Lombok · JUnit

## Getting Started

**Prerequisites:** Java 21. No database or external services are required.

From the project root directory:

```bash
./mvnw clean package
./mvnw spring-boot:run
```

The API runs at `http://localhost:8080`.

## API

**POST** `/api/mortgage-assessments`

### Example Request

```json
{
  "caseReference": "CASE-001",
  "loanAmount": 150000,
  "propertyValue": 250000,
  "expectedMonthlyRent": 2000,
  "propertyType": "HMO",
  "applicantType": "INDIVIDUAL",
  "propertyLocation": "ENGLAND",
  "hasLandlordExperience": true,
  "repaymentType": "INTEREST_ONLY"
}
```

### Example Responses — HTTP 200 OK

The following three examples are shortened excerpts from the same assessment response, illustrating different outcomes.

#### 1. Product Eligible — Funder Found

The HMO product meets all eligibility criteria, and `FUNDER_B` can support it.

```json
{
  "productId": "HMO_5Y_75",
  "eligibleProduct": true,
  "rejectionReasons": [],
  "funderAssessments": [
    {
      "funderId": "FUNDER_B",
      "eligibleFunder": true,
      "rejectionReasons": []
    }
  ]
}
```

#### 2. Product Eligible — Funder Rejected

The same HMO product is eligible, but `FUNDER_C` only supports limited company applicants.

```json
{
  "funderId": "FUNDER_C",
  "eligibleFunder": false,
  "rejectionReasons": [
    {
      "rejectionType": "APPLICANT_TYPE_NOT_SUPPORTED",
      "message": "Applicant type INDIVIDUAL is not supported by this funder."
    }
  ]
}
```

#### 3. Product Rejected — No Funder Assessment

The standard mortgage product is rejected because the application concerns an HMO property. Funders are therefore not evaluated for this product.

```json
{
  "productId": "STANDARD_2Y_75",
  "eligibleProduct": false,
  "rejectionReasons": [
    {
      "rejectionType": "PROPERTY_TYPE_NOT_SUPPORTED",
      "message": "Property type HMO is not supported by this product."
    }
  ],
  "funderAssessments": []
}
```

The complete response includes `caseReference` and assessments for all configured products and their applicable funders.

A valid application with no eligible products still returns HTTP 200 with the corresponding rejection reasons.

### Invalid Requests — HTTP 400 Bad Request

Invalid requests are handled centrally using `@ControllerAdvice`.

For example, a negative loan amount (`"loanAmount": -1000`) returns:

```json
{
  "message": "Request validation failed",
  "fieldErrors": {
    "loanAmount": "must be greater than 0"
  }
}
```

An unsupported enum value, such as `"propertyLocation": "ENGLANDD"`, returns:

```json
{
  "message": "Malformed JSON request or unsupported enum value",
  "fieldErrors": {}
}
```

Invalid input returns HTTP 400, while a valid application that fails lending criteria returns a normal HTTP 200 assessment result.

For example, `NORTHERN_IRELAND` is a valid location but is not supported by the configured products.

## How It Works

I separated the assessment into two stages.

### 1. Product Assessment

Every product is evaluated against the application's details, including:

- Property type and location
- Applicant type and repayment type
- Landlord experience
- Loan amount and property value
- Loan-to-Value (LTV)
- Interest Coverage Ratio (ICR)

A product is eligible only when all applicable rules pass. Otherwise, the service returns the reasons for rejection.

### 2. Funder Assessment

Only eligible products proceed to funder assessment.

Each funder is checked against its supported products, applicant types, locations and maximum LTV.

A product may be eligible even when no funder can support it.

The rules are implemented separately, making them easier to test, maintain and extend.

## Sample Products and Funders

The application includes four fictional mortgage products:

| Product ID | Property Type | Pay Rate | Max LTV |
|---|---|---:|---:|
| STANDARD_2Y_75 | STANDARD | 5.0% | 75% |
| STANDARD_5Y_75 | STANDARD | 5.5% | 75% |
| HMO_5Y_75 | HMO | 6.0% | 75% |
| MUFB_5Y_70 | MUFB | 6.0% | 70% |

Three funders have different lending criteria:

- **FUNDER_A:** Standard products, up to 65% LTV.
- **FUNDER_B:** Standard and HMO products, up to 75% LTV.
- **FUNDER_C:** HMO and MUFB products, limited companies only, up to 70% LTV.

These are representative examples rather than actual Landbay commercial arrangements.

## Lending Calculations

The application uses `BigDecimal` for monetary calculations.

**Loan-to-Value (LTV)**

```text
LTV = Loan Amount / Property Value
```

A mortgage must meet the maximum LTV permitted by the product and funder.

**Interest Coverage Ratio (ICR)**

ICR measures whether the rental income covers the assessed mortgage interest.

```text
Required Annual Rent =
Loan Amount × Stress Rate × Minimum ICR
```

The minimum ICR is 145% for individuals and 125% for limited companies.

For two-year fixed products, the stress rate is the higher of 5.5% or the pay rate plus 2 percentage points. Five-year fixed products use the pay rate.

## Assumptions and Scope

Each assessment covers one property and one applicant classification. Both interest-only and capital-and-interest repayment types are supported.

Applications are not stored, and credit checks, identity verification, legal checks and property valuations are outside scope.

Further assumptions and limitations are documented separately.

## Testing

Automated tests cover product and funder rules, lending thresholds, service behaviour, request validation and exception handling.

Run the tests using:

```bash
./mvnw clean test
```

## Documentation

- [Domain Research](docs/domain-research.md)
- [Assumptions](docs/assumptions.md)
- [Requirements Analysis and Questions](docs/requirements-analysis.md)
- [Production Considerations](docs/production-considerations.md)
- [AI Usage](docs/ai-usage.md)
