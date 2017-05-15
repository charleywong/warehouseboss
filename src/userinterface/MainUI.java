package userinterface;

import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainUI {
	@FXML private ImageView backgroundImg;
	@FXML private Button startBtn;
	@FXML private Button tutBtn;
	@FXML private Button exitBtn;
	
	private Timeline backgroundAnim;
	
	@FXML
	public void initialize(){
		backgroundAnim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(backgroundImg.imageProperty(), new Image("/Images/s_whboss0.png"))),
                new KeyFrame(Duration.millis(200), new KeyValue(backgroundImg.imageProperty(), new Image("/Images/s_whboss1.png"))),
                new KeyFrame(Duration.millis(400), new KeyValue(backgroundImg.imageProperty(), new Image("/Images/s_whboss2.png"))),
                new KeyFrame(Duration.millis(600), new KeyValue(backgroundImg.imageProperty(), new Image("/Images/s_whboss3.png"))),
                new KeyFrame(Duration.millis(800), new KeyValue(backgroundImg.imageProperty(), new Image("/Images/s_whboss4.png")))
                );
		backgroundAnim.setCycleCount(Timeline.INDEFINITE);
		backgroundAnim.play();
	}
	
	@FXML
	private void onButtonClick(ActionEvent event) throws IOException{
		backgroundAnim.stop();
		
		if(event.getSource() == startBtn){
			Parent gameUIRoot = FXMLLoader.load(getClass().getResource("GameUILayout.fxml"));
			Scene gameUIScene = new Scene(gameUIRoot, MainApplication.WIDTH, MainApplication.HEIGHT);
			Stage stage = (Stage) startBtn.getScene().getWindow();
			stage.setScene(gameUIScene);
			
			stage.show();
		} else if (event.getSource() == tutBtn){
			Parent tutUIRoot = FXMLLoader.load(getClass().getResource("TutorialUILayout.fxml"));
			Scene tutUIScene = new Scene(tutUIRoot, MainApplication.WIDTH, MainApplication.HEIGHT);
			Stage stage = (Stage) startBtn.getScene().getWindow();
			stage.setScene(tutUIScene);
			
			stage.show();
		} else if (event.getSource() == exitBtn){
			Platform.exit();
		}
	}
}
