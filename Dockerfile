FROM openjdk:17 as build
WORKDIR /app
COPY . ./
RUN ./mvnw clean package

FROM openjdk:17.0.2-jdk-slim
WORKDIR /app
COPY --from=build /app/target/english_test-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "english_test-0.0.1-SNAPSHOT.jar"]
EXPOSE 2022