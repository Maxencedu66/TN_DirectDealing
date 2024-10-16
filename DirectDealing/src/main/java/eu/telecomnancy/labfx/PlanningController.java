package eu.telecomnancy.labfx;

import javafx.scene.input.MouseEvent;

import com.calendarfx.view.CalendarView;
import javafx.fxml.FXML;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;


import java.time.LocalDate;
import java.time.LocalTime;

import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl;

public class PlanningController {

    @FXML
    private CalendarView planningCalendar;

    private SkeletonController skeleton_controller;
    private User currentUser;
    private Planning planning;
    
    private Calendar serviceOfferedCalendar; // services que j'ai publiés et qui sont réservés
    private Calendar equipmentOfferedCalendar; // équipements que j'ai publiés et qui sont réservés
    private Calendar serviceNoneOfferedCalendar; // services que j'ai publiés et qui ne sont pas réservés
    private Calendar equipmentNoneOfferedCalendar; // équipements que j'ai publiés et qui ne sont pas réservés
    private Calendar serviceDemandedCalendar; // services que j'ai réservés
    private Calendar equipmentDemandedCalendar; // équipements que j'ai réservés

    public void setSkeletonController(SkeletonController skeleton_controller) {
        this.skeleton_controller = skeleton_controller;
    }

    public void initialize() {
        currentUser = Main.getCurrentUser();
        planning = new Planning(currentUser);
        planningCalendar.setEntryFactory(param -> null);



        serviceOfferedCalendar = new Calendar("Services Proposés accepté");
        equipmentOfferedCalendar = new Calendar("Equipments Proposés accepté");
        serviceNoneOfferedCalendar = new Calendar("Services proposé non accepté");
        equipmentNoneOfferedCalendar = new Calendar("Equipments proposé non accepté");
        serviceDemandedCalendar = new Calendar("Services demandé");
        equipmentDemandedCalendar = new Calendar("Equipments demandé");

        // set des styles des calendriers
        serviceOfferedCalendar.setStyle(Calendar.Style.STYLE1);
        equipmentOfferedCalendar.setStyle(Calendar.Style.STYLE2);
        serviceNoneOfferedCalendar.setStyle(Calendar.Style.STYLE3);
        equipmentNoneOfferedCalendar.setStyle(Calendar.Style.STYLE4);
        serviceDemandedCalendar.setStyle(Calendar.Style.STYLE5);
        equipmentDemandedCalendar.setStyle(Calendar.Style.STYLE6);

        // Ajout des calendriers à la vue
        CalendarSource myCalendarSource = new CalendarSource("Ma Source");
        myCalendarSource.getCalendars().addAll(serviceOfferedCalendar, equipmentOfferedCalendar, serviceNoneOfferedCalendar, equipmentNoneOfferedCalendar, serviceDemandedCalendar, equipmentDemandedCalendar);
        planningCalendar.getCalendarSources().add(myCalendarSource);

        // on set le calendrier en mois de base
        planningCalendar.showMonthPage();

        // Désactiver le bouton d'ajout de sources de calendrier
        planningCalendar.setShowAddCalendarButton(false);

        // Définir les calendriers en lecture seule
        serviceOfferedCalendar.setReadOnly(true);
        equipmentOfferedCalendar.setReadOnly(true);
        serviceNoneOfferedCalendar.setReadOnly(true);
        equipmentNoneOfferedCalendar.setReadOnly(true);
        serviceDemandedCalendar.setReadOnly(true);
        equipmentDemandedCalendar.setReadOnly(true);

        
        AddEventToCalendar();
    }

    public void ajouterEvenement(CombinedOffer offer) {
        System.out.println("ajouterEvenement");

        // Vérifier si l'offre et son titre sont non null
        if (offer == null || offer.getTitle() == null) {
            System.out.println("Offre ou titre de l'offre non définis");
            return;
        }

        // Utiliser getStartBook et getEndBook, si disponibles, sinon utiliser getStart et getEnd
        LocalDate dateDebut = (offer.getStartBook() != null) ? offer.getStartBook() : offer.getStart();
        LocalDate dateFin = (offer.getEndBook() != null) ? offer.getEndBook() : offer.getEnd();


        // affiche les deux date debut et fin
        System.out.println("###########");
        System.out.println("dateDebut : " + dateDebut.toString() + "\n");
        System.out.println("dateFin : " + dateFin.toString() + "\n");
        // Vérifier si les dates sont non null
        if (dateDebut == null || dateFin == null) {
            System.out.println("Dates de début ou de fin non définies");
            return;
        }

        Entry<String> evenement = new Entry<>(offer.getTitle());

        // Configurer l'événement
        evenement.changeStartDate(dateDebut);
        evenement.changeStartTime(LocalTime.of(0, 0));
        evenement.changeEndDate(dateFin);
        evenement.changeEndTime(LocalTime.of(23, 59));

        // Sélectionner le calendrier approprié
        Calendar calendar = null;
        if (offer.getType() == CombinedOffer.OfferType.SERVICE_OFFER) {
            calendar = (offer.getEstPris() == null) ? serviceNoneOfferedCalendar :
                    (offer.getOwner() != null && offer.getOwner().getMail().equals(currentUser.getMail())) ? serviceOfferedCalendar :
                    serviceDemandedCalendar;
        } else if (offer.getType() == CombinedOffer.OfferType.EQUIPMENT_OFFER) {
            if(offer.getEstPris() == null){
                System.out.println(offer.getEstPris()+"\n\n\n");
                calendar = equipmentNoneOfferedCalendar;
            }
            else if(offer.getEstPris().equals(currentUser.getMail())){
                calendar = equipmentDemandedCalendar;
            }else{  
                calendar = equipmentOfferedCalendar;
            }
        }

        // Vérifier si le calendrier est défini
        if (calendar == null) {
            System.out.println("Calendrier non trouvé pour l'offre");
            return;
        }

        // Ajouter l'entrée au calendrier sélectionné
        calendar.addEntry(evenement);
    }

    private void AddEventToCalendar() {
        // update permet de mettre tout les event dans lesquels participent user
        planning.update();
        // Vérifiez que les méthodes getMyOffer() et getMyDemand() existent dans la classe Planning
        System.out.println("planning.getMyOffer()");
        for (CombinedOffer offer : planning.getMyOffer()) {
            handleOffer(offer);
        }
        for (CombinedOffer offer : planning.getMyDemand()) {
            handleOffer(offer);
        }
    }

    private void handleOffer(CombinedOffer offer) {
        System.out.println("offer.getType()");
        ajouterEvenement(offer);
    }
}
