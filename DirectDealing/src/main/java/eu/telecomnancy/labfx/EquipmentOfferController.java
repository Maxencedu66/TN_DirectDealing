package eu.telecomnancy.labfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EquipmentOfferController {
    private SkeletonController skeleton_controller;

    public void setSkeletonController(SkeletonController skeleton_controller){
        this.skeleton_controller = skeleton_controller;
    }

    
    
    @FXML private Label title;
    @FXML private Label description;
    @FXML private Label quantity;
    @FXML private Label dates;
    @FXML private Label price;

    @FXML private Button book;
    @FXML private Button contact;
    @FXML private Button ButtonDelete;
    @FXML private Button contactHelp;

    @FXML private DatePicker booking_begin;
    @FXML private DatePicker booking_end;

    private EquipmentOffer currentOffer;
    private User currentUser;


    public void setCurrentOffer(EquipmentOffer offer) {
        this.currentOffer = offer;
        currentUser = Main.getCurrentUser();
        displayOfferInfo();
    }

    private void displayOfferInfo() {
        title.setText("Titre : " + currentOffer.getName());
        description.setText("Description : " + currentOffer.getDescription());
        quantity.setText("Quantité : " + currentOffer.getQuantity());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String startDateString = currentOffer.getStartAvailability() != null ? currentOffer.getStartAvailability().format(formatter) : "Pas de date renseignée";
        String endDateString = currentOffer.getEndAvailability() != null ? currentOffer.getEndAvailability().format(formatter) : "Pas de date renseignée";
        dates.setText("Dates : du " + startDateString + " au " + endDateString);
        price.setText("Coût en florains : " + currentOffer.getPrice());
        if (currentOffer.getOwner().getMail().equals(currentUser.getMail())){
            book.setVisible(false);
            booking_begin.setVisible(false);
            booking_end.setVisible(false);
            contact.setVisible(false);
        }

        if (!currentUser.getMail().equals("admin")){
            ButtonDelete.setVisible(false);
        }else{
            contactHelp.setVisible(false);
            if(currentUser.getMail().equals(currentOffer.getMail())){
                ButtonDelete.setVisible(true);
            }
        }
    }

    @FXML public void handleBook(){
        System.out.println("Tentative de réservation de l'équipement !");

        // Obtenir l'email de l'utilisateur actuel
        String currentUserEmail = Main.getCurrentUser().getMail();

        // Vérifier que l'utilisateur actuel n'est pas le fournisseur de l'offre
        if (currentUserEmail.equals(currentOffer.getMail())) {
            System.out.println("Vous ne pouvez pas réserver votre propre offre");
            skeleton_controller.flash("Vous ne pouvez pas réserver votre propre offre", "red");
            return;
        }
        // Vérifier qu'un utilisateur n'a pas déjà réservé l'offre
        if (currentOffer.getEstPris() != null) {
            System.out.println("Cette offre a déjà été réservée");
            return;
        }

        LocalDate begin = booking_begin.getValue();
        LocalDate end = booking_end.getValue();

        if(begin == null || end == null){
            System.out.println("Veuillez renseigner une date de début et de fin");
            skeleton_controller.flash("Veuillez renseigner une date de début et de fin", "red");
            return;
        }else if(begin.isAfter(end)){
            skeleton_controller.flash("La date de début doit être avant la date de fin", "red");
            System.out.println("La date de début doit être avant la date de fin");
            return;
        }else if(begin.isBefore(currentOffer.getStartAvailability()) || end.isAfter(currentOffer.getEndAvailability())){
            skeleton_controller.flash("La date de début et de fin doivent être comprises dans la période de disponibilité de l'offre", "red");
            System.out.println("La date de début et de fin doivent être comprises dans la période de disponibilité de l'offre");
            return;
        }



        // Tenter de réserver l'offre
        if (currentOffer.reserveOffer(currentUserEmail, begin, end)) {
            System.out.println("Réservation réussie");
            skeleton_controller.flash("Réservation réussie", "green");

            // Création de deux nouvelles offres avant et après la période de réservation
            // si le premier jour de réservation est le même que le début de l'offre de base, on en crée qu'une après
            // inverse pour le dernier jour de réservation
            if(begin.equals(currentOffer.getStartAvailability()) && end.equals(currentOffer.getEndAvailability())){
                System.out.println("Offre bien créée");
                currentOffer.setEstPris(currentUserEmail);
                currentOffer.setBook_begin(begin);
                currentOffer.setBook_end(end);
                currentOffer.update();
            }else if(begin.equals(currentOffer.getStartAvailability())){
                EquipmentOffer newOffer = new EquipmentOffer(currentOffer.getOwner(), currentOffer.getName(), currentOffer.getDescription(), currentOffer.getQuantity(), end.plusDays(1), currentOffer.getEndAvailability(), currentOffer.getPrice());
                currentOffer.setEstPris(currentUserEmail);
                currentOffer.setBook_begin(begin);
                currentOffer.setBook_end(end);
                currentOffer.setEndAvailability(end);
                System.out.println("Offre bien créée");
                currentOffer.update();
                newOffer.update();
            }else if(end.equals(currentOffer.getEndAvailability())){
                EquipmentOffer newOffer = new EquipmentOffer(currentOffer.getOwner(), currentOffer.getName(), currentOffer.getDescription(), currentOffer.getQuantity(), currentOffer.getStartAvailability(), begin.minusDays(1), currentOffer.getPrice());
                currentOffer.setEstPris(currentUserEmail);
                currentOffer.setBook_begin(begin);
                currentOffer.setBook_end(end);
                currentOffer.setStartAvailability(begin);
                System.out.println("Offre bien créée");
                currentOffer.update();
                newOffer.update();
            }else{
                EquipmentOffer newOffer1 = new EquipmentOffer(currentOffer.getOwner(), currentOffer.getName(), currentOffer.getDescription(), currentOffer.getQuantity(), currentOffer.getStartAvailability(), begin.minusDays(1), currentOffer.getPrice());
                EquipmentOffer newOffer2 = new EquipmentOffer(currentOffer.getOwner(), currentOffer.getName(), currentOffer.getDescription(), currentOffer.getQuantity(), end.plusDays(1), currentOffer.getEndAvailability(), currentOffer.getPrice());
                
                currentOffer.setEstPris(currentUserEmail);
                currentOffer.setBook_begin(begin);
                currentOffer.setBook_end(end);
                currentOffer.setStartAvailability(begin);
                currentOffer.setEndAvailability(end);
                System.out.println("Offres bien créées");
                currentOffer.update();
                newOffer1.update();
                newOffer2.update();


            }


            System.out.println("L'offre est maintenant réservée par " + currentOffer.getEstPris() + "estpris: " + currentOffer.getEstPris());
            currentUser = Main.getCurrentUser();
            currentUser.setNbFlorain(currentUser.getNbFlorain() - currentOffer.getPrice());
            skeleton_controller.updateProfile();

            skeleton_controller.loadListEquipmentOfferPage();
        } else {
            System.out.println("Réservation échouée");
        }
    }

    @FXML
    public void handleContact() {
        System.out.println("Lancement d'une conversation avec le propriétaire de l'offre");
    
        // Formater la date de publication
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date;
        try {
            date = LocalDate.parse(currentOffer.getDate_publication(), inputFormatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Erreur de format de date", e);
        }
        String formattedDate = date.format(outputFormatter);
    
        // Construire le message
        String message = "J'ai besoin de discuter à propos de l'annonce de \n"
                         + "type \"" + "service" + "\" \n"
                         + "créée par \"" + currentOffer.getOwner().getPseudo() + "\" \n"
                         + "le \"" + formattedDate + "\" \n"
                         + "avec comme titre \"" + currentOffer.getName() + "\" \n"
                         + "comme description \"" + currentOffer.getDescription() + "\" et \n"
                         + "comme prix \"" + currentOffer.getPrice() + " florains\" \n"
                         + "située à \"" + currentOffer.getOwner().getLocalisation() + "\"";
    
        // Obtenir le pseudo du fournisseur
        String supplierPseudo = currentOffer.getOwner().getPseudo();
    
        // Passer le message et le pseudo du fournisseur au SkeletonController
        skeleton_controller.setSupplierForMessaging(supplierPseudo, message);
    
        // Charger la page de messagerie
        skeleton_controller.loadMessageriePage();
    }
    

    @FXML public void cancel(){
        System.out.println("Go back !");
        skeleton_controller.loadListEquipmentOfferPage();
    }

    @FXML public void delete(){
        System.out.println("Suppression de l'offre");
        currentOffer.delete();
        skeleton_controller.loadListEquipmentOfferPage();
    }

    @FXML public void contactHelp(){
        System.out.println("Help !!");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date;
        try {
            date = LocalDate.parse(currentOffer.getDate_publication(), inputFormatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Erreur de format de date", e);
        }
        String formattedDate = date.format(outputFormatter);
        
        String message = "J'ai besoin d'aide sur l'annonce de \n"
                         + "type \"" + "service" + "\" \n"
                         + "créé par \"" + currentUser.getPseudo() + "\" \n"
                         + "le \"" + formattedDate + "\" \n"
                         + "avec comme titre \"" + currentOffer.getName() + "\" \n"
                         + "comme description \"" + currentOffer.getDescription() + "\" et \n"
                         + "comme prix \"" + currentOffer.getPrice() + " florains\" \n"
                         + "situé à \"" + currentOffer.getOwner().getLocalisation() + "\"";
        
        skeleton_controller.setSupplierForMessaging("admin", message);
        skeleton_controller.loadMessageriePage();
    }
}
