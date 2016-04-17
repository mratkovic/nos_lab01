package hr.fer.zemris.nos.crypto.gui;

import hr.fer.zemris.nos.crypto.ciphers.Pecat;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class CertificateWrapper extends CryptoGUIWrapper {
	TextField pubKeyA;
	TextField privKeyA;
	TextField envelope;
	TextField pubKeyB;
	TextField privKeyB;
	TextField signature;

	void initObjects() {
		pubKeyA = new TextField();
		privKeyA = new TextField();

		pubKeyB = new TextField();
		privKeyB = new TextField();
		envelope = new TextField();
		signature = new TextField();
	}

	@Override
	public void initGUI() {
		initObjects();
		title = "Digital Certificate";

		GridPane grid = initGrid();
		int row = 0;

		// IN
		row++;
		addRow("Ulazna datoteka", inputFile, grid, row);

		// PUB KEY B
		row++;
		addRow("Javni kljuc primatelja:", pubKeyB, grid, row);

		// PRIV KEY A
		row++;
		addRow("Tajni kljuc posiljatelja:", privKeyA, grid, row);

		// envelope
		row++;
		addRow("Omotnica:", envelope, grid, row);


		// signature
		row++;
		addRow("Digitalni potpis:", signature, grid, row);


		// generiraj button
		row++;
		Button gen = new Button("Generiraj digitalni pecat");
		grid.add(gen, 1, row);

		gen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				try {
					if (!testFilesExist(inputFile.getText(), pubKeyB.getText(), privKeyA.getText())
						|| !testPathExist(envelope.getText(), signature.getText())) {
						return;
					}
					Pecat.generatePecat(inputFile.getText(), pubKeyB.getText(),
						privKeyA.getText(), envelope.getText(), signature.getText());
					showAlert("Generiran pecat",
						"Pecat uspjesno generiran");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		// PUB KEY A
		row++;
		addRow("Javni kljuc posiljatelja:", pubKeyA, grid, row);

		// PRIV KEY B
		row++;
		addRow("Tajni kljuc primatelja:", privKeyB, grid, row);

		// OUT
		row++;
		addRow("Izlazna datoteka:", outputFile, grid, row);

		row++;
		Button btn = new Button("Otvori digitalni pecat");
		grid.add(btn, 1, row);

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				try {
					if (!testFilesExist(envelope.getText(), signature.getText(), privKeyA.getText())
						|| !testPathExist(outputFile.getText())) {
						return;
					}
					boolean ok = Pecat.openPecat(envelope.getText(), signature.getText(), privKeyB.getText(),
						pubKeyA.getText(), outputFile.getText());
					if (ok) {
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
