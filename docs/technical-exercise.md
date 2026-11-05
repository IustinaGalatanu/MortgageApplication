# Mortgage Product and Funder Matching

## Context

Landbay is a specialist buy-to-let mortgage lender.

When a landlord applies for a mortgage, there may be a number of different mortgage products available to them. Different products have different lending criteria depending on characteristics of the borrower, property and proposed mortgage.

Landbay does not fund mortgages from its own balance sheet. We work with a number of institutional funders, such as banks and other financial institutions.

Different funders have different appetites for risk. The higher the risk, the higher the interest rate they need for returns. As a result, they may be willing to fund some types of mortgage or some Landbay products, but not others.

For Landbay to offer a mortgage, we therefore need to understand both:

- which mortgage products the application could be eligible for; and
- which funder or funders could support those products.

## The task

Build a **Java application using Spring Boot 4** that exposes an HTTP API.

Given the details of a proposed mortgage, the service should determine:

- which Landbay mortgage products the application could be eligible for;
- which funder or funders could support each eligible product;
- where an option is not available, enough information for another system to understand why.

A mortgage application will contain information including:

- a case reference;
- the amount the customer wants to borrow;
- the value of the property;
- the expected monthly rental income;
- the type of property;
- the type of applicant;
- the location of the property;
- whether the applicant has previous experience as a landlord.

Landbay offers a range of specialist buy-to-let mortgage products. These may target different types of property or borrower and may have different lending criteria. You should model a small representative set of products sufficient to demonstrate your solution. Where you need details that have not been specified, research the domain where useful and make and document reasonable assumptions.

You should also model a small number of institutional funders.

Funders should have different appetites. They may choose to support different products, different types of lending, or different levels of risk.

Your model of products and funders does not need to reproduce Landbay's real commercial arrangements. It should be realistic enough to demonstrate the problem meaningfully.

Some of the terminology or mechanics may be unfamiliar to you. You are free to research anything you need in order to understand the problem.

The application should run locally. You do not need to build a user interface, authentication, cloud infrastructure or integrations with other systems.

You may use either **Maven or Gradle**.

Aim to make the solution as **production-grade as you think is reasonably practical for a take-home exercise**. Where you deliberately stop short because of time, scope, infrastructure or deployment concerns, document what you would improve or add before relying on it in production.

## Design

We deliberately haven't prescribed:

- the structure of your API;
- the format of requests or responses;
- how products and funders should be represented;
- how products and funder appetite should be stored;
- how eligibility and funding rules should be structured;
- how unavailable products or funding options should be explained;
- how results should be ordered or prioritised.

These are part of the exercise.

There is deliberately some detail missing from the requirements.

Real software requirements are rarely complete. We are interested in what you notice, what assumptions you make and when you think something is important enough that you would ask for clarification.

Making reasonable assumptions is perfectly acceptable. Document them.

There isn't one expected architecture or solution.

We value clear, maintainable and supportable software over the amount of code written. Build what you think is appropriate for the problem.

## AI, documentation and search

You are explicitly encouraged to use the tools that you would use when working as a software engineer, including:

- AI coding agents such as Codex, Claude or equivalent;
- internet search;
- technical documentation;
- IDE tools;
- third-party libraries.

Using AI is not considered cheating.

However, the submitted solution is **your solution**.

You should understand the code you submit, be comfortable explaining the decisions behind it and take responsibility for its behaviour.

Please include the significant prompts you used while completing the exercise.

Also briefly describe how you used AI. Where relevant, we would be interested in examples where you changed or rejected something suggested by an AI tool.

We aren't assessing how elaborate your prompts are. We are interested in how you use the tools available to you to solve an engineering problem.

## Submission

Please provide:

- working source code;
- instructions for building and running the application;
- a short README describing your approach;
- assumptions you made;
- ambiguities or questions you noticed;
- anything significant you deliberately left out or would change before relying on the application in production;
- a brief description of how you used AI;
- the significant AI prompts you used.

Please make the project straightforward for us to build and run locally.

## Follow-up interview

Your submission will form part of a subsequent technical interview.

We'll discuss your approach and some of the decisions you made.

We may also introduce or change a requirement and work through how you would adapt your solution.

We're interested in how you reason about and evolve software you understand, rather than whether your initial implementation happens to predict every future requirement.