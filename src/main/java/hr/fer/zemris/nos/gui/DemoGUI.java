package hr.fer.zemris.nos.gui;

import hr.fer.zemris.nos.crypto.gui.AESWrapper;
import hr.fer.zemris.nos.crypto.gui.CertificateWrapper;
import hr.fer.zemris.nos.crypto.gui.DigitalSignatureWrapper;
import hr.fer.zemris.nos.crypto.gui.EnvelopeWrapper;
import hr.fer.zemris.nos.crypto.gui.RSAWrapper;
import hr.fer.zemris.nos.crypto.gui.SHA1ImplWrapper;
import hr.fer.zemris.nos.crypto.gui.SHAChecker;
import hr.fer.zemris.nos.crypto.gui.SHAWrapper;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.stage.Stage;

public class DemoGUI extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("DemoGUI");

        Tab aes = new AESWrapper();
        Tab rsa = new RSAWrapper();
        Tab sha = new SHAWrapper();
        Tab shaImpl = new SHA1ImplWrapper();
        Tab shaCmp = new SHAChecker();
        Tab omotnica = new EnvelopeWrapper();
        Tab potpis = new DigitalSignatureWrapper();
        Tab pecat = new CertificateWrapper();


        TabPane tabPane = new TabPane(aes, rsa, sha, shaImpl, shaCmp, omotnica, potpis, pecat);
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane, 300, 275);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
