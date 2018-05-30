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
        //formato Grados minutos y segundos (GMS)
        model.GPSProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                Double longitud = Math.abs(newValue.getLongitude());
                int gradosLongitud = longitud.intValue();
                Double minutos = (longitud - gradosLongitud) * 60;
                int minutosLongitud = minutos.intValue();
                Double segundos = (minutos - minutosLongitud) * 60;
                int segundosLongitud = segundos.intValue();
                String mili = String.valueOf(segundos - segundosLongitud).substring(2, 3);
                String longitudHemisferio = String.valueOf(newValue.getLongitudeHemisphere()).substring(0, 1);
                lblLON.setText(longitudHemisferio + " " + gradosLongitud + "º " + minutosLongitud + "' " + segundosLongitud + mili + "''");

                Double latitud = Math.abs(newValue.getLatitude());
                int gradosLatitud = latitud.intValue();
                minutos = (latitud - gradosLatitud) * 60;
                int minutosLatitud = minutos.intValue();
                segundos = (minutos - minutosLatitud) * 60;
                int segundosLatitud = segundos.intValue();
                mili = String.valueOf(segundos - segundosLatitud).substring(2, 3);
                String latitudHemisferio = String.valueOf(newValue.getLatitudeHemisphere()).substring(0, 1);
                lblLAT.setText(latitudHemisferio + " " + gradosLatitud + "º " + minutosLatitud + "' " + segundosLatitud + mili + "''");
            });
        });
    }

}
