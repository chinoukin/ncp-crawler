FROM openjdk:8-jdk-alpine
VOLUME /
ENV JVM_OPTS '-Xmx256m'
ENV JAVA_OPTS '--server.port=8080'
COPY target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar /app.jar ${JAVA_OPTS} ${0} ${@}"]
