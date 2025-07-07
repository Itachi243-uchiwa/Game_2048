# 🎮 2048 – Jeu JavaFX avec FXML, MVC et Design Patterns

Projet personnel réalisé en Java, ce 2048 propose une architecture propre, une interface moderne en JavaFX via FXML, et l’utilisation rigoureuse des design patterns.

## 🧠 Objectifs du projet

- Implémenter le célèbre jeu 2048
- Appliquer strictement le modèle MVC
- Intégrer JavaFX avec FXML
- Structurer le projet avec des design patterns : MVC, Observer, Factory, Singleton

## ✨ Fonctionnalités

- Interface JavaFX responsive conçue en FXML
- Déplacement clavier (haut, bas, gauche, droite)
- Fusion automatique des tuiles selon les règles du 2048
- Génération aléatoire des tuiles après chaque coup
- Calcul et affichage du score
- Détection de fin de partie (victoire ou blocage)
- Code propre, commenté, organisé

## 📁 Structure

2048/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ ├── model/ # Logique du jeu (grille, tuiles)
│ │ │ ├── view/ # Chargement FXML
│ │ │ └── controller/ # Gestion des événements clavier
│ │ └── resources/
│ │ └── fxml/ # Interfaces FXML
├── pom.xml # Dépendances et configuration Maven
└── README.md


## 🧱 Technologies

- Java 17+
- JavaFX 20+
- FXML
- Maven

## 🚀 Installation et exécution

```bash
git clone https://github.com/Itachi243-uchiwa/2048-javafx.git
cd 2048-javafx
mvn clean install
mvn javafx:run
````

## 🧪 Ce que j’ai appris

* Créer une application Java propre et modulaire
* Maîtriser FXML pour séparer la vue de la logique
* Appliquer les patterns MVC, Observer et Factory avec rigueur
* Gérer les événements clavier et une interface responsive

## 📜 Licence

Ce projet est sous licence MIT.

````

---

✅ Le bloc `bash` était mal clôturé (` ```bash` à la fin au lieu de ` ``` `)  
✅ Les listes d'apprentissages ont été converties en puces Markdown  
✅ L’espacement et l'encodage sont corrigés pour être lisibles sur GitHub

Souhaites-tu que je te livre un **fichier `README.md` complet prêt à déposer** dans ton dépôt GitHub ?
````

