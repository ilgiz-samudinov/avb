FROM ilgizsamudinov/oracle-jdk:24

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
