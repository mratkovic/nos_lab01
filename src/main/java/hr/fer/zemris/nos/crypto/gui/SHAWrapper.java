package hr.fer.zemris.nos.crypto.gui;

import hr.fer.zemris.nos.crypto.ciphers.SHA1;
import hr.fer.zemris.nos.util.UtilMethods;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class SHAWrapper extends CryptoGUIWrapper {

	@Override
	public void initGUI() {
		title = "SHA-1";
		GridPane grid = initGrid();

		int row = 1;
		// IN
		row++;
		addRow("Ulazna datoteka:", inputFile, grid, row);

		// OUT
		row++;
		addRow("Izlazna datoteka:", outputFile, grid, row);

		row++;
		Button btn = new Button("Izvrsi");
		grid.add(btn, 1, row);
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				try {
					if (!testFilesExist(inputFile.getText()) ||
						!testPathExist(outputFile.getText())) {
						return;
					}
					SHA1.hashFile(inputFile.getText(), outputFile.getText());
					showAlert("Uspjesno izracunat sazetak",
						"Uspjesno izracunat sazetak datoteke " + UtilMethods.getFileName(inputFile.getText()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		this.setContent(grid);
		this.setText(title);

	}

}
