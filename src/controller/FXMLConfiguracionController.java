/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import model.Model;

/**
 * FXML Controller class
 *
 * @author V
 */
public class FXMLConfiguracionController implements Initializable {

    @FXML
    private Label lblFichero;

    
    private Model model;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = Model.getInstance();
    }    

    @FXML
    private void pulsarElegirFichero(ActionEvent event) throws FileNotFoundException {
        FileChooser ficheroChooser = new FileChooser();
        ficheroChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ficheros NMEA", "*.NMEA"));
        ficheroChooser.setTitle("Fichero datos NMEA");
        
        File ficheroNMEA = ficheroChooser.showOpenDialog(lblFichero.getScene().getWindow());
        if (ficheroNMEA != null) {
            lblFichero.setText("Fichero: " + ficheroNMEA.getName());
            model.addSentenceReader(ficheroNMEA);
        }
    }
    
}
