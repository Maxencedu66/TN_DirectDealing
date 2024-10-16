package eu.telecomnancy.labfx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.time.format.DateTimeParseException;

public class ServiceOfferController {

    private SkeletonController skeleton_controller;
    private ServiceOffer service_offer;
    private User currentUser;

    public void setSkeletonController(SkeletonController skeleton_controller){
        this.skeleton_controller = skeleton_controller;
    }

    @FXML private Label title;
    @FXML private Label description;
    @FXML private Label date;
    @FXML private Label price;

    @FXML private Button book;
    @FXML private Button cancel;
    @FXML private Button contact;
    @FXML private Button ButtonDelete;
    @FXML private Button contactHelp;

    @FXML private DatePicker booking_begin;
    @FXML private DatePicker booking_end;

    public void setCurrentOffer(ServiceOffer offer) {
        this.service_offer = offer;
        currentUser = Main.getCurrentUser();
        displayOfferInfo();
    }

    private void displayOfferInfo() {
        title.setText("Titre : " + service_offer.getTitle());
        description.setText("Description : " + service_offer.getDescription());
    
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String startDateString = service_offer.getStart() != null ? service_offer.getStart().format(formatter) : "Pas de date renseignée";
        String endDateString = service_offer.getEnd() != null ? service_offer.getEnd().format(formatter) : "Pas de date renseignée";
        String time = service_offer.getTime() != null ? service_offer.getTime().toString() : "Pas d'heure renseigné";
        date.setText("Date : du " + startDateString + " au " + endDateString + " à " + time);
        price.setText("Coût en florains : " + service_offer.getPrice());
    

        if (service_offer.getSupplierMail().equals(currentUser.getMail())){
            book.setVisible(false);
            booking_begin.setVisible(false);
            booking_end.setVisible(false);
            contact.setVisible(false);
        }

        if (!currentUser.getMail().equals("admin")){
            ButtonDelete.setVisible(false);
        }else{
            contactHelp.setVisible(false);
            if(currentUser.getMail().equals(service_offer.getSupplierMail())){
                ButtonDelete.setVisible(true);
            }
        }
    }

