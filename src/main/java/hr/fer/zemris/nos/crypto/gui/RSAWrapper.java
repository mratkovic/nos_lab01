package hr.fer.zemris.nos.crypto.gui;

import hr.fer.zemris.nos.crypto.ciphers.RSA;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class RSAWrapper extends CryptoGUIWrapper {
	RadioButton encrypt;
	RadioButton decrypt;
	ToggleGroup modeGroup;

	TextField pubKey;
	TextField privKey;

	void initObjects() {
		pubKey = new TextField();
		privKey = new TextField();

		encrypt = new RadioButton("Kriptiranje");
		decrypt = new RadioButton("Dekriptiranje");
		modeGroup = new ToggleGroup();
	}

	@Override
	public void initGUI() {
		initObjects();
		title = "RSA";

		GridPane grid = initGrid();

		int row = 1;
		// PUB KEY
		row++;
		addRow("javni kljuc:", pubKey, grid, row);
		// PRIV KEY
		row++;
		addRow("Tajni kljuc:", privKey, grid, row);

		// generate
		row++;
		Button generate = new Button("Generiraj kljuceve");
		generate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				try {
					testPathExist(pubKey.getText(), privKey.getText());
					RSA.generateKeys(pubKey.getText(), privKey.getText(), 512);
					showAlert("Generirani kljucevi",
						"Datoteke s kljucevima uspjesno generirane i pohranjena na " + pubKey.getText() +
							" i " + privKey.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		grid.add(generate, 1, row, 5, 1);


		// IN
		row++;
		addRow("Ulazna datoteka:", inputFile, grid, row);

		// OUT
		row++;
		addRow("Izlazna datoteka:", outputFile, grid, row);


		// radio
		row++;
		encrypt.setToggleGroup(modeGroup);
		encrypt.setSelected(true);
		decrypt.setToggleGroup(modeGroup);

		grid.add(encrypt, 1, row, 2, 1);
		grid.add(decrypt, 2, row, 2, 1);

		Button btn = new Button("Izvrsi");
		grid.add(btn, 1, 8);

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				try {
					RadioButton selected = (RadioButton) modeGroup.getSelectedToggle();
					if (!testFilesExist(inputFile.getText()) ||
						!testPathExist(outputFile.getText())) {
						return;
					}

					if (selected == encrypt) {
						if (!testFilesExist(pubKey.getText())) {
							return;
						}
						RSA.encrypt(inputFile.getText(), outputFile.getText(), pubKey.getText());
						showAlert("Datoteka kriptirana",
							"Datoteka uspjesno kriptirana i pohranjena na " + outputFile.getText());
					} else {
						if (!testFilesExist(privKey.getText())) {
							return;
						}
						RSA.decrypt(inputFile.getText(), outputFile.getText(), privKey.getText());
						showAlert("Datoteka dekriptirana",
							"Datoteka uspjesno dekriptirana i pohranjena na " + outputFile.getText());
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
