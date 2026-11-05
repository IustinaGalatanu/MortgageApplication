
# Mortgage Product and Funder Matching

A Java Spring Boot API that checks a buy-to-let mortgage application against a small catalogue of mortgage products and funders. It returns eligible options and explains why others are rejected.

This is an eligibility assessment, not a mortgage offer or final lending decision.

## Tech Stack

Java 21 · Spring Boot 4 · Maven · Spring Web MVC · Bean Validation · JUnit · Lombok

## Getting Started

**Requirement:** Java 21. No database or external services are needed.

From the project root (the directory containing `pom.xml`):

```bash
./mvnw clean package
./mvnw spring-boot:run
```

The API runs at `http://localhost:8080` by default.

To run the tests separately:

```bash
./mvnw test
```

## API

**POST /api/mortgage-assessments**

### Example Request

This HMO application has a 72% LTV. It qualifies for the HMO product, but only `FUNDER_B` can support it under the sample funder rules.

```json
{
  "caseReference": "CASE-001",
  "loanAmount": 180000,
  "propertyValue": 250000,
  "expectedMonthlyRent": 2000,
  "propertyType": "HMO",
  "applicantType": "INDIVIDUAL",
  "propertyLocation": "ENGLAND",
  "hasLandlordExperience": true,
  "repaymentType": "INTEREST_ONLY"
}
```

### Testing the API

Using Postman:

`POST http://localhost:8080/api/mortgage-assessments`

Alternatively, using curl:

```bash
curl -i -X POST http://localhost:8080/api/mortgage-assessments \
  -H "Content-Type: application/json" \
  -d '{
    "caseReference": "CASE-001",
    "loanAmount": 180000,
    "propertyValue": 250000,
    "expectedMonthlyRent": 2000,
    "propertyType": "HMO",
    "applicantType": "INDIVIDUAL",
    "propertyLocation": "ENGLAND",
    "hasLandlordExperience": true,
    "repaymentType": "INTEREST_ONLY"
  }'
```

### Example Eligible Result — HTTP 200

The following is an excerpt showing the eligible product and funder (not the complete API response):

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

The full response includes `caseReference` and assessments for all four products.

The other three products are ineligible, and `FUNDER_A` and `FUNDER_C` are rejected for the HMO product, with their reasons included.

A valid request with no eligible products still returns HTTP 200 with rejection reasons.



### Invalid Request — HTTP 400

Invalid input is handled by a central `@ControllerAdvice`. Invalid or missing values return HTTP 400, while failing lending criteria is a normal HTTP 200 assessment result.

**Example 1 — Negative loan amount (`-1000`)**

```json
{
  "message": "Request validation failed",
  "fieldErrors": {
    "loanAmount": "must be greater than 0"
  }
}
```

**Example 2 — Invalid property location (`ENGLANDD`)**

```json
{
  "message": "Invalid request body",
  "fieldErrors": {
    "propertyLocation": "Invalid value: ENGLANDD"
  }
}
```

## How It Works

1. **Product assessment:** Every product is checked against property type, location, applicant type, repayment type, experience, loan limits, minimum property value, LTV and ICR. Failed rules produce rejection reasons.
2. **Funder assessment:** Only eligible products are checked against each funder's supported products, locations, applicant types and LTV limits. A product can be eligible even when no funder supports it.

Rules are implemented separately, making individual checks easier to test and change. Products and funders are configured in memory.

## Sample Catalogue

| Product ID | Property | Pay Rate | Max LTV | Experience Required |
|---|---|---:|---:|---|
| `STANDARD_2Y_75` | Standard | 5.0% | 75% | No |
| `STANDARD_5Y_75` | Standard | 5.5% | 75% | No |
| `HMO_5Y_75` | HMO | 6.0% | 75% | Yes |
| `MUFB_5Y_70` | MUFB | 6.0% | 70% | Yes |

- `FUNDER_A`: Standard products, up to 65% LTV.
- `FUNDER_B`: Standard and HMO products, up to 75% LTV.
- `FUNDER_C`: HMO and MUFB products, limited companies only, up to 70% LTV.

These are fictional lending products and funders.

LTV and ICR use `BigDecimal` and inclusive thresholds. The two-year ICR stress rate is the greater of 5.5% or the pay rate + 2 percentage points; five-year products use the pay rate.

## Scope and Testing

The service supports interest-only and capital-and-interest applications, but does not calculate repayment schedules. It does not store cases or perform credit, identity, legal or valuation checks.

These decisions and other assumptions are documented in `docs/`.

Automated tests cover validation, lending-rule boundaries, product/funder matching, services and exception handling.

## Documentation

- [Domain Research](docs/domain-research.md)
- [Assumptions](docs/assumptions.md)
- [Requirements Analysis and Questions](docs/requirements-analysis.md)
- [Production Considerations](docs/production-considerations.md)
- [AI Usage](docs/ai-usage.md)
