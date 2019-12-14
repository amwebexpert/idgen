./mvnw clean install
cf push idgen -p target/idgen-0-SNAPSHOT.jar
cf logs idgen
