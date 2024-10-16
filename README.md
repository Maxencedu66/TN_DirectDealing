# README du Projet TelecomNancy DirectDealing

1. [Projet souche Gradle/JavaFX/JUnit](#projet-souche-gradlejavafxjunit)
   - [Clonage et Initialisation](#clonage-et-initialisation)
   - [Structure du Projet](#structure-du-projet)
   - [Utilisation dans Visual Studio Code](#utilisation-dans-visual-studio-code)
   - [Utilisation dans IntelliJ](#utilisation-dans-intellij)
   - [Lancement à partir de l'archive .jar](#lancement-à-partir-de-larchive-jar)
2. [Vue d'Ensemble du Projet](#vue-densemble-du-projet)
3. [Objectif du Projet](#objectif-du-projet)
4. [Fonctionnalités Clés](#fonctionnalités-clés)
   - [Authentification et Gestion de Compte](#authentification-et-gestion-de-compte)
   - [Système de Messagerie Intégrée](#système-de-messagerie-intégrée)
   - [Cartographie des Offres](#cartographie-des-offres)
   - [Gestion des Offres](#gestion-des-offres)
   - [Intégration Calendrier](#intégration-calendrier)
   - [Économie de Florains](#économie-de-florains)
   - [Fonctionnalités de Recherche et Filtrage](#fonctionnalités-de-recherche-et-filtrage)
   -  [Vidéo présentant les fonctionnalités clés](#vidéo-présentant-les-fonctionnalités-clés)

5. [Technologies Utilisées](#technologies-utilisées)
6. [Architecture du Projet](#architecture-du-projet)
7. [Gestion de projet](#gestion-de-projet)


## Projet souche Gradle/JavaFX/JUnit

Pour simplifier le démarrage et si vous disposez déjà d'un kit de développement Java, vous pouvez utiliser la souche de projet que nous vous proposons. Pour cela, vous devez simplement cloner le projet git disponible à l'adresse suivante : https://gitlab.telecomnancy.univ-lorraine.fr/projets/2324/pcd2k24/pcd2k24-javafx-bootstrap

Cette souche contient un projet directement utilisable en utilisant le moteur de production Gradle (https://gradle.org/). Vous n'avez rien à installer, l'outil téléchargera les dépendances pour vous.

```bash
git clone https://gitlab.telecomnancy.univ-lorraine.fr/pcd2k24/codingweek-04.git
cd codingweek-04/DirectDealing
./gradlew run
```

Après quelques instants, le temps que l'outil télécharge les différentes dépendances (l'application Gradle et ses dépendances, la librairie JavaFX et ses dépendances, la libraire JUnit, etc.), compile le code de l'application et affiche la fenêtre de connexion.

Le projet souche suit la configuration standard d'un projet Java, à savoir :

```
├── all.txt
├── CodingWeek 2023-2024 - Sujet.pdf
├── DirectDealing
│   ├── BD
│   │   └── DirectDealing.db
│   │   ├── libs
│   │   │   └── labfx-1.0-SNAPSHOT.jar
│   │   ├── resources
│   │   │   └── main
│   │   │       ├── eu
│   │   │       │   └── telecomnancy
│   │   │       │       └── labfx
│   │   │       │           ├── cities.csv
│   │   │       │           ├── Page.fxml
│   │   │       │           ├── images
│   │   │       └── ical4j.properties
│   ├── build.gradle
│   ├── dist
│   │   ├── day1.jar
│   │   └── day3.jar
│   ├── gradle
│   ├── gradlew
│   ├── gradlew.bat
│   ├── settings.gradle
│   └── src
│       ├── main
│       │   ├── all.txt
│       │   ├── java
│       │   │   ├── eu
│       │   │   │   └── telecomnancy
│       │   │   │       └── labfx
│       │   │   │           ├── Controller.java
│       │   │   │           ├── Objet.java
│       │   │   └── module-info.java
│       │   ├── resources
│       │   │   ├── eu
│       │   │   │   └── telecomnancy
│       │   │   │       └── labfx
│       │   │   │           ├── cities.csv
│       │   │   │           ├── images
│       │   │   │           ├── Page.fxml
│       │   │   └── ical4j.properties
│       │   └── Sumup.py
│       └── test
│           └── java
│               └── eu
│                   └── telecomnancy
│                       └── labfx
│                           ├── TestEquipment.java
│                           ├── TestServiceOffer.java
│                           └── TestUser.java
├── Gestion de Projet
│   ├── Conventions.md
│   ├── fonctionnalités.md
│   └── RoadMap.md
└── README.md

```

- le répertoire `src/main/java` contient le code source Java des classes de notre application (dans des sous-répertoires correspondant aux paquetages de notre application).
- le répertoire `src/main/resources` contient les fichiers de ressources (images, données, etc.) de notre application;
- le répertoire `src/test/java` contient le code source Java des classes de tests de notre application (dans des sous-répertoires correspondant aux paquetages de notre application) ;

Ce projet souche est directement utilisable dans Visual Studio Code ou IntelliJ.

### Utilisation dans Visual Studio Code

Pour utiliser ce projet dans Visual Studio Code, il suffit d'ouvrir le répertoire que vous venez de cloner en tant que projet dans Visual Studio Code.

Vous aurez besoin d'avoir installé le paquet d'extensions Java ainsi que l'extension pour Gradle (https://code.visualstudio.com/docs/java/extensions).

Le projet devrait se configurer automatiquement (*classpath*, répertoire des ressources, etc.). Pour compiler/exécuter le projet, vous utiliserez alors soit le terminal (par exemple avec la commande `./gradlew run`) ou l'icône de l'onglet Gradle (cf. le petit éléphant).


### Utilisation dans IntelliJ

Pour utiliser ce projet dans IntelliJ, il suffit d'ouvrir le fichier `build.gradle` présent dans le répertoire vous venez de cloner en tant que projet dans IntelliJ.

Le projet devrait se configurer automatiquement (*classpath*, répertoire des ressources, etc.). Pour compiler/exécuter le projet, vous utiliserez alors soit le terminal (par exemple avec la commande `./gradlew run`) ou l'icône de l'onglet Gradle (cf. le petit éléphant).

### Lancement à partir de l'archive .jar

Pour lancer l'application TelecomNancy DirectDealing à partir de l'archive `.jar`, suivez ces étapes :

1. Ouvrez un terminal.
2. Naviguez jusqu'au répertoire contenant l'archive `.jar` (`DirectDealing/dist/`).
3. Exécutez la commande suivante :

   ```bash
   java --module-path ${JAVAFX_HOME}/lib --add-modules=javafx.base,javafx.controls,javafx.fxml -jar DirectDealing.jar

## Vue d'Ensemble du Projet

**TelecomNancy DirectDealing** est un projet visant à développer une application d'économie circulaire en ligne. Elle est conçue pour permettre aux utilisateurs de prêter, d'emprunter du matériel, ou de proposer/demander des services divers. Ce projet est structuré autour d'un ensemble de fonctionnalités clés visant à faciliter les interactions entre les utilisateurs tout en intégrant des éléments de géolocalisation, de planification, et de récompenses.

## Objectif du Projet

L'objectif principal est de créer une plateforme intuitive et facile à utiliser, où les utilisateurs peuvent efficacement proposer ou rechercher des services et du matériel. La plateforme vise à encourager l'économie circulaire en facilitant le partage de ressources au sein d'une communauté.

## Fonctionnalités Clés

- **Authentification et Gestion de Compte**: Sécurité et gestion personnalisée des comptes utilisateurs.
- **Système de Messagerie Intégrée**: Communication directe entre les utilisateurs pour la coordination des échanges.
- **Cartographie des Offres**: Affichage des offres disponibles sur une carte interactive, facilitant la recherche géolocalisée.
- **Gestion des Offres**: Création, consultation, et réservation d'offres de services ou de matériel.
- **Intégration Calendrier**: Gestion des réservations et planification des services.
- **Économie de Florains**: Système de monnaie virtuelle pour gérer les transactions au sein de l'application.
- **Fonctionnalités de Recherche et Filtrage**: Recherche avancée pour trouver rapidement ce que l'utilisateur cherche.

### Vidéo présentant les fonctionnalités clés

[Regardez la vidéo ici](https://youtu.be/VZfYyfOwzTY)

## Technologies Utilisées

- **Langage de Programmation**: Java (avec focus sur la programmation orientée objet).
- **Gestion de Version**: Git pour le contrôle de version et la collaboration en équipe.
- **Base de Données**: (Définir selon les besoins, ex: SQLite, MySQL, etc.).
- **Interface Utilisateur**: (Choisir la technologie selon les besoins, ex: JavaFX, Swing, etc.).

## Architecture du Projet

- **Modèle MVC** (Modèle-Vue-Contrôleur) pour une séparation claire entre la logique de l'interface utilisateur, la logique métier et la gestion des données.
- **APIs et Services Externes**: Intégration d'APIs pour des fonctionnalités spécifiques comme le calendrier.

## Gestion de Projet

Dans le dossier Gestion de Projet, vous retrouverez nos convention, les fonctionnalités que nous avons observés dans le sujet, la RoadMap de la semaine et le diagramme de classe.

---
