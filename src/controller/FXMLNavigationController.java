/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Model;

/**
 *
 * @author V
 */
public class FXMLNavigationController implements Initializable {

    @FXML
    private Button btnBrujula;
    @FXML
    private Button btnViento;
    @FXML
    private Button btnGPS;
    @FXML
    private Button btnInclinacion;
    @FXML
    private Button btnTemperatura;
    @FXML
    private Button btnGraficas;
    @FXML
    private Button btnNocturno;
    @FXML
    private Button btnConfiguracion;
    @FXML
    private Button btnApagar;
    @FXML
    private HBox hBox;

    private Model model = null;
    private Parent brujulaVentana;
    //declarado para la brujula fuera para luego poder llamar al metodo de establecer el estilo de la brujula
    private FXMLLoader loaderBrujula = null;
    private Parent vientoVentana;
    private Parent localizacionVentana;
    private Parent inclinacionVentana;
    private Parent temperaturaVentana;
    private Parent graficaVentana;
    private Parent configuracionVentana;
    private Button botonAnteriorPulsado = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            model = Model.getInstance();
            // inicializar los Parent para el cambio de ventana
            loaderBrujula = new FXMLLoader(getClass().getResource("/view/FXMLBrujula.fxml"));
            brujulaVentana = loaderBrujula.load();

            FXMLLoader loaderViento = new FXMLLoader(getClass().getResource("/view/FXMLViento.fxml"));
            vientoVentana = loaderViento.load();

            FXMLLoader loaderLocalizacion = new FXMLLoader(getClass().getResource("/view/FXMLLocalizacion.fxml"));
            localizacionVentana = loaderLocalizacion.load();

            FXMLLoader loaderInclinacion = new FXMLLoader(getClass().getResource("/view/FXMLInclinacion.fxml"));
            inclinacionVentana = loaderInclinacion.load();

            FXMLLoader loaderTemperatura = new FXMLLoader(getClass().getResource("/view/FXMLTemperatura.fxml"));
            temperaturaVentana = loaderTemperatura.load();

            FXMLLoader loaderGrafica = new FXMLLoader(getClass().getResource("/view/FXMLGraficas.fxml"));
            graficaVentana = loaderGrafica.load();

            FXMLLoader loaderConfiguracion = new FXMLLoader(getClass().getResource("/view/FXMLConfiguracion.fxml"));
            configuracionVentana = loaderConfiguracion.load();

        } catch (IOException ex) {
            Logger.getLogger(FXMLNavigationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    private void pulsarBtnBrujula(ActionEvent event) {
        cambiarVentana(brujulaVentana);
        botonAnteriorPulsado = btnBrujula;
    }

    @FXML
    private void pulsarBtnViento(ActionEvent event) {
        cambiarVentana(vientoVentana);
        botonAnteriorPulsado = btnViento;
    }

    @FXML
    private void pulsarBtnGPS(ActionEvent event) {
        cambiarVentana(localizacionVentana);
        botonAnteriorPulsado = btnGPS;
    }

    @FXML
    private void pulsarBtnInclinacion(ActionEvent event) {
        cambiarVentana(inclinacionVentana);
        botonAnteriorPulsado = btnInclinacion;
    }

    @FXML
    private void pulsarBtnTemperatura(ActionEvent event) {
        cambiarVentana(temperaturaVentana);
        botonAnteriorPulsado = btnTemperatura;
    }

    @FXML
    private void pulsarBtnGraficas(ActionEvent event) {
        cambiarVentana(graficaVentana);
        botonAnteriorPulsado = btnGraficas;
    }

    @FXML
    private void pulsarBtnNocturno(ActionEvent event) {
        //comparar el estilo actual para ver si es el diurno
        if (model.getEstilo()) {//si es diurno entonces cambiar a nocturno
            //cambiar a nocturno
            hBox.getScene().getStylesheets().clear();
            hBox.getScene().getStylesheets().add(getClass().getResource("/resources/style/estiloNocturno.css").toExternalForm());
            //actualizar variable de estilo
            model.setEstilo(false);
        } else {
            //cambiar a diurno
            hBox.getScene().getStylesheets().clear();
            hBox.getScene().getStylesheets().add(getClass().getResource("/resources/style/estiloDiurno.css").toExternalForm());
            //actualizar variable de estilo
            model.setEstilo(true);
        }
        //llamar al metodo de la brujula para actualizar su css
        loaderBrujula.<FXMLBrujulaController>getController().establecerEstiloBrujula();
        //hacer el foco al boton ultimo ya que al pulsar el boton del estilo se cambia el foco
        try {
            botonAnteriorPulsado.requestFocus();
        } catch (NullPointerException e) {}
    }

    @FXML
    private void pulsarBtnConfiguracion(ActionEvent event) {
        cambiarVentana(configuracionVentana);
        botonAnteriorPulsado = btnConfiguracion;
    }

    @FXML
    private void pulsarBtnApagar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar aplicación");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro de que desea cerrar la aplicación?");
        //añadir el icono de la aplicacion al alert
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/resources/img/icon.png"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
        botonAnteriorPulsado.requestFocus();
    }

    private void cambiarVentana(Parent parent) {
        //eliminar los hijos de la vista Navigation a excepcion de los botones y añadir los hijos de la siguiente vista
        // if size == 1 there is only an hbox with 2 buttons, at position 0 
        if (hBox.getChildren().size() > 1) {
            hBox.getChildren().remove(1);

        }
        hBox.getChildren().add(parent);
    }
}
