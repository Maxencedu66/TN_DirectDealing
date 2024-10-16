package eu.telecomnancy.labfx;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import java.io.InputStream;

public class ProfileController {

    private SkeletonController skeleton_controller;
    private static User currentUser;
    
    @FXML private Label labelPseudo;
    @FXML private Label labelSoldeFlorain;
    @FXML private Label State;
    @FXML private VBox vbox;
    @FXML private ImageView photoProfil;
    @FXML private Label unreadMessagesLabel;
    @FXML private Button help;


    public void initialize(){
        System.out.println("Initialisation du profile");
        currentUser = Main.getCurrentUser();
        State.setText(currentUser.getEtatCompte());

        if (currentUser.getMail().equals("admin")){
            help.setDisable(true);
        }

        String cheminImageProfil = currentUser.getPhotoProfil();
        //Chemin type : /Users/maxence/Downloads/chat.png
        //Set de l'image de profil
        if (cheminImageProfil == null) { //Si il y en a pas de personalisé on met l'image par défaut
            System.out.println("L'utilisateur n'a pas de photo de profil");
            cheminImageProfil = "src/main/resources/eu/telecomnancy/labfx/images/default_profile.png";
        }
        Image image = new Image("file:" + cheminImageProfil);
        photoProfil.setImage(image);
        //Redimensionner l'image pour qu'elle soit ronde
        photoProfil.setFitWidth(100); // Largeur de l'image
        photoProfil.setFitHeight(100); // Hauteur de l'image
        photoProfil.setPreserveRatio(false); // Conserve le ratio de l'image
        // Appliquer un viewport pour zoomer sur l'image
        double width = image.getWidth();
        double height = image.getHeight();
        double size = Math.min(width, height);
        double x = (width - size) / 2;
        double y = (height - size) / 2;
        Rectangle2D viewport = new Rectangle2D(x, y, size, size);
        photoProfil.setViewport(viewport);
        // Créez un Circle comme un clip pour l'ImageView
        double radius = photoProfil.getFitWidth() / 2;
        Circle clip = new Circle(radius, radius, radius);
        photoProfil.setClip(clip);
        updateProfileInfo(currentUser);
        // Faire en sorte de changer la forme du curseur quand on passe sur l'image
        photoProfil.setOnMouseEntered(event -> photoProfil.getScene().setCursor(Cursor.HAND));
        photoProfil.setOnMouseExited(event -> photoProfil.getScene().setCursor(Cursor.DEFAULT));

    }
    

    public void setSkeletonController(SkeletonController skeleton_controller){
        this.skeleton_controller = skeleton_controller;
    }

    public void updateUnreadMessagesCount() {
        int unreadCount = DataBase.countUnreadMessages(Main.getCurrentUser().getId());
        unreadMessagesLabel.setText(String.valueOf(unreadCount));
    }


    @FXML public void handleDeconnexion(){
        System.out.println("Deconnexion de la session");
        skeleton_controller.main_controller.loadWelcomePage();
    }
    
    @FXML
    public void updateProfileInfo(User user) {
        labelPseudo.setText(user.getPseudo());
        labelSoldeFlorain.setText(String.valueOf(user.getNbFlorain()));
        String imagePath = user.getPhotoProfil();
        Image profileImage;
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                profileImage = new Image("file:" + imagePath);
            } catch (IllegalArgumentException e) {
                // Image invalide ou non trouvée, utiliser une image par défaut
                InputStream inputStream;
                inputStream = getClass().getResourceAsStream("/eu/telecomnancy/labfx/images/kawai.png");
                profileImage = new Image(inputStream);
                }
        } else {
                InputStream inputStream;
                inputStream = getClass().getResourceAsStream("/eu/telecomnancy/labfx/images/kawai.png");
                profileImage = new Image(inputStream);
            
        }
        photoProfil.setImage(profileImage);
        updateUnreadMessagesCount();
    }
    

    @FXML 
    public void handlePrivateProfile() {
        System.out.println("Affichage du profil privé");
        skeleton_controller.loadPrivateProfile();
    }

    @FXML public void contactHelp(){
        System.out.println("Help !!");
        skeleton_controller.setSupplierForMessaging("admin");
        skeleton_controller.loadMessageriePage();
    }
    
}
