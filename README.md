# Car Rental App - DevOps Project

Application de gestion de location de voitures composée de deux microservices Spring Boot, un frontend Nginx, et une pipeline CI/CD GitHub Actions.

## Technologies

- Java 17 / Spring Boot 4.0.5
- Spring Data JPA + H2 (base de données en mémoire)
- JaCoCo (couverture de code)
- SonarCloud (qualité de code)
- Docker / Docker Compose
- GitHub Actions (CI/CD)
- Nginx (frontend)

## Lancer l'application

### Avec Docker Compose (recommandé)

```bash
docker compose up --build
```

| Service          | URL                        |
|------------------|----------------------------|
| Frontend         | http://localhost           |
| Car Service API  | http://localhost:8080/api  |
| Customer Service | http://localhost:8081/api  |
| H2 Console Cars  | http://localhost:8080/h2-console |

### En local (développement)

**Car Service (port 8080) :**
```bash
./gradlew bootRun
```

**Customer Service (port 8081) :**
```bash
./gradlew :customer-service:bootRun
```

## Console H2

Accessible à `http://localhost:8080/h2-console/` pendant que le car service tourne.

- **JDBC URL** : `jdbc:h2:mem:carsdb`
- **User Name** : `sa`
- **Password** : *(vide)*

## Tests

```bash
# Car Service
./gradlew test jacocoTestReport

# Customer Service
./gradlew :customer-service:test :customer-service:jacocoTestReport
```

Les rapports de couverture sont générés dans :
- `build/reports/jacoco/test/html/`
- `customer-service/build/reports/jacoco/test/html/`

## API Car Service (`/api`)

| Méthode | Endpoint                      | Description            |
|---------|-------------------------------|------------------------|
| GET     | `/api/cars`                   | Liste toutes les voitures |
| GET     | `/api/cars/available`         | Voitures disponibles   |
| GET     | `/api/cars/{plate}`           | Détail d'une voiture   |
| POST    | `/api/cars`                   | Ajouter une voiture    |
| POST    | `/api/cars/{plate}/rent`      | Louer une voiture      |
| POST    | `/api/cars/{plate}/return`    | Retourner une voiture  |
| PUT     | `/api/cars/{plate}`           | Modifier une voiture   |
| DELETE  | `/api/cars/{plate}`           | Supprimer une voiture  |

## API Customer Service (`/api`)

| Méthode | Endpoint                      | Description            |
|---------|-------------------------------|------------------------|
| GET     | `/api/customers`              | Liste tous les clients |
| GET     | `/api/customers/{id}`         | Détail d'un client     |
| POST    | `/api/customers`              | Ajouter un client      |
| PUT     | `/api/customers/{id}`         | Modifier un client     |
| DELETE  | `/api/customers/{id}`         | Supprimer un client    |

## CI/CD

GitHub Actions exécute à chaque push sur `main` ou `develop` :
1. Tests unitaires et d'intégration (Car Service + Customer Service)
2. Rapport JaCoCo (couverture de code)
3. Analyse SonarCloud (qualité de code)
4. Build des images Docker (car-service, customer-service, frontend)
