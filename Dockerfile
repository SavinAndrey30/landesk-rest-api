FROM openjdk:11
ADD target/tech-support-automation-0.0.1-SNAPSHOT.jar docker-landesk.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "docker-landesk.jar"]