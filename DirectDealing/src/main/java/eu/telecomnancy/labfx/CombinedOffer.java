package eu.telecomnancy.labfx;

import java.time.LocalDate;
import java.time.LocalTime;

public class CombinedOffer {
    // Champs communs
    private int id;
    private User owner;
    private String title;
    private String description;
    private int price;
    private String estPris;
    private String date_publication;
    private LocalDate start;
    private LocalDate end;
    private LocalDate startBook;
    private LocalDate endBook;
    private double distanceToCurrentUser;

    // Champs spécifiques à EquipmentOffer
    private int quantity;

    // Champs spécifiques à ServiceOffer
    private LocalTime time;
    private boolean isRecurrent;
    private String daysOfService;
    private int nbRecurrencingWeeks;

    // Type de l'offre pour différencier EquipmentOffer de ServiceOffer
    private OfferType type;

    public enum OfferType {
        EQUIPMENT_OFFER, SERVICE_OFFER
    }

    // Constructeur pour EquipmentOffer
    public CombinedOffer(EquipmentOffer equipmentOffer) {
        this.id = equipmentOffer.getId();
        this.owner = equipmentOffer.getOwner();
        this.title = equipmentOffer.getName();
        this.description = equipmentOffer.getDescription();
        this.price = equipmentOffer.getPrice();
        this.estPris = equipmentOffer.getEstPris();
        this.quantity = equipmentOffer.getQuantity();
        this.start = equipmentOffer.getStartAvailability();
        this.end = equipmentOffer.getEndAvailability();
        this.startBook = equipmentOffer.getBook_begin();
        this.endBook = equipmentOffer.getBook_end();
        this.type = OfferType.EQUIPMENT_OFFER;
        this.date_publication = equipmentOffer.getDate_publication();
    }

    // Constructeur pour ServiceOffer
    public CombinedOffer(ServiceOffer serviceOffer) {
        this.id          = serviceOffer.getId();
        this.owner       = serviceOffer.getSupplier();
        this.title       = serviceOffer.getTitle();
        this.description = serviceOffer.getDescription();
        this.price       = serviceOffer.getPrice();
        this.estPris     = serviceOffer.getEstPris();
        this.start       = serviceOffer.getStart();
        this.end         = serviceOffer.getEnd();
        this.startBook   = serviceOffer.getBook_begin();
        this.endBook     = serviceOffer.getBook_end();
        this.time        = serviceOffer.getTime();
        this.isRecurrent = serviceOffer.getIsRecurrent();
        this.daysOfService       = serviceOffer.getDaysOfService();
        this.nbRecurrencingWeeks = serviceOffer.getRecurrency();
        this.type = OfferType.SERVICE_OFFER;
        this.date_publication    = serviceOffer.getDate_publication();
    }


    // Getters et setters pour les champs communs et spécifiques

    public OfferType getType() {
        return type;
    }

    public String getTypeString() {
        if (this.type == OfferType.EQUIPMENT_OFFER) {
            return "Equipment";
        } else {
            return "Service";
        }
    }

    // Autres getters et setters...
    public int getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public String getOwnerName() {
        return owner.getNom();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getEstPris() {
        return this.estPris;
    }

    public int getQuantity() {
        if (this.type == OfferType.EQUIPMENT_OFFER) {
            return quantity;
        } else {
            throw new UnsupportedOperationException("getQuantity is not supported for ServiceOffer");
        }
    }

    public LocalDate getStart() {
        return this.start;
    }

    public String getStartString() {
        return start.toString();
    }

    public LocalDate getEnd() {
        return this.end;
    }

    public String getEndString() {
        return end.toString();
    }

    public LocalTime getTime() {
        if (this.type == OfferType.SERVICE_OFFER) {
            return time;
        } else {
            throw new UnsupportedOperationException("getTime is not supported for EquipmentOffer");
        }
    }

    public boolean getIsRecurrent() {
        if (this.type == OfferType.SERVICE_OFFER) {
            return isRecurrent;
        } else {
            throw new UnsupportedOperationException("getIsRecurrent is not supported for EquipmentOffer");
        }
    }

    public String getDaysOfService() {
        if (this.type == OfferType.SERVICE_OFFER) {
            return daysOfService;
        } else {
            throw new UnsupportedOperationException("getDaysOfService is not supported for EquipmentOffer");
        }
    }

    public int getNbRecurrencingWeeks() {
        if (this.type == OfferType.SERVICE_OFFER) {
            return nbRecurrencingWeeks;
        } else {
            throw new UnsupportedOperationException("getNbRecurrencingWeeks is not supported for EquipmentOffer");
        }
    }

    public String getDate_publication() {
        return date_publication;
    }

    public LocalDate getStartBook() {
        return startBook;
    }

    public LocalDate getEndBook() {
        return endBook;
    }

    public void setStartBook(LocalDate startBook) {
        this.startBook = startBook;
    }

    public void setEndBook(LocalDate endBook) {
        this.endBook = endBook;
    }
}
