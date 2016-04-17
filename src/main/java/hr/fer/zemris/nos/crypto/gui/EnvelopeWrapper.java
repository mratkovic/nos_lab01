package hr.fer.zemris.nos.crypto.gui;

import hr.fer.zemris.nos.crypto.ciphers.Envelope;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class EnvelopeWrapper extends CryptoGUIWrapper {
	TextField pubKey;
	TextField privKey;
	TextField envelope;

	void initObjects() {
		pubKey = new TextField();
		privKey = new TextField();
		envelope = new TextField();
	}

	@Override
	public void initGUI() {
		initObjects();
		title = "Digital Envelope";

		GridPane grid = initGrid();
		int row = 0;

		// IN
		row++;
		addRow("Ulazna datoteka", inputFile, grid, row);

		// PUB KEY
		row++;
		addRow("Javni kljuc:", pubKey, grid, row);

		// envelope
		row++;
		addRow("Omotnica:", envelope, grid, row);

		// generiraj button
		row++;
		Button gen = new Button("Generiraj digitalnu omotnicu");
		grid.add(gen, 1, row);

		gen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				try {
					if (!testFilesExist(inputFile.getText(), pubKey.getText())
						|| !testPathExist(envelope.getText())) {
						return;
					}
					Envelope.generateEnvelope(inputFile.getText(), pubKey.getText(), envelope.getText());
					showAlert("Generirana omotnica",
						"Omotnica uspjesno generirana i pohranjena na " + envelope.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		// PRIV KEY
		row++;
		addRow("Tajni kljuc:", privKey, grid, row);

		// OUT
		row++;
		addRow("Izlazna datoteka:", outputFile, grid, row);

		row++;
		Button btn = new Button("Otvori digitalnu omotnicu");
		grid.add(btn, 1, row);

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				try {
					if (!testFilesExist(envelope.getText(), privKey.getText())
						|| !testPathExist(outputFile.getText())) {
						return;
					}
					Envelope.openEnvelope(envelope.getText(), privKey.getText(), outputFile.getText());
					showAlert("Omotnica otvorena",
						"Omotnica uspjesno otvorena i pohranjena na " + outputFile.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		this.setContent(grid);
		this.setText(title);

	}
}
