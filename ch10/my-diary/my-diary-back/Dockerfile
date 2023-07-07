FROM openjdk:11-jdk AS builder
COPY ./rollingpaper/gradlew .
COPY ./rollingpaper/gradle gradle
COPY ./rollingpaper/build.gradle .
COPY ./rollingpaper/settings.gradle .
COPY ./rollingpaper/src src

RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM openjdk:11-jdk
COPY --from=builder build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]