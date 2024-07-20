# Maven build container 

FROM maven:3.8.5-openjdk-8 AS maven_build

# copy the local code to container image
#项目工作目录
WORKDIR /app
COPY pom.xml .
COPY src ./src

# build a release artifact
RUN mvn package -DskipTest

# Run the web service on container startup
CMD ["java","-jar","/app/target/usercenter-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]
