package hr.fer.zemris.nos.crypto.gui;

import java.io.IOException;

import hr.fer.zemris.nos.crypto.ciphers.SHA1;
import hr.fer.zemris.nos.crypto.ciphers.SHA1Impl;
import hr.fer.zemris.nos.crypto.util.CryptoConfig;
import hr.fer.zemris.nos.util.UtilMethods;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class SHAChecker extends CryptoGUIWrapper {
	TextField shaFile;
	TextField shaImplFile;

	void initObjects() {
		shaFile = new TextField();
		shaImplFile = new TextField();
	}
	@Override
	public void initGUI() {
		title = "SHA-1 test";
		GridPane grid = initGrid();
		initObjects();

		// IN
		int row = 1;
		// IN
		row++;
		addRow("Ulazna datoteka:", inputFile, grid, row);



		// SHA1
		row++;
		addRow("SHA1 izlazna datoteka:", shaFile, grid, row);

		Button gen1 = new Button("Generiraj");
		grid.add(gen1, 4, row);
		gen1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				try {
					if (!testFilesExist(inputFile.getText()) ||
						!testPathExist(shaFile.getText())) {
						return;
					}
					SHA1.hashFile(inputFile.getText(), shaFile.getText());
					showAlert("Uspjesno izracunat sazetak koristenjem gotove implementacije",
						"Uspjesno izracunat sazetak datoteke " + UtilMethods.getFileName(inputFile.getText()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});


		// My SHA1
		row++;
		addRow("SHA1 Impl izlazna datoteka:", shaImplFile, grid, row);


		Button gen2 = new Button("Generiraj");
		grid.add(gen2, 4, row);
		gen2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				try {
					if (!testFilesExist(inputFile.getText()) ||
						!testPathExist(shaImplFile.getText())) {
						return;
					}
					SHA1Impl.hashFile(inputFile.getText(), shaImplFile.getText());
					showAlert("Uspjesno izracunat sazetak koristenjem implementirane verzije SHA1",
						"Uspjesno izracunat sazetak datoteke " + UtilMethods.getFileName(inputFile.getText()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		row++;
		Button cmp = new Button("Usporedi sazetke");
		grid.add(cmp, 1, row);
		cmp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				try {
					if (!testFilesExist(shaFile.getText(), shaImplFile.getText())) {
						return;
					}
					boolean match = compareSignatures(shaFile.getText(), shaImplFile.getText());
					if (match) {
						showAlert("Sazeci odgovaraju", "Obje datoteke sadrze jednak potpis");
					} else {
						showAlert("Sazeci ne odgovaraju", "Datoteke sadrzi razlicite potpise");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		});

		this.setContent(grid);
		this.setText(title);

	}

	private boolean compareSignatures(final String path1, final String path2) throws IOException {
		CryptoConfig c1 = new CryptoConfig(path1);
		CryptoConfig c2 = new CryptoConfig(path2);
		return c1.get("Signature").equals(c2.get("Signature"));
	}
}
