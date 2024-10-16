package eu.telecomnancy.labfx;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;



public class User {

    private int id;
    private String prenom;
    private String nom;
    private String pseudo;
    private String mail;
    private String phone;
    private String photoProfil;
    private String localisation;
    private LocalDate dateInscription;
    private String statusCompte;
    private String etatCompte;
    private int nbFlorain;
    private String historiqueFlorain;
    private Double note;

    
    public User(String mail) {
        this.mail = mail;
        loadUserFromDB();
    }

    private void loadUserFromDB() {
        String sql = "SELECT * FROM profil WHERE mail = ?";

        try (Connection conn = DataBase.getConnection(); // Utiliser DatabaseUtil pour obtenir la connexion
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.mail);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.id = rs.getInt("id");
                this.prenom = rs.getString("prenom");
                this.nom = rs.getString("nom");
                this.pseudo = rs.getString("pseudo");
                this.phone = rs.getString("phone");
                this.photoProfil = rs.getString("photo_profil");
                this.localisation = rs.getString("localisation");
                String dateString = rs.getString("date_inscription");
                System.out.println("datestring" + dateString);
                if (dateString != null && !dateString.isEmpty()) {
                this.dateInscription = LocalDate.parse(dateString);
                } else {
                this.dateInscription = null; // ou une date par défaut si nécessaire
                }
                this.statusCompte = rs.getString("status_compte");
                this.etatCompte = rs.getString("etat_compte");
                this.nbFlorain = rs.getInt("nb_florain");
                this.historiqueFlorain = rs.getString("historique_florain");
                this.note = rs.getDouble("note");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public void update() {
        String sql = "UPDATE profil SET prenom = ?, nom = ?, pseudo = ?, phone = ?, photo_profil = ?, localisation = ?, status_compte = ?, etat_compte = ?, nb_florain = ?, historique_florain = ?, note = ? WHERE mail = ?";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.prenom);
            pstmt.setString(2, this.nom);
            pstmt.setString(3, this.pseudo);
            pstmt.setString(4, this.phone);
            pstmt.setString(5, this.photoProfil);
            pstmt.setString(6, this.localisation);
            pstmt.setString(7, this.statusCompte);
            pstmt.setString(8, this.etatCompte);
            pstmt.setInt(9, this.nbFlorain);
            pstmt.setString(10, this.historiqueFlorain);
            pstmt.setDouble(11, this.note);
            pstmt.setString(12, this.mail);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Getters et Setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMail() {
        return mail;
    }

    public String setMail(String mail) {
        return this.mail;
    }
    
    public String getPseudo() {
        return pseudo;
    }
    
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getPhotoProfil() {
        return photoProfil;
    }
    
    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }
    
    public String getLocalisation() {
        return localisation;
    }
    
    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
    
