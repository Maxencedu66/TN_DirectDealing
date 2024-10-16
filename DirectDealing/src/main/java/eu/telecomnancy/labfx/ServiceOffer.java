package eu.telecomnancy.labfx;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalTime;


// Description: Classe représentant une offre de service. Elle contient un titre, une description, une date et une heure.
//              Elle peut être récurrente, auquel cas on lui ajoute un tableau de jours de la semaine où le service doit être réalisé.$

public class ServiceOffer {
    private int id;
    private User supplier;
    private String title;
    private String description;
    private LocalDate start;
    private LocalDate end;
    private LocalTime time;
    private boolean isRecurrent;
    private String daysOfService; // Stocké comme une chaîne, par exemple "1,3,5"
    private int nbRecurrencingWeeks;
    private String supplier_mail;
    private int price;
    private String estPris;
    private String date_publication;
    private LocalDate book_begin;
    private LocalDate book_end;

    // Constructeur
    public ServiceOffer(String supplier_mail) {
        this.supplier_mail = supplier_mail;
        this.supplier = new User(supplier_mail);
        System.out.println("Constructeur avec un loaddb 1");
        loadServiceFromDB();
    }

    public ServiceOffer(String supplierMail, String title, String description, LocalDate start, LocalDate end, LocalTime time, int price) {
        System.out.println("Constructeur avec un loaddb 2 ");
        this.supplier_mail = supplierMail;
        this.supplier = new User(supplierMail);
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.time = time;
        this.price = price;
        createNewOffer();
    }

       public ServiceOffer(String supplier_mail, String title, String description, LocalDate start, LocalDate end, LocalTime time, int price, String estPris){
                System.out.println("Constructeur sans un loaddb 5 ");
        this.supplier_mail = supplier_mail;
        this.supplier = new User(supplier_mail);
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.time = time;
        this.price = price;
    }


    public ServiceOffer(String supplier_mail, String title, String description){
                System.out.println("Constructeur avec un loaddb 3");
        this.supplier_mail = supplier_mail;
        this.supplier = new User(supplier_mail);
        this.title = title;
        this.description = description;
        loadServiceFromDB();
    }

    public ServiceOffer(String supplier_mail, String title, String description, String start, String estPris){
        System.out.println("Constructeur avec un loaddb 4");
        this.supplier_mail = supplier_mail;
        this.supplier = new User(supplier_mail);
        this.title = title;
        this.description = description;
        this.start = LocalDate.parse(start);
        this.estPris = estPris;
        loadServiceFromDBHome();
    }

 

    public ServiceOffer(User supplier, String title, String description, LocalDate start, LocalDate end, LocalTime time, boolean isRecurrent, String daysOfService, int price) {
        System.out.println("Constructeur avec un loaddb 6");
        this.supplier = supplier;
        this.supplier_mail = supplier.getMail();
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.time = time;
        this.isRecurrent = isRecurrent;
        this.daysOfService = daysOfService;
        this.price = price;
        this.estPris = null;
        if (isRecurrent) {
            String[] days = daysOfService.split(",");
            this.nbRecurrencingWeeks = days.length;
        } else {
            this.nbRecurrencingWeeks = 0;
        }
        createNewOffer();
    }




