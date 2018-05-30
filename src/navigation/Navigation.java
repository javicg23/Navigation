/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navigation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author V
 */
public class Navigation extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLNavigation.fxml"));
        Scene scene = new Scene(root);
        //eliminar bordes de pantalla
        stage.initStyle(StageStyle.UNDECORATED);

        //aplicar la plantilla css a la scene
        scene.getStylesheets().add(getClass().getResource("/resources/style/estiloDiurno.css").toExternalForm());
        //añadir el icono a la aplicacion
        Image icon = new Image(getClass().getResourceAsStream("/resources/img/icon.png"));
        stage.getIcons().add(icon);

        //añadir titulo a la aplicacion
        stage.setTitle("Navigation");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
