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
public class FXMLVientoController implements Initializable {

    @FXML
    private Label lblTWD;
    @FXML
    private Label lblTWS;
    @FXML
    private Label lblAWA;
    @FXML
    private Label lblAWS;

    private Model model;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = Model.getInstance();
        
        // anadimos un listener para que cuando cambie el valor en el modelo 
        //se actualice su valor en su correspondiente representacion grafica
        //TWD - direccion del viento
        model.TWDProperty().addListener((observable, oldValue, newValue) -> {
            String dat = String.valueOf(newValue);
            Platform.runLater(() -> {
                lblTWD.setText(dat);
            });
        });
        
        //TWS - velocidad del viento
        model.TWSProperty().addListener((observable, oldValue, newValue) -> {
            String dat = String.valueOf(newValue);
            Platform.runLater(() -> {
                lblTWS.setText(dat);
            });
        });
        
        
        //AWS - velocidad del viento medida en el barco
        model.AWSProperty().addListener((observable, oldValue, newValue) -> {
            String dat = String.valueOf(newValue);
            Platform.runLater(() -> {
                lblAWS.setText(dat);
            });
        });
        
        //AWA - angulo aparente del viento
        model.AWAProperty().addListener((observable, oldValue, newValue) -> {
            String dat = String.valueOf(newValue);
            Platform.runLater(() -> {
                lblAWA.setText(dat);
            });
        });
    }    
    
}
