## Conventions pour le Développement de l'Application "TelecomNancy DirectDealing"

- **Classes** : en anglais, singulier, suivant le style *EquipmentOffer* (pas de tiret, pas de underscore, majuscule au début de chaque mot)
- **Variables** : en anglais, suivant le style *equipment_offer* (pas de tiret, underscore pour séparer les mots, pas de majuscule)
- **Méthodes** : en anglais, suivant le style *getEquipmentOffer* (pas de tiret, pas de underscore, majuscule au début de chaque mot sauf pour le premier)
- **Commentaires** : en français, suivant le style `// Ceci est un commentaire`
  - À placer avant une méthode pour expliquer ce qu'elle fait
  - À placer avant une classe pour expliquer ce qu'elle fait
  - À placer avant une variable pour expliquer ce qu'elle contient et son utilité
- **Commit** : en français, suivant le style *Ajout de la classe EquipmentOffer*.
- **Merge** : à ne faire que si le code est fonctionnel et que les tests sont passés. À faire sur la branche *master*. Ne faire qu'en présence des autres membres du groupe ou après avoir prévenu les autres membres du groupe.
- **Ajouter une page Test** : 
  - Créer la page Test.fxml
  - Créer le controller TestController, avec les lignes: 
    ```java
    private SkeletonController skeleton_controller;
    public void setSkeletonController(SkeletonController skeleton_controller){
        this.skeleton_controller = skeleton_controller;
    }
    ```
    puis ajouter les fonction Actions sous la forme:
    ```java
    @FXML public void handleAction(){
        System.out.println("Réalisation de l'action");
        //Actions à réaliser;
    }
    ```
  - Lui associer le Testcontroller dans le fichier .fxml
  - Faire dans le SkeletonController la fonction pour charger la page: (remplacez tous les "test" par le nom de votre page)
    ```java
    // Fonction qui permet de charger la test page:
    public void loadTestPage(){
        try {
            System.out.println("Chargement de la page Test");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/TestPage.fxml"));
            Parent test = loader.load();

            TestController test_controller = loader.getController();
            test_controller.setSkeletonController(this);

            // Ajouter la page d'inscription à la scène
            mainContent.getChildren().setAll(test);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    ```
  - Normalement c'est bon