    public LocalDate getDateInscription() {
        return dateInscription;
    }
    
    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }
    
    public String getStatusCompte() {
        return statusCompte;
    }
    
    public void setStatusCompte(String statusCompte) {
        this.statusCompte = statusCompte;
    }
    
    public String getEtatCompte() {
        return etatCompte;
    }
    
    public void setEtatCompte(String etatCompte) {
        if (etatCompte.equals("actif") || etatCompte.equals("sommeil")) {
            this.etatCompte = etatCompte;
        } else {
            throw new IllegalArgumentException("L'état du compte doit être 'actif' ou 'inactif'");
        }
    }
    
    public int getNbFlorain() {
        return nbFlorain;
    }
    
    public void setNbFlorain(int nbFlorain) {
        this.nbFlorain = nbFlorain;
    }
    
    public String getHistoriqueFlorain() {
        return historiqueFlorain;
    }
    
    public void setHistoriqueFlorain(String historiqueFlorain) {
        this.historiqueFlorain = historiqueFlorain;
    }
    
    public Double getNote() {
        return note;
    }
    
    public void setNote(Double note) {
        this.note = note;
    }
    
    public double calculateDistanceTo(User otherUser) {
        double[] thisLocation = getCoordinates(this.localisation);
        double[] otherLocation = getCoordinates(otherUser.getLocalisation());
    
        double distance = haversine(thisLocation[0], thisLocation[1], otherLocation[0], otherLocation[1]);
        System.out.println("Calcul de la distance entre " + this.localisation + " (" + thisLocation[0] + ", " + thisLocation[1] + ") et " + otherUser.getLocalisation() + " (" + otherLocation[0] + ", " + otherLocation[1] + ") : " + distance + " km");
        //print user city and other user city
        return distance;
    }

    // Méthode pour obtenir les coordonnées (latitude, longitude) d'une localisation
    private double[] getCoordinates(String cityName) {
        String line;
        InputStream inputStream;
        inputStream = getClass().getResourceAsStream("/eu/telecomnancy/labfx/cities.csv");
    
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            while ((line = br.readLine()) != null) {
                String[] cityData = line.split(",");
                if (cityData[0].equals("id")) {
                    continue; // Ignorer l'en-tête
                }
                // Vérifier si le nom de la ville correspond (en tenant compte des guillemets)
                String cityInCsv = cityData[4].replace("\"", ""); // Retirer les guillemets
                if (cityInCsv.equalsIgnoreCase(cityName)) {
                    double latitude = Double.parseDouble(cityData[6]);
                    double longitude = Double.parseDouble(cityData[7]);
                    return new double[]{latitude, longitude};
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier CSV : " + e.getMessage());
            e.printStackTrace();
        }
    return new double[]{0.0, 0.0}; // Retourner une valeur par défaut en cas d'échec
    }
    
    

    // Méthode Haversine pour calculer la distance entre deux points GPS
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // Rayon de la Terre en kilomètres

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }

    public void updateDistancesForNewUser() {
        try (Connection conn = DataBase.getConnection()) {
            // Récupérer tous les utilisateurs existants
            String sqlGetUsers = "SELECT * FROM profil";
            List<User> existingUsers = new ArrayList<>();
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetUsers);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(rs.getString("mail")); // Assurez-vous que le constructeur User initialise correctement un utilisateur
                    existingUsers.add(user);
                }
            }
    
            // Calculer et insérer les distances pour le nouvel utilisateur
            String sqlInsertDistance = "INSERT INTO user_distances (user_email1, user_email2, distance) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertDistance)) {
                for (User existingUser : existingUsers) {
                    double distance = this.calculateDistanceTo(existingUser);
    
                    pstmt.setString(1, this.mail); // ID du nouvel utilisateur
                    pstmt.setString(2, existingUser.getMail()); // ID de l'utilisateur existant
                    pstmt.setDouble(3, distance);
                    pstmt.executeUpdate();
    
                    // Éventuellement, insérer aussi l'inverse pour la symétrie si nécessaire
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception
        }
    }

    public double getDistanceTo(User otherUser) {
        double distance = 0.0;
        try (Connection conn = DataBase.getConnection()) {
            // Requête SQL pour obtenir la distance entre currentUser et offerOwner
            String sql = "SELECT distance FROM user_distances WHERE (user_email1 = ? AND user_email2 = ?) OR (user_email1 = ? AND user_email2 = ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Paramètres pour currentUser et offerOwner
                pstmt.setString(1, this.mail); // mail de currentUser
                pstmt.setString(2, otherUser.getMail()); // mail de offerOwner
                pstmt.setString(3, otherUser.getMail()); // mail de offerOwner
                pstmt.setString(4, this.mail); // mail de currentUser
    
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    distance = rs.getDouble("distance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception
        }
        return distance;
    }
    


    public List<User> getAllOtherUsers() {
        List<User> otherUsers = new ArrayList<>();
        try (Connection conn = DataBase.getConnection()) {
            String sqlGetUsers = "SELECT * FROM profil WHERE mail != ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetUsers)) {
                pstmt.setString(1, this.mail);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    User user = new User(rs.getString("mail"));
                    otherUsers.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception
        }
        return otherUsers;
    }

    public void updateDistances(List<User> otherUsers) {
        try (Connection conn = DataBase.getConnection()) {
            String sqlUpdateDistance = "UPDATE user_distances SET distance = ? WHERE (user_email1 = ? AND user_email2 = ?) OR (user_email1 = ? AND user_email2 = ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdateDistance)) {
                for (User otherUser : otherUsers) {
                    double newDistance = this.calculateDistanceTo(otherUser);
    
                    pstmt.setDouble(1, newDistance);
                    pstmt.setString(2, this.mail);
                    pstmt.setString(3, otherUser.getMail());
                    pstmt.setString(4, otherUser.getMail());
                    pstmt.setString(5, this.mail);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception
        }
    }
    
    public void recalculateDistances() {
        List<User> otherUsers = getAllOtherUsers();
        updateDistances(otherUsers);
    }
    


}

    