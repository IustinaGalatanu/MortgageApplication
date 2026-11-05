# Production Considerations

The current application focuses on mortgage product eligibility and funder matching. It uses an in-memory catalogue and evaluates each request independently.

If I were to develop this into a production system, I would prioritise the following improvements.

## 1. Database & Application History

I would introduce a PostgreSQL database to store mortgage applications, assessment results, products and funder criteria.

This would allow brokers to save applications, revisit previous assessments and track their progress without submitting the same information again.

## 2. Broker Authentication & Access Control

I would add authentication so that each mortgage broker has their own account.

Brokers would only be able to access applications belonging to their organisation, while administrators could manage products and lending criteria.

I would also introduce role-based permissions to control who can view, create or modify information.

## 3. Configurable Lending Criteria

Currently, products and funders are configured directly in Java.

I would move their criteria into a manageable configuration system, allowing authorised users to update interest rates, LTV limits and funder preferences without changing application code.

Changes would be versioned so previous decisions could still be explained.

## 4. Audit Trail & Decision History

I would record which rules and catalogue versions were used for each assessment.

This would make it possible to reproduce historical decisions, investigate unexpected results and explain why an application was eligible at a particular time.

## 5. Eligibility Suggestions

Rather than only explaining why an application was rejected, I would explore a **"What would make this eligible?"** feature.

For example, the system could calculate the maximum loan supported by the LTV and ICR limits, helping brokers understand whether adjusting the requested amount could create an eligible option.

Any suggestions would still need to satisfy both product and funder criteria.

## 6. Testing Changes Before Release

Before activating new lending criteria, I would test them against historical applications.

This would show how many previous decisions would change and help identify unexpected consequences before the new rules go live.

## 7. Security, Monitoring & Reliability

Finally, I would introduce stronger API security, structured logging, monitoring and automated deployment checks.

I would also review how sensitive applicant information is stored and accessed, ensuring the system remains reliable and maintainable as usage increases.

## Priorities

I would start with **data persistence, broker access control and configurable criteria**, as these provide the foundation for a usable production system.

Decision history, eligibility suggestions and testing future rule changes would follow once that foundation is in place.