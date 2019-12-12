# Namespace id generator

REST Backend API for identifiers generation within namespaces

## Technologies
* Kotlin language
* Spring Boot
* Spring Data JPA
* Swagger (API doc)
* H2

## Setup instructions

Since the project is Maven based and uses Maven Wrapper (https://github.com/takari/maven-wrapper) there is no need to install any tooling and the build
process only require a JDK to be available at command line. The final package is a runnable JAR and will serve the REST application at localhost:8080.

| Command                               | Comments               |
|---------------------------------------|------------------------|
| ./mvnw clean install                  | Build from the commandline. This generate the `idgen-0-SNAPSHOT.jar` runnable jar into `target/` folder |
| java -jar target/idgen-0-SNAPSHOT.jar | Run application from the commandline. This starts the Spring Boot application ready to serve requests at localhost:8080 |
| ./deploy-local.sh                     | Convenient script to build and launch locally. This also starts the Spring Boot application ready to serve requests at localhost:8080 |


### Intellij & Kotlin setup

* Right-click Project > Open module settings > Modules > Dependencies > Module SDK, Then add the kotlin SDK
* Launch main class from Intelli-J: `com.amwebexpert.idgen.Application`

## Welcome page listing other URLs:

| URL                                   | Description               |
|---------------------------------------|---------------------------|
| http://localhost:8080                 | Welcome page |
| http://localhost:8080/index.html      | Welcome page |
| http://localhost:8080/swagger-ui.html | Swagger is really easy to integrate with SpringBoot and useful for documenting and testing REST APIs |
| http://localhost:8080/h2-console      | The H2 console is the tool to view and query persisted data using browser. See notes below for login.|

Note for H2 console login:

| Info     | Value               |
|----------|---------------------|
| Jdbc URL | jdbc:h2:mem:testdb  |
| Username | sa                  |
| Password | [leave blank]       |

## Explanation of any key tradeoffs made in this approach 

The database was not required because for the small requirements a little in-memory HashMap (namespace as key and collection of IDs as value) would
have been sufficient. However I decided to use JPA involving 2 little tables (namespace having a 1 to many relationship with identifiers). Since H2 is
an in-memory database it delivers fast input/output and allows the offline storage if ever required (so best of both worlds in my opinion)

When looking at the final H2 solution we can see that:
* repositories a just interfaces
* code base is yet simple, in terms of Controller - Service - Repository
* code is easy to understand and maintain
* domain objects are mostly annotations

#### Pros:

* Usage of the JPA ID generation mechanism (no code)
* Database unique constraints protection for namespace and identifiers
* An HashMap (or any other memory structure) is transient and does not survive server reboot as opposed to a small database
* Easy to scale the service by X orders of magnitude more traffic by having multiple service instances and/or JVM that would still 
guarantee unique constraint (all pointing to the single DB server instance)
* JPA Repositories are easy to write here by using interfaces convention (no code)
* By using @Transactional annotation we are protected from concurrency (simple optimistic locking by default) when multiple threads are 
trying to create same namespace twice
* No need to set synchronized method in order to protect namespace creation collisions (which would be required with the 
HashMap approach even for only one JVM scenario)

#### Cons:

* Memory usage if increased because we are using JPA, H2, hibernate, transaction wrappers
* Slower than a plain old in-memory solution (HashMap or any memory structure)
* Repositories and configurations to write for the JPA / H2, although Spring Boot simplify this process.

## Directions for future work

* Scaling the application
    * should be as simple as deploying more instances of the application and configuring load balancing stuff depending on the target cloud platform
    * a single database instance would be enough for multiple app instances
* My first though was to make usage of UUID (instead of long datatype) in case we would like to expose APIs like 
namespace maintenance (CRUD operations). However performance would then be affected since 
UUID generation is slower than integers generation, so not sure I would go the UUID way, see 
discussions here: https://stackoverflow.com/questions/7114694/should-i-use-uuids-for-resources-in-my-public-api.


##### Note regarding H2:

Actually the database is H2 but could be PostgreSQL or anything else. H2 is just very easy to startup with.
The H2 Setup (jdbc:h2:mem:testdb) does not survive server reboot, but it's a matter of configuration as explained here:
http://www.h2database.com/html/cheatSheet.html
So even with H2 we can persist the database content and survive server reboot. There exists also a server mode to
simulate a real instance of a shared database instance for multiple REST service instances.
