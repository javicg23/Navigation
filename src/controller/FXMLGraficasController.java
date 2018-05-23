/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import model.Model;

/**
 * FXML Controller class
 *
 * @author V
 */
public class FXMLGraficasController implements Initializable {

    @FXML
    private Label lblTWD;
    @FXML
    private Label lblTWS;
    @FXML
    private LineChart<String, Number> chartTWD;
    @FXML
    private Label lbl0porcentaje;
    @FXML
    private Label lbl25porcentaje;
    @FXML
    private Label lbl50porcentaje;
    @FXML
    private Label lbl75porcentaje;
    @FXML
    private Label lbl100porcentaje;
    @FXML
    private LineChart<Double, String> chartTWS;
    @FXML
    private Slider sliderIntervalo;
    
    private Model model;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = model.getInstance();
        
        XYChart.Series serieTWD = new XYChart.Series();
        chartTWD.getData().add(serieTWD);
        
        // anadimos un listener para que cuando cambie el valor en el modelo 
        //se actualice su valor en su correspondiente representacion grafica
        //TWD - direccion del viento
        model.TWDProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                //calcular el tiempo actual del sistema para el eje de la Y de la grafica
                LocalTime time = LocalTime.now();
                int segons = (time.getHour() * 3600) + (time.getMinute() * 60) + time.getSecond();
                String tiempoX = String.valueOf(segons);
                //crear el punto de la grafica y añadirlo a la lista
                if (serieTWD.getData().size() >= sliderIntervalo.getValue() * 60) {
                    serieTWD.getData().remove(0);
                }
                serieTWD.getData().add(new XYChart.Data(tiempoX, newValue));
                
                //sincronizar amb el label que indica el valor del TWD
                lblTWD.setText(newValue + "");
            });
        });
        
        //TWS - velocidad del viento
        model.TWSProperty().addListener((observable, oldValue, newValue) -> {
            String dat = String.valueOf(newValue);
            Platform.runLater(() -> {
                lblTWS.setText(dat);
            });
        });
        
        //eliminar que se vean la leyenda de los ejes en ambas graficas
        chartTWS.getXAxis().setTickLabelsVisible(false);
        chartTWS.getYAxis().setTickLabelsVisible(false);
        chartTWD.getXAxis().setTickLabelsVisible(false);
        chartTWD.getYAxis().setTickLabelsVisible(false);
        
        //listener para sincronizar el slider para el intervalo de tiempo
        sliderIntervalo.valueProperty().addListener((observable, oldValue, newValue) -> {
            //modificar la leyenda del eje x de las graficas
            int valorSlider = newValue.intValue();
            lbl25porcentaje.setText(valorSlider * 0.25 + "");
            lbl50porcentaje.setText(valorSlider * 0.5 + "");
            lbl75porcentaje.setText(valorSlider * 0.75 + "");
            lbl100porcentaje.setText(valorSlider * 1.0 + "");
            
            //establecer el filtro de intervalo de tiempo a la grafica
            //como se recibe 1 dato cada segundo limitar el tamaño de la serie de acuerdo al filtro seleccionado
            int tamañoSerie = valorSlider * 60;
            
        });
    }    
    
}
