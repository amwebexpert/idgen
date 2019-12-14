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
process only requires a JDK to be available at command line. The final package is a runnable JAR and will serve the REST application at localhost:8080.

| Command                               | Comments               |
|---------------------------------------|------------------------|
| ./mvnw clean install                  | Build from the command line. This generates the `idgen-0-SNAPSHOT.jar` runnable jar into `target/` folder |
| java -jar target/idgen-0-SNAPSHOT.jar | Run application from the command line. This starts the Spring Boot application ready to serve requests at localhost:8080 |
| ./deploy-local.sh                     | Convenient script to build and launch locally. This also starts the Spring Boot application ready to serve requests at localhost:8080 |

### IntelliJ & Kotlin setup

* Right-click Project > Open module settings > Modules > Dependencies > Module SDK, Then add the kotlin SDK
* Launch main class from Intelli-J: `com.amwebexpert.idgen.Application`

### Eclipse & Kotlin setup
I'm an IntelliJ fan so I've not integrated Eclipse + Kotlin so far... so here are the instructions:
* https://kotlinlang.org/docs/tutorials/getting-started-eclipse.html

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
have been sufficient. However, I decided to use JPA involving 2 little tables (namespace having a 1 to many relationships with identifiers). Since H2 is
an in-memory database it delivers fast input/output and allows the offline storage if ever required (so best of both worlds in my opinion)

When looking at the final H2 solution we can see that:
* repositories a just interfaces
* code base is yet simple, in terms of Controller - Service - Repository
* code is easy to understand and maintain
* domain objects are mostly annotations
* with added memory cache (ehcache) we can improve performances as we avoid JPA calls to determine if namespace does exist (see @Cacheable annotation on IdGeneratorService)

#### Pros:

* An HashMap (or any other memory structure) is transient and does not survive server reboot as opposed to a small database
* Database can be useful to store other information like namespace creation time, identifier creation time, user-agent, ip address...
* Usage of the JPA ID generation mechanism (no code)
* Database unique constraints protection for namespace and identifiers
* Easy to scale the service by X orders of magnitude more traffic by having multiple service instances and/or JVM that would still 
guarantee unique constraint (all pointing to the single DB server instance)
* JPA Repositories are easy to write here by using interfaces convention (no code)
* By using @Transactional annotation we are protected from concurrency (simple optimistic locking by default) when multiple threads are 
trying to create the same namespace twice
* No need to set synchronized method in order to protect namespace creation collisions (which would be required with the 
HashMap approach even for only one JVM scenario)

#### Cons:

* Memory usage if increased because we are using JPA, H2, hibernate, transaction wrappers
* Slower than a plain old in-memory solution (HashMap or any memory structure)
* Repositories and configurations to write for the JPA / H2, although Spring Boot simplifies this process.

## Directions for future work

* ConcurrencyTestKt (src/test/kotlin) is a concurrency test starting 100 parallel requests: it shows that average REST call
duration is around 17 ms with the H2 approach and 14 ms for the pure in-memory structure. Any architecture or decisions changes may 
affect this kind of tests and give hints on impacts
* Add exception handlers for returning right HTTP 4xx and 5xx codes. For instance 409 Conflict special return code for
DataIntegrityViolationException (when concurrent calls try to create the same namespace)
* Add validations for the namespace (let's say we do not want weird characters)
* It would probably be more appropriate to use POST verb and return HTTP 201 (as opposed to GET and HTTP 200)
* My first thought was to make usage of UUID (instead of long datatype) in case we would like to expose APIs like 
namespace maintenance (CRUD operations). However performance would then be affected since 
UUID generation is slower than integers generation, so, not sure I would go the UUID way, see 
discussions here: https://stackoverflow.com/questions/7114694/should-i-use-uuids-for-resources-in-my-public-api.

##### Note regarding H2:

* Actually the database is H2 but could be PostgreSQL or anything else. H2 is just very easy to start up with and has the
in-memory built-in capability. 
* Actual H2 setup (jdbc:h2:mem:testdb) does not survive server reboot, but it's a matter of 
configuration as explained here: http://www.h2database.com/html/cheatSheet.html. The only requirement is a disk space.
* So even with H2 we can persist the database content and survive server reboot. There exists also a server mode to
simulate a real instance of a shared database instance for multiple REST service instances.
* Note that if we want the numeric part to restart at 1 each time a new namespace is created it would possible by simply creating
new dedicated DB sequence at namespace creation time

##### Note regarding scaling:

* For 1 deployed instance (https://idgen.cfapps.io/) I tested that H2 approach can execute around 40 concurrent requests in 1 second.
Just ask me for tests since I'm billed by server usage :-) I can also demonstrate the full test process usage.

Some executions stats (ConcurrencyTest.kt targeting https://idgen.cfapps.io/)

| Execution #  | Stats                                                      |
|--------------|------------------------------------------------------------|
|     1        | 100 calls executed in: 2757 ms which means 27 ms per call  |
|     2        | 100 calls executed in: 2047 ms which means 20 ms per call  |
|     3        | 100 calls executed in: 2992 ms which means 29 ms per call  |
|     4        | 100 calls executed in: 2158 ms which means 21 ms per call  |
|     5        | 100 calls executed in: 1414 ms which means 14 ms per call  |


* For more than one application instances, we would need to:
    * use the H2 Server mode (instead of In-memory mode)
    * dedicate a server instance as the H2 server instance and allow TCP communication at a dedicated port on that instance
    * any additional instance should point to that TCP+port. For instance: jdbc:h2:tcp://idgen.cfapps.io:8084/~/sample

##### References

- https://help.talend.com/reader/ZAKVeO5MqWsrKfPqGglBkg/X_YjOtoB2bAUr7JPptwkEg
- https://stackoverflow.com/a/21672625/704681
- https://www.baeldung.com/spring-boot-access-h2-database-multiple-apps
- http://www.h2database.com/html/tutorial.html#using_server
- http://www.h2database.com/html/features.html#database_url
- https://www.h2database.com/html/commands.html#create_sequence
- https://dba.stackexchange.com/a/126856/32720
- Backup and restore: http://h2database.com/html/tutorial.html#upgrade_backup_restore
- https://stackoverflow.com/a/21078872/704681
