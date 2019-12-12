# idgen Backend

## Intellij & Kotlin setup

Right-click Projet (or F4) > Open module settings > Modules > Dependencies > Module SDK

- Then add the kotlin SDK

## Commands

###### Build jar and launch locally:

    ./deploy-local.sh

###### Launch from Intelli-J: 

    - Main class: `JournalisationApplication`

## Welcome page listing other URLs:

    http://localhost:8080/index.html

## Sagger API

Swagger is really easy to integrate with SpringBoot and useful for documenting and testing REST.

    http://localhost:8080/swagger-ui.html

## REST API best practices
- https://www.baeldung.com/rest-api-pagination-in-spring
- https://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api
- https://hackernoon.com/restful-api-designing-guidelines-the-best-practices-60e1d954e7c9
- https://swagger.io/blog/api-design/api-design-best-practices/

## Explanation of any key tradeoffs made in this approach 

The database was not required because for the small requirements a little in-memory HashMap (namespace as key and collection of IDs as value) would
have been sufficient. However I decided to use JPA with hibernate involving 2 little tables (namespace having a 1 to many relationship with identifiers)

Pros:

    - Usage of the ID generation mechanism (no code)
    - Database constraints protect unicity for namespace and identifiers
    - An HashMap (or any other memory) is transient and does not survive server reboot as opposed to a small database
    - Easy to scale the service by X orders of magnitude more traffic by having multiple service instances and/or JVM that would still guarantee unicity (all pointing to the single DB server instance)
    - JPA Repositories are easy to write here by using interfaces convention (no code)
    - By using @Transactional annotation we are protected from concurency (simple optimistic locking by default) when multiple threads try to create same namespace twice

Cons:

    - Memory usage if increased because we are using JPA, hibernate, transaction wrappers, etc.
    - Slowler than a pure in-memory solution (HashMap or any memory structure) 
    - More code and configurations to write for the JPA / H2 / Hibernate, although Spring Boot simplify this process

Actually the database is H2 but could be PostgreSQL or anything else. H2 is just very easy to startup with. The idea is to generate unique IDs even if
multiple instances of service or multiple instances of JVM run concurrently 
