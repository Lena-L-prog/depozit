package app;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Image icon = new Image(getClass().getResource("/depozit.png").toExternalForm());
        ImageView splashImage = new ImageView(icon);
        splashImage.setFitWidth(300);
        splashImage.setPreserveRatio(true);

        StackPane splashLayout = new StackPane(splashImage);
        Scene splashScene = new Scene(splashLayout, 400, 400);

        Stage splashStage = new Stage();
        splashStage.setScene(splashScene);
        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> {
            splashStage.close();

            app.WarehouseUI ui = new app.WarehouseUI();
            Scene mainScene = new Scene(ui.getRoot(), 900, 600);
            mainScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

            primaryStage.setTitle("Depozit IMSP SR Ungheni");
            primaryStage.getIcons().add(icon);
            primaryStage.setScene(mainScene);
            primaryStage.show();
        });
        delay.play();
    }
    public static void main(String[] args) {
        launch(args);
    }
}

