FROM openjdk:8-jdk-alpine
VOLUME /tmp
ENV JVM_OPTS '-Xmx256m'
ENV JAVA_OPTS '--server.port=1443 --server.http.port=8080'
COPY target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar /app.jar ${JAVA_OPTS} ${0} ${@}"]
