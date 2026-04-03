# Hello Service - Spring Boot

Application Spring Boot simple qui expose un service REST retournant "hello".

## Technologies
- Java 17
- Spring Boot 4.0.5
- Gradle
- Docker

## Build et Run localement

### Avec Gradle
```bash
./gradlew build
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```

### Avec Docker
```bash
docker build -t hello .
docker run -p 4000:8080 hello
```

Accéder à l'application : http://localhost:4000/

## Docker Hub
Image disponible sur Docker Hub : `arezkib/hello-service:latest`

```bash
docker pull arezkib/hello-service:latest
docker run -p 4000:8080 arezkib/hello-service:latest
```

## CI/CD
Le projet utilise GitHub Actions pour automatiser le build et les tests à chaque push.
