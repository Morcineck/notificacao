from gradle:7.5-jdk17 as build
WORKDIR /app
COPY . .
run gradle build --no-daemon

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/notificacao.jar

EXPOSE 8083

CMD ["java", "-jar", "/app/notificacao.jar"]