/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Model;

/**
 * FXML Controller class
 *
 * @author V
 */
public class FXMLLocalizacionController implements Initializable {

    @FXML
    private Label lblLAT;
    @FXML
    private Label lblLON;

    private Model model;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = Model.getInstance();
        
        // anadimos un listener para que cuando cambie el valor en el modelo 
        //se actualice su valor en su correspondiente representacion grafica
        // LAT y LON - latitud y longitud de GPS
        model.GPSProperty().addListener((observable, oldValue, newValue)-> {
            Platform.runLater(() -> {
                lblLON.setText(String.valueOf(newValue.getLongitude()) + " " + newValue.getLongitudeHemisphere());
                lblLAT.setText(String.valueOf(newValue.getLatitude()) + " " + newValue.getLatitudeHemisphere());
            });
        });
    }    
    
}
