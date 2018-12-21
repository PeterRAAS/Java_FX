package lottoFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @Autor: Peter Raes
 * @Date: 23/10/2018
 * @Project: LottoFX
 * @Purpose: random lotto nummers generator
 */

public class LottoFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("LottoFX.fxml"));
        primaryStage.setTitle("LottoFX");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);

    }
}
