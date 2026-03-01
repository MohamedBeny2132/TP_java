# TP Java : Gestion d'Annonces (Spring Boot 3)

Ce projet est une API de gestion d'annonces développée avec Spring Boot 3.3. Il utilise MapStruct pour le mapping DTO et Lombok pour simplifier le code des entités.

## Initialisation du Projet

### Prérequis
- Java 17
- Maven

### Lancement
Pour démarrer l'application localement avec la base de données H2 (en mémoire) :
```bash
./mvnw spring-boot:run
```
L'application sera accessible sur `http://localhost:8080`.

## Endpoints Principaux

### Documentation API (Swagger)
L'interface Swagger permet de tester tous les endpoints :
- **URL** : [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### API Annonces
- `GET /api/annonces` : Liste toutes les annonces (Accès Public)
- `POST /api/annonces` : Créer une annonce (Authentification requise)
- `GET /api/annonces/{id}` : Détails d'une annonce (Accès Public)

### Console Base de Données (H2)
- **URL** : [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL** : `jdbc:h2:mem:testdb`
- **User** : `sa` (pas de mot de passe)

## Sécurité & Authentification

- **JWT** : L'authentification est basée sur des tokens JWT.
- **Accès Public** : Les endpoints de consultation (`GET`), Swagger et la console H2 sont accessibles sans authentification pour faciliter les tests.
- **Accès Restreint** : Les modifications (POST, PUT, DELETE) nécessitent un token valide via le header `Authorization: Bearer <token>`.

## Notes Techniques
- **MapStruct & Lombok** : La configuration de la compilation a été ajustée dans le `pom.xml` pour assurer la compatibilité entre MapStruct et Lombok.
- **Démarrage** : Si le port 8080 est déjà utilisé, assurez-vous de couper les anciens processus Java avant de relancer.
