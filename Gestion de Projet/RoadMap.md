## Roadmap de l'Application "TelecomNancy DirectDealing"

Afin d'avoir un suivi et une communication efficace, nous utiliserons une roadmap créée avec LucidsPark : [RoadMap](https://lucid.app/lucidspark/9039a86d-42ac-4d6e-91ca-1853b478cb23/edit?viewport_loc=441%2C-479%2C4385%2C2295%2C0_0&invitationId=inv_d149e85c-bacd-4785-afb0-c95203244463)

Cette roadmap sera mise à jour régulièrement et sera accessible à tous les membres du projet.

## ToDo List

### 08/01/2024

- Visuels de l'application : schémas, diagramme, fichiers FXML, etc, (Page de garde  Page de connexion Page d'inscription Affichage profil)
- Lien du croquis des viuels : [Croquis](https://lucid.app/lucidspark/3e2b87ea-0b26-4c6a-bc3a-3376331c43c6/edit?view_items=eFG3Hkxl6jku&invitationId=inv_aa13254e-0082-48cd-b5fd-fd50973800d6)
- Début de la gestion des profils utilisateurs :
    - Création de compte
    - Connexion
    - Création de la base de données utilisateurs (
        table profil : id, prenom, nom, pseudo, mail,phone, password, photo_profil, localisation, date_inscription, status_compte : particulier/professionnel, etat_compte : sommeil ou non, nb_florain, historique_florain, note)
    - Pattern d'un profil : 
        - Nom
        - Prénom
        - Nom d'utilisateur
        - Mail
        - Numéro de téléphone (optionnel)
        - Mot de passe
        - Photo de profil (optionnel)
        - Localisation
- **Livrable :** Page principale avec la gestion des profils utilisateurs

### 09/01/2024

- Début de la gestion des annonces :
    - controller pour les pages de création et de consultation
    - controller pour l'affichage d'une annonce
- Finir les DB EquipmentOffer et ServiceOffer
- Controller et affichage du profil :
    - Profil public
    - Profil privé
    - Modification du profil
- Finir la création des pages :
    - Page 'homePage'
    - Pages profils utilisateur
    - Pages création / consultation d'annonces
- **Livrable :** Page principale avec la gestion des profils utilisateurs et des annonces

