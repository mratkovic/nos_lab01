package hr.fer.zemris.nos.crypto.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import hr.fer.zemris.nos.crypto.ciphers.AES;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class AESWrapper extends CryptoGUIWrapper {
	TextField key;

	RadioButton encrypt;
	RadioButton decrypt;
	ToggleGroup modeGroup;

	void initObjects() {
		key = new TextField();
		encrypt = new RadioButton("Kriptiranje");
		decrypt = new RadioButton("Dekriptiranje");
		modeGroup = new ToggleGroup();
	}

	@Override
	public void initGUI() {
		initObjects();

		title = "AES";

		GridPane grid = initGrid();

		// KEY
		Label keylabel = new Label("Kljuc:");
		grid.add(keylabel, 0, 1);
		grid.add(key, 1, 1, 3, 1);

		Button keyGenBtn = new Button("Generiraj");
		keyGenBtn.setOnAction(
			new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					try {
						if (!testPathExist(key.getText())) {
							return;
						}
						int len = pickKeyLenght();
						AES.generateKeyFile(key.getText(), len);
						showAlert("Generiran kljuc",
							"Datoteka s kljucem uspjesno generirana i pohranjena na " + key.getText());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			});
		grid.add(keyGenBtn, 4, 1);

		Button keyPathFind = new Button("Odaberi");
		addChooseFileCallback(keyPathFind, key);
		grid.add(keyPathFind, 5, 1);

		Button showKey = new Button("Pregledaj");
		addShowFileCallback(showKey, key);
		grid.add(showKey, 6, 1);

		// IN
		Label inFileLabel = new Label("Ulazna datoteka:");
		grid.add(inFileLabel, 0, 2);
		grid.add(inputFile, 1, 2, 4, 1);

		Button inFind = new Button("Odaberi");
		addChooseFileCallback(inFind, inputFile);
		grid.add(inFind, 5, 2);

		Button showInFile = new Button("Pregledaj");
		addShowFileCallback(showInFile, inputFile);
		grid.add(showInFile, 6, 2);

		// OUT
		Label outFile = new Label("Izlazna datoteka:");
		grid.add(outFile, 0, 3);
		grid.add(outputFile, 1, 3, 4, 1);

		Button outFind = new Button("Odaberi");
		addChooseFileCallback(outFind, outputFile);
		grid.add(outFind, 5, 3);

		Button showOutFile = new Button("Pregledaj");
		addShowFileCallback(showOutFile, outputFile);
		grid.add(showOutFile, 6, 3);

		// radio
		encrypt.setToggleGroup(modeGroup);
		encrypt.setSelected(true);
		decrypt.setToggleGroup(modeGroup);

		grid.add(encrypt, 1, 4);
		grid.add(decrypt, 3, 4);

		Button btn = new Button("Izvrsi");
		grid.add(btn, 1, 5);

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				try {
					RadioButton selected = (RadioButton) modeGroup.getSelectedToggle();
					if (!testPathExist(outputFile.getText()) || !testFilesExist(inputFile.getText(), key.getText())) {
						return;
					}
					if (selected == encrypt) {
						AES.encrypt(inputFile.getText(), outputFile.getText(), key.getText());
						showAlert("Datoteka kriptirana",
							"Datoteka uspjesno kriptirana i pohranjena na " + outputFile.getText());
					} else {

						AES.decrypt(inputFile.getText(), outputFile.getText(), key.getText());
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

	private int pickKeyLenght() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Duljina kljuca");
		alert.setHeaderText("Postavke generatora kljuca AES algoritma");
		alert.setContentText("Odaberite duljinu kljuca za AES algoritam");

		HashMap<ButtonType, Integer> options = new HashMap<>();
		options.put(new ButtonType("128"), 128);
		options.put(new ButtonType("192"), 192);
		options.put(new ButtonType("256"), 256);
		alert.getButtonTypes().setAll(options.keySet());

		Optional<ButtonType> result = alert.showAndWait();
		int len = options.get(result.get());
		// TODO java.security.InvalidKeyException: Illegal key size or default
		// parameters
		if (len != 128) {
			System.err.println("Other sizes not supported, default 128");
		}
		return 128;
	}
}
