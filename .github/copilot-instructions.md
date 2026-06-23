# Copilot Instructions

## Project overview
Acme Ponto Inteligente ("Smart Time Clock") is a REST API for employee time tracking. It allows companies to register employees and track their work hours — clock-in, clock-out, lunch breaks, and pauses.

## Tech stack
- Java 11, Spring Boot 2.0.3.RELEASE, Maven
- Testing: JUnit 5, Mockito 2.23.4

## Build, run, and test — use these exact commands
- This project builds on **Java 11**. Ensure JAVA_HOME points to a JDK 11 before any build or test; do not change the project's target version to make a build pass.
- Build: `mvn clean verify`
- Run all tests: `mvn test`
- Run a single test: `mvn test -Dtest=ClassName#methodName`
- Always run `mvn test` and confirm it passes before opening a PR.

## Architecture & structure
- Layered: controller → service → repository. Keep business logic in services, never in controllers.
- Packages: controllers in `[com.acme...controllers]`, services in `[...services]`, entities in `[...entities]`, DTOs in `[...dtos]`.
- DTOs are mapped manually; do not expose entities directly in controller responses.

## Coding conventions
- Constructor injection only; no field injection.
- Validate request bodies with Bean Validation annotations on the DTO, not with manual checks in the controller.
- Logging: use SLF4J. Never log request bodies, tokens, account numbers, or any customer PII.
- Keep domain terminology consistent with the existing code.

## Testing conventions
- Place tests under `src/test/java`, mirroring the package of the class under test; name them `ClassNameTest`.
- Cover the happy path AND edge/error cases, not just the happy path.
- Mock collaborators with Mockito; do not hit a real database in unit tests.
- Never weaken, delete, or disable an existing test to make the suite pass. If a test fails, diagnose the root cause and fix the real problem.
- Do not add trivial assertions (e.g. assertNotNull alone) to inflate coverage; assert real behavior.

## Things to avoid (require explicit human approval)
- Do not add or upgrade dependencies without flagging it in the PR.
- Do not change public API contracts (endpoints, request/response shapes).
- Do not modify files unrelated to the assigned task.
- Never commit secrets, credentials, or connection strings.

## Pull requests
- Keep each PR scoped to its issue.
- In the PR description, state what you changed and how you verified it (which tests you ran and their result).