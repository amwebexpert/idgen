./mvnw clean install
cf push idgen -p target/idgen-0-SNAPSHOT.jar
# cf logs idgen

echo Starting browser at server welcome page
google-chrome --user-data-dir=/tmp https://idgen.cfapps.io/