    public void createNewOffer(){
        String sql = "INSERT INTO service_offers (supplier_mail, title, description, start, end, time, is_recurrent, days_of_service, price, date_publication) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
    
            pstmt.setString(1, this.supplier.getMail());
            pstmt.setString(2, this.title);
            pstmt.setString(3, this.description);
            pstmt.setString(4, (this.start != null) ? this.start.toString() : null);
            pstmt.setString(5, (this.end != null) ? this.end.toString() : null);
            pstmt.setString(6, (this.time != null) ? this.time.toString() : null);
            pstmt.setBoolean(7, this.isRecurrent);
            pstmt.setString(8, this.daysOfService);
            pstmt.setInt(9, this.price);
            pstmt.setString(10, LocalDate.now().toString());
    
            int affectedRows = pstmt.executeUpdate();
    
            // Vérifier si l'insertion a réussi et récupérer l'ID généré
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.id = generatedKeys.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadServiceFromDB() {
        String sql = "SELECT * FROM service_offers WHERE supplier_mail = ? AND title = ? AND description = ?";

        try (Connection conn = DataBase.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, supplier_mail);
            pstmt.setString(2, title); // Assure-toi que la variable 'title' est définie et contient le titre à vérifier
            pstmt.setString(3, description); // Assure-toi que la variable 'description' est définie et contient la description à vérifier
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.id = rs.getInt("id");
                this.title = rs.getString("title");
                this.description = rs.getString("description");
                String startDateString = rs.getString("start");
                if (startDateString != null && !startDateString.isEmpty()) {
                    this.start = LocalDate.parse(startDateString);
                    } else {
                    this.start = null;
                    }
                String endDateString = rs.getString("end");
                if (endDateString != null && !endDateString.isEmpty()) {
                    this.end = LocalDate.parse(endDateString);
                    } else {
                    this.end = null;
                    }
                String timeString = rs.getString("time");
                if (timeString != null && !timeString.isEmpty()) {
                    this.time = LocalTime.parse(timeString);
                    } else {
                    this.time = null;
                    }
                this.isRecurrent = rs.getBoolean("is_recurrent");
                this.daysOfService = rs.getString("days_of_service");
                this.price = rs.getInt("price");
                this.date_publication = rs.getString("date_publication");
                this.estPris = rs.getString("estPris");
                String bookingBeginString = rs.getString("book_begin");
                if (bookingBeginString != null && !bookingBeginString.isEmpty()) {
                    this.book_begin = LocalDate.parse(bookingBeginString);
                    } else {
                    this.book_begin = null;
                    }
                String bookingEndString = rs.getString("book_end");
                if (bookingEndString != null && !bookingEndString.isEmpty()) {
                    this.book_end = LocalDate.parse(bookingEndString);
                    } else {
                    this.book_end = null;
                    }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadServiceFromDBHome() {
        String sql = "SELECT * FROM service_offers WHERE supplier_mail = ? AND title = ? AND description = ? AND start = ? AND estPris IS NULL";

        try (Connection conn = DataBase.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, supplier_mail);
            pstmt.setString(2, title); // Assure-toi que la variable 'title' est définie et contient le titre à vérifier
            pstmt.setString(3, description); // Assure-toi que la variable 'description' est définie et contient la description à vérifier
            pstmt.setString(4, this.start.toString());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.id = rs.getInt("id");
                this.title = rs.getString("title");
                this.description = rs.getString("description");
                String startDateString = rs.getString("start");
                if (startDateString != null && !startDateString.isEmpty()) {
                    this.start = LocalDate.parse(startDateString);
                    } else {
                    this.start = null;
                    }
                String endDateString = rs.getString("end");
                if (endDateString != null && !endDateString.isEmpty()) {
                    this.end = LocalDate.parse(endDateString);
                    } else {
                    this.end = null;
                    }
                String timeString = rs.getString("time");
                if (timeString != null && !timeString.isEmpty()) {
                    this.time = LocalTime.parse(timeString);
                    } else {
                    this.time = null;
                    }
                this.isRecurrent = rs.getBoolean("is_recurrent");
                this.daysOfService = rs.getString("days_of_service");
                this.price = rs.getInt("price");
                this.date_publication = rs.getString("date_publication");
                this.estPris = rs.getString("estPris");
                String bookingBeginString = rs.getString("book_begin");
                if (bookingBeginString != null && !bookingBeginString.isEmpty()) {
                    this.book_begin = LocalDate.parse(bookingBeginString);
                    } else {
                    this.book_begin = null;
                    }
                String bookingEndString = rs.getString("book_end");
                if (bookingEndString != null && !bookingEndString.isEmpty()) {
                    this.book_end = LocalDate.parse(bookingEndString);
                    } else {
                    this.book_end = null;
                    }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        String sql = "UPDATE service_offers SET supplier_mail = ?, title = ?, description = ?, start = ?, end = ?, time = ?, is_recurrent = ?, days_of_service = ?, price = ?, WHERE id = ?";
    
        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, this.supplier.getMail());
            pstmt.setString(2, this.title);
            pstmt.setString(3, this.description);
            pstmt.setString(4, (this.start != null) ? this.start.toString() : null);
            pstmt.setString(5, (this.end != null) ? this.end.toString() : null);
            pstmt.setString(6, (this.time != null) ? this.time.toString() : null);
            pstmt.setBoolean(7, this.isRecurrent);
            pstmt.setString(8, this.daysOfService);
            pstmt.setInt(9, this.nbRecurrencingWeeks);
            pstmt.setInt(10, this.id);
    
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public boolean reserveOffer(ServiceOffer offer, String currentUserEmail, LocalDate begin, LocalDate end) {
        Connection conn = null;
        try {
            conn = DataBase.getConnection();
            conn.setAutoCommit(false); // Démarrer une transaction
    
            // Vérifier si l'offre est déjà réservée
            String sql = "SELECT estPris FROM service_offers WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, offer.getId());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next() && rs.getString("estPris") != null) {
                    System.out.println("Cette offre a déjà été réservée");
                    conn.rollback();
                    return false;
                }
            }
    
            // Mettre à jour les florains
            if (!updateFlorains(conn, currentUserEmail, offer.getSupplierMail(), offer.getPrice())) {
                conn.rollback();
                return false;
            }
    
            // Réserver l'offre
            sql = "UPDATE service_offers SET estPris = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, currentUserEmail);
                pstmt.setInt(2, offer.getId());
                pstmt.executeUpdate();
            }
    
            // Ajouter les dates de réservation
            sql = "UPDATE service_offers SET book_begin = ?, book_end = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, begin.toString());
                pstmt.setString(2, end.toString());
                pstmt.setInt(3, offer.getId());
                pstmt.executeUpdate();
            }
            this.book_begin = begin;
            this.book_end = end;

