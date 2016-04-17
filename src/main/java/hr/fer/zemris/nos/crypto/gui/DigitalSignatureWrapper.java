package hr.fer.zemris.nos.crypto.gui;

import hr.fer.zemris.nos.crypto.ciphers.Signature;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class DigitalSignatureWrapper extends CryptoGUIWrapper {
	TextField pubKey;
	TextField privKey;
	TextField signature;

	void initObjects() {
		pubKey = new TextField();
		privKey = new TextField();
		signature = new TextField();
	}

	@Override
	public void initGUI() {
		initObjects();
		title = "Digital Signature";

		GridPane grid = initGrid();
		int row = 0;

		// IN
		row++;
		addRow("Ulazna datoteka:", inputFile, grid, row);

		// PRIV KEY
		row++;
		addRow("Tajni kljuc:", privKey, grid, row);

		// signature
		row++;
		addRow("Digitalni potpis:", signature, grid, row);

		// generiraj button
		row++;
		Button gen = new Button("Generiraj digitalni potpis");
		grid.add(gen, 1, row);

		gen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				try {
					if (!testFilesExist(inputFile.getText(), privKey.getText())
						|| !testPathExist(signature.getText())) {
						return;
					}
					Signature.generateSignature(inputFile.getText(), privKey.getText(), signature.getText());
					showAlert("Generiran potpis",
						"Potpis uspjesno generiran i pohranjen na " + signature.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		// PUB KEY
		row++;
		addRow("javni kljuc:", pubKey, grid, row);

		row++;
		Button btn = new Button("Otvori digitalnu omotnicu");
		grid.add(btn, 1, row);

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				try {
					if (!testFilesExist(signature.getText(), pubKey.getText(), inputFile.getText())) {
						return;
					}
					boolean match = Signature.checkSignature(signature.getText(), pubKey.getText(),
						inputFile.getText());
					if (match) {
						showAlert("Omotnica otvorena", "Potpis i sazetak odgovaraju");
					} else {
						showAlert("Omotnica otvorena", "Potpis i sazetak NE odgovaraju");
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		this.setContent(grid);
		this.setText(title);

	}
}
