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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import model.Model;
import model.Section;
import model.TGauge;

/**
 * FXML Controller class
 *
 * @author V
 */
public class FXMLTemperaturaController implements Initializable {

    private Model model;
    @FXML
    private HBox hboxTemperatura;

    private TGauge gauge = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = Model.getInstance();

        // anadimos un listener para que cuando cambie el valor en el modelo 
        //se actualice su valor en su correspondiente representacion grafica
        //TEMP - temperatura
        model.TEMPProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                gauge.setValue((double) newValue);
            });
        });

        Section[] sections = new Section[]{
            new Section(-10, 0, Color.rgb(0, 0, 170)),
            new Section(0, 10, Color.rgb(50, 50, 255)),
            new Section(10, 20, Color.rgb(255, 255, 102)),
            new Section(20, 30, Color.rgb(255, 165, 0)),
            new Section(30, 40, Color.rgb(255, 50, 50)),
            new Section(40, 50, Color.rgb(170, 0, 0))
        };
        gauge = new TGauge();
        gauge.setValue(-10);
        gauge.setMinValue(-10);
        gauge.setMaxValue(50);
        gauge.setThreshold(20);
        gauge.setSections(sections);

        hboxTemperatura.getChildren().add(gauge);
    }

}
