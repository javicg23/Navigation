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
public class FXMLBrujulaController implements Initializable {

    @FXML
    private Label lblCOG;
    @FXML
    private Label lblSOG;

    
    private Model model;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = Model.getInstance();
        
        // anadimos un listener para que cuando cambie el valor en el modelo 
        //se actualice su valor en su correspondiente representacion grafica
        //COG - rumbo de embarcacion obtenida por GPS
        model.COGProperty().addListener((observable, oldValue, newValue) -> {
            String dat = String.valueOf(newValue);
            Platform.runLater(() -> {
                lblCOG.setText(dat);
            });
        });
        
        //SOG - velocidad de embarcacion obtenida por GPS
        model.SOGProperty().addListener((observable, oldValue, newValue) -> {
            String dat = String.valueOf(newValue);
            Platform.runLater(() -> {
                lblSOG.setText(dat);
            });
        });
        
    }    
    
}
