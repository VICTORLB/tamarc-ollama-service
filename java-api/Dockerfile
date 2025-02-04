FROM eclipse-temurin:17-jdk as build

WORKDIR /app

# Copia os arquivos do contexto raiz do projeto
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./
COPY src ./src

# Constrói o aplicativo
RUN chmod +x gradlew && ./gradlew build -x test

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
