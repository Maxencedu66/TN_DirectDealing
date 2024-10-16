package eu.telecomnancy.labfx;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.LocalTime;


public class Planning {
    private User user;
    private ArrayList<CombinedOffer> my_offer; // Les offres que j'ai publiées et qui sont réservées
    private ArrayList<CombinedOffer> my_demand; // Les offres que d'autres ont publiées et que j'ai réservées

    public Planning(User user) {
        this.user = user;
        this.my_offer = new ArrayList<CombinedOffer>();
        this.my_demand = new ArrayList<CombinedOffer>();
    }

    public void update(){
        String sql_equipment = "SELECT owner_mail, name, description, estPris, start_availability, end_availability, estPris, price, quantity, book_begin, book_end FROM equipement WHERE (owner_mail = ? OR estPris = ?)";

        try (Connection conn = DataBase.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql_equipment)) {
            pstmt.setString(1, user.getMail());
            pstmt.setString(2, user.getMail());
            ResultSet rs = pstmt.executeQuery();
            int i = 0;
            while (rs.next()) {
                System.out.println("Je suis dans le while " + i + "\n");
                i++;
                String ownerMail             = rs.getString("owner_mail");
                String name                  = rs.getString("name");
                String description           = rs.getString("description");
                String estPris               = rs.getString("estPris");
                String start_availability    = rs.getString("start_availability");
                String end_availability      = rs.getString("end_availability");
                int price                    = rs.getInt("price");
                int quantity                 = rs.getInt("quantity");
                String book_begin            = rs.getString("book_begin");
                String book_end              = rs.getString("book_end");

                EquipmentOffer equipment     = new EquipmentOffer(ownerMail, name, description, quantity, LocalDate.parse(start_availability), LocalDate.parse(end_availability), price);
                if (estPris != null) {
                    equipment.setEstPris(estPris);
                }
                if (book_begin != null) {
                    equipment.setBook_begin(LocalDate.parse(book_begin));
                    equipment.setBook_end(LocalDate.parse(book_end));
                }
                else {
                    System.out.println("book_begin est null");
                }
                if (ownerMail.equals(user.getMail())) {
                    // Si l'offre est prise et que je suis le propriétaire, je l'ajoute à mes offres
                    this.my_offer.add(new CombinedOffer(equipment));
                } else {
                    // Si l'offre est prise et que je suis le client, je l'ajoute à mes demandes
                    this.my_demand.add(new CombinedOffer(equipment));
                }
                // Print les dates de début et de fin


            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        String sql_service = "SELECT supplier_mail, title, description, estPris, start, end, time, price, book_begin, book_end FROM service_offers WHERE supplier_mail = ? OR estPris = ?";

        try (Connection conn = DataBase.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql_service)) {
            pstmt.setString(1, user.getMail());
            pstmt.setString(2, user.getMail());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String supplierMail = rs.getString("supplier_mail");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String estPris = rs.getString("estPris");
                String start = rs.getString("start");
                String end = rs.getString("end");
                String time = rs.getString("time");
                int price = rs.getInt("price");
                String book_begin = rs.getString("book_begin");
                String book_end = rs.getString("book_end");


                ServiceOffer service = new ServiceOffer(supplierMail, title, description);
                if (estPris != null) {
                    service.setEstPris(estPris);
                
                }
                service.setStart(LocalDate.parse(start));
                service.setEnd(LocalDate.parse(end));
                service.setTime(LocalTime.of(12, 0));
                service.setPrice(price);




                if (book_begin != null) {
                    service.setBook_begin(LocalDate.parse(book_begin));
                    service.setBook_end(LocalDate.parse(book_end));
                }
                if (supplierMail.equals(user.getMail())) {
                    // Si l'offre est prise et que je suis le propriétaire, je l'ajoute à mes offres
                    this.my_offer.add(new CombinedOffer(service));
                } 
                else {
                    // Si l'offre est prise et que je suis le client, je l'ajoute à mes demandes
                    this.my_demand.add(new CombinedOffer(service));
                }
                
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<CombinedOffer> getMyOffer() {
        return my_offer;
    }

    public ArrayList<CombinedOffer> getMyDemand() {
        return my_demand;
    }



}

