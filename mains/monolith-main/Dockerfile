FROM openjdk:17-jdk
VOLUME /tmp
ARG JAR_FILE=build/libs/monolith-main-2.0.0.jar
COPY ${JAR_FILE} jiniaslog-2.jar
ENTRYPOINT ["java","-jar","/jiniaslog-2.jar"]