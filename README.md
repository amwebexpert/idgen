# idgen Backend

## Intellij & Kotlin

Right-click Projet (or F4) > Open module settings > Modules > Dependencies > Module SDK : ==> ADD the SDK kotlin.

## Commands

###### Build jar and launch locally:

    ./deploy-local.sh

###### Launch from Intelli-J: 

    - Main class: `JournalisationApplication`

## Url de l'application

    http://localhost:8080/index-backend.html

## Sagger API

    http://localhost:8080/swagger-ui.html

## REST API best practices
- https://www.baeldung.com/rest-api-pagination-in-spring
- https://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api
- https://hackernoon.com/restful-api-designing-guidelines-the-best-practices-60e1d954e7c9
- https://swagger.io/blog/api-design/api-design-best-practices/

## Database

Actually the database is H2 but could be Postgresql or anything else. The idea is to generate unique IDs even if
multiple instances of service or multiple instances of JVM run concurrently (assuming we would have one DB server well know by services)
