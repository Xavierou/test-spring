FROM openjdk
ARG JAR_FILE=target/*.jar
COPY ./target/Bookstore-0.0.1-SNAPSHOT.jar bookstore.jar
ENTRYPOINT ["java", "jar", "bookstore.jar"]
