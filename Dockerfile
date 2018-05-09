FROM jdk8:slim

ADD target/*.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom" ,"-jar" , "-Xms128m","-Xmx128m" ,"/app.jar"]