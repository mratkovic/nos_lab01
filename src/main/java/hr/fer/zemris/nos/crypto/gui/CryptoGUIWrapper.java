package hr.fer.zemris.nos.crypto.gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public abstract class CryptoGUIWrapper extends Tab {
	String title;
	TextField inputFile = new TextField();
	TextField outputFile = new TextField();

	public CryptoGUIWrapper() {
		super();
		initGUI();
	}

	public abstract void initGUI();

	protected GridPane initGrid() {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text(title);
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);
		return grid;
	}

	public void showAlert(final String title, final String msg) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.getDialogPane().setPrefSize(480, 320);
		alert.showAndWait();
	}

	public void addShowFileCallback(final Button btn, final TextField pathField) {
		btn.setOnAction(
			new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					if (!pathField.getText().isEmpty()) {
						try {
							Runtime.getRuntime().exec(new String[] { "sublime-text", pathField.getText() });
						} catch (IOException e1) {
							System.err.println("Could not open file " + pathField.getText());
						}
					}
				}
			});
	}

	public void addChooseFileCallback(final Button btn, final TextField pathField) {
		btn.setOnAction(
			new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					Node node = (Node) e.getSource();
					File file = new FileChooser().showOpenDialog(node.getScene().getWindow());
					if (file != null) {
						pathField.setText(file.getAbsolutePath());
						pathField.positionCaret(pathField.getText().length());
					}
				}
			});
	}

	public boolean testFilesExist(final String... paths) {
		for (String path : paths) {
			Path child = Paths.get(path);

			if (!Files.exists(child)) {
				showAlert("Nepostojeca datoteka", "Predana datoteka " + path + " ne postoji");
				return false;
			}
		}
		return true;
	}

	public boolean testPathExist(final String... paths) {
		for (String path : paths) {
			Path child = Paths.get(path);

			if (child == null || !Files.exists(child.getParent())) {
				showAlert("Nepostojeca datoteka", "Predana datoteka " + path + " ne postoji");
				return false;
			}
		}
		return true;
	}

	protected void addRow(final String labelStr, final TextField field, final GridPane grid, final int row) {
		Label label = new Label(labelStr);
		grid.add(label, 0, row);
		grid.add(field, 1, row);

		Button choose = new Button("Odaberi");
		addChooseFileCallback(choose, field);
		grid.add(choose, 2, row);

		Button show = new Button("Pregledaj");
		addShowFileCallback(show, field);
		grid.add(show, 3, row);
	}

}
