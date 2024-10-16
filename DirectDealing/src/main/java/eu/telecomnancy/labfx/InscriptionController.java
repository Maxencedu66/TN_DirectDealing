package eu.telecomnancy.labfx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.util.regex.Pattern;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public class InscriptionController {

    private MainController mainController;

    @FXML private TextField prenom;
    @FXML private TextField nom;
    @FXML private TextField pseudo;
    @FXML private TextField mail;
    @FXML private TextField password;
    @FXML private TextField password2;
    @FXML private TextField localisation;
    @FXML private TextField telephone;
    @FXML private ImageView imageView;
    @FXML private HBox flashMessageContainer;
    @FXML private Label flashMessageLabel;

    private String imagePath;

    // Fonction qui permet d'afficher un message flash
    public void flash(String message, String color) {
        flashMessageLabel.setText(message);
        flashMessageContainer.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 20;");
        flashMessageContainer.setVisible(true);
        // Temporisateur pour masquer le message flash après 5 secondes
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> closeFlashMessage()));
        timeline.setCycleCount(1);
        timeline.play();
    }

    @FXML
    private void closeFlashMessage() {
        flashMessageContainer.setVisible(false);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void handleInscription() throws IOException {
        String prenomValue = prenom.getText();
        String nomValue = nom.getText();
        String pseudoValue = pseudo.getText();
        String mailValue = mail.getText();
        String passwordValue = password.getText();
        String password2Value = password2.getText();
        String localisationValue = localisation.getText();
        String phoneValue = telephone.getText();
    
        // Vérifiez si les champs obligatoires sont remplis
        if (prenomValue.isEmpty()){
            flash("Le champ prénom est obligatoire", "red");
            return;
        }
        if (nomValue.isEmpty()){
            flash("Le champ nom est obligatoire", "red");
            return;
        }
        if (pseudoValue.isEmpty()){
            flash("Le champ pseudo est obligatoire", "red");
            return;
        }
        if (mailValue.isEmpty()){
            flash("Le champ mail est obligatoire", "red");
            return;
        }
        if (passwordValue.isEmpty()){
            flash("Le champ mot de passe est obligatoire", "red");
            return;
        }
        if (localisationValue.isEmpty()){
            flash("Le champ localisation est obligatoire", "red");
            return;
        }

        if (!Main.testCity(localisationValue)) {
            System.out.println("La ville n'est pas valide");
            return;
        }

        String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        String PHONE_REGEX = "^\\+?\\d{1,3}?[-.\\s]?\\(?\\d{1,3}\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$";

        if(!Pattern.matches(EMAIL_REGEX, mailValue)) {
            flash("La forme du main n'est pas valide", "red");
            return;
        }

        if(!Pattern.matches(PHONE_REGEX, phoneValue)) {
            flash("La forme du téléphone n'est pas valide", "red");
            return;
        }

        if (!Main.testCity(localisationValue)) {
            System.out.println("La ville n'est pas valide");
            return;
        }

        if(!Pattern.matches(EMAIL_REGEX, mailValue) || !Pattern.matches(PHONE_REGEX, phoneValue)) {
            System.out.println("Le mail ou le téléphone n'est pas valide");
            return;
        }
    
        if (!passwordValue.equals(password2Value)) {
            System.out.println("Les mots de passe ne correspondent pas");
            flash("Les mots de passe ne correspondent pas", "red");
            return;
        }
        
        try (Connection conn = DataBase.getConnection()) {
            if (userExists(conn, pseudoValue, mailValue)) {
                System.out.println("Un utilisateur avec ce pseudo ou ce mail existe déjà");
                flash("Un utilisateur avec ce pseudo ou ce mail existe déjà", "red");
                return;
            }
    
            String sql = "INSERT INTO profil (prenom, nom, pseudo, mail, password, phone, localisation, date_inscription, nb_florain, photo_profil, etat_compte) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 100, ?, 'actif')";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, prenomValue);
                pstmt.setString(2, nomValue);
                pstmt.setString(3, pseudoValue);
                pstmt.setString(4, mailValue);
                pstmt.setString(5, passwordValue);
                pstmt.setString(6, phoneValue.isEmpty() ? null : phoneValue); // Téléphone non obligatoire
                pstmt.setString(7, localisationValue);
                pstmt.setString(8, LocalDate.now().toString());
                pstmt.setString(9, imagePath); // Photo de profil non obligatoire
                pstmt.executeUpdate();
                User newUser = new User(mailValue);
                //get all info of user
                System.out.println("Utilisateur créé" + newUser.getMail() + newUser.getPseudo() + newUser.getNbFlorain() + newUser.getPhotoProfil() + newUser.getEtatCompte() + newUser.getHistoriqueFlorain() + newUser.getNote() + newUser.getPhone() + newUser.getLocalisation());
                flash("Utilisateur créé", "green");
                newUser.updateDistancesForNewUser(); // Mise à jour des distances pour le nouvel utilisateur
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Redirection vers WelcomePage.fxml
        mainController.loadWelcomePage();
    }
    
    

    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
    
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
    
            // Stocker le chemin d'accès de l'image
            imagePath = selectedFile.getAbsolutePath();
        }
    }


    private boolean userExists(Connection conn, String pseudo, String mail) throws SQLException {
    String sql = "SELECT * FROM profil WHERE pseudo = ? OR mail = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, pseudo);
        pstmt.setString(2, mail);
        ResultSet rs = pstmt.executeQuery();
        return rs.next(); // Retourne vrai si un enregistrement existe
        }
    }


    // Bouton qui charge la page de bienvenue
    @FXML
    public void handleRetour() throws IOException {
        mainController.loadWelcomePage();
    }

}




