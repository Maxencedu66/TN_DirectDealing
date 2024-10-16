package eu.telecomnancy.labfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.image.Image;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.control.RadioButton;

import java.util.List;


public class PrivateProfileController {

    private SkeletonController skeleton_controller;
    private static User currentUser;
    @FXML private TextField pseudo;
    @FXML private TextField prenom;
    @FXML private TextField nom;
    @FXML private Label mail;
    @FXML private TextField phone;
    @FXML private TextField localisation;

    @FXML private RadioButton ButtonActif;
    @FXML private RadioButton ButtonSommeil;

    @FXML private Label dateInscription;
    @FXML private Label nbFlorain;

    @FXML private ImageView photoProfil;
    @FXML private String photoProfilPath;

    private ToggleGroup state_group = new ToggleGroup();

    public void initialize(){
        System.out.println("Initialisation du profile privé");
        currentUser = Main.getCurrentUser();
        updateUIWithUserData();
        dateInscription.setText(currentUser.getDateInscription().toString());
        nbFlorain.setText(String.valueOf(currentUser.getNbFlorain()));
        mail.setText(currentUser.getMail());

        String cheminImageProfil = currentUser.getPhotoProfil();

        ButtonActif.setToggleGroup(state_group);
        ButtonSommeil.setToggleGroup(state_group);

        ButtonActif.setSelected(true);

        if(currentUser.getEtatCompte().equals("sommeil")){
            ButtonSommeil.setSelected(true);
        }

        try {
            InputStream inputStream;
            if (cheminImageProfil == null) {
                System.out.println("L'utilisateur n'a pas de photo de profil");
                inputStream = getClass().getResourceAsStream("/eu/telecomnancy/labfx/images/kawai.png");
            } else {
                inputStream = new FileInputStream(cheminImageProfil);
            }
            Image image = new Image(inputStream);
            photoProfil.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message or setting a default image
        }
    }  


    private void updateUIWithUserData() {
        if (currentUser != null) {
            pseudo.setText(currentUser.getPseudo());
            prenom.setText(currentUser.getPrenom());
            nom.setText(currentUser.getNom());
            phone.setText(currentUser.getPhone());
            localisation.setText(currentUser.getLocalisation());


        }
    }
    
    @FXML
    private void handleSaveButtonAction() {
        if (currentUser != null) {
            System.out.println("Sauvegarde des données de l'utilisateur");
    
            String oldLocation = currentUser.getLocalisation(); // Sauvegarde de l'ancienne localisation pour comparaison
    
            // Mise à jour des informations de l'utilisateur
            currentUser.setPseudo(pseudo.getText());
            currentUser.setPrenom(prenom.getText());
            currentUser.setNom(nom.getText());
            currentUser.setPhone(phone.getText());
            currentUser.setLocalisation(localisation.getText());
    
            currentUser.update(); // Mise à jour des données dans la base de données
    
            // Vérifie si la localisation a été modifiée
            if (!oldLocation.equals(currentUser.getLocalisation())) {
                currentUser.recalculateDistances(); // Recalcul des distances si la localisation a changé
            }
    
            skeleton_controller.updateProfile(); // Mise à jour de l'interface utilisateur
            skeleton_controller.flash("Modifications enregistrées", "green");
        }
    }
    

    @FXML
    private void changeStateToActif(){
        if (currentUser != null) {
            currentUser.setEtatCompte("actif");
            currentUser.update(); 
            System.out.println(currentUser.getEtatCompte());
            skeleton_controller.loadProfilePage();
        }
    }

    @FXML
    private void changeStateToSommeil(){
        if (currentUser != null) {
            currentUser.setEtatCompte("sommeil");
            currentUser.update(); 
            System.out.println(currentUser.getEtatCompte());
            skeleton_controller.loadProfilePage();
        }
    }

    @FXML
    private void onPhotoProfilClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez une Image de Profil");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(photoProfil.getScene().getWindow());
        if (file != null) {
            currentUser.setPhotoProfil(file.getAbsolutePath());
            Image image = new Image("file:" + file.getAbsolutePath());
            photoProfil.setImage(image);
        }
    }

    public void setSkeletonController(SkeletonController skeleton_controller){
        this.skeleton_controller = skeleton_controller;
    }


}
