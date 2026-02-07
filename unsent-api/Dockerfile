# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

# copy parent + module poms first (cache friendly)
COPY pom.xml .
COPY unsent-api/pom.xml unsent-api/pom.xml

# download deps
RUN mvn -B -pl unsent-api -am dependency:go-offline

# copy source
COPY unsent-api unsent-api

# build ONLY this module
RUN mvn -B -pl unsent-api -am clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /build/unsent-api/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]