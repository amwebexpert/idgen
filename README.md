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

    http://localhost:8080/swagger-ui.html

## REST API best practices
- https://www.baeldung.com/rest-api-pagination-in-spring
- https://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api
- https://hackernoon.com/restful-api-designing-guidelines-the-best-practices-60e1d954e7c9
- https://swagger.io/blog/api-design/api-design-best-practices/

## Database

Actually the database is H2 but could be PostgreSQL or anything else. The idea is to generate unique IDs even if
multiple instances of service or multiple instances of JVM run concurrently (assuming we would have one DB server well know by services)
