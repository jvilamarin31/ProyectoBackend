FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests package


FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENV SPRING_MONGODB_URI=mongodb+srv://juanvila_db_user:95JNtH2Ujz7UKlYn@cluster0.qfukbh5.mongodb.net/
ENV SPRING_MONGODB_DATABASE=ProyectoBD
ENV JWT_SECRET=9f82a1c7b4d9e6f0a3c5d7e8f9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]