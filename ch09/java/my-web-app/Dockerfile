FROM adoptopenjdk:11-jre-hotspot

WORKDIR /usr/src/app

COPY ./target/my-web-app-1.0-SNAPSHOT.jar app.jar

# Include all dependencies in a lib directory
COPY ./target/dependency/*.jar ./lib/
COPY ./src/main/resources/static ./static

#CMD ["java", "-cp", "app.jar:./static/*", "com.example.mywebapp.MainApplication"]
CMD ["java", "-cp", "app.jar:lib/*", "com.example.mywebapp.MainApplication"]

