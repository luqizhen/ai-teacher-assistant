# Research Decisions: Piano Teacher Management System

**Date**: 2025-01-30  
**Purpose**: Document technology choices and research findings for implementation

## Database Technology

**Decision**: H2 for development, PostgreSQL for production  
**Rationale**: H2 provides zero-configuration development with in-memory and file-based options. PostgreSQL offers robust production features, better performance, and wider ecosystem support. Spring Data JPA makes switching between databases seamless.  
**Alternatives considered**: 
- MySQL: Similar to PostgreSQL but less advanced JSON support
- SQLite: Limited for concurrent access, not ideal for web applications

## Frontend Architecture

**Decision**: Static HTML/CSS/JS with vanilla JavaScript  
**Rationale**: Simplicity meets requirements - no complex state management needed, faster load times, easier Chinese character handling, and avoids build complexity. Modern JavaScript features (fetch API, modules) provide sufficient functionality.  
**Alternatives considered**:
- React: Overkill for this application size, adds unnecessary complexity
- Vue.js: Good option but adds build tooling requirements
- Angular: Too heavyweight for simple CRUD application

## Internationalization Approach

**Decision**: Spring Boot MessageSource + JavaScript i18n  
**Rationale**: Spring Boot's built-in MessageSource handles server-side internationalization seamlessly. Client-side JavaScript will handle UI text switching. This dual approach provides complete Chinese language support with minimal overhead.  
**Alternatives considered**:
- Frontend-only i18n: Would require loading all translations upfront
- Backend-only i18n: Limited flexibility for dynamic UI elements

## Authentication Strategy

**Decision**: Simple form-based authentication with session management  
**Rationale**: Single-teacher application doesn't need complex OAuth or JWT. Session-based auth is simpler, more secure for this use case, and integrates well with Spring Security.  
**Alternatives considered**:
- JWT: Unnecessary complexity for single-user application
- OAuth: Overkill for internal application without third-party integration

## Scheduling Algorithm

**Decision**: Simple overlap detection with historical pattern prioritization  
**Rationale**: Balances functionality with complexity. Finds available time slots through overlap analysis, then prioritizes based on previous successful lesson times. Provides 80% of value with 20% of complexity compared to ML approaches.  
**Alternatives considered**:
- ML-based recommendations: Overkill for small scale, requires significant data
- Simple overlap only: Lacks intelligence for better user experience

## Docker Deployment Strategy

**Decision**: Multi-container Docker Compose setup  
**Rationale**: Separate containers for backend and frontend provide isolation, independent scaling, and easier maintenance. Docker Compose orchestrates the complete application with a single command.  
**Alternatives considered**:
- Single container: Would require serving static files from Spring Boot, less flexible
- Kubernetes: Overkill for single-teacher deployment

## Performance Optimization

**Decision**: Database indexing + frontend caching + CDN-ready assets  
**Rationale**: Database indexes on frequently queried fields (student names, lesson dates). Frontend implements browser caching for static assets. Asset structure supports CDN deployment if needed.  
**Alternatives considered**:
- Redis caching: Unnecessary complexity for this scale
- Complex frontend bundling: Not needed for vanilla JS approach

## Testing Strategy

**Decision**: Layered testing with JUnit 5, Mockito, and Selenium  
**Rationale**: Unit tests for business logic (JUnit 5 + Mockito), integration tests for API endpoints (Spring Boot Test), and E2E tests for user workflows (Selenium). Provides comprehensive coverage without over-engineering.  
**Alternatives considered**:
- Only unit tests: Insufficient for API and UI validation
- Only E2E tests: Too slow for rapid development feedback

## Security Considerations

**Decision**: Spring Security with HTTPS, input validation, and CSRF protection  
**Rationale**: Spring Security provides comprehensive security out of the box. HTTPS protects data in transit. Input validation prevents injection attacks. CSRF protection for form submissions.  
**Alternatives considered**:
- Custom security implementation: Prone to errors and maintenance burden
- No security: Unacceptable for any web application

## Chinese Character Support

**Decision**: UTF-8 encoding throughout + proper font handling  
**Rationale**: UTF-8 is the standard for Unicode character support. Spring Boot and modern browsers handle UTF-8 natively. CSS will ensure proper Chinese fonts are available.  
**Alternatives considered**:
- GB2312 encoding: Legacy standard, less compatible
- Image-based text: Inflexible and poor accessibility
