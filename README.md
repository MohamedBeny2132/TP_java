# Projet Annonces - TP Développement Web & API

Ce projet est une plateforme de gestion d'annonces avec une architecture en couches (DAO, Service, API/Servlet).

## Architecture
- **Persistance** : Hibernate/JPA avec PostgreSQL (production) et H2 (tests).
- **Service** : Logique métier centralisée.
- **Servlet** : Interface Web classique (Login, JSP).
- **API REST** : Endpoints JAX-RS (Jersey) documentés avec OpenAPI.
- **Sécurité** : Authentification Stateless (Token-based) pour l'API via `SecurityFilter`.

## Industrialisation (Partie IV)
- **Tests** : 
    - Tests Unitaires (Surefire) : `mvn test`
    - Tests d'Intégration (Failsafe) : `mvn verify` (avec base H2 In-Memory).
- **Logs** : Utilisation de Logback pour un suivi structuré (voir `logback.xml`).
- **Documentation** : OpenAPI 3 intégrée au code via des annotations.
- **Tests de Charge** : Simulation d'appels concurrents dans `SimpleLoadTest.java`.
- **Bonus JAAS** : Authentification stateless utilisant `LoginModule` personnalisés pour la gestion de l'identité via DB et Token.

## Flux d'Authentification JAAS
1. **Login** (`POST /login`) : L'utilisateur envoie ses identifiants. `LoginResource` utilise `LoginContext("MasterAnnonceLogin")` qui délègue à `DbLoginModule`.
2. **Génération de Token** : Si l'authentification réussit, un token UUID est généré et renvoyé au client.
3. **Appels Sécurisés** : Le client envoie le token dans le header `Authorization: Bearer <token>`.
4. **Reconstruction d'Identité** : `SecurityFilter` utilise `LoginContext("MasterAnnonceToken")` qui délègue à `TokenLoginModule`. Ce dernier valide le token et peuple le `Subject` JAAS avec un `UserPrincipal` et un `RolePrincipal`.

## Problèmes rencontrés & Solutions Techniques

1. **Cohérence des Espaces de Noms (JAX-RS vs Validation)** : 
    - Nous avons harmonisé tout le projet sur `javax.validation` pour assurer une compatibilité parfaite avec Jersey 2.x (utilisé pour Tomcat 9). Cela a résolu les erreurs 500 silencieuses lors des tests de validation d'entrée.
2. **Désérialisation JSON** :
    - Les classes génériques comme `PaginatedResponses` ont été complétées par des constructeurs sans arguments et des setters pour permettre à Jackson de reconstruire les objets lors des tests API.
3. **Séparation des Tests Maven** :
    - Configuration du `maven-failsafe-plugin` pour exécuter les tests d'intégration (`*IT.java`) séparément des tests unitaires, assurant une isolation propre de la base H2.
4. **Gestion du Scope de Requête** :
    - Refactorisation de `AnnonceResource` pour injecter `@Context` dans les méthodes plutôt que dans les champs, évitant ainsi les `IllegalStateException` dans le cadre de test Jersey.

## Exécution

```bash
# Compiler et exécuter tous les tests (unitaires + intégration)
mvn clean verify
```
