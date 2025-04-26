package application;

import java.io.File;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/*this class to create the main interface*/
public class FirstScreen {

	private Label fileLabel;
	private Button begainButton;
	private Label decompressLabel;
	private Button compressButton;
	private Button decompressButton;
	private Label compressLabel;
	private Button cancelCompressOrDecompressButton;
	private CompressScreen c = new CompressScreen();

	public FirstScreen() {

		ImageView firstbuttonImage = new ImageView("open.png");
		fileLabel = new Label("", firstbuttonImage);
		firstbuttonImage.setFitWidth(200);
		firstbuttonImage.setFitHeight(200);

		begainButton = new Button("Click here for selection");
		begainButton.setStyle(
				"-fx-background-color: #007BFF;-fx-text-fill: white;-fx-font-size: 30px;-fx-background-radius: 30px;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';");

		begainButton.setAlignment(Pos.CENTER);
		fileLabel.setAlignment(Pos.CENTER);
		scale(begainButton);

		compressLabel = new Label("Compress");
		compressLabel.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';");
		ImageView compressImage = new ImageView("compress.png");
		compressImage.setFitWidth(66);
		compressImage.setFitHeight(66);
		compressButton = new Button("", compressImage);
		compressButton.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");
		scale(compressButton);

		decompressLabel = new Label("Decompress");
		decompressLabel.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';");
		ImageView decompressImage = new ImageView("decompress1.png");
		decompressImage.setFitWidth(70);
		decompressImage.setFitHeight(70);
		decompressButton = new Button("", decompressImage);
		decompressButton.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");
		scale(decompressButton);
		decompressButton.setAlignment(Pos.CENTER);

		cancelCompressOrDecompressButton = new Button("Cancel");
		cancelCompressOrDecompressButton.setStyle(
				"-fx-background-color: #007BFF;-fx-text-fill: white;-fx-font-size: 12px;-fx-background-radius: 30px;-fx-font-weight: bold;-fx-font-family: 'Times New Roman';");
		scale(cancelCompressOrDecompressButton);
		compressOrDecompresscreen();
	}

	public void showFirstScreen() {
		VBox vbx = new VBox(5);
		vbx.getChildren().addAll(fileLabel, begainButton);
		vbx.setStyle("-fx-background-color:#fffee0");
		vbx.setAlignment(Pos.CENTER);
		Scene scene = new Scene(vbx);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();
	}

	private Stage stagee;

	public void compressOrDecompresscreen() {
		BorderPane bp = new BorderPane();
		VBox compressVBox = new VBox();
		compressVBox.getChildren().addAll(compressButton, compressLabel);
		compressVBox.setAlignment(Pos.CENTER);

		VBox decompressVBox = new VBox();
		decompressVBox.getChildren().addAll(decompressButton, decompressLabel);
		decompressVBox.setAlignment(Pos.CENTER);

		HBox hbx = new HBox(40);
		hbx.getChildren().addAll(compressVBox, decompressVBox);
		hbx.setAlignment(Pos.CENTER);

		bp.setCenter(hbx);
		bp.setRight(cancelCompressOrDecompressButton);
		bp.setAlignment(cancelCompressOrDecompressButton, Pos.BOTTOM_RIGHT);
		bp.setMargin(cancelCompressOrDecompressButton, new Insets(0, 10, 10, 0));
		bp.setStyle("-fx-background-color:#fffee0");
		Scene scene = new Scene(bp, 500, 170);
		stagee = new Stage();
		stagee.setScene(scene);
		begainButton.setOnAction(e -> {
			stagee.show();

		});

		cancelCompressOrDecompressButton.setOnAction(e -> {
			stagee.close();
		});

		compressFile();
		deCompressFile();
	}

	private Stage stage;
	private FileChooser fileChooserCompress;
	private File selecetCompressFile;
	private File compressFile;

	// to choose a file for the compress process
	public void compressFile() {
		stage = new Stage();
		fileChooserCompress = new FileChooser();

		compressButton.setOnAction(e -> {
			fileChooserCompress.setInitialDirectory(new File("C:\\"));

			selecetCompressFile = fileChooserCompress.showOpenDialog(stage);
			if (selecetCompressFile == null) {
				return;
			}
			if (selecetCompressFile.getName().endsWith(".huf")) {
				showAlert(Alert.AlertType.ERROR, "File Type", "you cant select a huf type");
				return;
			}
			try {

				c.getStatistic().setDisable(true);
				c.getHuffman().setDisable(true);
				c.getHeader().setDisable(true);
				compressFile = new File(selecetCompressFile.getPath());
				stagee.close();
				c.compressScreen();
				c.getStartCompress().setOnAction(a -> {

					HuffmanCompress h = new HuffmanCompress(compressFile.getAbsolutePath(), c);
					c.getStatistic().setDisable(false);
					c.getHuffman().setDisable(false);
					c.getHeader().setDisable(false);

				});

			} catch (NullPointerException r) {
				showAlert(Alert.AlertType.ERROR, "File Required", "you must select a file!!!");
			}
		});

	}

	private Stage stg;
	private FileChooser fileChooserDecompress;
	private File selecetDecompressFile;
	private File decompressFile;

	// to choose the file from the decompress process
	public void deCompressFile() {
		stg = new Stage();
		fileChooserDecompress = new FileChooser();

		decompressButton.setOnAction(e -> {
			fileChooserDecompress.setInitialDirectory(new File("C:\\"));
			fileChooserDecompress.getExtensionFilters().add(new FileChooser.ExtensionFilter("Huffman Files", "*.huf"));
			selecetDecompressFile = fileChooserDecompress.showOpenDialog(stg);
			if (selecetDecompressFile == null) {
				return;
			}
			decompressFile = new File(selecetDecompressFile.getPath());
			HuffmanDecompress decompress = new HuffmanDecompress(decompressFile.getAbsolutePath());
			stagee.close();

		});

	}

	private void showAlert(Alert.AlertType alertType, String title, String message) {
		try {
			Alert alert = new Alert(alertType);
			alert.setTitle(title);
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();

		} catch (Exception e) {
		}
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

	public Button getBegainButton() {
		return begainButton;
	}

	public void setBegainButton(Button begainButton) {
		this.begainButton = begainButton;
	}

	public Button getCompressButton() {
		return compressButton;
	}

	public void setCompressButton(Button compressButton) {
		this.compressButton = compressButton;
	}

	public Button getDecompressButton() {
		return decompressButton;
	}

	public void setDecompressButton(Button decompressButton) {
		this.decompressButton = decompressButton;
	}

	public Button getCancelCompressOrDecompressButton() {
		return cancelCompressOrDecompressButton;
	}

	public void setCancelCompressOrDecompressButton(Button cancelCompressOrDecompressButton) {
		this.cancelCompressOrDecompressButton = cancelCompressOrDecompressButton;
	}

}
