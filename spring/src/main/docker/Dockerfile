FROM adoptopenjdk/openjdk14:ubi

LABEL author="svbugakov"
MAINTAINER svbugakov@gmail.com

# environment
EXPOSE 8080

# executable ADD service-one-5.0.0-SNAPSHOT.jar app.jar
ADD simpleBoot-1.0-SNAPSHOT.jar app.jar

# run app
RUN /bin/sh -c 'touch /app.jar'
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]
