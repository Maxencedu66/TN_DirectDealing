package eu.telecomnancy.labfx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.Cursor;


public class MenuController {
    @FXML private ImageView homeButton;
    private SkeletonController skeleton_controller;

    public void setSkeletonController(SkeletonController skeleton_controller){
        this.skeleton_controller = skeleton_controller;
    }


    @FXML public void initialize() {

        homeButton.setOnMouseEntered(event -> homeButton.getScene().setCursor(Cursor.HAND));
        homeButton.setOnMouseExited(event -> homeButton.getScene().setCursor(Cursor.DEFAULT));

    }

    @FXML public void handleService() {
        System.out.println("Service");
        skeleton_controller.loadListServiceOfferPage();;
    }

    @FXML public void handleHome() {
        System.out.println("Home");
        skeleton_controller.loadHomePage();
    }

    @FXML public void handleEquipment() {
        System.out.println("Equipment");
        skeleton_controller.loadListEquipmentOfferPage();
    }


    @FXML public void handleMap() {
        System.out.println("Map");
        skeleton_controller.loadMapPage();
    }

    @FXML public void handlePlanning() {
        System.out.println("Planning");
        skeleton_controller.loadPlanningPage();
    }

    @FXML public void handleMessagerie() {
        System.out.println("Messagerie");
        skeleton_controller.loadMessageriePage();
    }
}
