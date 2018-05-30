/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
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
    @FXML
    private Label lblMaxTWD;
    @FXML
    private Label lblMediaTWD;
    @FXML
    private Label lblMinTWD;
    @FXML
    private VBox vBoxAbcisas;
    @FXML
    private Label lblMaxTWS;
    @FXML
    private Label lblMediaTWS;
    @FXML
    private Label lblMinTWS;

    private Model model;
    private XYChart.Series serieTWD = null;
    private XYChart.Series serieTWS = null;
    private double valorIntervalo = 6;
    private DecimalFormat decimalFormat = new DecimalFormat("#.#", new DecimalFormatSymbols(Locale.US));
    private int segundos = 0;
    private int ejeAbcisas = 0;
    private double maxTWD = 0;
    private double mediaTWD = 0;
    private double maxTWS = 0;
    private double mediaTWS = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = model.getInstance();

        //crear las series y asignarlas a las graficas
        serieTWD = new XYChart.Series();
        serieTWS = new XYChart.Series();
        chartTWD.getData().add(serieTWD);
        chartTWS.getData().add(serieTWS);

        // anadimos un listener para que cuando cambie el valor en el modelo 
        //se actualice su valor en su correspondiente representacion grafica
        //TWD - direccion del viento
        model.TWDProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                //sincronizar amb el label que indica el valor del TWD
                lblTWD.setText(newValue + "");

                //detectar si se ha llegado al tiempo limite del intervalo y si es asi, asignar los labels a valores fijos
                //recorrer los labels de la leyenda para ir asignandole su tiempo en formato mm:ss
                if (segundos >= valorIntervalo * 60) {
                    segundos = (int) (valorIntervalo * 60);
                } else {
                    segundos++;
                }

                ObservableList<Node> leyendaAbcisas = vBoxAbcisas.getChildren();
                for (int i = 1; i < leyendaAbcisas.size(); i++) {
                    Node node = leyendaAbcisas.get(i);
                    if (node instanceof Label) {
                        Label label = (Label) node;
                        Double valorLeyendaMinutos = (segundos * 0.25 * i) / 60;
                        Double valorLeyendaSegundos = (segundos * 0.25 * i) % 60;
                        label.setText(String.format("%02d:%02d", valorLeyendaMinutos.intValue(), valorLeyendaSegundos.intValue()));
                    }
                }

                //actualizar el primer valor la leyenda solo al principio ya que siempre sera el mismo
                if (lbl0porcentaje.getText().equals("")) {
                    lbl0porcentaje.setText("00:00");
                }

                //crear el punto de la grafica y añadirlo a la lista
                //comprobar si se esta en el limite del intervalo y si es así borrar un punto
                if (Integer.parseInt(lbl100porcentaje.getText().substring(0, 2)) == valorIntervalo) {
                    serieTWD.getData().remove(0);
                }
                serieTWD.getData().add(new XYChart.Data(ejeAbcisas + "", newValue));
                //aumentar contador de abcisas
                ejeAbcisas++;

                //actualizar leyenda de coordenadas
                if (lblMinTWD.getText().equals("")) {
                    lblMinTWD.setText("0.0");
                }
                if (maxTWD < newValue.doubleValue()) {
                    maxTWD = newValue.doubleValue();
                }
                lblMaxTWD.setText(maxTWD + "");
                mediaTWD = Double.parseDouble(decimalFormat.format(maxTWD / 2));
                lblMediaTWD.setText(mediaTWD + "");
            });
        });

        // anadimos un listener para que cuando cambie el valor en el modelo 
        //se actualice su valor en su correspondiente representacion grafica
        //TWS - velocidad del viento
        model.TWSProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                //sincronizar amb el label que indica el valor del TWS
                lblTWS.setText(newValue + "");

                //detectar si se ha llegado al tiempo limite del intervalo y si es asi, asignar los labels a valores fijos
                //recorrer los labels de la leyenda para ir asignandole su tiempo en formato mm:ss
                if (segundos >= valorIntervalo * 60) {
                    segundos = (int) (valorIntervalo * 60);
                }

                //crear el punto de la grafica y añadirlo a la lista
                //comprobar si se esta en el limite del intervalo y si es así borrar un punto
                if (Integer.parseInt(lbl100porcentaje.getText().substring(0, 2)) == valorIntervalo) {
                    serieTWS.getData().remove(0);
                }
                serieTWS.getData().add(new XYChart.Data(ejeAbcisas + "", newValue));

                //actualizar leyenda de coordenadas
                if (lblMinTWS.getText().equals("")) {
                    lblMinTWS.setText("0.0");
                }
                if (maxTWS < newValue.doubleValue()) {
                    maxTWS = newValue.doubleValue();
                }
                lblMaxTWS.setText(maxTWS + "");
                mediaTWS = Double.parseDouble(decimalFormat.format(maxTWD / 2));
                lblMediaTWS.setText(mediaTWS + "");
            });
        });

        //listener para sincronizar el slider para el intervalo de tiempo
        sliderIntervalo.valueProperty().addListener((observable, oldValue, newValue) -> {

            //asignar el nuevo valor del intervalo al atributo 
            valorIntervalo = (double) newValue;
            int nuevoTamañoGrafica = newValue.intValue();
            //reducir el tamaño de la grafica al intervalo indicado
            //solo si ha llegado a su maximo habra que eliminar
            if (serieTWD.getData().size() > nuevoTamañoGrafica * 60) {
                int datosBorrarGrafica = serieTWD.getData().size() - (nuevoTamañoGrafica * 60);
                //eliminar los datos para que salgan los del intervalo
                for (int i = 0; i < datosBorrarGrafica; i++) {
                    serieTWD.getData().remove(0);
                    serieTWS.getData().remove(0);
                }
            }
        });

        //eliminar que se vean la leyenda de los ejes en ambas graficas
        chartTWS.getXAxis().setTickLabelsVisible(false);
        chartTWS.getYAxis().setTickLabelsVisible(false);
        chartTWD.getXAxis().setTickLabelsVisible(false);
        chartTWD.getYAxis().setTickLabelsVisible(false);

        //quitar las linias verticales interiores de la grafica y la de los ejes
        chartTWD.setVerticalGridLinesVisible(false);
        chartTWD.setVerticalZeroLineVisible(false);
        chartTWD.setHorizontalZeroLineVisible(false);
        chartTWS.setVerticalGridLinesVisible(false);
        chartTWS.setVerticalZeroLineVisible(false);
        chartTWS.setHorizontalZeroLineVisible(false);
        
    }

}
