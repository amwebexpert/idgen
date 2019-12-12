./mvnw clean install -DskipTests
java -Dspring.profiles.active=development -jar target/idgen-0-SNAPSHOT.jar
