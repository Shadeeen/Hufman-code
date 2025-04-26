package application;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/*statistic class to create the statistic interface */
public class Statistics {

	private Label statisticsLabel;
	private Label fileSizeLabel;
	private Label newFileSizeLabel;
	private Label compressionRatioLabel;
	private Label fileSize;
	private Label newFileSize;
	private Label compressionRatio;
	private Button doneStatisticsButton;

	public Statistics() {

		statisticsLabel = new Label("THE STATISTICS:");
		statisticsLabel.setStyle(
				"-fx-font-size: 30px;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';-fx-text-fill: white;-fx-background-color: #007BFF;-fx-background-radius: 20px;");

		fileSizeLabel = new Label("old File Size: ");
		newFileSizeLabel = new Label("New File Size: ");
		compressionRatioLabel = new Label("% compression: ");
		labelDesign(compressionRatioLabel);
		labelDesign(fileSizeLabel);
		labelDesign(newFileSizeLabel);

		fileSize = new Label();
		newFileSize = new Label();
		compressionRatio = new Label();
		labelDesign(compressionRatio);
		labelDesign(fileSize);
		labelDesign(newFileSize);

		doneStatisticsButton = new Button("Done");
		scale(doneStatisticsButton);
		doneStatisticsButton.setStyle(
				"-fx-background-color: #007BFF;-fx-text-fill: white;-fx-font-size: 15px;-fx-background-radius: 30px;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';");

	}

	private Stage stage = new Stage();

	public void showStatistice() {
		BorderPane bp = new BorderPane();
		GridPane gp = new GridPane(7, 17);
		gp.add(statisticsLabel, 0, 0);
		gp.add(fileSizeLabel, 0, 1);
		gp.add(newFileSizeLabel, 0, 2);
		gp.add(compressionRatioLabel, 0, 3);

		gp.add(fileSize, 1, 1);
		gp.add(newFileSize, 1, 2);
		gp.add(compressionRatio, 1, 3);
		gp.setAlignment(Pos.CENTER);
		bp.setCenter(gp);
		bp.setAlignment(gp, Pos.CENTER);

		bp.setRight(doneStatisticsButton);

		bp.setAlignment(doneStatisticsButton, Pos.BOTTOM_RIGHT);
		bp.setMargin(doneStatisticsButton, new Insets(0, 10, 10, 0));
		bp.setStyle("-fx-background-color:#fffee0");
		Scene scene = new Scene(bp, 430, 360);

		stage.setScene(scene);
		stage.show();

	}

	public void labelDesign(Label label) {
		label.setStyle(
				"-fx-font-size: 25px;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';-fx-text-fill: white;-fx-background-color: #95E1FF;-fx-background-radius: 20px;");
	}

	private void scale(Button button) {
		ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
		scaleUp.setToX(1.1);
		scaleUp.setToY(1.1);
		ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
		scaleDown.setToX(1.0);
		scaleDown.setToY(1.0);
		button.setOnMouseEntered(e -> scaleUp.play());
		button.setOnMouseExited(e -> scaleDown.play());
	}

	public Label getFileSize() {
		return fileSize;
	}

	public void setFileSize(Label fileSize) {
		this.fileSize = fileSize;
	}

	public Label getNewFileSize() {
		return newFileSize;
	}

	public void setNewFileSize(Label newFileSize) {
		this.newFileSize = newFileSize;
	}

	public Label getCompressionRatio() {
		return compressionRatio;
	}

	public void setCompressionRatio(Label compressionRatio) {
		this.compressionRatio = compressionRatio;
	}

	public Button getDoneStatisticsButton() {
		return doneStatisticsButton;
	}

	public void setDoneStatisticsButton(Button doneStatisticsButton) {
		this.doneStatisticsButton = doneStatisticsButton;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