    @FXML public void handleBook() {
        System.out.println("Tentative de réservation de l'offre");
        
        // Obtenir l'email de l'utilisateur actuel
        String currentUserEmail = Main.getCurrentUser().getMail();
    
        // Vérifier que l'utilisateur actuel n'est pas le fournisseur de l'offre
        if (currentUserEmail.equals(service_offer.getSupplierMail())) {
            System.out.println("Vous ne pouvez pas réserver votre propre offre");
            skeleton_controller.flash("Vous ne pouvez pas réserver votre propre offre", "red");
            return;
        }
    
        // Vérifier qu'un utilisateur n'a pas déjà réservé l'offre
        if (service_offer.getEstPris() != null) {
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
            System.out.println("La date de début doit être avant la date de fin");
            skeleton_controller.flash("La date de début doit être avant la date de fin", "red");
            return;
        }else if(begin.isBefore(service_offer.getStart()) || end.isAfter(service_offer.getEnd())){
            System.out.println("La date de début et de fin doivent être comprises dans la période de disponibilité de l'offre");
            skeleton_controller.flash("La date de début et de fin doivent être comprises dans la période de disponibilité de l'offre", "red");
            return;
        }


        // Tenter de réserver l'offre
        if (service_offer.reserveOffer(service_offer, currentUserEmail, begin, end)) {
            System.out.println("Offre réservée avec succès");
            skeleton_controller.flash("Offre réservée avec succès", "green");

            // Création de deux nouvelles offres avant et après la période de réservation
            // si le premier jour de réservation est le même que le début de l'offre de base, on en crée qu'une après
            // inverse pour le dernier jour de réservation
            if(begin.equals(service_offer.getStart()) && end.equals(service_offer.getEnd())){
                System.out.println("Offre bien créée");
            }else if(begin.equals(service_offer.getStart())){
                ServiceOffer newOffer = new ServiceOffer(service_offer.getSupplier(), service_offer.getTitle(), service_offer.getDescription(), end.plusDays(1), service_offer.getEnd(), service_offer.getTime(), false, null, service_offer.getPrice());
                System.out.println("Offre bien créée");
            }else if(end.equals(service_offer.getEnd())){
                ServiceOffer newOffer = new ServiceOffer(service_offer.getSupplier(), service_offer.getTitle(), service_offer.getDescription(), service_offer.getStart(), begin.minusDays(1), service_offer.getTime(), false, null, service_offer.getPrice());
                System.out.println("Offre bien créée");
            }else{
                ServiceOffer newOffer1 = new ServiceOffer(service_offer.getSupplier(), service_offer.getTitle(), service_offer.getDescription(), service_offer.getStart(), begin.minusDays(1), service_offer.getTime(), false, null, service_offer.getPrice());
                ServiceOffer newOffer2 = new ServiceOffer(service_offer.getSupplier(), service_offer.getTitle(), service_offer.getDescription(), end.plusDays(1), service_offer.getEnd(), service_offer.getTime(), false, null, service_offer.getPrice());
                System.out.println("Offres bien créées");
            }



            service_offer.setEstPris(currentUserEmail);
            //set nb florain user
            currentUser = Main.getCurrentUser();
            currentUser.setNbFlorain(currentUser.getNbFlorain() - service_offer.getPrice());
            skeleton_controller.updateProfile();
  
            skeleton_controller.loadListServiceOfferPage();
   
        } else {
            System.out.println("La réservation de l'offre a échoué");
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
            date = LocalDate.parse(service_offer.getDate_publication(), inputFormatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Erreur de format de date", e);
        }
        String formattedDate = date.format(outputFormatter);
    
        // Construire le message
        String message = "J'aimerais discuter de l'annonce de \n"
                         + "type \"" + "service" + "\" \n"
                         + "créée par \"" + service_offer.getSupplier().getPseudo() + "\" \n"
                         + "le \"" + formattedDate + "\" \n"
                         + "avec comme titre \"" + service_offer.getTitle() + "\" \n"
                         + "comme description \"" + service_offer.getDescription() + "\" et \n"
                         + "comme prix \"" + service_offer.getPrice() + " florains\" \n"
                         + "située à \"" + service_offer.getSupplier().getLocalisation() + "\"";
    
        // Obtenir le pseudo du fournisseur
        String supplierPseudo = service_offer.getSupplier().getPseudo();
    
        // Passer le message et le pseudo du fournisseur au SkeletonController
        skeleton_controller.setSupplierForMessaging(supplierPseudo, message);
    
        // Charger la page de messagerie
        skeleton_controller.loadMessageriePage();
    }
    

    @FXML public void cancel(){
        System.out.println("Go back !");
        skeleton_controller.loadListServiceOfferPage();
    }

    @FXML public void delete(){
        System.out.println("Suppression de l'offre");
        service_offer.delete();
        skeleton_controller.loadListServiceOfferPage();
    }

    @FXML public void contactHelp(){
        System.out.println("Help !!");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date;
        try {
            date = LocalDate.parse(service_offer.getDate_publication(), inputFormatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Erreur de format de date", e);
        }
        String formattedDate = date.format(outputFormatter);
        String message = "J'ai besoin d'aide sur l'annonce de \n"
        + "type \"" + "service" + "\" \n"
        + "créé par \"" + currentUser.getPseudo() + "\" \n"
        + "le \"" + formattedDate + "\" \n"
        + "avec comme titre \"" + service_offer.getTitle() + "\" \n"
        + "comme description \"" + service_offer.getDescription() + "\" et \n"
        + "comme prix \"" + service_offer.getPrice() + " florains\" \n"
        + "situé à \"" + service_offer.getSupplier().getLocalisation() + "\""; 
       
        skeleton_controller.setSupplierForMessaging("admin", message);
        skeleton_controller.loadMessageriePage();
    }
}
