package eu.telecomnancy.labfx;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;


public class Main extends Application {

    private static Stage primaryStage;
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Main.currentUser = currentUser;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static ArrayList<ServiceOffer> getAllService() {
        String sql = "SELECT supplier_mail, title, description, estPris FROM service_offers";

        try (Connection conn = DataBase.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            ArrayList<ServiceOffer> all_service = new ArrayList<>();

            while (rs.next()) {
                if(rs.getString("estPris") != null){
                    continue;
                }
                String supplierMail = rs.getString("supplier_mail");
                String title = rs.getString("title");
                String description = rs.getString("description");

                ServiceOffer service = new ServiceOffer(supplierMail, title, description);
                all_service.add(service);
            }

            return all_service;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ArrayList<ServiceOffer> getAllServiceHome() {
        String sql = "SELECT supplier_mail, title, description, start, estPris FROM service_offers";

        try (Connection conn = DataBase.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            ArrayList<ServiceOffer> all_service = new ArrayList<>();

            while (rs.next()) {
                if(rs.getString("estPris") != null){
                    continue;
                }
                String supplierMail = rs.getString("supplier_mail");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String start = rs.getString("start");

                ServiceOffer service = new ServiceOffer(supplierMail, title, description, start, null);
                all_service.add(service);
            }

            return all_service;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ArrayList<EquipmentOffer> getAllEquipment() {
        String sql = "SELECT owner_mail, name, description, estPris FROM equipement";

        try (Connection conn = DataBase.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            ArrayList<EquipmentOffer> all_equipment = new ArrayList<>();

            while (rs.next()) {
                if(rs.getString("estPris") != null){
                    continue;
                }
                String owner_mail = rs.getString("owner_mail");
                String name = rs.getString("name");
                String description = rs.getString("description");

                EquipmentOffer equipment = new EquipmentOffer(owner_mail, name, description);
                all_equipment.add(equipment);
            }
            
            return all_equipment;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ArrayList<EquipmentOffer> getAllEquipmentHome() {
        String sql = "SELECT owner_mail, name, description, start_availability, estPris FROM equipement";

        try (Connection conn = DataBase.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            ArrayList<EquipmentOffer> all_equipment = new ArrayList<>();

            while (rs.next()) {
                if(rs.getString("estPris") != null){
                    continue;
                }
                String owner_mail = rs.getString("owner_mail");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String start_availability = rs.getString("start_availability");

                EquipmentOffer equipment = new EquipmentOffer(owner_mail, name, description, start_availability, null);
                all_equipment.add(equipment);
            }
            
            return all_equipment;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Fonction qui permet de modifier le curseur de la souris lorsqu'on passe dessus
    public static void applyCursorChangeToScene(Parent parent) {
    
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof Parent && (node != null)) {
                applyCursorChangeToScene((Parent) node);
            }

            if (isClickable(node)) {
                node.setOnMouseEntered(event -> {
                    if (node.getScene() != null) {
                        node.getScene().setCursor(Cursor.HAND);
                    }
                });
                node.setOnMouseExited(event -> {
                    if (node.getScene() != null) {
                        node.getScene().setCursor(Cursor.DEFAULT);
                    }
                });
            }
        }
    }

    private static boolean isClickable(Node node) {
        // Définir les critères pour un nœud cliquable, par exemple :
        return node instanceof Button || node instanceof TableView || node instanceof ListView;
    }
    

    public static void main(String[] args) {
        DataBase.initializeDatabase();
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        setPrimaryStage(primaryStage);
        primaryStage.setTitle("DirectDealing");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/WelcomePage.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 1600, 900);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    // Méthode pour vérifier si une ville existe dans le fichier CSV
    public static boolean testCity(String cityName){
        String line;
        InputStream inputStream;
        inputStream = Main.class.getResourceAsStream("/eu/telecomnancy/labfx/cities.csv");
        Boolean test = false;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            while ((line = br.readLine()) != null) {
                String[] cityData = line.split(",");
                if (cityData[4].equals(cityName)){
                    test = true;
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier CSV : " + e.getMessage());
            e.printStackTrace();
        }

        return test;
    }
}
