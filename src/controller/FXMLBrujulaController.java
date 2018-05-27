/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import eu.hansolo.medusa.*;
import eu.hansolo.medusa.Gauge.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
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
    @FXML
    private HBox hboxBrujula;
    
    private Model model;
    private Gauge gauge = null;
    
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
        //HDG - rumbo magnetico
        model.HDGProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                gauge.setValue((double) newValue);
            });
        });
        
        //crear la brujula
        gauge = GaugeBuilder.create()
                          .minValue(0)
                          .maxValue(359)
                          .startAngle(180)
                          .angleRange(360)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)
                          .customTickLabels("N", "", "", "", "", "", "", "", "",
                                            "E", "", "", "", "", "", "", "", "",
                                            "S", "", "", "", "", "", "", "", "",
                                            "W", "", "", "", "", "", "", "", "")
                          .customTickLabelFontSize(72)
                          .minorTickMarksVisible(false)
                          .mediumTickMarksVisible(false)
                          .majorTickMarksVisible(true)
                          .valueVisible(true)
                          .needleType(NeedleType.BIG)
                          .needleShape(NeedleShape.ROUND)
                          .knobType(KnobType.FLAT)
                          .animated(false)
                          .needleBehavior(NeedleBehavior.OPTIMIZED)
                          .build();
        
        //añadir la brujula al hbox de la vista
        hboxBrujula.getChildren().add(gauge);
        
        //css de la brujula por defecto en diurno (es el valor que faltaba)
        gauge.borderPaintProperty().setValue(Gauge.DARK_COLOR);
        
    }    
    
    //metodo para cambiar el css de la brujula
    public void establecerEstiloBrujula() {
        //cambiar estilo grafica dependiendo del css
        if (model.getEstilo()) {//estilo diurno, poner brujula en diurno
            gauge.setValueColor(Color.BLACK);
            gauge.setTickLabelColor(Color.BLACK);
            gauge.majorTickMarkColorProperty().setValue(Color.BLACK);
            gauge.knobColorProperty().setValue(Color.BLACK);
            gauge.borderPaintProperty().setValue(Gauge.DARK_COLOR);
        } else {
            //poner el estilo de grafica en nocturno
            gauge.setValueColor(Color.WHITE);
            gauge.setTickLabelColor(Color.WHITE);
            gauge.majorTickMarkColorProperty().setValue(Color.WHITE);
            gauge.knobColorProperty().setValue(Color.WHITE);
            gauge.borderPaintProperty().setValue(Gauge.BRIGHT_COLOR);
        }
    }
}
