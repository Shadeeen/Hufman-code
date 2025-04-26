package application;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CompressScreen {
	private Button startCompress;
	private Button statistic;
	private Button huffman;
	private Button header;
	private Button cancelCompress;
	private Label compressProcessLabel;

	private Statistics s = new Statistics();

	public CompressScreen() {

		ImageView startCompressImage = new ImageView("startCompress.png");
		startCompressImage.setFitWidth(150);
		startCompressImage.setFitHeight(150);
		startCompress = new Button("", startCompressImage);
		startCompress.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");
		scale(startCompress);

		statistic = new Button("Statistics");
		statistic.setStyle(
				"-fx-background-color: #007BFF;-fx-text-fill: white;-fx-font-size: 20px;-fx-background-radius: 30px;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';");
		scale(statistic);

		huffman = new Button("Haffman Tabel");
		huffman.setStyle(
				"-fx-background-color: #007BFF;-fx-text-fill: white;-fx-font-size: 20px;-fx-background-radius: 30px;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';");
		scale(huffman);

		header = new Button("Header");
		header.setStyle(
				"-fx-background-color: #007BFF;-fx-text-fill: white;-fx-font-size: 20px;-fx-background-radius: 30px;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';");
		scale(header);

		cancelCompress = new Button("cancel");
		cancelCompress.setStyle(
				"-fx-background-color: #007BFF;-fx-text-fill: white;-fx-font-size: 15px;-fx-background-radius: 30px;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';");
		scale(cancelCompress);

		compressProcessLabel = new Label(" The Compress Process ");
		compressProcessLabel.setStyle(
				"-fx-font-size: 30px;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';-fx-text-fill: white;-fx-background-color: #007BFF;-fx-background-radius: 40px;");
		compressProcessLabel.setAlignment(Pos.CENTER);

		statistic.setDisable(true);
		huffman.setDisable(true);
		header.setDisable(true);
	}
	
	private Stage stage = new Stage();
	public void compressScreen() {
		HBox hbx = new HBox(10);
		hbx.getChildren().addAll(statistic, huffman, header);
		hbx.setAlignment(Pos.CENTER);
		VBox vbx = new VBox(70);
		vbx.getChildren().addAll(startCompress, hbx);
		vbx.setAlignment(Pos.CENTER);
		BorderPane bp = new BorderPane();
		bp.setCenter(vbx);
		bp.setRight(cancelCompress);
		bp.setTop(compressProcessLabel);
		bp.setAlignment(compressProcessLabel, Pos.TOP_CENTER);
		bp.setMargin(compressProcessLabel, new Insets(10, 60, 0, 0));
		bp.setAlignment(vbx, Pos.CENTER);
		bp.setAlignment(cancelCompress, Pos.BOTTOM_RIGHT);
		bp.setMargin(cancelCompress, new Insets(0, 10, 10, 0));
		bp.setStyle("-fx-background-color:#fffee0");
		Scene scene = new Scene(bp, 910, 360);
		stage.setScene(scene);
		stage.show();

		cancelCompress.setOnAction(e -> {
			stage.close();
		});

		statistic.setOnAction(e -> {
			stage.close();
			s.showStatistice();
		});
		s.getDoneStatisticsButton().setOnAction(e->{
			s.getStage().close();
			stage.show();
		});
	}

	
	public void scale(Button button) {
		ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
		scaleUp.setToX(1.1);
		scaleUp.setToY(1.1);
		ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
		scaleDown.setToX(1.0);
		scaleDown.setToY(1.0);
		button.setOnMouseEntered(e -> scaleUp.play());
		button.setOnMouseExited(e -> scaleDown.play());
	}
	
	

	public Button getStartCompress() {
		return startCompress;
	}

	public void setStartCompress(Button startCompress) {
		this.startCompress = startCompress;
	}

	public Button getStatistic() {
		return statistic;
	}

	public void setStatistic(Button statistic) {
		this.statistic = statistic;
	}

	public Button getHuffman() {
		return huffman;
	}

	public void setHuffman(Button huffman) {
		this.huffman = huffman;
	}

	public Button getHeader() {
		return header;
	}

	public void setHeader(Button header) {
		this.header = header;
	}

	public Button getCancelCompress() {
		return cancelCompress;
	}

	public void setCancelCompress(Button cancelCompress) {
		this.cancelCompress = cancelCompress;
	}

	public Label getCompressProcessLabel() {
		return compressProcessLabel;
	}

	public void setCompressProcessLabel(Label compressProcessLabel) {
		this.compressProcessLabel = compressProcessLabel;
	}

	public Statistics getS() {
		return s;
	}

	public void setS(Statistics s) {
		this.s = s;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	
}
