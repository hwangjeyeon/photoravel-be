FROM openjdk:17-jdk

EXPOSE 8080

ADD ./build/libs/*.jar photoravel-be-0.0.1-SNAPSHOT.jar

ENV TZ=Asia/Seoul

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "photoravel-be-0.0.1-SNAPSHOT.jar", "sh", "-c", "java -jar -Dspring.profiles.active=prod photoravel-be-0.0.1-SNAPSHOT.jar"]