            conn.commit(); // Valider la transaction
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la réservation de l'offre");
            if (conn != null) {
                try {
                    conn.rollback(); // Annuler la transaction en cas d'erreur
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaurer le mode de commit automatique
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
    }

    public boolean updateFlorains(Connection conn, String buyerEmail, String sellerEmail, int price) throws SQLException {
        // Déduire les florains du compte de l'acheteur
        String sql = "UPDATE profil SET nb_florain = nb_florain - ? WHERE mail = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, price);
            pstmt.setString(2, buyerEmail);
            pstmt.executeUpdate();
        }
    
        // Ajouter les florains au compte du vendeur
        sql = "UPDATE profil SET nb_florain = nb_florain + ? WHERE mail = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, price);
            pstmt.setString(2, sellerEmail);
            pstmt.executeUpdate();
        }
    
        return true;
    }
    public static List<ServiceOffer> searchOffers(User currentUser, String keywords, LocalDate begin, LocalDate end, Integer minPrice, Integer maxPrice, String timeMin, String timeMax, double radius) {
        System.out.println("Début de la recherche des offres avec un rayon de " + radius + " km et les mots clés " + keywords + " et les dates " + begin + " " + end + " et les prix " + minPrice + " " + maxPrice);
    
        List<ServiceOffer> offers = new ArrayList<>();
    
        // Construction de la requête SQL avec les filtres nécessaires
        String sql = "SELECT id, supplier_mail, title, description, start, end, time, price, estPris FROM service_offers WHERE estPris IS NULL";
    
        if (!keywords.isEmpty()) {
            sql += " AND title LIKE ?";
        }
        if (begin != null && end != null) {
            // Les offres doivent être disponibles pour toute la période demandée
            sql += " AND (start <= ? AND end >= ?)";
        
        } else if (begin != null) {
            // Les offres doivent commencer au plus tard à la date de début
            sql += " AND (start <= ?)";
        } else if (end != null) {
            // Les offres doivent se terminer au plus tôt à la date de fin
            sql += " AND (end >= ?)";
        }
        if (minPrice != null) {
            sql += " AND price >= ?";
        }
        if (maxPrice != null) {
            sql += " AND price <= ?";
        }
        if (!timeMin.isEmpty()) {
            sql += " AND time >= ?";
        }
        if (!timeMax.isEmpty()) {
            sql += " AND time <= ?";
        }
    
        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            int paramIndex = 1;
    
            if (!keywords.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + keywords + "%");
            }
            if (begin != null) {
                pstmt.setString(paramIndex++, begin.toString());
            }
            if (end != null) {
                pstmt.setString(paramIndex++, end.toString());
            }
            if (minPrice != null) {
                pstmt.setInt(paramIndex++, minPrice);
            }
            if (maxPrice != null) {
                pstmt.setInt(paramIndex++, maxPrice);
            }
            if (!timeMin.isEmpty()) {
                pstmt.setString(paramIndex++, timeMin);
            }
            if (!timeMax.isEmpty()) {
                pstmt.setString(paramIndex++, timeMax);
            }
    
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate serviceStart = rs.getString("start") != null ? LocalDate.parse(rs.getString("start")) : null;
                    LocalDate serviceEnd = rs.getString("end") != null ? LocalDate.parse(rs.getString("end")) : null;
                    LocalTime time = rs.getString("time") != null ? LocalTime.parse(rs.getString("time")) : null;
    
                    ServiceOffer offer = new ServiceOffer(
                        rs.getString("supplier_mail"),
                        rs.getString("title"),
                        rs.getString("description"),
                        serviceStart,
                        serviceEnd,
                        time,
                        rs.getInt("price"),
                        rs.getString("estPris")


                    );
                    offers.add(offer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (ServiceOffer offer : offers) {
            System.out.println("id: " + offer.getId() + " title: " + offer.getTitle() + " description: " + offer.getDescription() + " start: " + offer.getStart() + " end: " + offer.getEnd() + " time: " + offer.getTime() + " isRecurrent: " + offer.getIsRecurrent() + " repetitionDay: " + offer.getDaysOfService() + " price: " + offer.getPrice() + " nb recurrence: " + offer.getRecurrency() + " supplier mail: " + offer.getSupplierMail() + " est pris: " + offer.getEstPris());
        }
// Filtrer les offres par rayon
        return offers.stream()
            .filter(offer -> isWithinRadius(currentUser, offer.getSupplier(), radius))
            .collect(Collectors.toList());
 
    }

    private static boolean isWithinRadius(User currentUser, User offerOwner, double radius) {
        // Calcul de la distance entre l'utilisateur actuel et le propriétaire de l'offre
        double distance = currentUser.getDistanceTo(offerOwner);
    
        // Vérifiez si la distance est inférieure ou égale au rayon spécifié
        return distance <= radius;
    }

    public void delete() {
        String sql = "DELETE FROM service_offers WHERE id = ?";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, this.id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Offre supprimée avec succés");
            } else {
                System.out.println("Aucune offre trouvée avec cet ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la suppression de l'offre");
        }
    }
    

    public String getSupplierMail(){
        return supplier_mail;
    }

    public User getSupplier(){
        return supplier;
    }

    public int getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public LocalDate getStart(){
        return start;
    }

    public String getStartStr(){
        if(start == null){
            return "À définir";
        }else{
            return start.toString();
        }
    }

    public void setStart(LocalDate start){
        this.start = start;
    }

    public LocalDate getEnd(){
        return end;
    }

    public String getEndStr(){
        if(end == null){
            return "À définir";
        }else{
            return end.toString();
        }
    }

    public void setEnd(LocalDate end){
        this.end = end;
    }

    public LocalTime getTime(){
        return time;
    }

    public String getTimeStr(){
        if(time == null){
            return "À définir";
        }else{
            return time.toString();
        }
    }

    public void setTime(LocalTime time){
        this.time = time;
    }

    public void setRecurrent(boolean isRecurrent) {
        this.isRecurrent = isRecurrent;
    }

    public void setDaysOfService(String daysOfService) {
        this.daysOfService = daysOfService;
    }


    public boolean getIsRecurrent() {
        return isRecurrent;
    }

    public String getDaysOfService() {
        return daysOfService;
    }

    public int getRecurrency() {
        return nbRecurrencingWeeks;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setNbRecurrencingWeeks(String daysOfService) {
        if (this.isRecurrent) {
            String[] days = daysOfService.split(",");
            this.nbRecurrencingWeeks = days.length;
        } else {
            this.nbRecurrencingWeeks = 0;
        }
    }

    //get estPris
    public String getEstPris(){
        return estPris;
    }

    //set estPris
    public void setEstPris(String estPris) {
        this.estPris = estPris;
    }

    //get date_publication
    public String getDate_publication(){
        return date_publication;
    }

    //get book_begin
    public LocalDate getBook_begin(){
        return book_begin;
    }

    //get book_end
    public LocalDate getBook_end(){
        return book_end;
    }

    public void setBook_begin(LocalDate book_begin){
        this.book_begin = book_begin;
    }

    public void setBook_end(LocalDate book_end){
        this.book_end = book_end;
    }

}

