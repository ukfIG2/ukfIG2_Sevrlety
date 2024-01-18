package application;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main_Lyziar extends Application{
	
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root, 1080, 1080);
		
		
		primaryStage.setTitle("Lyziar");
		
		Game game = new Game(1080, 1080);
		root.getChildren().add(game);
		
		MyTimer timer = new MyTimer(game);
		timer.start();
		

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
